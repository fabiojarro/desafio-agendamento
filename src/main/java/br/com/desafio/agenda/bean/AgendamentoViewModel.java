package br.com.desafio.agenda.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import br.com.desafio.agenda.entity.Agendamento;
import br.com.desafio.agenda.entity.Cliente;

public class AgendamentoViewModel {

	private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	
	private Agendamento agendamento;
	
	public AgendamentoViewModel(Agendamento agendamento) {
		this.agendamento = agendamento;
	}
	
	public Cliente getCliente() {
		return agendamento.getCliente();
	}
	
	public String getData() {
		return DATE_FORMAT.format(agendamento.getData());
	}
}
