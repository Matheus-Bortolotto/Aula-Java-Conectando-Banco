package com.example.clinic;

import com.example.clinic.domain.model.Consulta;
import com.example.clinic.domain.service.AgendaService;
import com.example.clinic.infra.dao.ConsultaJdbcRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

public class App {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withLocale(new Locale("pt", "BR"));

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            var repo = new ConsultaJdbcRepository();
            var service = new AgendaService(repo);

            System.out.println("=== Agenda Clínica ===");
            boolean running = true;
            while (running) {
                System.out.println();
                System.out.println("1) Agendar consulta");
                System.out.println("2) Listar consultas do médico por intervalo");
                System.out.println("3) Cancelar consulta");
                System.out.println("0) Sair");
                System.out.print("Escolha: ");
                String op = in.nextLine().trim();

                switch (op) {
                    case "1" -> agendar(in, service);
                    case "2" -> listar(in, service);
                    case "3" -> cancelar(in, service);
                    case "0" -> running = false;
                    default -> System.out.println("Opção inválida!");
                }
            }
            System.out.println("Até logo!");
        }
    }

    private static void agendar(Scanner in, AgendaService service) {
        try {
            long pacienteId = readLong(in, "ID do Paciente: ");
            long medicoId = readLong(in, "ID do Médico: ");
            LocalDateTime inicio = readDateTime(in, "Início (dd/MM/yyyy HH:mm): ");
            int duracao = (int) readLong(in, "Duração em minutos (mín. 15): ");
            LocalDateTime fim = inicio.plusMinutes(duracao);

            var consulta = new Consulta(null, pacienteId, medicoId, inicio, fim);
            Long id = service.agendar(consulta);
            System.out.println("Consulta agendada com sucesso! ID = " + id);
        } catch (Exception e) {
            System.err.println("Falha ao agendar: " + e.getMessage());
        }
    }

    private static void listar(Scanner in, AgendaService service) {
        try {
            long medicoId = readLong(in, "ID do Médico: ");
            LocalDateTime ini = readDateTime(in, "Início do intervalo (dd/MM/yyyy HH:mm): ");
            LocalDateTime fim = readDateTime(in, "Fim do intervalo (dd/MM/yyyy HH:mm): ");

            var consultas = service.listarConsultasDoMedicoNoIntervalo(medicoId, ini, fim);
            if (consultas.isEmpty()) {
                System.out.println("Nenhuma consulta encontrada.");
            } else {
                consultas.forEach(c -> System.out.printf(
                        "ID=%d | Paciente=%d | Início=%s | Fim=%s%n",
                        c.getId(), c.getPacienteId(), c.getInicio().format(DTF), c.getFim().format(DTF)
                ));
            }
        } catch (Exception e) {
            System.err.println("Falha na listagem: " + e.getMessage());
        }
    }

    private static void cancelar(Scanner in, AgendaService service) {
        try {
            long id = readLong(in, "ID da consulta para cancelar: ");
            service.cancelar(id);
            System.out.println("Consulta cancelada.");
        } catch (Exception e) {
            System.err.println("Falha ao cancelar: " + e.getMessage());
        }
    }

    private static long readLong(Scanner in, String label) {
        while (true) {
            System.out.print(label);
            String s = in.nextLine().trim();
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Tente novamente.");
            }
        }
    }

    private static LocalDateTime readDateTime(Scanner in, String label) {
        while (true) {
            System.out.print(label);
            String s = in.nextLine().trim();
            try {
                return LocalDateTime.parse(s, DTF);
            } catch (DateTimeParseException e) {
                System.out.println("Data/hora inválida. Use o formato dd/MM/yyyy HH:mm.");
            }
        }
    }
}
