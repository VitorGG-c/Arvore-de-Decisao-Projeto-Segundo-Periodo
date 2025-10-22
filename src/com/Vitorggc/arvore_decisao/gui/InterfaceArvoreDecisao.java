package com.Vitorggc.arvore_decisao.gui;

import com.Vitorggc.arvore_decisao.model.No;
import com.Vitorggc.arvore_decisao.model.Usuario;
import com.Vitorggc.arvore_decisao.service.ArvoreDecisao;
import com.Vitorggc.arvore_decisao.service.PlacarService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InterfaceArvoreDecisao extends JFrame implements ActionListener {

    private ArvoreDecisao arvore;
    private PlacarService placarService;
    private String usuarioAtual;

    // Componentes da Interface
    private JTextArea outputArea;
    private JButton simButton;
    private JButton naoButton;
    private JButton placarButton;
    private JButton aprenderListaButton;
    private JButton limparArvoreButton;

    private JTabbedPane tabbedPane;
    private ArvoreGraficaPanel arvoreGraficaPanel;
    private JScrollPane arvoreScrollPane;

    private No noAtual;
    private No noPai;
    private Boolean foiRespostaSim;

    // Estilos
    private Color backgroundColor = new Color(240, 240, 240);
    private Font mainFont = new Font("Arial", Font.PLAIN, 16);
    private Font buttonFont = new Font("Arial", Font.BOLD, 14);

    public InterfaceArvoreDecisao() {
        super("Jogo dos Animais");

        this.usuarioAtual = solicitarUsuario();
        this.arvore = new ArvoreDecisao();
        this.placarService = new PlacarService();

        configurarJanela();
        criarBarraNavegacao();
        criarAbas();

        iniciarJogo();
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Jogo dos Animais - Logado como: " + this.usuarioAtual);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(backgroundColor);
    }

    private void criarBarraNavegacao() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        navPanel.setBackground(new Color(220, 220, 220));

        placarButton = new JButton("Ver Placar");
        aprenderListaButton = new JButton("Aprender Lista Pré-definida");
        limparArvoreButton = new JButton("Limpar Árvore");

        placarButton.addActionListener(this);
        aprenderListaButton.addActionListener(this);
        limparArvoreButton.addActionListener(this);

        navPanel.add(placarButton);
        navPanel.add(aprenderListaButton);
        navPanel.add(limparArvoreButton);

        add(navPanel, BorderLayout.NORTH);
    }

    private void criarAbas() {
        tabbedPane = new JTabbedPane();

        // Aba 1: Jogo
        JPanel gamePanel = criarPainelJogo();
        tabbedPane.addTab("Jogo", null, gamePanel, "Aba principal do jogo de adivinhação");

        // Aba 2: Visualização da Árvore
        arvoreGraficaPanel = new ArvoreGraficaPanel(arvore.getRaiz());
        arvoreScrollPane = new JScrollPane(arvoreGraficaPanel);
        tabbedPane.addTab("Visualização da Árvore", null, arvoreScrollPane, "Veja a estrutura da árvore de conhecimento");

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel criarPainelJogo() {
        JPanel gamePanel = new JPanel(new BorderLayout(10, 10));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(mainFont);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        gamePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        simButton = new JButton("Sim");
        simButton.setFont(buttonFont);
        simButton.addActionListener(this);

        naoButton = new JButton("Não");
        naoButton.setFont(buttonFont);
        naoButton.addActionListener(this);

        buttonPanel.add(simButton);
        buttonPanel.add(naoButton);
        gamePanel.add(buttonPanel, BorderLayout.SOUTH);

        return gamePanel;
    }

    private String solicitarUsuario() {
        String nome = JOptionPane.showInputDialog(null, "Bem-vindo! Por favor, digite seu nome de usuário:", "Login", JOptionPane.PLAIN_MESSAGE);
        if (nome == null || nome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nome de usuário inválido. O programa será encerrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return nome.trim();
    }

    private void iniciarJogo() {
        outputArea.setText("Pense em um animal. Eu tentarei adivinhar!\n\n");
        noAtual = arvore.getRaiz();
        noPai = null;
        atualizarPergunta();
        atualizarVisualizacaoArvore();
        tabbedPane.setSelectedIndex(0); // Foca na aba do jogo
    }

    private void atualizarVisualizacaoArvore() {
        arvoreGraficaPanel.setRaiz(arvore.getRaiz());
    }

    private void atualizarPergunta() {
        if (noAtual.isEhAnimal()) {
            outputArea.append("O animal em que você pensou é " + noAtual.getTexto() + "?\n");
        } else {
            outputArea.append(noAtual.getTexto() + "\n");
        }
    }

    private void adicionarConhecimento() {
        No animalErrado = noAtual;
        String novoAnimal = JOptionPane.showInputDialog(this, "Eu desisto! Qual animal você pensou?");
        if (novoAnimal == null || novoAnimal.trim().isEmpty()) {
            outputArea.append("\nJogo cancelado. Vamos começar de novo!\n");
            iniciarJogo();
            return;
        }

        String novaPergunta = JOptionPane.showInputDialog(this, "Digite uma pergunta de 'sim' ou 'não' que diferencie um(a) " + animalErrado.getTexto() + " de um(a) " + novoAnimal + ".");
        if (novaPergunta == null || novaPergunta.trim().isEmpty()) {
            outputArea.append("\nJogo cancelado. Vamos começar de novo!\n");
            iniciarJogo();
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this, "Para um(a) " + novoAnimal + ", a resposta para '" + novaPergunta + "' é 'SIM'?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.CLOSED_OPTION) {
            outputArea.append("\nJogo cancelado. Vamos começar de novo!\n");
            iniciarJogo();
            return;
        }

        finalizarAprendizado(novoAnimal.trim(), novaPergunta.trim(), resposta == JOptionPane.YES_OPTION, animalErrado);
    }

    private void finalizarAprendizado(String novoAnimal, String novaPergunta, boolean respostaSim, No noAntigo) {
        No novoNoPergunta = new No(novaPergunta, false);
        No novoNoAnimal = new No(novoAnimal, true);

        if (respostaSim) {
            novoNoPergunta.setSim(novoNoAnimal);
            novoNoPergunta.setNao(noAntigo);
        } else {
            novoNoPergunta.setSim(noAntigo);
            novoNoPergunta.setNao(novoNoAnimal);
        }

        if (noPai == null) {
            arvore.setRaiz(novoNoPergunta);
        } else if (foiRespostaSim) {
            noPai.setSim(novoNoPergunta);
        } else {
            noPai.setNao(novoNoPergunta);
        }

        placarService.adicionarPontos(this.usuarioAtual, 2);
        JOptionPane.showMessageDialog(this, "Obrigado! Aprendi uma coisa nova e você ganhou 2 pontos!", "Aprendizado", JOptionPane.INFORMATION_MESSAGE);

        arvore.salvarArvore();
        iniciarJogo();
    }

    private void mostrarPlacar() {
        List<Usuario> ranking = placarService.getRanking();
        StringBuilder placarTexto = new StringBuilder("--- PLACAR DE PONTOS ---\n\n");
        if (ranking.isEmpty()) {
            placarTexto.append("Ainda não há pontuações registradas.");
        } else {
            int pos = 1;
            for (Usuario u : ranking) {
                placarTexto.append(String.format("%dº: %s - %d pontos\n", pos++, u.getNome(), u.getPontuacao()));
            }
        }
        JOptionPane.showMessageDialog(this, placarTexto.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == simButton) {
            handleResposta(true);
        } else if (source == naoButton) {
            handleResposta(false);
        } else if (source == placarButton) {
            mostrarPlacar();
        } else if (source == aprenderListaButton) {
            int resp = JOptionPane.showConfirmDialog(this, "Isso substituirá a árvore atual. Deseja continuar?", "Aviso", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                arvore.carregarArvorePredefinida();
                iniciarJogo();
            }
        } else if (source == limparArvoreButton) {
            int resp = JOptionPane.showConfirmDialog(this, "Isso apagará todo o conhecimento da árvore. Deseja continuar?", "Aviso", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                arvore.resetarArvore();
                iniciarJogo();
            }
        }
    }

    private void handleResposta(boolean resposta) {
        if (noAtual.isEhAnimal()) {
            if (resposta) {
                JOptionPane.showMessageDialog(this, "Eu venci! Viva!", "Vitória", JOptionPane.INFORMATION_MESSAGE);
                iniciarJogo();
            } else {
                adicionarConhecimento();
            }
        } else {
            noPai = noAtual;
            foiRespostaSim = resposta;
            noAtual = resposta ? noAtual.getSim() : noAtual.getNao();
            atualizarPergunta();
        }
    }
}