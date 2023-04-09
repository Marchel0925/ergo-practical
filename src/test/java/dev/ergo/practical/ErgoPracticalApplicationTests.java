package dev.ergo.practical;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ergo.practical.common.exception.ExceptionHandlers;
import dev.ergo.practical.common.response.SuccessResponse;
import dev.ergo.practical.controller.PersonController;
import dev.ergo.practical.model.Person;
import dev.ergo.practical.service.person.PersonService;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.experimental.results.ResultMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = PersonController.class)
@ExtendWith(SpringExtension.class)
@MockBean(ExceptionHandlers.class)
// Mocking authentication
@WithMockUser
@Slf4j
class ErgoPracticalApplicationTests {

	// MockMvc is the primary point of entry for server-side Spring MVC test support.
	@Autowired
	private MockMvc mockMvc;

	// Used to populate a Spring ApplicationContext with mocks
	@MockBean
	private PersonService personService;

	// Jackson object mapper for JSON serialization
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private PersonController controller;

	private List<Person> fakePeople;

	@BeforeEach
	public void init() {
		log.info("Populating people list");
		fakePeople = new ArrayList<>();
		fakePeople.add(new Person(1, "Gail", "Bowery", "male", new Timestamp(2014, 6, 24, 22, 12, 31, 0), "824 64846235", "gbowery0@geocities.jp"));
		fakePeople.add(new Person(2, "Cristobal", "Piddocke", "male", new Timestamp(2017, 11, 25, 19, 58, 19, 0), "113 91250992", "cpiddocke1@posterous.com"));
		fakePeople.add(new Person(3, "Ermengarde", "Mettricke", "female", new Timestamp(2022, 1, 27, 7, 15, 35, 0), "940 38761611", "emettricke2@theglobeandmail.com"));
	}

	// Possible endpoints [/api]
	// GET /person
	// GET /person?name=...
	// GET /person?birth-day=...
	// GET /person?name=...&birth-day=... Ex: http://localhost:8080/api/person?birthday=1403637151000&name=Gali
	// POST /person
	// PUT /person
	// DELETE /person/{id}

	@Test
	void contextLoads() {
		// Sanity check to see if the context is being created
		assertThat(controller).isNotNull();
	}

	@Test
	public void givenPersonApiEndpoint_whenFetchAllEndpointCalled_thenItShouldReturnAPersonList() throws Exception {
		log.info("Testing endpoint /person");
		Mockito.when(personService.findAll()).thenReturn(fakePeople);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/person")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(jsonPath("$.message", Matchers.equalTo("People list.")))
				.andExpect(jsonPath("$.data", Matchers.isA(List.class)))
				.andExpect(jsonPath("$.data", Matchers.hasSize(3)))
				.andExpect(jsonPath("$.data[0].id", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.data[0].firstName", Matchers.equalTo("Gail")))
				.andReturn();

		// Assert that the status code is 200
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());

		// Assert that the service was called once
		Mockito.verify(personService, Mockito.times(1)).findAll();
	}

    @Test
    public void givenPersonApiEndpoint_whenFetchByBirthDateEndpointCalled_thenItShouldReturnAPersonListWith1Person() throws Exception {
		log.info("Testing endpoint /person?birthday=...");
		Person p = fakePeople.get(0);
		Mockito.when(personService.findByBirthDay(p.getBirthDay())).thenReturn(List.of(p));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/person?birthday=" + p.getBirthDay().getTime())
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(jsonPath("$.message", Matchers.equalTo("People found by birthday.")))
				.andExpect(jsonPath("$.data", Matchers.isA(List.class)))
				.andExpect(jsonPath("$.data", Matchers.hasSize(1)))
				.andExpect(jsonPath("$.data[0].id", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.data[0].firstName", Matchers.equalTo("Gail")))
				.andReturn();

		// Assert that the status code is 200
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());

		// Assert that the service was called once
		Mockito.verify(personService, Mockito.times(1)).findByBirthDay(p.getBirthDay());
		Mockito.verify(personService, Mockito.times(0)).findAll();
    }
}
