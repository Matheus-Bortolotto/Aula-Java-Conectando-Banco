package com.example.clinic.domain.model;

import java.time.LocalDateTime;

public class Consulta {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    public Consulta(Long id, Long pacienteId, Long medicoId, LocalDateTime inicio, LocalDateTime fim) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.inicio = inicio;
        this.fim = fim;
    }

    public Long getId() { return id; }
    public Long getPacienteId() { return pacienteId; }
    public Long getMedicoId() { return medicoId; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFim() { return fim; }

    public void setId(Long id) { this.id = id; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public void setFim(LocalDateTime fim) { this.fim = fim; }
}
