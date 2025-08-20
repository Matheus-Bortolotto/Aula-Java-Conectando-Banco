package com.example.clinic.ui;

import com.example.clinic.domain.model.Paciente;
import com.example.clinic.infra.dao.PacienteJdbcRepository;

import javax.swing.*;
import java.awt.*;

public class CadastroPacienteUI extends JFrame {

    public CadastroPacienteUI() {
        super("Cadastro de Paciente");
        var repo = new PacienteJdbcRepository();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        JTextField txtNome = new JTextField();
        JTextField txtEmail = new JTextField();
        JButton btnSalvar = new JButton("Salvar");

        int row = 0;
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        panel.add(new JLabel("Nome:"), c);
        c.gridx = 1; c.gridy = row++; c.weightx = 1.0;
        panel.add(txtNome, c);

        c.gridx = 0; c.gridy = row; c.weightx = 0;
        panel.add(new JLabel("Email:"), c);
        c.gridx = 1; c.gridy = row++; c.weightx = 1.0;
        panel.add(txtEmail, c);

        c.gridx = 0; c.gridy = row; c.gridwidth = 2;
        panel.add(btnSalvar, c);

        btnSalvar.addActionListener(ev -> {
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            if (nome.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha nome e e-mail.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Long id = repo.salvar(new Paciente(null, nome, email));
                JOptionPane.showMessageDialog(this, "Paciente salvo com ID " + id + ".", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("[OK] Paciente cadastrado: id=" + id + ", nome=" + nome + ", email=" + email);
                txtNome.setText("");
                txtEmail.setText("");
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
