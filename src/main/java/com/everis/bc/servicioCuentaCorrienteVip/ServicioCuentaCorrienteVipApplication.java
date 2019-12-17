package com.everis.bc.servicioCuentaCorrienteVip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@EnableEurekaClient
@SpringBootApplication
public class ServicioCuentaCorrienteVipApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioCuentaCorrienteVipApplication.class, args);
	}

}
