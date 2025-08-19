package com.example.clinic;

import com.example.clinic.domain.model.Consulta;
import com.example.clinic.domain.service.AgendaService;
import com.example.clinic.infra.dao.ConsultaJdbcRepository;

import java.time.LocalDateTime;

public class App {
    public static void main(String[] args) {
        // Cria o repositório (implementação JDBC)
        var repo = new ConsultaJdbcRepository();
        var service = new AgendaService(repo);

        // Define horário da consulta (válido)
        var inicio = LocalDateTime.now().plusHours(2).withHour(10).withMinute(0).withSecond(0).withNano(0);
        var fim = inicio.plusMinutes(30);

        var consulta = new Consulta(null, 1L, 10L, inicio, fim);

        try {
            Long id = service.agendar(consulta);
            System.out.println("Consulta agendada com sucesso! ID = " + id);
        } catch (Exception e) {
            System.err.println("Erro ao agendar consulta: " + e.getMessage());
        }
    }
}
