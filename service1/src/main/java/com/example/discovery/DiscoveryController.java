package com.example.discovery;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class DiscoveryController {
	@Autowired
	DiscoveryService discoveryService;

	@GetMapping(value="/services")
	public List<String> services(){
		return discoveryService.getServices();
	}
	
	/*RestTemplateClientCommunicator 호출*/
	@GetMapping(value="/resttemplate/{id}")
	public String resttemplate(@PathVariable("id") String id) {
		return discoveryService.resttemplate(id);
	}
	
	/*RibbonClientCommunicator 호출*/
	@GetMapping(value="/ribbon/{id}")
	public String ribbon(@PathVariable("id") String id, @RequestHeader("foo") String foo) {
		log.info("Http Header foo : {}", foo);
		return discoveryService.ribbon(id);
	}
	
	/*FeignClientCommunicator 호출*/
	@GetMapping(value="/feign/{id}")
	public String feign(@PathVariable("id") String id) {
		return discoveryService.feign(id);
	}
}
