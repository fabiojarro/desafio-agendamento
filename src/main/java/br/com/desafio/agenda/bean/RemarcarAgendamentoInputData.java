package br.com.desafio.agenda.bean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class RemarcarAgendamentoInputData {

	private Integer agendamentoId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
	private Date data;

	public Integer getAgendamentoId() {
		return agendamentoId;
	}

	public void setAgendamentoId(Integer agendamentoId) {
		this.agendamentoId = agendamentoId;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}
