package br.com.desafio.agenda.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.desafio.agenda.entity.Agendamento;

public interface AgendamentoRepository extends CrudRepository<Agendamento, Integer>, AgendamentoRepositoryCustom{}
