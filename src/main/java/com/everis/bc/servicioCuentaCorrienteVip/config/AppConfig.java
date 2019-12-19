package com.everis.bc.servicioCuentaCorrienteVip.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
	
	@Value("${valores.uri_tc}")
	private String url;
	
	@Bean
	public WebClient webClientRegister() {
		return WebClient.create(url);
	}
}
