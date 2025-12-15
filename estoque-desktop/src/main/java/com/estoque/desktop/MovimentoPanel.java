package com.estoque.desktop;

import com.estoque.core.model.MovimentoEstoque;
import com.estoque.core.model.Produto;
import com.estoque.core.model.TipoMovimentacao;
import com.estoque.core.service.ProdutoService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class MovimentoPanel extends JPanel {

    private ProdutoService service;
    private JComboBox<Produto> cbProduto;
    private JTextField txtQuantidade;
    private JComboBox<TipoMovimentacao> cbTipo;

    public MovimentoPanel() {
        this.service = new ProdutoService();
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Produto:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        cbProduto = new JComboBox<>();
        add(cbProduto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        add(new JLabel("Quantidade:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtQuantidade = new JTextField();
        add(txtQuantidade, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Tipo:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        cbTipo = new JComboBox<>(TipoMovimentacao.values());
        add(cbTipo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton btnSalvar = new JButton("Registrar Movimento");
        add(btnSalvar, gbc);

        btnSalvar.addActionListener(e -> registrarMovimento());
    }

    public void carregarProdutos() {
        cbProduto.removeAllItems();
        List<Produto> produtos = service.listarTodos();
        for (Produto p : produtos) {
            cbProduto.addItem(p);
        }
    }

    private void registrarMovimento() {
        try {
            Produto p = (Produto) cbProduto.getSelectedItem();
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Selecione um produto!");
                return;
            }

            BigDecimal qtd = new BigDecimal(txtQuantidade.getText().replace(",", "."));
            TipoMovimentacao tipo = (TipoMovimentacao) cbTipo.getSelectedItem();

            MovimentoEstoque mov = new MovimentoEstoque();
            mov.setProduto(p);
            mov.setQuantidade(qtd);
            mov.setTipoMovimentacao(tipo);

            service.registrarMovimento(mov);

            JOptionPane.showMessageDialog(this, "Movimento registrado!");
            txtQuantidade.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
