package com.everis.bc.servicioCuentaCorrienteVip.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.everis.bc.servicioCuentaCorrienteVip.model.CuentaCorrienteVip;
import com.everis.bc.servicioCuentaCorrienteVip.model.Movimientos;
import com.everis.bc.servicioCuentaCorrienteVip.service.ServiceCta;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CuentaCorrienteVipController {

	@Autowired
	private ServiceCta s_cuenta;

	@PostMapping("/saveCcorrienteVipData")
	public Mono<CuentaCorrienteVip> saveCcorrienteData(@RequestBody CuentaCorrienteVip cuenta){
		return s_cuenta.saveData(cuenta);
	}
	
	@GetMapping("/getCcorrienteVipData/{doc}")
	public Mono<CuentaCorrienteVip> getCcorrienteData(@PathVariable("doc") String doc){
		return s_cuenta.getDataByDoc(doc);
	}
	
	@GetMapping("/getCcorrienteVipSaldo/{nro_cuenta}")
	public Mono<Map<String, Object>> getCcorrienteSaldo(@PathVariable("nro_cuenta") String nro_cuenta){
		return s_cuenta.getSaldo(nro_cuenta);
	}
	
	@PostMapping("/savePagotdcCorrienteVip")
	public Mono<Map<String, Object>> savePagotdcCorriente(@RequestBody Movimientos movimiento){
		return s_cuenta.savePagotdc(movimiento);
	}
	
	@PostMapping("/saveDepositoCorrienteVip")
	public Mono<Map<String, Object>> saveDepositoCorriente(@RequestBody Movimientos movimiento){
		return s_cuenta.saveDeposito(movimiento);
	}
	
	@PostMapping("/saveRetiroCorrienteVip")
	public Mono<Map<String, Object>> saveRetiroCorriente(@RequestBody Movimientos movimiento){
		return s_cuenta.saveRetiro(movimiento);
	}
	
	@GetMapping("/getMovimientosCorrienteVip/{nro_cuenta}")
	public Flux<Movimientos> getMovimientosCorriente(@PathVariable("nro_cuenta") String nro_cuenta){
		return s_cuenta.getMovimientos(nro_cuenta);
	}
	
	@GetMapping("/getRangeMovimientosCorrienteVip/{nro_cuenta}/{from}/{to}")
	public Flux<Movimientos> getRangeMovimientosCorrienteVip(@PathVariable("nro_cuenta") String nro_cuenta, @PathVariable("from") String from, @PathVariable("to") String to){
		return s_cuenta.getRangeMovimientos(nro_cuenta, from, to);
	}

}
