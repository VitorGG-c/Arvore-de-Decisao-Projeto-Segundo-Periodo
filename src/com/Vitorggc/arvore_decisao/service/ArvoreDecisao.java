package com.Vitorggc.arvore_decisao.service;

import com.Vitorggc.arvore_decisao.model.No;
import java.io.*;

public class ArvoreDecisao {
    private No raiz;
    private static final String ARQUIVO = "arvore.dat";

    public ArvoreDecisao() {
        carregarArvore();
        if (this.raiz == null) {
            System.out.println("Nenhum arquivo de árvore encontrado. Criando árvore inicial...");
            this.raiz = new No("O animal é um mamífero?", false);
            this.raiz.setSim(new No("elefante", true));
            this.raiz.setNao(new No("pássaro", true));
        }
    }

    public No getRaiz() {
        return raiz;
    }

    public void setRaiz(No novaRaiz) {
        this.raiz = novaRaiz;
    }

    public void salvarArvore() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(raiz);
            System.out.println("Árvore salva com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar a árvore: " + e.getMessage());
        }
    }

    private void carregarArvore() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            this.raiz = (No) ois.readObject();
            System.out.println("Árvore carregada com sucesso.");
        } catch (FileNotFoundException e) {
            this.raiz = null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar a árvore: " + e.getMessage());
            this.raiz = null;
        }
    }
}