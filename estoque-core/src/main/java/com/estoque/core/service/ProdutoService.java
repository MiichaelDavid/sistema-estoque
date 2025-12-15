package com.estoque.core.service;

import com.estoque.core.model.MovimentoEstoque;
import com.estoque.core.model.Produto;
import com.estoque.core.model.TipoMovimentacao;
import com.estoque.core.util.JPAUtil;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class ProdutoService {

    public void salvar(Produto produto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (produto.getId() == null) {
                em.persist(produto);
            } else {
                em.merge(produto);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Produto> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Produto p ORDER BY p.descricao", Produto.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Produto buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    // New method required by desktop module logic or future usage
    public void registrarMovimento(MovimentoEstoque movimento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movimento);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public BigDecimal calcularSaldo(Produto produto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Sum Entradas
            BigDecimal entradas = em.createQuery(
                    "SELECT SUM(m.quantidade) FROM MovimentoEstoque m WHERE m.produto = :produto AND m.tipoMovimentacao = :tipo",
                    BigDecimal.class)
                    .setParameter("produto", produto)
                    .setParameter("tipo", TipoMovimentacao.ENTRADA)
                    .getSingleResult();

            BigDecimal saidas = em.createQuery(
                    "SELECT SUM(m.quantidade) FROM MovimentoEstoque m WHERE m.produto = :produto AND m.tipoMovimentacao = :tipo",
                    BigDecimal.class)
                    .setParameter("produto", produto)
                    .setParameter("tipo", TipoMovimentacao.SAIDA)
                    .getSingleResult();

            if (entradas == null)
                entradas = BigDecimal.ZERO;
            if (saidas == null)
                saidas = BigDecimal.ZERO;

            return entradas.subtract(saidas);
        } finally {
            em.close();
        }
    }

    public List<Produto> buscarEstoqueBaixo() {
        // This is tricky because balance is calculated.
        // We can either fetch all and filter in memory (easier) or write a complex
        // query.
        // Given integration requirements and simplicity, in-memory filter is acceptable
        // for this assignment unless performance is critical.
        // However, a query is better.
        // "SELECT p FROM Produto p WHERE (SELECT COALESCE(SUM(CASE WHEN
        // m.tipoMovimentacao = 'ENTRADA' THEN m.quantidade ELSE -m.quantidade END), 0)
        // FROM MovimentoEstoque m WHERE m.produto = p) < p.quantidadeMinima"

        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT p FROM Produto p WHERE " +
                    "(SELECT COALESCE(SUM(CASE WHEN m.tipoMovimentacao = 'ENTRADA' THEN m.quantidade ELSE -m.quantidade END), 0) "
                    +
                    " FROM MovimentoEstoque m WHERE m.produto = p) < p.quantidadeMinima";

            return em.createQuery(jpql, Produto.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Produto> buscarPorNome(String nome) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Produto p WHERE lower(p.descricao) LIKE lower(:nome)", Produto.class)
                    .setParameter("nome", "%" + nome + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
