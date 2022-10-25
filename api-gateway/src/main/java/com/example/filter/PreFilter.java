package com.example.filter;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.google.common.net.HttpHeaders;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PreFilter extends ZuulFilter {
	
	/*해당 필터가 어떤 필터인지 결정*/
	@Override
	public String filterType() {
		return "pre";
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
	
	private static final String AUTHORIZATION_VALUE = "12345";
	
	/*요청 응답 라우팅 전/후에 특정 처리를 해줄 로직을 구현*/
	@Override
	public Object run() { 
		RequestContext ctx = RequestContext.getCurrentContext();
		log.info("====== START Pre Filter. ======");
		
		/*요쳥 URI 가져오기*/
		String reqUri = ctx.getRequest().getRequestURI();
		/*요청 Http Body 정보 구현*/
		String reqHttpBody = getReqHttpBody(ctx);
		log.info("[Pre Filter] : Request reqUri : {} HttpBody : {}", reqUri, reqHttpBody);
		
		/*
		 * API 요청시, 인증을 위해 HttpHeader에서 Authorization에 유효한 키값을 담아서 전송
		 *  > Zuul에서는 해당 정보 추출하여 키값이 유효한 경우에 API 라우팅
		 *  */
		String authorization = ctx.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
		/*키 값이 없거나 유효하지 않은 경우에 401 Unauthorized 에러 반환*/
		if(StringUtils.isEmpty(authorization) || !AUTHORIZATION_VALUE.equals(authorization)) {
			respError(ctx);
		}
		ctx.addZuulRequestHeader("foo", "bar");
		return null;
	}
	
	/*키 값이 없거나 유효하지 않은 경우에 401 Unauthorized 에러 반환*/
	private void respError(RequestContext ctx) {
		try {
			ctx.setSendZuulResponse(false);
			ctx.getResponse().sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*요청 Http Body 정보 구현*/
	private String getReqHttpBody(RequestContext ctx) {
		String reqHttpBody = null;
		try {
			/*RequestContext 객체에서 InputStream으로 추출 > StreamUtils 유틸로 String으로 변환하여 반환*/
			InputStream in = (InputStream) ctx.get("requestEntity");
			if(in == null) {
				in = ctx.getRequest().getInputStream();
				reqHttpBody = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
			}
		} catch (Exception e) {
			log.error("It is failed to obtainning Request Http Body", e);
			return "";
		}
		return reqHttpBody;
	}
}
