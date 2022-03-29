package br.com.desafio.agenda.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AgendamentoInputData {

	private Integer clienteId;
	
	private Integer servicoId;
	
	@JsonFormat(pattern="yyyy-MM-dd-HH:mm:ss")
	private Date data;
	
	private String observacao;

	public Integer getClienteId() {
		return clienteId;
	}

	public void setClienteId(Integer clienteId) {
		this.clienteId = clienteId;
	}

	public Integer getServicoId() {
		return servicoId;
	}

	public void setServicoId(Integer servicoId) {
		this.servicoId = servicoId;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}
