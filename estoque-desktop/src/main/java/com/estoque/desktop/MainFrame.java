package com.estoque.desktop;

import javax.swing.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private ProdutoPanel produtoPanel;
    private MovimentoPanel movimentoPanel;

    public MainFrame() {
        setTitle("Sistema de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        produtoPanel = new ProdutoPanel();
        movimentoPanel = new MovimentoPanel();

        tabbedPane.addTab("Produtos (CRUD)", produtoPanel);
        tabbedPane.addTab("Movimentação", movimentoPanel);

        // Refresh combo box when switching to movement tab
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == movimentoPanel) {
                movimentoPanel.carregarProdutos();
            } else if (tabbedPane.getSelectedComponent() == produtoPanel) {
                produtoPanel.carregarTabela();
            }
        });

        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
}
