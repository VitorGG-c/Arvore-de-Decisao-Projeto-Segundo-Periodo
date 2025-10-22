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
            criarArvoreBase();
        }
    }

    private void criarArvoreBase(){
        this.raiz = new No("O animal que você pensou vive na água?", false);
        this.raiz.setSim(new No("tubarão", true));
        this.raiz.setNao(new No("macaco", true));
    }

    public void resetarArvore() {
        criarArvoreBase();
        salvarArvore();
        System.out.println("Árvore resetada para o estado inicial.");
    }

    public void carregarArvorePredefinida() {
        No no1 = new No("É um mamífero?", false);
        raiz = no1;

        // Ramo Sim (Mamíferos)
        No no2 = new No("É um animal doméstico?", false);
        no1.setSim(no2);
        no2.setSim(new No("Cachorro", true));
        No no3 = new No("É considerado o 'Rei da Selva'?", false);
        no2.setNao(no3);
        no3.setSim(new No("Leão", true));
        no3.setNao(new No("Elefante", true));


        // Ramo Não (Não Mamíferos)
        No no4 = new No("Tem penas?", false);
        no1.setNao(no4);
        No no5 = new No("Sabe voar?", false);
        no4.setSim(no5);
        no5.setSim(new No("Águia", true));
        no5.setNao(new No("Pinguim", true));

        No no6 = new No("Vive na água?", false);
        no4.setNao(no6);
        no6.setSim(new No("Peixe", true));
        no6.setNao(new No("Cobra", true));

        // Adicione mais 17 animais e perguntas aqui para completar os 25...
        // Exemplo:
        No no7 = new No("É um felino?", false);
        no2.setSim(no7); // Substituindo o cachorro por uma nova pergunta
        no7.setSim(new No("Gato", true));
        no7.setNao(new No("Cachorro", true)); // Cachorro agora é a resposta "não" para "é um felino?"

        salvarArvore();
        System.out.println("Árvore pré-definida carregada.");
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
        File f = new File(ARQUIVO);
        if (!f.exists()) {
            this.raiz = null;
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            this.raiz = (No) ois.readObject();
            System.out.println("Árvore carregada com sucesso.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar a árvore: " + e.getMessage());
            this.raiz = null;
        }
    }
}