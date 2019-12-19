package com.everis.bc.servicioCuentaCorrienteVip.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.everis.bc.servicioCuentaCorrienteVip.model.CuentaCorrienteVip;
import com.everis.bc.servicioCuentaCorrienteVip.model.Listas;
import com.everis.bc.servicioCuentaCorrienteVip.model.Movimientos;
import com.everis.bc.servicioCuentaCorrienteVip.repository.Repo;
import com.everis.bc.servicioCuentaCorrienteVip.repository.RepoMovimientos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Configuration
public class ServiceCtaImplement implements ServiceCta {

	@Autowired
	private Repo repo1;
	@Autowired
	private RepoMovimientos repoMov;
	@Autowired
	WebClient client;
	@Value("${valores.comision}")
	private double comision;
	@Value("${valores.movesxmonth}")
	private int movesxmonth;
	@Value("${valores.montominapertura}")
	private double montominapertura;

	private List<String> docs;

	@Override
	public Mono<CuentaCorrienteVip> saveData(CuentaCorrienteVip cuenta) {
		//Map<String, Object> respuesta = new HashMap<String, Object>();
		CuentaCorrienteVip ccv=new CuentaCorrienteVip();
		List<String> doc = new ArrayList<>();
		for (Listas h : cuenta.getTitulares()) {
			doc.add(h.getDoc());
		}
		Date fecha=new Date();
		
		cuenta.setMovesxmonth(movesxmonth);
		cuenta.setLastmove(fecha);
		if(cuenta.getSaldo()>=montominapertura) {
			return repo1.findByTitularesDocList(doc).flatMap(ctas -> {
				return Mono.just(ctas);
			}).switchIfEmpty(repo1.save(cuenta).flatMap(cta -> {
				return Mono.just(cta);
			})).next();
		}else {
			return Mono.just(ccv);
		}
		

		/*
		 * return repo1.findByTitularesDocList(docs).map(ctadb -> {
		 * respuesta.put("Error", "El cliente ya posee una cuenta corriente"); return
		 * respuesta; }).switchIfEmpty( return repo1.save(cuenta).map(cta->{
		 * respuesta.put("Mensaje: ", "guardado correcto"); return respuesta; }); );
		 * 
		 * });
		 */

	}

	@Override
	public Flux<CuentaCorrienteVip> getData() {
		// TODO Auto-generated method stub
		return repo1.findAll();
	}

	@Override
	public Mono<Void> deleteData(String id) {
		// TODO Auto-generated method stub
		return repo1.findById(id).flatMap(cta -> repo1.delete(cta));
	}

	@Override
	public Mono<CuentaCorrienteVip> editData(String id, CuentaCorrienteVip cuenta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Map<String, Object>> savePagotdc(Movimientos movimiento) {
		Map<String, Object> respuesta = new HashMap<String, Object>();

		return repo1.findByNro_cuenta(movimiento.getNro_cuenta()).map(cta -> {
			Double saldo = cta.getSaldo();

			if (saldo >= movimiento.getMonto()) {
				cta.setSaldo(saldo - movimiento.getMonto());
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				LocalDateTime now = LocalDateTime.now();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("nro_tarjeta", movimiento.getNro_tarjeta());
				params.put("descripcion", "pago");
				params.put("monto", movimiento.getMonto());
				params.put("fecha", movimiento.getFecha());

				client.post().uri("/tc/saveMovimientosTC").accept(MediaType.APPLICATION_JSON_UTF8)
						.body(BodyInserters.fromObject(params)).retrieve().bodyToMono(Movimientos.class).subscribe();
				// System.out.println(dtf.format(now));
				repo1.save(cta).subscribe();
				repoMov.save(movimiento).subscribe();
				respuesta.put("Result",
						"Pago realizado, su nuevo saldo de cuenta es: " + (saldo - movimiento.getMonto()));
				return respuesta;
			} else {

				respuesta.put("Result", "Su saldo no es suficiente para realizar la operaciòn");
				return respuesta;
			}

		});

	}

	@Override
	public Flux<Movimientos> getMovimientos(String nro_cuenta) {
		// TODO Auto-generated method stub
		return repoMov.findByNro_cuenta(nro_cuenta);
	}

	@Override
	public Mono<CuentaCorrienteVip> getDataByDoc(String doc) {
		// TODO Auto-generated method stub
		return repo1.findByTitularesDoc(doc).switchIfEmpty(Mono.just("").flatMap(r -> {
			CuentaCorrienteVip cta2 = new CuentaCorrienteVip();
			return Mono.just(cta2);
		}));
	}

	@Override
	public Mono<Map<String, Object>> getSaldo(String nro_cuenta) {
		// TODO Auto-generated method stub
		Map<String, Object> respuesta = new HashMap<String, Object>();

		return repo1.findByNro_cuenta(nro_cuenta).map(cta -> {
			respuesta.put("saldo", cta.getSaldo());
			return respuesta;
		});
		// return null;
	}

	@Override
	public Mono<Map<String, Object>> saveDeposito(Movimientos mov) {
		// TODO Auto-generated method stub
		Map<String, Object> respuesta = new HashMap<String, Object>();
		Date fecha=new Date();
		return repo1.findByNro_cuenta(mov.getNro_cuenta()).map(cta -> {
			//-----------------valida si tiene movimientos disponibles en el mes-----------------//
			if (cta.getLastmove().getMonth() == mov.getFecha().getMonth() && cta.getMovesxmonth() > 0) {
				Double saldo = cta.getSaldo();
				cta.setSaldo(saldo + mov.getMonto());
				cta.setMovesxmonth(cta.getMovesxmonth() - 1);
				cta.setLastmove(fecha);
				repo1.save(cta).subscribe();
				repoMov.save(mov).subscribe();
				respuesta.put("Result", "Deposito realizado, su nuevo saldo es: " + (saldo + mov.getMonto()));
				return respuesta;
			} else {
			//-si el mes de la transaccion es distinto reinicia la cantidad de movimientos por mes-//
				if (cta.getLastmove().getMonth() != mov.getFecha().getMonth()) {
					Double saldo = cta.getSaldo();
					cta.setSaldo(saldo + mov.getMonto());
					cta.setLastmove(fecha);
					cta.setMovesxmonth(4);
					repo1.save(cta).subscribe();
					repoMov.save(mov).subscribe();
					respuesta.put("Result", "Deposito realizado, su nuevo saldo es: " + (saldo + mov.getMonto()));
					return respuesta;
			//--si no tiene movimientos disponibles en el mes aplica el cobro de comision--//
				} else {
					Double saldo = cta.getSaldo();
					//Double comision = 15.00;
					
					if ((saldo+mov.getMonto()) - comision >=0) {
						cta.setSaldo(saldo + mov.getMonto() - comision);
						cta.setLastmove(fecha);
						mov.setComision(comision);
						repo1.save(cta).subscribe();
						repoMov.save(mov).subscribe();
						respuesta.put("Result", "Deposito realizado, su nuevo saldo es S/: " + (saldo + mov.getMonto())
								+ ". Se cobro una comision de: S/" + comision);
						return respuesta;
					} else {
						respuesta.put("Result", "Su saldo no es suficiente para realizar la operaciòn");
						return respuesta;
					}
				}
			}
			/*
			 * Double saldo=cta.getSaldo(); cta.setSaldo(saldo+mov.getMonto());
			 * repo1.save(cta).subscribe(); repoMov.save(mov).subscribe();
			 * respuesta.put("Result",
			 * "Deposito realizado, su nuevo saldo es: "+(saldo+mov.getMonto())); return
			 * respuesta;
			 */
		});

	}

	@Override
	public Mono<Map<String, Object>> saveRetiro(Movimientos mov) {
		// TODO Auto-generated method stub
		Map<String, Object> respuesta = new HashMap<String, Object>();
		Date fecha=new Date();
		return repo1.findByNro_cuenta(mov.getNro_cuenta()).map(cta -> {
			//-----------------valida si tiene movimientos disponibles en el mes-----------------//
			if (cta.getLastmove().getMonth() == mov.getFecha().getMonth() && cta.getMovesxmonth() > 0) {
				Double saldo = cta.getSaldo();
				cta.setSaldo(saldo + mov.getMonto());
				cta.setLastmove(fecha);
				cta.setMovesxmonth(cta.getMovesxmonth() - 1);
				if (saldo >= mov.getMonto()) {
					cta.setSaldo(saldo - mov.getMonto());
					repo1.save(cta).subscribe();
					repoMov.save(mov).subscribe();
					respuesta.put("Result", "Retiro realizado, su nuevo saldo es: " + (saldo - mov.getMonto()));
					return respuesta;
				} else {
					respuesta.put("Result", "Su saldo no es suficiente para realizar la operaciòn");
					return respuesta;
				}

			} else {
			//-si el mes de la transaccion es distinto reinicia la cantidad de movimientos por mes-//
				if (cta.getLastmove().getMonth() != mov.getFecha().getMonth()) {
					Double saldo = cta.getSaldo();
					cta.setSaldo(saldo + mov.getMonto());
					if (saldo >= mov.getMonto()) {
						cta.setSaldo(saldo - mov.getMonto());
						cta.setLastmove(fecha);
						cta.setMovesxmonth(4);
						repo1.save(cta).subscribe();
						repoMov.save(mov).subscribe();
						respuesta.put("Result", "Retiro realizado, su nuevo saldo es: " + (saldo - mov.getMonto()));
						return respuesta;
					} else {
						respuesta.put("Result", "Su saldo no es suficiente para realizar la operaciòn");
						return respuesta;
					}
		//--si no tiene movimientos disponibles en el mes aplica el cobro de comision--//
				} else {
					Double saldo = cta.getSaldo();
					//Double comision = 15.0;
					cta.setSaldo(saldo + mov.getMonto());
					cta.setLastmove(fecha);
					if (saldo >= mov.getMonto() + comision) {
						cta.setSaldo(saldo - mov.getMonto() - comision);
						repo1.save(cta).subscribe();
						repoMov.save(mov).subscribe();
						respuesta.put("Result", "Retiro realizado, su nuevo saldo es: " + (saldo - mov.getMonto())
								+ ". Se cobro una comision de: S/" + comision);
						return respuesta;
					} else {
						respuesta.put("Result", "Su saldo no es suficiente para realizar la operaciòn");
						return respuesta;
					}
				}
			}

		});
	}

	@Override
	public Flux<Movimientos> getRangeMovimientos(String nro_cuenta, String from, String to) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		 try {
			
			Date first = df.parse(from);
			Date last = df.parse(to);
			System.out.println(first.toString()+" "+last);
			
			//return repoMov.findAllDateRangeByNro_cuenta(nro_cuenta, first, last);
			return repoMov.findByFechaBetween(first, last).filter(moves->moves.getNro_cuenta().equals(nro_cuenta));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
		
	}
}
