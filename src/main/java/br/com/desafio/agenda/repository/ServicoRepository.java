package br.com.desafio.agenda.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.desafio.agenda.entity.Servico;

public interface ServicoRepository extends CrudRepository<Servico, Integer>{}
