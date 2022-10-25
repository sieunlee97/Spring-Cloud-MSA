package com.example.hystrix;

import java.io.IOException;

import org.springframework.stereotype.Repository;

import com.example.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class HystrixTestDao {
	public String getIdInfo(String id) {
		log.info("Searching from Database ...");
		
		/* DB 호출 3회중 랜덤 1회 11초 지연 */
		CommonUtil.randomlyRunLog();
		//return id+"'s info";
		throw new RuntimeException("I/O Exception");
	}
}
