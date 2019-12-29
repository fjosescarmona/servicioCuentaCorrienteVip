package com.everis.bc.servicioCuentaCorrienteVip.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.everis.bc.servicioCuentaCorrienteVip.model.CuentaCorrienteVip;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Repo extends ReactiveMongoRepository<CuentaCorrienteVip, String>{
	@Query("{ 'titulares.doc': ?0 }")
	public Flux<CuentaCorrienteVip> findByTitularesDoc(String doc);
	@Query("{ 'nro_cuenta': ?0 }")
	public Mono<CuentaCorrienteVip> findByNro_cuenta(String nro_cuenta);
	@Query("{ 'titulares.doc': {$in:[ ?0 ]}, 'bankcode': ?1 }")
	public Flux<CuentaCorrienteVip> findByTitularesDocList(List<String> docs, String bankcode);

	
	//public boolean existByTitulares(String doc);
}
