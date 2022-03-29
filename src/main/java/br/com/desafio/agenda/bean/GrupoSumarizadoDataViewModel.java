package br.com.desafio.agenda.bean;

import java.math.BigDecimal;

public class GrupoSumarizadoDataViewModel {

	private String data;
	
	private BigDecimal valorTotal;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
}
