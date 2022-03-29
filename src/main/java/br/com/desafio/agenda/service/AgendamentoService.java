package br.com.desafio.agenda.service;

import java.util.Collection;

import br.com.desafio.agenda.bean.AgendamentoInputData;
import br.com.desafio.agenda.bean.AgendamentoQueryFilter;
import br.com.desafio.agenda.bean.GrupoDataAgendamentoViewModel;
import br.com.desafio.agenda.bean.GrupoSumarizadoClienteViewModel;
import br.com.desafio.agenda.bean.RemarcarAgendamentoInputData;
import br.com.desafio.agenda.entity.Agendamento;

public interface AgendamentoService {
	
	public Collection<Agendamento> list();
	
	public Agendamento save(AgendamentoInputData agendamentoInputData);
	
	public Agendamento reschedule(RemarcarAgendamentoInputData remarcarAgendamentoInputData);
	
	public Collection<GrupoDataAgendamentoViewModel> listByGroup();
	
	public Collection<GrupoSumarizadoClienteViewModel> summarize(AgendamentoQueryFilter filter);
	
}
