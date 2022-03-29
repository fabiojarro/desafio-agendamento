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

import br.com.desafio.agenda.entity.Cliente;
import br.com.desafio.agenda.exception.ApiException;
import br.com.desafio.agenda.repository.ClienteRepository;
import br.com.desafio.agenda.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

	private ClienteRepository clienteRepository;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public void setClienteRepository(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}
	
	public Cliente save(Cliente cliente) {
		if(cliente.getId() != null) {
			Optional<Cliente> clienteOldOptional = clienteRepository.findById(cliente.getId());
			if(clienteOldOptional.isPresent()) {
				Cliente clienteExistente = clienteOldOptional.get();
				clienteExistente.setCpf(cliente.getCpf());
				clienteExistente.setNome(cliente.getNome());
				
				cliente = clienteRepository.save(clienteExistente);
				if(logger.isDebugEnabled()) {
					logger.debug(new StringBuilder(cliente.toString()).append(" atualizado com sucesso.").toString());
				}
				
				return cliente;
			}
			
			String message = new StringBuilder("O cliente com id ").append(cliente.getId()).append(" não foi encontrado.").toString();
			
			throw new ApiException(message);
		}
		
		cliente = clienteRepository.save(cliente);

		if(logger.isDebugEnabled()) {
			logger.debug(new StringBuilder(cliente.toString()).append(" criado com sucesso.").toString());
		}		
		return cliente;
	}

	public Collection<Cliente> list() {
		Iterable<Cliente> iterableCliente = clienteRepository.findAll();
		if(iterableCliente == null) {
			return Collections.emptyList();
		}

		List<Cliente> clientes = new ArrayList<>();
		iterableCliente.forEach(clientes::add);

		return clientes; 
	}

	public void remove(Integer id) {
		Optional<Cliente> cliente = getById(id);
		
		if(cliente.isPresent()) {
			clienteRepository.delete(cliente.get());
			if(logger.isDebugEnabled()) {
				logger.debug(new StringBuilder(cliente.get().toString()).append(" removido com sucesso.").toString());
			}
			return;
		}
		
		String message = new StringBuilder("O cliente com id ").append(id).append(" não foi encontrado.").toString();
		throw new ApiException(message);
	}

	@Override
	public Optional<Cliente> getById(Integer id) {
		return clienteRepository.findById(id);
	}
}
