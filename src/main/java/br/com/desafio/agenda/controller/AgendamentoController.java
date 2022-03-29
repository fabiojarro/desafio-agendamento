package br.com.desafio.agenda.controller;


import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.agenda.bean.AgendamentoInputData;
import br.com.desafio.agenda.bean.AgendamentoQueryFilter;
import br.com.desafio.agenda.bean.AgendamentoViewModel;
import br.com.desafio.agenda.bean.ErrorMessage;
import br.com.desafio.agenda.bean.GrupoDataAgendamentoViewModel;
import br.com.desafio.agenda.bean.GrupoSumarizadoClienteViewModel;
import br.com.desafio.agenda.bean.RemarcarAgendamentoInputData;
import br.com.desafio.agenda.entity.Agendamento;
import br.com.desafio.agenda.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

	private AgendamentoService agendamentoService;
	
	@Autowired
	public void setAgendamentoService(AgendamentoService agendamentoService) {
		this.agendamentoService = agendamentoService;
	}
	
	@Operation(summary = "Listar agendamentos.",tags = {"agendamento"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Lista de agendamentos.", 
					content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AgendamentoViewModel.class)))}),
			@ApiResponse(responseCode = "400", description = "Os parâmetros recebidos são inválidos",
						 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))}),
			@ApiResponse(responseCode = "404", description = "O cliente não foi encontrado para a alteração.",
			 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))})
	})
	@GetMapping
	public Collection<AgendamentoViewModel> list(){
		Collection<Agendamento> agendamentos = agendamentoService.list();
		
		if(agendamentos == null || agendamentos.isEmpty()) {
			return Collections.emptyList();
		}
		
		return agendamentos.stream()
						   .map(agendamento -> new AgendamentoViewModel(agendamento))
						   .collect(Collectors.toList());
	}
	
	@Operation(summary = "Listar agendamentos agrupados por data e valor.", tags = {"agendamento"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Lista de agendamentos agrupados.", 
					content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = GrupoDataAgendamentoViewModel.class)))}),
			@ApiResponse(responseCode = "400", description = "Os parâmetros recebidos são inválidos",
						 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))}),
			@ApiResponse(responseCode = "404", description = "O cliente não foi encontrado para a alteração.",
			 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))})
	})
	@GetMapping("/agrupar")
	public Collection<GrupoDataAgendamentoViewModel> listByGroup(){
		return agendamentoService.listByGroup();
	}
	
	@Operation(summary = "Criar agendamento.",
			parameters = {
					@Parameter(name = "data", description = "Filtro de data. Formato: yyyy-MM-dd-HH:mm:ss", required = false, in = ParameterIn.QUERY),
					@Parameter(name = "clienteId", description = "Filtro de id do cliente", required = false, in = ParameterIn.QUERY)
			},			
			tags = {"agendamento"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "201", description = "Agendamento criado.", 
					content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = GrupoSumarizadoClienteViewModel.class)))}),
			@ApiResponse(responseCode = "400", description = "Os parâmetros recebidos são inválidos",
						 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))})
	})	
	@GetMapping("/sumarizar")
	public Collection<GrupoSumarizadoClienteViewModel>summarize(@Parameter(hidden = true, required = false) AgendamentoQueryFilter filter){
		return agendamentoService.summarize(filter);
	}
	
	@Operation(summary = "Criar agendamento.",tags = {"agendamento"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(value = "{\"clienteId\":0,\"servicoId\":0,\"data\":\"2022-01-02-13:59:59\",\"observacao\":\"string\"}"))))
	@ApiResponses( value = {
			@ApiResponse(responseCode = "201", description = "Agendamento criado.", 
					content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Agendamento.class))}),
			@ApiResponse(responseCode = "400", description = "Os parâmetros recebidos são inválidos",
						 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))})
	})
	@PostMapping @ResponseStatus(code = HttpStatus.CREATED)
	public Agendamento create(@RequestBody AgendamentoInputData agendamentoInputData) {
		return agendamentoService.save(agendamentoInputData);
	}
	
	@Operation(summary = "Reagendar agendamento.",tags = {"agendamento"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(value = "{\"data\":\"2022-01-02-13:59:59\",\"agendamentoId\":0}"))))
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Agendamento alterado.", 
					content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Agendamento.class))}),
			@ApiResponse(responseCode = "400", description = "Os parâmetros recebidos são inválidos",
						 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))})
	})
	@PutMapping("/reagendar")
	public Agendamento reschedule(@RequestBody RemarcarAgendamentoInputData remarcarAgendamentoInputData) {
		return agendamentoService.reschedule(remarcarAgendamentoInputData);
	}
}
