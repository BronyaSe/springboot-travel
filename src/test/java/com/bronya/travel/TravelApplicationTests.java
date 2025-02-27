package com.bronya.travel;

import com.bronya.travel.Mapper.RouteMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TravelApplicationTests {

	@Autowired
	private RouteMapper routeMapper;

	@Test
	public void testDeserialize() throws Exception {
		String json = "[\"pic1.jpg\", \"pic2.jpg\"]";
		List<String> pics = new ObjectMapper().readValue(json, new TypeReference<List<String>>() {});
		assertNotNull(pics);
		System.out.println(pics); // 应输出 [pic1.jpg, pic2.jpg]
	}

}
