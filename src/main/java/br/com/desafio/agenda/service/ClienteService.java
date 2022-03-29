package br.com.desafio.agenda.service;

import java.util.Collection;
import java.util.Optional;

import br.com.desafio.agenda.entity.Cliente;

public interface ClienteService {

	public Optional<Cliente> getById(Integer id);
	
	public Cliente save(Cliente cliente);
	
	public Collection<Cliente> list();
	
	public void remove(Integer id);
}
