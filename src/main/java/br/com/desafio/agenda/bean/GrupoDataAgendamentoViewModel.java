package br.com.desafio.agenda.bean;

import java.util.List;

public class GrupoDataAgendamentoViewModel {

	private String data;
	
	private List<GrupoValorAgendamentoViewModel> valores;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<GrupoValorAgendamentoViewModel> getValores() {
		return valores;
	}

	public void setValores(List<GrupoValorAgendamentoViewModel> valores) {
		this.valores = valores;
	}
}
