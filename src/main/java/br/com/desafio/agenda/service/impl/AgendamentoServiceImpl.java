package br.com.desafio.agenda.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import br.com.desafio.agenda.service.AgendamentoService;
import br.com.desafio.agenda.service.ClienteService;
import br.com.desafio.agenda.service.ServicoService;

@Service
public class AgendamentoServiceImpl implements AgendamentoService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private AgendamentoRepository agendamentoRepository;
	
	private ServicoService servicoService;
	
	private ClienteService clienteService; 
	
	@Autowired
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	
	@Autowired
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	@Autowired
	public void setAgendamentoRepository(AgendamentoRepository agendamentoRepository) {
		this.agendamentoRepository = agendamentoRepository;
	}

	@Override
	public Collection<Agendamento> list() {
		Iterable<Agendamento> iterableCliente = agendamentoRepository.findAll();
		if(iterableCliente == null) {
			return Collections.emptyList();
		}

		List<Agendamento> agendamentos = new ArrayList<>();
		iterableCliente.forEach(agendamentos::add);

		return agendamentos;
	}

	@Override
	public Agendamento save(AgendamentoInputData agendamentoInputData) {
		Optional<Cliente> cliente = clienteService.getById(agendamentoInputData.getClienteId());
		
		if(!cliente.isPresent()) {
			throw new ApiException("O cliente informado não existe");
		}
		
		Optional<Servico> servico = servicoService.getById(agendamentoInputData.getServicoId());
		
		if(!servico.isPresent()) {
			throw new ApiException("O serviço informado não existe.");
		}
		
		Agendamento agendamento = new Agendamento();
		agendamento.setData(agendamentoInputData.getData());
		agendamento.setObservacao(agendamentoInputData.getObservacao());
		agendamento.setCliente(cliente.get());
		agendamento.setServico(servico.get());
		
		agendamento = agendamentoRepository.save(agendamento);
		
		if(logger.isDebugEnabled()) {
			logger.debug(new StringBuilder(agendamento.toString()).append(" criado com sucesso!").toString());
		}
		
		return agendamento;
	}

	@Override
	public Agendamento reschedule(RemarcarAgendamentoInputData remarcarAgendamentoInputData) {
		Optional<Agendamento> agendamentoOptional = agendamentoRepository.findById(remarcarAgendamentoInputData.getAgendamentoId());
		
		if(agendamentoOptional.isPresent()) {
			Agendamento agendamento = agendamentoOptional.get();
			agendamento.setData(remarcarAgendamentoInputData.getData());
			agendamento = agendamentoRepository.save(agendamento);
			
			if(logger.isDebugEnabled()) {
				logger.debug(new StringBuilder(agendamento.toString()).append(" remarcado com sucesso!").toString());
			}
			
			return agendamento;
		}
		
		throw new ApiException("Agendamento não encontrado.");
	}

	@Override
	public Collection<GrupoDataAgendamentoViewModel> listByGroup() {
		Collection<Agendamento> agendamentos = this.list();

		Map<String, Map<BigDecimal, List<Agendamento>>>  grupos = agendamentos
																	.stream()
																	.collect(Collectors.groupingBy(Agendamento::getSimpleData, Collectors.groupingBy(Agendamento::getValor)));
		
		List<GrupoDataAgendamentoViewModel> list = new ArrayList<>();
		grupos.forEach((data,agendamentosPorValor) -> {
			
			List<GrupoValorAgendamentoViewModel> valores = new ArrayList<>();
			GrupoDataAgendamentoViewModel grupoDataAgendamentoViewModel = new GrupoDataAgendamentoViewModel();
			
			agendamentosPorValor.forEach((valor,listaAgendamentos) -> {
				GrupoValorAgendamentoViewModel grupoValorAgendamentoViewModel = new GrupoValorAgendamentoViewModel();
				grupoValorAgendamentoViewModel.setValor(valor);
				grupoValorAgendamentoViewModel.setAgendamentos(listaAgendamentos);
				valores.add(grupoValorAgendamentoViewModel);
			});
			
			grupoDataAgendamentoViewModel.setData(data);
			grupoDataAgendamentoViewModel.setValores(valores);
			list.add(grupoDataAgendamentoViewModel);
		});
		
		return list;
	}

	@Override
	public Collection<GrupoSumarizadoClienteViewModel> summarize(AgendamentoQueryFilter filter) {
		Collection<Agendamento> agendamentos = this.agendamentoRepository.listBy(filter);
		
		Map<Cliente, Map<String, DoubleSummaryStatistics>> grupos = agendamentos
				.stream()
				.collect(
						Collectors.groupingBy(Agendamento::getCliente, Collectors.groupingBy(
								Agendamento::getSimpleData , Collectors.summarizingDouble(a -> a.getValor().doubleValue()))));
		
		List<GrupoSumarizadoClienteViewModel> list = new ArrayList<>();
		grupos.forEach((cliente,valoresPorData) -> {
			
			GrupoSumarizadoClienteViewModel clienteViewModel = new GrupoSumarizadoClienteViewModel();
			List<GrupoSumarizadoDataViewModel> datas = new ArrayList<>();
			
			valoresPorData.forEach((data,valorSumarizado) -> {
				GrupoSumarizadoDataViewModel dataViewModel = new GrupoSumarizadoDataViewModel();
				dataViewModel.setData(data);
				dataViewModel.setValorTotal(new BigDecimal(valorSumarizado.getSum()));
				datas.add(dataViewModel);
			});
			
			clienteViewModel.setCliente(cliente);
			clienteViewModel.setDatas(datas);
			list.add(clienteViewModel);
		});
		
		return list;
	}
}
