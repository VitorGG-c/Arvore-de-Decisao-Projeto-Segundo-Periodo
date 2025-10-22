package com.Vitorggc.arvore_decisao.service;

import com.Vitorggc.arvore_decisao.model.Usuario;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlacarService {
    private Map<String, Usuario> usuarios;
    private static final String ARQUIVO_PLACAR = "placar.dat";

    public PlacarService() {
        carregarPlacar();
        if (this.usuarios == null) {
            this.usuarios = new HashMap<>();
        }
    }

    public Usuario getUsuario(String nome) {
        // Retorna o usuário existente ou cria um novo se não existir
        return usuarios.computeIfAbsent(nome, Usuario::new);
    }

    public void adicionarPontos(String nomeUsuario, int pontos) {
        Usuario usuario = getUsuario(nomeUsuario);
        usuario.incrementarPontuacao(pontos);
        salvarPlacar();
    }

    public List<Usuario> getRanking() {
        List<Usuario> ranking = new ArrayList<>(usuarios.values());
        Collections.sort(ranking);
        return ranking;
    }

    public void salvarPlacar() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PLACAR))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o placar: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarPlacar() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_PLACAR))) {
            this.usuarios = (Map<String, Usuario>) ois.readObject();
            System.out.println("Placar carregado com sucesso.");
        } catch (FileNotFoundException e) {
            System.out.println("Nenhum arquivo de placar encontrado. Criando novo placar.");
            this.usuarios = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar o placar: " + e.getMessage());
            this.usuarios = new HashMap<>();
        }
    }
}