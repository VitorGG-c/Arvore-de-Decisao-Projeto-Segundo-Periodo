package com.Vitorggc.arvore_decisao.model;

import java.io.Serializable;

public class No implements Serializable {
    private String texto;
    private No sim;
    private No nao;
    private boolean ehAnimal;

    public No(String texto, boolean ehAnimal) {
        this.texto = texto;
        this.ehAnimal = ehAnimal;
        this.sim = null;
        this.nao = null;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public No getSim() {
        return sim;
    }

    public void setSim(No sim) {
        this.sim = sim;
    }

    public No getNao() {
        return nao;
    }

    public void setNao(No nao) {
        this.nao = nao;
    }

    public boolean isEhAnimal() {
        return ehAnimal;
    }

    public void setEhAnimal(boolean ehAnimal) {
        this.ehAnimal = ehAnimal;
    }

    public boolean isFolha() {
        return sim == null && nao == null;
    }
}