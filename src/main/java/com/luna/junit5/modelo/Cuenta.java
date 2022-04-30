package com.luna.junit5.modelo;

import java.math.BigDecimal;

import com.luna.junit5.exceptions.DineroException;

public class Cuenta {
	
	private String persona;
	
	/**
	 * Es mas preciso trabajar con BigDecimal, BigInteger son mas exactas para numeros grandes
	 */
	private BigDecimal saldo;
	
	private Banco banco;
	
	

	public Cuenta(String persona, BigDecimal saldo) {
		this.persona = persona;
		this.saldo = saldo;
	}

	/**
	 * @return the persona
	 */
	public String getPersona() {
		return persona;
	}

	/**
	 * @param persona the persona to set
	 */
	public void setPersona(String persona) {
		this.persona = persona;
	}

	/**
	 * @return the saldo
	 */
	public BigDecimal getSaldo() {
		return saldo;
	}

	/**
	 * @param saldo the saldo to set
	 */
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	

	/**
	 * @return the banco
	 */
	public Banco getBanco() {
		return banco;
	}

	/**
	 * @param banco the banco to set
	 */
	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj ==null || !(obj instanceof Cuenta) ) {
			return false;
		}
		Cuenta c= (Cuenta) obj;
		if(this.persona==null || this.saldo==null) {
			return false;
		}
		
		return this.persona.equals(c.getPersona()) && this.saldo.equals(c.getSaldo());
	}
	
	
	
	public void debito(BigDecimal monto) {
		//Susbtrac es como una resta para el saldo
		BigDecimal b=this.saldo.subtract(monto);
		
		if(b.compareTo(BigDecimal.ZERO)<0) {
			throw new DineroException("Dinero Insuficiente");
		}
		
		this.saldo=b;
		
	}
	
	
	public void credito(BigDecimal monto) {
		//.add le suma lo que venga en el monto
		this.saldo = this.saldo.add(monto);
		
	}
	
	
	

}


















