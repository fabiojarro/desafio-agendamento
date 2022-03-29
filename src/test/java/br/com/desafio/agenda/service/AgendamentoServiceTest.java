package br.com.desafio.agenda.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.desafio.agenda.bean.AgendamentoInputData;
import br.com.desafio.agenda.bean.AgendamentoQueryFilter;
import br.com.desafio.agenda.bean.GrupoDataAgendamentoViewModel;
import br.com.desafio.agenda.bean.GrupoSumarizadoClienteViewModel;
import br.com.desafio.agenda.bean.GrupoSumarizadoDataViewModel;
import br.com.desafio.agenda.bean.GrupoValorAgendamentoViewModel;
import br.com.desafio.agenda.bean.RemarcarAgendamentoInputData;
import br.com.desafio.agenda.entity.Agendamento;
import br.com.desafio.agenda.entity.Cliente;
import br.com.desafio.agenda.entity.Servico;
import br.com.desafio.agenda.exception.ApiException;
import br.com.desafio.agenda.repository.AgendamentoRepository;
import br.com.desafio.agenda.service.impl.AgendamentoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AgendamentoServiceTest {
	
	@Mock
	private ClienteService clienteService;
	
	@Mock
	private ServicoService servicoService;
	
	@Mock
	private AgendamentoRepository agendamentoRepository;
	
	@InjectMocks
	private AgendamentoServiceImpl agendamentoService;
	
	
	@Test
	public void testShouldReturnAgendamentoColletion(){
		agendamentoService.list();
		verify(agendamentoRepository, times(1)).findAll();
		
		Iterable<Agendamento> agendamentos = Arrays.asList(new Agendamento());
		when(agendamentoRepository.findAll()).thenReturn(agendamentos);
		
		Collection<Agendamento> collection = agendamentoService.list();
		assertThat(collection.size(), is(1));
	}
	
	@Test
	public void testShouldCreateAgendamento(){
		AgendamentoInputData input = new AgendamentoInputData();
		input.setClienteId(10);
		input.setServicoId(20);
		Date data = new Date();
		input.setData(data);
		input.setObservacao("uma observacao");
		
		Servico servico = new Servico();
		Cliente cliente = new Cliente();
		
		when(servicoService.getById(20)).thenReturn(Optional.of(servico));
		when(clienteService.getById(10)).thenReturn(Optional.of(cliente));
		when(agendamentoRepository.save(any())).thenReturn(new Agendamento());
		
		ArgumentCaptor<Agendamento> captor = ArgumentCaptor.forClass(Agendamento.class);
		agendamentoService.save(input);
		
		verify(agendamentoRepository, times(1)).save(captor.capture());
		
		Agendamento agendamento = captor.getValue();
		
		assertThat(agendamento.getData(), is(data));
		assertThat(agendamento.getCliente(), is(cliente));
		assertThat(agendamento.getServico(), is(servico));
		assertThat(agendamento.getObservacao(), is("uma observacao"));
	}
	
	@Test
	public void testShouldThrowExceptionWhenServicoNotExists(){
		AgendamentoInputData input = new AgendamentoInputData();
		input.setClienteId(10);
		input.setServicoId(20);
		Date data = new Date();
		input.setData(data);
		input.setObservacao("uma observacao");
		
		when(servicoService.getById(20)).thenReturn(Optional.empty());
		when(clienteService.getById(10)).thenReturn(Optional.of(new Cliente()));
		
		assertThrows(ApiException.class, () -> agendamentoService.save(input));
	}
	
	@Test
	public void testShouldThrowExceptionWhenClienteNotExists(){
		AgendamentoInputData input = new AgendamentoInputData();
		input.setClienteId(10);
		input.setServicoId(20);
		Date data = new Date();
		input.setData(data);
		input.setObservacao("uma observacao");
		
		when(clienteService.getById(10)).thenReturn(Optional.empty());
		
		assertThrows(ApiException.class, () -> agendamentoService.save(input));
	}

	@Test
	public void testShouldThrowExceptionWhenRescheduleAndAgendamentoNotExists(){
		RemarcarAgendamentoInputData input = new RemarcarAgendamentoInputData();
		input.setAgendamentoId(10);
		input.setData(new Date());
		
		when(agendamentoRepository.findById(10)).thenReturn(Optional.empty());
		
		assertThrows(ApiException.class, () -> agendamentoService.reschedule(input));
	}
	
	@Test
	public void testShouldRescheduleAgendamento(){
		RemarcarAgendamentoInputData input = new RemarcarAgendamentoInputData();
		input.setAgendamentoId(10);
		Date data = new Date();
		input.setData(data);
		Agendamento agendamento = mock(Agendamento.class);
				
		when(agendamentoRepository.findById(10)).thenReturn(Optional.of(agendamento));
		when(agendamentoRepository.save(agendamento)).thenReturn(agendamento);
		
		agendamentoService.reschedule(input);
		
		verify(agendamento, times(1)).setData(data);
		verify(agendamentoRepository, times(1)).save(agendamento);
	}
	
	@Test
	public void testShouldGroupByDateAndValue(){
		Cliente joao = createCliente("123", "joao", 10);
		Cliente marcos = createCliente("567", "marcos", 20);
		
		Servico servicoA = createServico("A", "A", new BigDecimal(1000), 13);
		Servico servicoB = createServico("B", "B", new BigDecimal(2000), 15);
		Servico servicoC = createServico("C", "C", new BigDecimal(2500), 17);
		
		Date today = Date.from(LocalDate.of(2022, 1, 8).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date yesterday = Date.from(LocalDate.of(2022, 1, 7).atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Agendamento agendamento1 = create(joao, servicoA, yesterday);
		Agendamento agendamento2 = create(joao, servicoB, yesterday);
		Agendamento agendamento3 = create(joao, servicoC, yesterday);
		Agendamento agendamento4 = create(marcos, servicoA, today);
		Agendamento agendamento5 = create(marcos, servicoB, yesterday);
		
		Iterable<Agendamento> agendamentos = Arrays.asList(agendamento1, agendamento2, agendamento3, agendamento4, agendamento5);
		
		when(agendamentoRepository.findAll()).thenReturn(agendamentos);
		
		Collection<GrupoDataAgendamentoViewModel> list = agendamentoService.listByGroup();
		
		assertThat(list.size(), is(2));
		Optional<GrupoDataAgendamentoViewModel> groupToday = list.stream().filter(group -> group.getData().equals("08/01/2022")).findAny();
		assertThat(groupToday.get().getValores().size(), is(1));
		
		Optional<GrupoValorAgendamentoViewModel> groupValue1000Today = groupToday.get().getValores()
				 .stream().filter(valor ->  valor.getValor().equals(new BigDecimal(1000))).findAny();
		assertThat(groupValue1000Today.get().getAgendamentos().size(), is(1));
		
		Optional<GrupoValorAgendamentoViewModel> groupValue2000Today = groupToday.get().getValores()
				 .stream().filter(valor ->  valor.getValor().equals(new BigDecimal(2000))).findAny();
		assertThat(groupValue2000Today.isPresent(), is(false));
		
		Optional<GrupoValorAgendamentoViewModel> groupValue2500Today = groupToday.get().getValores()
				 .stream().filter(valor ->  valor.getValor().equals(new BigDecimal(2500))).findAny();
		assertThat(groupValue2500Today.isPresent(), is(false));
		
		Optional<GrupoDataAgendamentoViewModel> groupYesterday = list.stream().filter(group -> group.getData().equals("07/01/2022")).findAny();
		assertThat(groupYesterday.get().getValores().size(), is(3));
		
		Optional<GrupoValorAgendamentoViewModel> groupValue1000Yesterday = groupYesterday.get().getValores()
				 .stream().filter(valor ->  valor.getValor().equals(new BigDecimal(1000)))
				 .findAny();
		assertThat(groupValue1000Yesterday.get().getAgendamentos().size(), is(1));
		
		Optional<GrupoValorAgendamentoViewModel> groupValue2000Yesterday = groupYesterday.get().getValores()
				 .stream().filter(valor ->  valor.getValor().equals(new BigDecimal(2000)))
				 .findAny();
		assertThat(groupValue2000Yesterday.get().getAgendamentos().size(), is(2));
		
		Optional<GrupoValorAgendamentoViewModel> groupValue2500Yesterday = groupYesterday.get().getValores()
				 .stream().filter(valor ->  valor.getValor().equals(new BigDecimal(2500)))
				 .findAny();
		
		assertThat(groupValue2500Yesterday.get().getAgendamentos().size(), is(1));
	}
	
	@Test
	public void testShouldSummarizeByClienteAndValorTotal(){
		Cliente joao = createCliente("123", "joao", 10);
		Cliente marcos = createCliente("567", "marcos", 20);
		
		Servico servicoA = createServico("A", "A", new BigDecimal(1000), 13);
		Servico servicoB = createServico("B", "B", new BigDecimal(2000), 15);
		Servico servicoC = createServico("C", "C", new BigDecimal(2500), 17);
		
		Date today = Date.from(LocalDate.of(2022, 1, 8).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date yesterday = Date.from(LocalDate.of(2022, 1, 7).atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Agendamento agendamento1 = create(joao, servicoA, yesterday);
		Agendamento agendamento2 = create(joao, servicoB, yesterday);
		Agendamento agendamento3 = create(joao, servicoC, yesterday);
		Agendamento agendamento4 = create(marcos, servicoA, today);
		Agendamento agendamento5 = create(marcos, servicoB, yesterday);
		
		Collection<Agendamento> agendamentos = Arrays.asList(agendamento1, agendamento2, agendamento3, agendamento4, agendamento5);
		
		when(agendamentoRepository.listBy(any())).thenReturn(agendamentos);
		
		Collection<GrupoSumarizadoClienteViewModel> collection = agendamentoService.summarize(new AgendamentoQueryFilter());
		
		assertThat(collection.size(), is(2));
		
		Optional<GrupoSumarizadoClienteViewModel> sumarizadoJoao = collection.stream().filter(group -> group.getCliente().equals(joao)).findAny();
		assertThat(sumarizadoJoao.get().getDatas().size(), is(1));
		Optional<GrupoSumarizadoDataViewModel> sumarizadoJoaoPorData = sumarizadoJoao.get().getDatas().stream().filter(data -> data.getData().equals("07/01/2022")).findAny();
		assertThat(sumarizadoJoaoPorData.get().getValorTotal(), is(new BigDecimal(5500)));
		
		Optional<GrupoSumarizadoClienteViewModel> sumarizadoMarcos = collection.stream().filter(group -> group.getCliente().equals(marcos)).findAny();
		assertThat(sumarizadoMarcos.get().getDatas().size(), is(2));
		
		Optional<GrupoSumarizadoDataViewModel> sumarizadoMarcosPorDataYesterday = sumarizadoMarcos.get().getDatas().stream().filter(data -> data.getData().equals("07/01/2022")).findAny();
		assertThat(sumarizadoMarcosPorDataYesterday.get().getValorTotal(), is(new BigDecimal(2000)));
		
		Optional<GrupoSumarizadoDataViewModel> sumarizadoMarcosPorDataToday = sumarizadoMarcos.get().getDatas().stream().filter(data -> data.getData().equals("08/01/2022")).findAny();
		assertThat(sumarizadoMarcosPorDataToday.get().getValorTotal(), is(new BigDecimal(1000)));
	}
	
	private Agendamento create(Cliente cliente, Servico servico, Date date) {
		Agendamento agendamento = new Agendamento();
		agendamento.setCliente(cliente);
		agendamento.setServico(servico);
		agendamento.setData(date);
		return agendamento;
	}
	
	private Cliente createCliente(String cpf, String nome, Integer id) {
		Cliente cliente = new Cliente();
		cliente.setCpf(cpf);
		cliente.setNome(nome);
		cliente.setId(id);
		return cliente;
	}
	
	private Servico createServico(String descricao, String codigo, BigDecimal valor, Integer id) {
		Servico servico = new Servico();
		servico.setId(id);
		servico.setDescricao(descricao);
		servico.setCodigo(codigo);
		servico.setValor(valor);
		return servico;
	}
}
