package com.example.clinic.infra.dao;

import com.example.clinic.domain.model.Medico;

import java.util.List;

public class MedicoJdbcRepository {

    private final MedicoDao delegate = new MedicoDao();

    public Long salvar(Medico m) { return delegate.salvar(m); }
    public Medico buscarPorId(long id) { return delegate.buscarPorId(id); }
    public List<Medico> listarTodos() { return delegate.listarTodos(); }
    public void atualizar(Medico m) { delegate.atualizar(m); }
    public void deletar(long id) { delegate.deletar(id); }
}
