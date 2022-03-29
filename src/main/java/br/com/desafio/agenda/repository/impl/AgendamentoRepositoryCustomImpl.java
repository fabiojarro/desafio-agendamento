package br.com.desafio.agenda.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import br.com.desafio.agenda.bean.AgendamentoQueryFilter;
import br.com.desafio.agenda.entity.Agendamento;
import br.com.desafio.agenda.entity.Cliente;
import br.com.desafio.agenda.repository.AgendamentoRepositoryCustom;

@Repository
public class AgendamentoRepositoryCustomImpl implements AgendamentoRepositoryCustom {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Collection<Agendamento> listBy(AgendamentoQueryFilter filter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Agendamento> query = builder.createQuery(Agendamento.class);
        Root<Agendamento> root = query.from(Agendamento.class);
        Join<Agendamento, Cliente> joinCliente = root.join("cliente", JoinType.INNER);
        
        TypedQuery<Agendamento> typedQuery = entityManager.createQuery(query);
        
        if(filter == null || filter.isEmpty()) {
        	return typedQuery.getResultList();
        }
        
        List<Predicate> predicates = new ArrayList<Predicate>();
        if(filter.getClienteId() != null) {
        	Predicate filterCliente = builder.equal(joinCliente.get("id"), filter.getClienteId());
        	predicates.add(filterCliente);
        }
        
        if(filter.getData() != null) {
        	Predicate filterData = builder.equal(root.get("data"), filter.getData());
        	predicates.add(filterData);
        }
        
        query.where(predicates.toArray(new Predicate[predicates.size()]));

        query.select(root);
        
        typedQuery = entityManager.createQuery(query);
        
        if(typedQuery.getResultList() == null || typedQuery.getResultList().isEmpty()) {
        	return Collections.emptyList();
        }
        
        return typedQuery.getResultList();
	}

}
