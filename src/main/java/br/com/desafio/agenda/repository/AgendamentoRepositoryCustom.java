package br.com.desafio.agenda.repository;

import java.util.Collection;

import br.com.desafio.agenda.bean.AgendamentoQueryFilter;
import br.com.desafio.agenda.entity.Agendamento;

public interface AgendamentoRepositoryCustom {

	public Collection<Agendamento> listBy(AgendamentoQueryFilter filter);
	
}
