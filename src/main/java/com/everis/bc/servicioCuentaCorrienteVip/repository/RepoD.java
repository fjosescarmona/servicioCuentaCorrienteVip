package com.everis.bc.servicioCuentaCorrienteVip.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.everis.bc.servicioCuentaCorrienteVip.model.CuentaCorrienteVip;
import com.everis.bc.servicioCuentaCorrienteVip.model.Deudores;

import reactor.core.publisher.Flux;


public interface RepoD extends ReactiveMongoRepository<Deudores, String>{
	@Query("{ 'documento': {$in:[ ?0 ]} }")
	public Flux<CuentaCorrienteVip> findByTitularesDocList(List<String> docs);
}
