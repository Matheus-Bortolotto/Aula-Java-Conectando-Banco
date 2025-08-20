package com.example.clinic.domain.service;

import com.example.clinic.domain.model.Consulta;
import com.example.clinic.infra.dao.ConsultaJdbcRepository;
import com.example.clinic.infra.dao.PacienteJdbcRepository;
import com.example.clinic.infra.dao.MedicoJdbcRepository;

import java.util.List;

public class AgendaService {

    private final ConsultaJdbcRepository consultaRepo;
    private final PacienteJdbcRepository pacienteRepo;
    private final MedicoJdbcRepository medicoRepo;

    public AgendaService(ConsultaJdbcRepository consultaRepo) {
        this.consultaRepo = consultaRepo;
        this.pacienteRepo = new PacienteJdbcRepository();
        this.medicoRepo = new MedicoJdbcRepository();
    }

    public Long agendar(Consulta consulta) {
        // 1. Verificar se paciente existe
        var paciente = pacienteRepo.buscarPorId(consulta.getPacienteId());
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não encontrado (ID = " + consulta.getPacienteId() + ")");
        }

        // 2. Verificar se médico existe
        var medico = medicoRepo.buscarPorId(consulta.getMedicoId());
        if (medico == null) {
            throw new IllegalArgumentException("Médico não encontrado (ID = " + consulta.getMedicoId() + ")");
        }

        // 3. Impedir conflito de horários do mesmo médico
        List<Consulta> consultasDoMedico = consultaRepo.buscarPorMedico(consulta.getMedicoId());
        boolean conflito = consultasDoMedico.stream().anyMatch(c ->
                consulta.getInicio().isBefore(c.getFim()) &&
                        consulta.getFim().isAfter(c.getInicio())
        );
        if (conflito) {
            throw new IllegalArgumentException("Conflito de horário: o médico já possui uma consulta nesse intervalo.");
        }

        // 4. Salvar e retornar ID
        return consultaRepo.salvar(consulta);
    }
}
