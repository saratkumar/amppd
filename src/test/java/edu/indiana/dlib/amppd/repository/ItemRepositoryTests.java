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

import org.apache.commons.lang.StringUtils;
import org.hamcrest.core.StringContains;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import edu.indiana.dlib.amppd.model.Item;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemRepositoryTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired 
	private ObjectMapper mapper = new ObjectMapper();
	private Item item ;
	
	@BeforeClass
	public static void setupTest() 
	{
	    FixtureFactoryLoader.loadTemplates("edu.indiana.dlib.amppd.testData");
	}
	
	@Before
	public void deleteAllBeforeTests() throws Exception {
		// TODO somehow deleting all as below causes SQL FK violation when running the whole test suites, even though running this test class alone is fine.
//		itemRepository.deleteAll();
	}

	@Test
	public void shouldReturnRepositoryIndex() throws Exception {

		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
				jsonPath("$._links.items").exists());
	}

	@Test
	public void shouldCreateItem() throws Exception {

		mockMvc.perform(post("/items").content(
				"{\"name\": \"Item 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("items/")));
	}

	@Test
	public void shouldRetrieveItem() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/items").content(
				"{\"name\": \"Item 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.name").value("Item 1")).andExpect(
						jsonPath("$.description").value("For test"));
	}

	@Test
	public void shouldQueryItemDescription() throws Exception {
		
		item = Fixture.from(Item.class).gimme("valid");
		
		String json = mapper.writeValueAsString(item);
		mockMvc.perform(post("/items")
				  .content(json)).andExpect(
						  status().isCreated());
		
		mockMvc.perform(
				get("/items/search/findByDescription?description={description}", item.getDescription())).andDo(
						MockMvcResultHandlers.print()).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.items[0].description").value(
										item.getDescription()));
	}
	
	@Test
	public void shouldQueryItemCreatedBy() throws Exception {
		
		item = Fixture.from(Item.class).gimme("valid");
		
		String json = mapper.writeValueAsString(item);
		mockMvc.perform(post("/items")
				  .content(json)).andExpect(
						  status().isCreated());
		
		mockMvc.perform(
				get("/items/search/findByCreatedBy?createdBy={createdBy}", item.getCreatedBy())).andDo(
						MockMvcResultHandlers.print()).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.items[0].createdBy").value(
										item.getCreatedBy()));
	}
	
	@Test
	public void shouldQueryItemNameKeyword() throws Exception {
		
		item = Fixture.from(Item.class).gimme("valid");
		String[] words = StringUtils.split(item.getName());
		
		String json = mapper.writeValueAsString(item);
		mockMvc.perform(post("/items")
				  .content(json)).andExpect(
						  status().isCreated());
		
		mockMvc.perform(
				get("/items/search/findByKeyword?keyword={keyword}", words[words.length-1])).andDo(
						MockMvcResultHandlers.print()).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.items[0].name").value(new StringContains(
										item.getName())));
	}
		
	@Test
	public void shouldQueryItemDescriptionKeyword() throws Exception {
		
		item = Fixture.from(Item.class).gimme("valid");
		String[] words = StringUtils.split(item.getDescription());
		
		String json = mapper.writeValueAsString(item);
		mockMvc.perform(post("/items")
				  .content(json)).andExpect(
						  status().isCreated());
		
		mockMvc.perform(
				get("/items/search/findByKeyword?keyword={keyword}", words[words.length-1])).andDo(
						MockMvcResultHandlers.print()).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.items[0].description").value(new StringContains(
										item.getDescription())));
	}	

	@Test
	public void shouldUpdateItem() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/items").content(
				"{\"name\": \"Item 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content(
				"{\"name\": \"Item 1.1\", \"description\":\"For test\"}")).andExpect(
						status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.name").value("Item 1.1")).andExpect(
						jsonPath("$.description").value("For test"));
	}

	@Test
	public void shouldPartiallyUpdateItem() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/items").content(
				"{\"name\": \"Item 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(
				patch(location).content("{\"name\": \"Item 1.1.1\"}")).andExpect(
						status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.name").value("Item 1.1.1")).andExpect(
						jsonPath("$.description").value("For test"));
	}

	@Test
	public void shouldDeleteItem() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/items").content(
				"{ \"name\": \"Item 1.1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}
}
