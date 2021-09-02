package edu.indiana.dlib.amppd.repository;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import edu.indiana.dlib.amppd.fixture.DataentityProcessor;
import edu.indiana.dlib.amppd.model.Collection;
import edu.indiana.dlib.amppd.util.TestHelper;
import edu.indiana.dlib.amppd.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CollectionRepositoryTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired 
	private TestHelper testHelper;
	
	@Autowired
    private TestUtil testUtil;	

	@Autowired 
	private DataentityProcessor dataentityProcessor;
	
	@Autowired 
	private ObjectMapper mapper;
	
	private String token;
	
	@BeforeClass
	public static void loadFixture() {
		FixtureFactoryLoader.loadTemplates("edu.indiana.dlib.amppd.fixture");
	}

	@Before
	public void setup() throws Exception {
		// TODO do a more refined delete to remove all data that might cause conflicts for tests in this class 
		// deleting all as below causes SQL FK violation when running the whole test suites, even though running this test class alone is fine,
		// probably due to the fact that some other tests call TestHelper to create the complete hierarchy of data entities from collection down to primaryfile
//		collectionRepository.deleteAll(); 		
		token = "Bearer " + testHelper.getToken();
	}

	@Test
	public void shouldReturnRepositoryIndex() throws Exception {
		// the root URL should include a link for collections
		mockMvc.perform(get("/").header("Authorization", token)).andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links.collections").exists());
	}

	@Test
	public void shouldCreateCollection() throws Exception {
		// get a valid random collection fixture
		Collection collection = Fixture.from(Collection.class).uses(dataentityProcessor).gimme("valid");
		String json = testUtil.toJson(collection);
		
		// create the collection, should succeed with the collection's URL as the location header
		mockMvc.perform(post("/collections").header("Authorization", token).content(json))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString("collections/")));
	}
	
	@Test
	public void shouldCreateCollectionWithLongDescription() throws Exception {
		// generate a long description beyond default VARCHAR max length
		StringBuffer description = new StringBuffer();
		for (int i=0; i<256; i++) {
			description.append("Long description ");
		}

		// get a valid random collection fixture and populate it with the long description
		Collection collection = Fixture.from(Collection.class).uses(dataentityProcessor).gimme("valid");
		collection.setDescription(description.toString());
		String json = testUtil.toJson(collection);
		
		// create the collection, should succeed with the collection's URL as the location header
		mockMvc.perform(post("/collections").header("Authorization", token).content(json))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString("collections/")));			
	}	
	
	@Test
	public void shouldListCollections() throws Exception {
		// create a collection to ensure some collections exist for listing 
		Collection collection = Fixture.from(Collection.class).uses(dataentityProcessor).gimme("valid");
		String json = testUtil.toJson(collection);
		mockMvc.perform(post("/collections").header("Authorization", token).content(json)).andReturn();		
		
		// list all collections, should include at least one collection
		mockMvc.perform(get("/collections").header("Authorization", token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.collections").exists())
			.andExpect(jsonPath("$._embedded.collections").isNotEmpty());	
	}	
	
	@Test
	public void shouldRetrieveCollection() throws Exception {
		// create a collection for retrieval
		Collection collection = Fixture.from(Collection.class).uses(dataentityProcessor).gimme("valid");
		String json = testUtil.toJson(collection);
		MvcResult mvcResult = mockMvc.perform(post("/collections").header("Authorization", token).content(json)).andReturn();		
		String location = mvcResult.getResponse().getHeader("Location");
		
		// retrieve the created collection by accessing the returned location, fields should match
		mockMvc.perform(get(location).header("Authorization", token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(collection.getName()))
			.andExpect(jsonPath("$.description").value(collection.getDescription()));
	}

	@Test
	public void shouldQueryCollections() throws Exception {
		// create a collection to ensure some collections exist for querying 
		Collection collection = Fixture.from(Collection.class).uses(dataentityProcessor).gimme("valid");
		String json = testUtil.toJson(collection);
		mockMvc.perform(post("/collections").header("Authorization", token).content(json)).andReturn();

		// query collections by name, should return at least one collection
		mockMvc.perform(get("/collections/search/findByName?name={name}", collection.getName()).header("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.collections[0].name").value(collection.getName()));
	}

	@Test
	public void shouldUpdateCollection() throws Exception {
		// create a collection for update
		Collection collection = Fixture.from(Collection.class).uses(dataentityProcessor).gimme("valid");
		String json = testUtil.toJson(collection);
		MvcResult mvcResult = mockMvc.perform(post("/collections").header("Authorization", token).content(json)).andReturn();			
		String location = mvcResult.getResponse().getHeader("Location");
		
		// update user-changeable fields
		collection.setName(collection.getName() + " Updated");
		collection.setDescription(collection.getDescription() + " updated");
		json = testUtil.toJson(collection);

		// update the whole collection
		mockMvc.perform(put(location).header("Authorization", token).content(json))
				.andExpect(status().isNoContent());

		// retrieve the updated collection, updated fields should match
		mockMvc.perform(get(location).header("Authorization", token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(collection.getName()))
			.andExpect(jsonPath("$.description").value(collection.getDescription()));
	}

	@Test
	public void shouldPartiallyUpdateCollection() throws Exception {
		// create a collection for partial-update
		Collection collection = Fixture.from(Collection.class).uses(dataentityProcessor).gimme("valid");
		String json = testUtil.toJson(collection);
		MvcResult mvcResult = mockMvc.perform(post("/collections").header("Authorization", token).content(json)).andReturn();			
		String location = mvcResult.getResponse().getHeader("Location");
		
		// update only the name field
		String name = collection.getName() + " Updated";
		json = "{\"name\": \"" + name + "\"}";

		// partial-update the collection
		mockMvc.perform(patch(location).header("Authorization", token).content(json))
			.andExpect(status().isNoContent());

		// retrieve the updated collection, updated name should match
		mockMvc.perform(get(location).header("Authorization", token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(name));
	}

	@Test
	public void shouldDeleteCollection() throws Exception {
		// create a collection for delete
		Collection collection = Fixture.from(Collection.class).uses(dataentityProcessor).gimme("valid");
		String json = testUtil.toJson(collection);
		MvcResult mvcResult = mockMvc.perform(post("/collections").header("Authorization", token).content(json)).andReturn();			
		String location = mvcResult.getResponse().getHeader("Location");
		
		// delete the created collection, then retrieve the same collection, should return nothing
		mockMvc.perform(delete(location).header("Authorization", token)).andExpect(status().isNoContent());
		mockMvc.perform(get(location).header("Authorization", token)).andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldErrorOnInvalidCreate() throws Exception {
		// get an invalid random collection fixture
		Collection collection = Fixture.from(Collection.class).gimme("invalid");
		String json = mapper.writeValueAsString(collection);
		
		// create the invalid collection, should fail with all validation errors
		mockMvc.perform(post("/collections").header("Authorization", token).content(json))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.validationErrors").isNotEmpty())
			.andExpect(jsonPath("$.validationErrors[0].field").value("handleBeforeCreate.collection.name"))
			.andExpect(jsonPath("$.validationErrors[0].message").value("must not be blank"));
	}
	
	@Test
	public void shouldErrorOnDuplicateCreate() throws Exception {
		// create a valid collection 1st time
		Collection collection = Fixture.from(Collection.class).uses(dataentityProcessor).gimme("valid");
		String json = testUtil.toJson(collection);
		mockMvc.perform(post("/collections").header("Authorization", token).content(json)).andReturn();		
		
		// create the above collection 2nd time, should fail with non-unique name validation error
		mockMvc.perform(post("/collections").header("Authorization", token).content(json))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.validationErrors").isNotEmpty())
			.andExpect(jsonPath("$.validationErrors[0].field").value("handleBeforeCreate.collection"))
			.andExpect(jsonPath("$.validationErrors[0].message").value("dataentity name must be unique within its parent's scope"));
	}
		
	@Test
	public void shouldErrorOnInvalidUpdate() throws Exception {
		// create a valid collection for update
		Collection collection = Fixture.from(Collection.class).gimme("valid");
		String json = testUtil.toJson(collection);
		MvcResult mvcResult = mockMvc.perform(post("/collections").header("Authorization", token).content(json)).andReturn();	
		String location = mvcResult.getResponse().getHeader("Location");		
		
		// update user-changeable fields to invalid values
		collection.setName("");
		json = testUtil.toJson(collection);
		
		// update the collection with invalid fields, should fail with all validation errors
		mockMvc.perform(put(location).header("Authorization", token).content(json))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.validationErrors").isNotEmpty())
			.andExpect(jsonPath("$.validationErrors[0].field").value("handleBeforeUpdate.collection.name"))
			.andExpect(jsonPath("$.validationErrors[0].message").value("must not be blank"));
	}	

}
