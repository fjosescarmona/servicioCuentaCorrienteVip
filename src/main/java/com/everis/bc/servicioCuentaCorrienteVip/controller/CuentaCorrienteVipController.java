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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Api(value="Current Account VIP", description="All operations and transations")
public class CuentaCorrienteVipController {

	@Autowired
	private ServiceCta s_cuenta;
	
	@ApiOperation(value = "Create and save a VIP account", response = CuentaCorrienteVip.class)
	@PostMapping("/saveCcorrienteVipData")
	public Mono<CuentaCorrienteVip> saveCcorrienteData(@RequestBody CuentaCorrienteVip cuenta){
		return s_cuenta.saveData(cuenta);
	}
	
	@ApiOperation(value = "Get VIP account data by doc number", response = CuentaCorrienteVip.class)
	@GetMapping("/getCcorrienteVipData/{doc}")
	public Mono<CuentaCorrienteVip> getCcorrienteData(@PathVariable("doc") String doc){
		return s_cuenta.getDataByDoc(doc);
	}
	
	@ApiOperation(value = "Get VIP account balance by account number")
	@GetMapping("/getCcorrienteVipSaldo/{nro_cuenta}")
	public Mono<Map<String, Object>> getCcorrienteSaldo(@PathVariable("nro_cuenta") String nro_cuenta){
		return s_cuenta.getSaldo(nro_cuenta);
	}
	
	@ApiOperation(value = "Save credit cards payments trough VIP account")
	@PostMapping("/savePagotdcCorrienteVip")
	public Mono<Map<String, Object>> savePagotdcCorriente(@RequestBody Movimientos movimiento){
		return s_cuenta.savePagotdc(movimiento);
	}
	
	@ApiOperation(value = "Save deposits trough VIP account")
	@PostMapping("/saveDepositoCorrienteVip")
	public Mono<Map<String, Object>> saveDepositoCorriente(@RequestBody Movimientos movimiento){
		return s_cuenta.saveDeposito(movimiento);
	}
	
	@ApiOperation(value = "Save retirements trough VIP account")
	@PostMapping("/saveRetiroCorrienteVip")
	public Mono<Map<String, Object>> saveRetiroCorriente(@RequestBody Movimientos movimiento){
		return s_cuenta.saveRetiro(movimiento);
	}
	
	@ApiOperation(value = "Get all VIP account movements by account number", response = Movimientos.class)
	@GetMapping("/getMovimientosCorrienteVip/{nro_cuenta}")
	public Flux<Movimientos> getMovimientosCorriente(@PathVariable("nro_cuenta") String nro_cuenta){
		return s_cuenta.getMovimientos(nro_cuenta);
	}
	
	@ApiOperation(value = "Get VIP account movements by account number and date range", response = Movimientos.class)
	@GetMapping("/getRangeMovimientosCorrienteVip/{nro_cuenta}/{from}/{to}")
	public Flux<Movimientos> getRangeMovimientosCorrienteVip(@PathVariable("nro_cuenta") String nro_cuenta, @PathVariable("from") String from, @PathVariable("to") String to){
		return s_cuenta.getRangeMovimientos(nro_cuenta, from, to);
	}

}
