package br.com.desafio.agenda.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.desafio.agenda.entity.Cliente;
import br.com.desafio.agenda.exception.ApiException;
import br.com.desafio.agenda.repository.ClienteRepository;
import br.com.desafio.agenda.service.impl.ClienteServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

	@Mock
	private ClienteRepository clienteRepository;
	
	@InjectMocks
	private ClienteServiceImpl clienteService;
	
	@Test
	public void testShouldReturnClienteColletion(){
		clienteService.list();
		verify(clienteRepository, times(1)).findAll();
		
		Iterable<Cliente> clientes = Arrays.asList(new Cliente());
		when(clienteRepository.findAll()).thenReturn(clientes);
		
		Collection<Cliente> collection = clienteService.list();
		assertThat(collection.size(), is(1));
	}
	
	@Test
	public void testShouldThrowExceptionWhenRemoveClienteThatNotExists() {
		when(clienteRepository.findById(10)).thenReturn(Optional.empty());
		assertThrows(ApiException.class, () -> clienteService.remove(10));
	}
	
	@Test
	public void testShouldRemoveCliente() {
		Cliente cliente = new Cliente();
		when(clienteRepository.findById(10)).thenReturn(Optional.of(cliente));
		
		clienteService.remove(10);
		
		verify(clienteRepository, times(1)).delete(cliente);
	}
	
	@Test
	public void testShouldGetClienteById(){
		Cliente cliente = new Cliente();
		cliente.setId(10);
		when(clienteRepository.findById(10)).thenReturn(Optional.of(cliente));
		
		Optional<Cliente> optionalCliente = clienteService.getById(10);
		
		verify(clienteRepository, times(1)).findById(10);
		assertThat(optionalCliente.get(), is(cliente));
	}
	
	@Test
	public void testShouldCreateCliente(){
		Cliente cliente = new Cliente();
		cliente.setCpf("123456");
		cliente.setNome("Mario");
		
		when(clienteRepository.save(cliente)).thenReturn(cliente);
		
		Cliente clientePersisted = clienteService.save(cliente);
		
		verify(clienteRepository, times(1)).save(cliente);
		assertThat(clientePersisted, is(cliente));
	}
	
	@Test
	public void testShouldUpdateCliente(){
		Cliente cliente = new Cliente();
		cliente.setId(9);
		cliente.setCpf("123456");
		cliente.setNome("Mario");
		
		when(clienteRepository.findById(9)).thenReturn(Optional.of(cliente));
		when(clienteRepository.save(cliente)).thenReturn(cliente);
		
		Cliente clientePersisted = clienteService.save(cliente);
		
		verify(clienteRepository, times(1)).save(cliente);
		verify(clienteRepository, times(1)).findById(9);
		assertThat(clientePersisted, is(cliente));
	}
	
	@Test
	public void testShouldThrowExceptionWhenClienteNotExists(){
		Cliente cliente = new Cliente();
		cliente.setId(90);
		
		when(clienteRepository.findById(90)).thenReturn(Optional.empty());
		
		assertThrows(ApiException.class, () -> clienteService.save(cliente));
		verify(clienteRepository, times(1)).findById(90);
	}
}
