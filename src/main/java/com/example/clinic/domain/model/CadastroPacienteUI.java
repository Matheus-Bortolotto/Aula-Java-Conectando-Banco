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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNome = new JTextField(15);
        JTextField txtCpf = new JTextField(15);
        JTextField txtIdade = new JTextField(15);
        JButton btnSalvar = new JButton("Salvar");

        int row = 0;
        c.gridx=0; c.gridy=row; panel.add(new JLabel("Nome:"), c);
        c.gridx=1; c.gridy=row++; panel.add(txtNome, c);

        c.gridx=0; c.gridy=row; panel.add(new JLabel("CPF:"), c);
        c.gridx=1; c.gridy=row++; panel.add(txtCpf, c);

        c.gridx=0; c.gridy=row; panel.add(new JLabel("Idade:"), c);
        c.gridx=1; c.gridy=row++; panel.add(txtIdade, c);

        c.gridx=0; c.gridy=row; c.gridwidth=2; panel.add(btnSalvar, c);

        btnSalvar.addActionListener(ev -> {
            try {
                String nome = txtNome.getText().trim();
                String cpf = txtCpf.getText().trim();
                int idade = Integer.parseInt(txtIdade.getText().trim());

                Paciente paciente = new Paciente(null, nome, cpf, idade);
                Long id = repo.salvar(paciente);

                JOptionPane.showMessageDialog(this,
                        "Paciente cadastrado! ID = " + id,
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                txtNome.setText(""); txtCpf.setText(""); txtIdade.setText("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar paciente: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
    }
}
