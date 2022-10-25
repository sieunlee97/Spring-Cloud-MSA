package com.example.discovery;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

import com.example.rest.FeignClientCommunicator;
import com.example.rest.RestTemplateClientCommunicator;
import com.example.rest.RibbonClientCommunicator;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class DiscoveryService {
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	RestTemplateClientCommunicator restTemplateClientCommunicator;
	
	@Autowired
	RibbonClientCommunicator ribbonClientCommunicator;
	
	@Autowired
	FeignClientCommunicator feignClientCommunicator;
	
	public List getServices() {
		List<String> services = new ArrayList<String>();
		discoveryClient.getServices().forEach(serviceName -> {
			discoveryClient.getInstances(serviceName).forEach(instance -> {
				services.add(String.format("%s:%s", serviceName, instance.getUri()));
			});
		});
		return services;
	}
	
	/*RestTemplateClientCommunicator 호출*/
	public String resttemplate(String id) {
		log.info("Communicating by RestTemplateClientCommunicator.");
		return restTemplateClientCommunicator.getName(id);
	}
	
	/*RibbonClientCommunicator 호출*/
	public String ribbon(String id) {
		log.info("Communicating by RibbonClientCommunicator.");
		return ribbonClientCommunicator.getName(id);
	}
	
	/*FeignClientCommunicator 호출*/
	public String feign(String id) {
		log.info("Communicating by FeignClientCommunicator.");
		return id + " is " + feignClientCommunicator.getName(id);
	}
}
