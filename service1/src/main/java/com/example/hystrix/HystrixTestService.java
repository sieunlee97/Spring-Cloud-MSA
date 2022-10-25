package com.example.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.example.rest.RibbonClientCommunicator;
import com.example.util.CommonUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RefreshScope
@Service
public class HystrixTestService {
	
	@Autowired
	RibbonClientCommunicator ribbonClientCommunicator;
	
	@Autowired
	HystrixTestDao hystrixTestDao;
	
	
	/** 기본 설정 - 1초 이상 지연 발생시 500 Internal Serve Error 발생 */
	@HystrixCommand
	public String getName(String id) {
		log.info("Communicating ... ");
		
		/** 호출 3회중 랜덤 1회 3초 통신지 지연된걸로 간주한다. */
		CommonUtil.randomlyRunLog(3, 3000);
		return ribbonClientCommunicator.getName(id);
	}
	
	@HystrixCommand(
			fallbackMethod="buildFailbackInfo",
			threadPoolKey="idInfoThreadPool",
			threadPoolProperties =
			{
				// 스레드 풀의 갯수를 정의
				@HystrixProperty(name = "coreSize", value = "30"),
				@HystrixProperty(name = "maxQueueSize", value = "10")
			}, commandProperties= {
				/** 정의 : 15초 동안 10건의 호출 중 75% 에러(3초 이상 SELECT 지연)가 발생하면 Circuit을 7초 동안 Open 하라 */
				// DB SELECT Timeout 설정 3초
                //@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="3000"),
                // 서비스 호출 문제를 모니터할 시간 간격을 설정 (위 정의에서 15초 동안에 해당)
                @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"),
                // 히스트릭스가 호출 차단을 고려하는데 필요한 요청 수 (위 정의에서 10건에 해당)
                @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),
                // 호출 차단 실패 비율 (위 정의에서 75%에 해당)
                @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="50"),
                // 차단 후 서비스의 회복 상태를 확인할 때까지 대기할 시간 (위 정의에서 7초에 해당)
                @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),
                // 설정한 시간 간격 동안 통계를 수집할 횟수를 설정
                @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")
			}
	)
	public String getIdInfo(String id) {
		return hystrixTestDao.getIdInfo(id);
	}
	
	
	private String buildFallbackIdInfo(String id) {
		return id+"'s Fallback info";
	}
}
