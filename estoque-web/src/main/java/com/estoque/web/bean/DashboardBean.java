package com.estoque.web.bean;

import com.estoque.core.model.Produto;
import com.estoque.core.service.ProdutoService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@ManagedBean
@ViewScoped
public class DashboardBean implements Serializable {

    private ProdutoService service;
    private List<Produto> produtosBaixoEstoque;
    private Map<Long, BigDecimal> saldos;

    @PostConstruct
    public void init() {
        service = new ProdutoService();
        carregarDados();
    }

    public void carregarDados() {
        produtosBaixoEstoque = service.buscarEstoqueBaixo();
        saldos = new HashMap<>();
        // Pre-calculate balances for display
        for (Produto p : produtosBaixoEstoque) {
            saldos.put(p.getId(), service.calcularSaldo(p));
        }
    }

    public List<Produto> getProdutosBaixoEstoque() {
        return produtosBaixoEstoque;
    }

    public BigDecimal getSaldo(Produto p) {
        return saldos.getOrDefault(p.getId(), BigDecimal.ZERO);
    }
}
