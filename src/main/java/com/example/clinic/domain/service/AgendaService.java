package com.example.clinic.domain.service;

import com.example.clinic.domain.model.Consulta;
import com.example.clinic.infra.dao.PacienteJdbcRepository;
import com.example.clinic.infra.dao.MedicoJdbcRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class AgendaService {

    private static final long ANTECEDENCIA_MINUTOS = 60;

    private final ConsultaRepository consultaRepo;   // Interface (injeção por contrato)
    private final PacienteJdbcRepository pacienteRepo;
    private final MedicoJdbcRepository medicoRepo;

    public AgendaService(ConsultaRepository consultaRepo) {
        this.consultaRepo = consultaRepo;
        this.pacienteRepo = new PacienteJdbcRepository();
        this.medicoRepo = new MedicoJdbcRepository();
    }

    public Long agendar(Consulta consulta) {
        if (consulta == null) {
            throw new IllegalArgumentException("Consulta não pode ser nula.");
        }
        if (consulta.getInicio() == null || consulta.getFim() == null) {
            throw new IllegalArgumentException("Início e fim da consulta são obrigatórios.");
        }
        if (!consulta.getFim().isAfter(consulta.getInicio())) {
            throw new IllegalArgumentException("Horário fim deve ser após o início.");
        }

        // 1) Antecedência mínima (60 min)
        LocalDateTime agora = LocalDateTime.now();
        long minutosAteInicio = Duration.between(agora, consulta.getInicio()).toMinutes();
        if (minutosAteInicio < ANTECEDENCIA_MINUTOS) {
            throw new IllegalArgumentException("A consulta deve ser agendada com pelo menos "
                    + ANTECEDENCIA_MINUTOS + " minutos de antecedência.");
        }

        // 2) Paciente existe
        var paciente = pacienteRepo.buscarPorId(consulta.getPacienteId());
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não encontrado (ID = " + consulta.getPacienteId() + ").");
        }

        // 3) Médico existe
        var medico = medicoRepo.buscarPorId(consulta.getMedicoId());
        if (medico == null) {
            throw new IllegalArgumentException("Médico não encontrado (ID = " + consulta.getMedicoId() + ").");
        }

        // 4) Conflito de horário (mesmo médico)
        List<Consulta> consultasDoMedico = consultaRepo.buscarPorMedico(consulta.getMedicoId());
        boolean conflito = consultasDoMedico.stream().anyMatch(c ->
                consulta.getInicio().isBefore(c.getFim()) &&
                        consulta.getFim().isAfter(c.getInicio())
        );
        if (conflito) {
            throw new IllegalArgumentException("Conflito de horário: o médico já possui consulta nesse intervalo.");
        }

        // 5) Persistir e retornar ID
        return consultaRepo.salvar(consulta);
    }
}
