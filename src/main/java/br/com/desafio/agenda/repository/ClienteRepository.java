package br.com.desafio.agenda.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.desafio.agenda.entity.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Integer>{}
