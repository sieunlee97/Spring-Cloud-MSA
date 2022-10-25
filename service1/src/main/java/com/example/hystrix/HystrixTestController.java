package com.example.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HystrixTestController {
	
	@Autowired
	HystrixTestService hystrixTestService;
	
	/*service2의 API 호출, 회원 ID 가져오는 기능*/
	@GetMapping(value="/hystrix1")
	public String hystrix1() {
		String id= "1";
		return hystrixTestService.getName(id);
	}
	
	/*DB에서 회원 ID 가져오는 기능*/
	@GetMapping(value="/hystrix2")
	public String hystrix2() {
		String id= "1";
		return hystrixTestService.getIdInfo(id);
	}
}
