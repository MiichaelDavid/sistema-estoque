package com.estoque.web.bean;

import com.estoque.core.model.Produto;
import com.estoque.core.service.ProdutoService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ManagedBean
@ViewScoped
public class BuscaProdutoBean implements Serializable {

    private ProdutoService service;
    private String termoBusca;
    private List<Produto> produtosEncontrados;
    private Produto produtoSelecionado;
    private BigDecimal saldoAtual;

    @PostConstruct
    public void init() {
        service = new ProdutoService();
    }

    public void buscar() {
        if (termoBusca != null && !termoBusca.isEmpty()) {
            produtosEncontrados = service.buscarPorNome(termoBusca);
        }
    }

    public void selecionarProduto(Produto produto) {
        this.produtoSelecionado = produto;
        this.saldoAtual = service.calcularSaldo(produto);
    }

    // Getters and Setters
    public String getTermoBusca() {
        return termoBusca;
    }

    public void setTermoBusca(String termoBusca) {
        this.termoBusca = termoBusca;
    }

    public List<Produto> getProdutosEncontrados() {
        return produtosEncontrados;
    }

    public Produto getProdutoSelecionado() {
        return produtoSelecionado;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }
}
