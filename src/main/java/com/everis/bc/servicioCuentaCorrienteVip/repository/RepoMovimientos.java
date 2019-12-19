package com.everis.bc.servicioCuentaCorrienteVip.repository;

import java.util.Date;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.everis.bc.servicioCuentaCorrienteVip.model.Movimientos;

import reactor.core.publisher.Flux;
import java.util.List;
import java.lang.String;

public interface RepoMovimientos extends ReactiveMongoRepository<Movimientos, String>{

	@Query("{ 'nro_cuenta': ?0 }")
	public Flux<Movimientos> findByNro_cuenta(String nro_cuenta);
	
	@Query("{'nro_cuenta': ?0, 'fecha': { $gte:ISODate( '?1' ), $lt:ISODate( '?2' ) }}")
	public Flux<Movimientos> findAllDateRangeByNro_cuenta(String nro_cuenta, Date fecha, Date fecha2);
	//@Query("{ 'nro_cuenta': ?0 }")
	public Flux<Movimientos> findByFechaBetween(Date from, Date to);
	
}
