package com.example.cluster;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class SessionResourceConfig {

	@Bean
	@Scope(WebApplicationContext.SCOPE_SESSION)
	public Visitor visitor(HttpServletRequest request) {
		return new Visitor(request.getRemoteAddr());
	}

}
