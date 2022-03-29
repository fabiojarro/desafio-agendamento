package br.com.desafio.agenda.entity;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class AgendamentoTest {

	@Test
	public void testShouldReturnStringDate() throws ParseException {
		Agendamento agendamento = new Agendamento();
		Date data = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss" ).parse( "2022-02-01-13-59-59");
		agendamento.setData(data);
		
		assertThat(agendamento.getSimpleData(), is("01/02/2022"));
	}
	
	@Test
	public void testShouldReturnValueFromService(){
		Agendamento agendamento = new Agendamento();
		assertThat(agendamento.getValor(), is(nullValue()));
		
		Servico servico = new Servico();
		servico.setValor(BigDecimal.TEN);
		agendamento.setServico(servico);
		
		assertThat(agendamento.getValor(), is(BigDecimal.TEN));
	}
	
}
