package com.Vitorggc.arvore_decisao.model;

import java.io.Serializable;
import java.util.Objects;

public class Usuario implements Serializable, Comparable<Usuario> {
    private String nome;
    private int pontuacao;

    public Usuario(String nome) {
        this.nome = nome;
        this.pontuacao = 0;
    }

    public String getNome() {
        return nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void incrementarPontuacao(int pontos) {
        this.pontuacao += pontos;
    }

    @Override
    public int compareTo(Usuario other) {
        // Ordena da maior pontuação para a menor
        return Integer.compare(other.pontuacao, this.pontuacao);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(nome, usuario.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}