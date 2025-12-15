package com.estoque.desktop;

import com.estoque.core.model.Produto;
import com.estoque.core.service.ProdutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class ProdutoPanel extends JPanel {

    private ProdutoService service;
    private JTable table;
    private DefaultTableModel tableModel;

    // Form fields
    private JTextField txtDescricao;
    private JTextField txtQtdMinima;
    private JTextField txtValor;
    private Long produtoSelecionadoId = null;

    public ProdutoPanel() {
        this.service = new ProdutoService();
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Produto"));

        formPanel.add(new JLabel("Descrição:"));
        txtDescricao = new JTextField();
        formPanel.add(txtDescricao);

        formPanel.add(new JLabel("Qtd. Mínima:"));
        txtQtdMinima = new JTextField();
        formPanel.add(txtQtdMinima);

        formPanel.add(new JLabel("Valor:"));
        txtValor = new JTextField();
        formPanel.add(txtValor);

        JPanel buttonPanel = new JPanel();
        JButton btnNovo = new JButton("Novo / Limpar");
        JButton btnSalvar = new JButton("Salvar");
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);

        formPanel.add(buttonPanel); // Just occupying space or add to bottom

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columns = { "ID", "Descrição", "Qtd. Min", "Valor", "Data Cadastro" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Events
        btnSalvar.addActionListener(e -> salvarProduto());
        btnNovo.addActionListener(e -> limparFormulario());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                carregarProduto(table.getSelectedRow());
            }
        });

        carregarTabela();
    }

    private void salvarProduto() {
        try {
            Produto p = new Produto();
            if (produtoSelecionadoId != null) {
                p = service.buscarPorId(produtoSelecionadoId);
            }

            p.setDescricao(txtDescricao.getText());

            try {
                p.setQuantidadeMinima(Integer.parseInt(txtQtdMinima.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantidade Mínima deve ser um número inteiro válido.",
                        "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String valorStr = txtValor.getText().replace(",", ".");
                p.setValor(new BigDecimal(valorStr));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor deve ser um número válido (ex: 10.50).", "Erro de Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            service.salvar(p);
            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
            limparFormulario();
            carregarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limparFormulario() {
        txtDescricao.setText("");
        txtQtdMinima.setText("");
        txtValor.setText("");
        produtoSelecionadoId = null;
        table.clearSelection();
    }

    private void carregarProduto(int row) {
        Long id = (Long) table.getValueAt(row, 0);
        Produto p = service.buscarPorId(id);
        if (p != null) {
            produtoSelecionadoId = p.getId();
            txtDescricao.setText(p.getDescricao());
            txtQtdMinima.setText(String.valueOf(p.getQuantidadeMinima()));
            txtValor.setText(p.getValor().toString());
        }
    }

    public void carregarTabela() {
        tableModel.setRowCount(0);
        List<Produto> produtos = service.listarTodos();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Produto p : produtos) {
            tableModel.addRow(new Object[] {
                    p.getId(),
                    p.getDescricao(),
                    p.getQuantidadeMinima(),
                    p.getValor(),
                    p.getDataCadastro() != null ? sdf.format(p.getDataCadastro()) : ""
            });
        }
    }
}
