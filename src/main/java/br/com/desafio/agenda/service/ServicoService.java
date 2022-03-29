package br.com.desafio.agenda.service;

import java.util.Collection;
import java.util.Optional;

import br.com.desafio.agenda.entity.Servico;

public interface ServicoService {

	public Optional<Servico> getById(Integer id);
	
	public Servico save(Servico servico);
	
	public Collection<Servico> list();
	
	public void remove(Integer id);
}
