package com.example.clinic.ui;

import com.example.clinic.domain.model.Medico;
import com.example.clinic.infra.dao.MedicoJdbcRepository;

import javax.swing.*;
import java.awt.*;

public class CadastroMedicoUI extends JFrame {

    public CadastroMedicoUI() {
        super("Cadastro de Médico");
        var repo = new MedicoJdbcRepository();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        JTextField txtNome = new JTextField();
        JTextField txtCrm = new JTextField();
        JButton btnSalvar = new JButton("Salvar");

        int row = 0;
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        panel.add(new JLabel("Nome:"), c);
        c.gridx = 1; c.gridy = row++; c.weightx = 1.0;
        panel.add(txtNome, c);

        c.gridx = 0; c.gridy = row; c.weightx = 0;
        panel.add(new JLabel("CRM:"), c);
        c.gridx = 1; c.gridy = row++; c.weightx = 1.0;
        panel.add(txtCrm, c);

        c.gridx = 0; c.gridy = row; c.gridwidth = 2;
        panel.add(btnSalvar, c);

        btnSalvar.addActionListener(ev -> {
            String nome = txtNome.getText().trim();
            String crm = txtCrm.getText().trim();
            if (nome.isEmpty() || crm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha nome e CRM.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Long id = repo.salvar(new Medico(null, nome, crm));
                JOptionPane.showMessageDialog(this, "Médico salvo com ID " + id + ".", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("[OK] Médico cadastrado: id=" + id + ", nome=" + nome + ", CRM=" + crm);
                txtNome.setText("");
                txtCrm.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
    }
}
