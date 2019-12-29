package com.everis.bc.servicioCuentaCorrienteVip.service;


import java.util.List;
import java.util.Map;

import com.everis.bc.servicioCuentaCorrienteVip.model.CuentaCorrienteVip;
import com.everis.bc.servicioCuentaCorrienteVip.model.Deudores;
import com.everis.bc.servicioCuentaCorrienteVip.model.Movimientos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceCta {
	
	public Mono<CuentaCorrienteVip> saveData(CuentaCorrienteVip cuenta);
	
	public Flux<CuentaCorrienteVip> getData();
	
	public Flux<CuentaCorrienteVip> getDataByDoc(String doc);

	public Mono<Map<String, Object>> getSaldo(String nro_cuenta);

	public Mono<Void> deleteData(String id);
	
	public Mono<CuentaCorrienteVip> editData(String id, CuentaCorrienteVip cuenta);
	
	public Mono<Movimientos> savePagotdc(Movimientos mov);
	
	public Mono<Movimientos> saveDeposito(Movimientos mov);
	
	public Mono<Movimientos> saveRetiro(Movimientos mov);
	
	public Mono<Movimientos> getTransfer(Movimientos mov);
	
	public Mono<Movimientos> setTransfer(Movimientos mov);
	
	public Flux<Movimientos> getMovimientos(String nro_cuenta);
	
	public Flux<Movimientos> getRangeMovimientos(String nro_cuenta, String from, String to);
	
	public Flux<Deudores> saveDeudoresVip(List<Deudores> deudores);
}
