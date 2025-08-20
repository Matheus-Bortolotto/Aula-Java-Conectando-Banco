package com.example.clinic.infra.dao;

import com.example.clinic.domain.model.Paciente;

import java.util.List;

public class PacienteJdbcRepository {

    private final PacienteDao delegate = new PacienteDao();

    public Long salvar(Paciente p) { return delegate.salvar(p); }
    public Paciente buscarPorId(long id) { return delegate.buscarPorId(id); }
    public List<Paciente> listarTodos() { return delegate.listarTodos(); }
    public void atualizar(Paciente p) { delegate.atualizar(p); }
    public void deletar(long id) { delegate.deletar(id); }
}
