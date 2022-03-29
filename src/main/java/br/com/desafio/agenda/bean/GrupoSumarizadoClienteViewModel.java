package br.com.desafio.agenda.bean;

import java.util.List;

import br.com.desafio.agenda.entity.Cliente;

public class GrupoSumarizadoClienteViewModel {

	private Cliente cliente;
	
	private List<GrupoSumarizadoDataViewModel> datas;

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<GrupoSumarizadoDataViewModel> getDatas() {
		return datas;
	}

	public void setDatas(List<GrupoSumarizadoDataViewModel> datas) {
		this.datas = datas;
	}	
}
