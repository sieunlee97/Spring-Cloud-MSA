package com.example.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PostFilter extends ZuulFilter {
	
	/*해당 필터가 어떤 필터인지 결정*/
	@Override
	public String filterType() {
		return "post";
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
		RequestContext ctx = RequestContext.getCurrentContext();
		log.info("====== START Post Filter. ======");
		
		int httpStatus = ctx.getResponse().getStatus();
		String respHttpBody = getRespHttpBody(ctx);
		log.info("[Post Filter] HttpStatus : {}, HttpBody : {}", httpStatus, respHttpBody);
		return null;
	}
	
	private String getRespHttpBody(RequestContext ctx) {
		String responseBody = ctx.getResponseBody();
		if(responseBody == null) {
			InputStream is = ctx.getResponseDataStream();
			try {
				byte[] ib = StreamUtils.copyToByteArray(is);
				responseBody = new String(ib, StandardCharsets.UTF_8);
				ctx.setResponseDataStream(new ByteArrayInputStream(ib));
			} catch (IOException e) {
				log.error("It is failed to obtainning Response Http Body.", e);
			}
		}
		return responseBody;
	}
}
