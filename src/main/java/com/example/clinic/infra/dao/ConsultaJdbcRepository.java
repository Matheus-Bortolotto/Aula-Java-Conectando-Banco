package com.example.clinic.infra.dao;

import com.example.clinic.domain.model.Consulta;
import com.example.clinic.domain.service.ConsultaRepository;
import com.example.clinic.infra.db.OracleConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaJdbcRepository implements ConsultaRepository {

    @Override
    public List<Consulta> buscarPorMedico(long medicoId) {
        String sql = "SELECT id, paciente_id, medico_id, inicio, fim FROM consultas WHERE medico_id = ? ORDER BY inicio";
        List<Consulta> lista = new ArrayList<>();
        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, medicoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(map(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar consultas por médico", e);
        }
    }

    @Override
    public Long salvar(Consulta consulta) {
        String sql = "INSERT INTO consultas (paciente_id, medico_id, inicio, fim) VALUES (?, ?, ?, ?)";
        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID"})) {
            ps.setLong(1, consulta.getPacienteId());
            ps.setLong(2, consulta.getMedicoId());
            ps.setTimestamp(3, Timestamp.valueOf(consulta.getInicio()));
            ps.setTimestamp(4, Timestamp.valueOf(consulta.getFim()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    consulta.setId(id);
                    return id;
                }
            }
            throw new RuntimeException("Falha ao obter ID gerado da consulta");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar consulta", e);
        }
    }

    // Métodos utilitários opcionais (se você já tiver, mantenha)
    public List<Consulta> listarPorMedicoNoIntervalo(long medicoId, LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT id, paciente_id, medico_id, inicio, fim FROM consultas " +
                "WHERE medico_id = ? AND inicio < ? AND fim > ? ORDER BY inicio";
        List<Consulta> lista = new ArrayList<>();
        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, medicoId);
            ps.setTimestamp(2, Timestamp.valueOf(fim));
            ps.setTimestamp(3, Timestamp.valueOf(inicio));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(map(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar intervalo", e);
        }
    }

    public void deletar(long id) {
        String sql = "DELETE FROM consultas WHERE id = ?";
        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar consulta", e);
        }
    }

    private Consulta map(ResultSet rs) throws SQLException {
        return new Consulta(
                rs.getLong("id"),
                rs.getLong("paciente_id"),
                rs.getLong("medico_id"),
                rs.getTimestamp("inicio").toLocalDateTime(),
                rs.getTimestamp("fim").toLocalDateTime()
        );
    }
}
