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

import br.com.desafio.agenda.entity.Servico;
import br.com.desafio.agenda.exception.ApiException;
import br.com.desafio.agenda.repository.ServicoRepository;
import br.com.desafio.agenda.service.impl.ServicoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ServicoServiceTest {

	@Mock
	private ServicoRepository servicoRepository;
	
	@InjectMocks
	private ServicoServiceImpl servicoService;
	
	@Test
	public void testShouldReturnServicoColletion(){
		servicoService.list();
		verify(servicoRepository, times(1)).findAll();
		
		Iterable<Servico> servicos = Arrays.asList(new Servico());
		when(servicoRepository.findAll()).thenReturn(servicos);
		
		Collection<Servico> collection = servicoService.list();
		assertThat(collection.size(), is(1));
	}
	
	@Test
	public void testShouldThrowExceptionWhenRemoveServicoThatNotExists() {
		when(servicoRepository.findById(10)).thenReturn(Optional.empty());
		assertThrows(ApiException.class, () -> servicoService.remove(10));
	}
	
	@Test
	public void testShouldRemoveServico() {
		Servico Servico = new Servico();
		when(servicoRepository.findById(10)).thenReturn(Optional.of(Servico));
		
		servicoService.remove(10);
		
		verify(servicoRepository, times(1)).delete(Servico);
	}
	
	@Test
	public void testShouldGetServicoById(){
		Servico servico = new Servico();
		servico.setId(10);
		when(servicoRepository.findById(10)).thenReturn(Optional.of(servico));
		
		Optional<Servico> optionalServico = servicoService.getById(10);
		
		verify(servicoRepository, times(1)).findById(10);
		assertThat(optionalServico.get(), is(servico));
	}
	
	@Test
	public void testShouldCreateServico(){
		Servico servico = new Servico();
		servico.setCodigo("123456");
		servico.setDescricao("Serviço A");
		
		when(servicoRepository.save(servico)).thenReturn(servico);
		
		Servico servicoPersisted = servicoService.save(servico);
		
		verify(servicoRepository, times(1)).save(servico);
		assertThat(servicoPersisted, is(servico));
	}
	
	@Test
	public void testShouldUpdateServico(){
		Servico servico = new Servico();
		servico.setId(9);
		servico.setCodigo("123456");
		servico.setDescricao("Serviço A");
		
		when(servicoRepository.findById(9)).thenReturn(Optional.of(servico));
		when(servicoRepository.save(servico)).thenReturn(servico);
		
		Servico servicoPersisted = servicoService.save(servico);
		
		verify(servicoRepository, times(1)).save(servico);
		verify(servicoRepository, times(1)).findById(9);
		assertThat(servicoPersisted, is(servico));
	}
	
	@Test
	public void testShouldThrowExceptionWhenServicoNotExists(){
		Servico servico = new Servico();
		servico.setId(90);
		
		when(servicoRepository.findById(90)).thenReturn(Optional.empty());
		
		assertThrows(ApiException.class, () -> servicoService.save(servico));
		verify(servicoRepository, times(1)).findById(90);
	}
}
