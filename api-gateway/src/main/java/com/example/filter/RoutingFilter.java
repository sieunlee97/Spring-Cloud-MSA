package com.example.filter;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RoutingFilter extends ZuulFilter {
	
	/*해당 필터가 어떤 필터인지 결정*/
	@Override
	public String filterType() {
		return "route";
	}
	
	/*필터들의 실행 순서를 정해줌*/
	@Override
	public int filterOrder() {
		return 1;
	}
	
	/*해당 필터가 작동할 지 여부 결정*/
	@Override
	public boolean shouldFilter() {
		return true;
	}
	
	
	/*요청 응답 라우팅 전/후에 특정 처리를 해줄 로직을 구현*/
	@Override
	public Object run() { 
		log.info("====== START Rout Filter. ======");
		return null;
	}
}
