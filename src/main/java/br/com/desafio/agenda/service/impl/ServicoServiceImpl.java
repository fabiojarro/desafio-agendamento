package br.com.desafio.agenda.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.desafio.agenda.entity.Servico;
import br.com.desafio.agenda.exception.ApiException;
import br.com.desafio.agenda.repository.ServicoRepository;
import br.com.desafio.agenda.service.ServicoService;

@Service
public class ServicoServiceImpl implements ServicoService{

	private ServicoRepository servicoRepository;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public void setServicoRepository(ServicoRepository servicoRepository) {
		this.servicoRepository = servicoRepository;
	}
	
	@Override
	public Optional<Servico> getById(Integer id) {
		return servicoRepository.findById(id);
	}

	@Override
	public Servico save(Servico servico) {
	
		if(servico.getId() != null) {
			Optional<Servico> servicoOldOptional = servicoRepository.findById(servico.getId());
			if(servicoOldOptional.isPresent()) {
				Servico servicoExistente = servicoOldOptional.get();
				servicoExistente.setCodigo(servico.getCodigo());
				servicoExistente.setDescricao(servico.getDescricao());
				servicoExistente.setValor(servico.getValor());
				
				servico = servicoRepository.save(servicoExistente);
				if(logger.isDebugEnabled()) {
					logger.debug(new StringBuilder(servico.toString()).append(" atualizado com sucesso.").toString());
				}
				
				return servico;
			}
			
			String message = new StringBuilder("O serviço com id ").append(servico.getId()).append(" não foi encontrado.").toString();
			
			throw new ApiException(message);
		}
		
		servico = servicoRepository.save(servico);

		if(logger.isDebugEnabled()) {
			logger.debug(new StringBuilder(servico.toString()).append(" criado com sucesso.").toString());
		}		
		return servico;
	}

	@Override
	public Collection<Servico> list() {
		Iterable<Servico> iterableCliente = servicoRepository.findAll();
		if(iterableCliente == null) {
			return Collections.emptyList();
		}

		List<Servico> servicos = new ArrayList<>();
		iterableCliente.forEach(servicos::add);

		return servicos;
	}

	@Override
	public void remove(Integer id) {
		Optional<Servico> servico = getById(id);
		
		if(servico.isPresent()) {
			servicoRepository.delete(servico.get());
			if(logger.isDebugEnabled()) {
				logger.debug(new StringBuilder(servico.get().toString()).append(" removido com sucesso.").toString());
			}
			return;
		}
		
		String message = new StringBuilder("O serviço com id ").append(id).append(" não foi encontrado.").toString();
		throw new ApiException(message);
		
	}

}
