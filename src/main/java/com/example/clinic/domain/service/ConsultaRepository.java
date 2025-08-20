package com.example.clinic.domain.service;

import com.example.clinic.domain.model.Consulta;
import java.util.List;

public interface ConsultaRepository {
    Long salvar(Consulta consulta);
    List<Consulta> buscarPorMedico(long medicoId);
}
