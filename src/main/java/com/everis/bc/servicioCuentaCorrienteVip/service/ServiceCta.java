package com.everis.bc.servicioCuentaCorrienteVip.service;


import java.util.Map;

import com.everis.bc.servicioCuentaCorrienteVip.model.CuentaCorrienteVip;
import com.everis.bc.servicioCuentaCorrienteVip.model.Movimientos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceCta {
	
	public Mono<CuentaCorrienteVip> saveData(CuentaCorrienteVip cuenta);
	
	public Flux<CuentaCorrienteVip> getData();
	
	public Mono<CuentaCorrienteVip> getDataByDoc(String doc);

	public Mono<Map<String, Object>> getSaldo(String nro_cuenta);

	public Mono<Void> deleteData(String id);
	
	public Mono<CuentaCorrienteVip> editData(String id, CuentaCorrienteVip cuenta);
	
	public Mono<Map<String, Object>> savePagotdc(Movimientos mov);
	
	public Mono<Map<String, Object>> saveDeposito(Movimientos mov);
	
	public Mono<Map<String, Object>> saveRetiro(Movimientos mov);
	
	public Flux<Movimientos> getMovimientos(String nro_cuenta);
}
