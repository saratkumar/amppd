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
import edu.indiana.dlib.amppd.model.CollectionSupplement;
import edu.indiana.dlib.amppd.repository.CollectionSupplementRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CollectionSupplementRepositoryTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CollectionSupplementRepository supplementRepository;
	
	@Autowired 
	private ObjectMapper mapper = new ObjectMapper();
	private CollectionSupplement obj ;

	
	@BeforeClass
	public static void setupTest() 
	{
	    FixtureFactoryLoader.loadTemplates("edu.indiana.dlib.amppd.testData");
	}
	
	@Before
	public void deleteAllBeforeTests() throws Exception {
		// TODO do a more refined delete to remove all data that might cause conflicts for tests in this class 
		// deleting all as below causes SQL FK violation when running the whole test suites, even though running this test class alone is fine,
		// probably due to the fact that some other tests call TestHelper to create the complete hierarchy of data entities from unit down to primaryfile
//		supplementRepository.deleteAll();
	}

	@Test
	public void shouldReturnCollectionSupplementRepositoryIndex() throws Exception {

		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
				jsonPath("$._links.collectionSupplements").exists());

	}
	
	@Test
	public void shouldCreateCollectionSupplement() throws Exception {

		mockMvc.perform(post("/collectionSupplements").content(
				"{\"name\": \"CollectionSupplement 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("collectionSupplements/")));
	}
	
	@Test
	public void shouldRetrieveCollectionSupplement() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/collectionSupplements").content(
				"{\"name\": \"CollectionSupplement 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.name").value("CollectionSupplement 1")).andExpect(
						jsonPath("$.description").value("For test"));
	}

	@Test
	public void shouldQueryCollectionSupplement() throws Exception {
		
		obj = Fixture.from(CollectionSupplement.class).gimme("valid");
		
		String json = mapper.writeValueAsString(obj);
		mockMvc.perform(post("/collectionSupplements")
				  .content(json)).andExpect(
						  status().isCreated());
		mockMvc.perform(
				get("/collectionSupplements/search/findByName?name={name}", obj.getName())).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.collectionSupplements[0].name").value(
										obj.getName()));
	}

	@Test
	public void shouldUpdateCollectionSupplement() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/collectionSupplements").content(
				"{\"name\": \"CollectionSupplement 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content(
				"{\"name\": \"CollectionSupplement 1.1\", \"description\":\"For test\"}")).andExpect(
						status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.name").value("CollectionSupplement 1.1")).andExpect(
						jsonPath("$.description").value("For test"));
	}

	@Test
	public void shouldPartiallyUpdateCollectionSupplement() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/collectionSupplements").content(
				"{\"name\": \"CollectionSupplement 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(
				patch(location).content("{\"name\": \"CollectionSupplement 1.1.1\"}")).andExpect(
						status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.name").value("CollectionSupplement 1.1.1")).andExpect(
						jsonPath("$.description").value("For test"));
	}

	@Test
	public void shouldDeleteCollectionSupplement() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/collectionSupplements").content(
				"{ \"name\": \"CollectionSupplement 1.1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}
}
