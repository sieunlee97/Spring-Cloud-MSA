package com.example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
/*
 * 스프링의 RestTemplate를 이용한 서비스간 Http 통신
 * */
import org.springframework.web.client.RestTemplate;

/**
 *Http 통신 방법 - 일반 RestTemplate을 이용한 통신
 */

@Component
public class RestTemplateClientCommunicator {
	@Autowired
	private DiscoveryClient discoveryClient;
	
	public String getName(String id) {
		RestTemplate restTemplate = new RestTemplate();
		List<ServiceInstance> instances = discoveryClient.getInstances("service2");
		
		if(instances.size() == 0) {
			return null;
		}
		/*인스턴스들 중 0번째 클라이언트에 요청*/
		String serviceUri = String.format("%s/name/%s", instances.get(0).getUri().toString(), id);
		
		ResponseEntity<String> restExchange =
				restTemplate.exchange(
						serviceUri,
						HttpMethod.GET,
						null, String.class, id);
		return id+" is "+restExchange.getBody();
	}
}
