package br.com.desafio.agenda.bean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AgendamentoQueryFilter {

	@DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
	private Date data;
	
	private Integer clienteId;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getClienteId() {
		return clienteId;
	}

	public void setClienteId(Integer clienteId) {
		this.clienteId = clienteId;
	}
	
	@JsonIgnore
	public boolean isEmpty() {
		return data == null && clienteId == null;
	}
}
