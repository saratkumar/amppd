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

import java.util.HashMap;

import org.junit.Before;
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

import edu.indiana.dlib.amppd.factory.ObjectFactory;
import edu.indiana.dlib.amppd.model.Item;
import edu.indiana.dlib.amppd.model.Primaryfile;
import edu.indiana.dlib.amppd.repository.PrimaryfileRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PrimaryfileRepositoryTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PrimaryfileRepository primaryfileRepository;
	
	@Autowired 
	private ObjectMapper mapper;
	private Primaryfile objPrimaryFile ;
	private ObjectFactory objFactory = new ObjectFactory();

	@Before
	public void initiateBeforeTests() throws ClassNotFoundException
	{
		HashMap params = new HashMap<String, String>();
		objPrimaryFile= (Primaryfile)objFactory.createDataEntityObject(params, "Primaryfile");
		
	}
	
	@Before
	public void deleteAllBeforeTests() throws Exception {
		primaryfileRepository.deleteAll();
	}

	@Test
	public void shouldReturnRepositoryIndex() throws Exception {

		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
				jsonPath("$._links.primaryfiles").exists());
	}

	@Test
	public void shouldCreatePrimaryfile() throws Exception {

		mockMvc.perform(post("/primaryfiles").content(
				"{\"name\": \"Primaryfile 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("primaryfiles/")));
	}

	@Test
	public void shouldRetrievePrimaryfile() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/primaryfiles").content(
				"{\"name\": \"Primaryfile 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.name").value("Primaryfile 1")).andExpect(
						jsonPath("$.description").value("For test"));
	}
	
	
	@Test
	public void shouldQueryItemDescription() throws Exception {
		
		objPrimaryFile.setName("Primary File 200");
		objPrimaryFile.setDescription("For testing Primary File Respository using Factories");
		String json = mapper.writeValueAsString(objPrimaryFile);
		mockMvc.perform(post("/primaryfiles")
				  .content(json)).andExpect(
						  status().isCreated());
		mockMvc.perform(
				get("/primaryfiles/search/findByDescription?description={description}", "For testing Primary File Respository using Factories")).andDo(
						MockMvcResultHandlers.print()).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.primaryfiles[0].description").value(
										"For testing Primary File Respository using Factories"));
	}

	
	@Test
	public void shouldQueryItemCreatedDate() throws Exception {
		
		long createdDate = 1012019;
		objPrimaryFile.setName("Primary File 200");
		objPrimaryFile.setDescription("For testing Primary File Respository using Factories");
		objPrimaryFile.setCreatedDate(createdDate);
		String json = mapper.writeValueAsString(objPrimaryFile);
		mockMvc.perform(post("/primaryfiles")
				  .content(json)).andExpect(
						  status().isCreated());
		mockMvc.perform(
				get("/primaryfiles/search/findByCreatedDate?createdDate={createdDate}", "1012019")).andDo(
						MockMvcResultHandlers.print()).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.primaryfiles[0].createdDate").value(
										"1012019"));
	}

	
	@Test
	public void shouldQueryItemCreatedBy() throws Exception {
		
		objPrimaryFile.setName("Primary File 200");
		objPrimaryFile.setDescription("For testing Primary File Respository using Factories");
		objPrimaryFile.setCreatedBy("Developer");
		String json = mapper.writeValueAsString(objPrimaryFile);
		mockMvc.perform(post("/primaryfiles")
				  .content(json)).andExpect(
						  status().isCreated());
		mockMvc.perform(
				get("/primaryfiles/search/findByCreatedBy?createdBy={createdBy}", "Developer")).andDo(
						MockMvcResultHandlers.print()).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.primaryfiles[0].createdBy").value(
										"Developer"));
	}
	

	@Test
	public void shouldQueryPrimaryfileName() throws Exception {

		mockMvc.perform(post("/primaryfiles").content(
				"{ \"name\": \"Primaryfile 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated());

		mockMvc.perform(
				get("/primaryfiles/search/findByName?name={name}", "Primaryfile 1")).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.primaryfiles[0].name").value(
										"Primaryfile 1"));
	}

	@Test
	public void shouldUpdatePrimaryfile() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/primaryfiles").content(
				"{\"name\": \"Primaryfile 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content(
				"{\"name\": \"Primaryfile 1.1\", \"description\":\"For test\"}")).andExpect(
						status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.name").value("Primaryfile 1.1")).andExpect(
						jsonPath("$.description").value("For test"));
	}

	@Test
	public void shouldPartiallyUpdatePrimaryfile() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/primaryfiles").content(
				"{\"name\": \"Primaryfile 1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(
				patch(location).content("{\"name\": \"Primaryfile 1.1.1\"}")).andExpect(
						status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.name").value("Primaryfile 1.1.1")).andExpect(
						jsonPath("$.description").value("For test"));
	}

	@Test
	public void shouldDeletePrimaryfile() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/primaryfiles").content(
				"{ \"name\": \"Primaryfile 1.1\", \"description\":\"For test\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}
}
