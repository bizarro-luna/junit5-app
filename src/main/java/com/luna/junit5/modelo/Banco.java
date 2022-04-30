package com.luna.junit5.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {
	
	
	private String nombre;
	
	private List<Cuenta> cuentas;

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	/**
	 * @return the cuentas
	 */
	public List<Cuenta> getCuentas() {
		return cuentas;
	}

	/**
	 * @param cuentas the cuentas to set
	 */
	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}
	
	public void agregarCuenta(Cuenta cuenta) {
		
		if(cuentas==null) {
			cuentas= new ArrayList<Cuenta>();
		}
		
		cuentas.add(cuenta);
		cuenta.setBanco(this);
	}

	/**
	 * Metodo para tests
	 */
	public void transferir(Cuenta origen,Cuenta destino,BigDecimal monto) {
		
		origen.debito(monto);
		destino.credito(monto);
		
		
	}

}
