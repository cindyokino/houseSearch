package com.cindyokino.houseSearch.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HouseController.class)
public class HouseControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void nothing() throws Exception {
		String body = this.mockMvc.perform(get("/houses"))
		.andDo(print())
		.andReturn()
		.getResponse()
		.getContentAsString();
		
		System.out.println("Hwllo World test");
		System.out.println(body);
		
//		MatchersAssert.assertThat(body, CoreMatchers.notNullValue());
	}

}
