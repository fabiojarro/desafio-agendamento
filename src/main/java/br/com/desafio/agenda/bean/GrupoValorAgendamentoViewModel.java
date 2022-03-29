package br.com.desafio.agenda.bean;

import java.math.BigDecimal;
import java.util.List;

import br.com.desafio.agenda.entity.Agendamento;

public class GrupoValorAgendamentoViewModel {

	private BigDecimal valor;
	
	private List<Agendamento> agendamentos;

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public List<Agendamento> getAgendamentos() {
		return agendamentos;
	}

	public void setAgendamentos(List<Agendamento> agendamentos) {
		this.agendamentos = agendamentos;
	}
}
