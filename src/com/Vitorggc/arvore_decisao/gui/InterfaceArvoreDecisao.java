package com.Vitorggc.arvore_decisao.gui;

import com.Vitorggc.arvore_decisao.model.No;
import com.Vitorggc.arvore_decisao.service.ArvoreDecisao;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class InterfaceArvoreDecisao extends JFrame implements ActionListener {

    private ArvoreDecisao arvore;
    private JTextArea outputArea;
    private JButton simButton;
    private JButton naoButton;
    private JButton verArvoreButton;
    private JScrollPane arvoreScrollPane;
    private JPanel arvorePanelContainer;

    private No noAtual;
    private No noPai;
    private Boolean foiRespostaSim;

    // Cores e estilos
    private Color backgroundColor = new Color(240, 240, 240);
    private Color panelColor = Color.WHITE;
    private Color textColor = Color.DARK_GRAY;
    private Font mainFont = new Font("Arial", Font.PLAIN, 16);
    private Font buttonFont = new Font("Arial", Font.BOLD, 14);

    public InterfaceArvoreDecisao() {
        super("MeCHUPAJava");
        this.arvore = new ArvoreDecisao();
        this.noAtual = arvore.getRaiz();

        // Configurações da Janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(backgroundColor);


        JPanel gamePanel = new JPanel(new BorderLayout(10, 10));
        gamePanel.setBackground(panelColor);
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(mainFont);
        outputArea.setBackground(panelColor);
        outputArea.setForeground(textColor);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        gamePanel.add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(panelColor);

        simButton = new JButton("Sim");
        simButton.setFont(buttonFont);
        simButton.addActionListener(this);

        naoButton = new JButton("Não");
        naoButton.setFont(buttonFont);
        naoButton.addActionListener(this);

        buttonPanel.add(simButton);
        buttonPanel.add(naoButton);

        gamePanel.add(buttonPanel, BorderLayout.SOUTH);
        add(gamePanel, BorderLayout.CENTER);


        JPanel sidePanel = new JPanel(new BorderLayout(10, 10));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        verArvoreButton = new JButton("Ver Árvore Completa");
        verArvoreButton.setFont(buttonFont);
        verArvoreButton.addActionListener(this);
        sidePanel.add(verArvoreButton, BorderLayout.NORTH);

        arvorePanelContainer = new JPanel(new BorderLayout());
        arvorePanelContainer.setBackground(Color.WHITE);
        arvoreScrollPane = new JScrollPane(arvorePanelContainer);
        sidePanel.add(arvoreScrollPane, BorderLayout.CENTER);

        add(sidePanel, BorderLayout.EAST);

        iniciarJogo();
        setVisible(true);
    }

    private void iniciarJogo() {
        outputArea.setText("Pense em um animal. Eu tentarei adivinhar!\n");
        noAtual = arvore.getRaiz();
        noPai = null;
        atualizarPergunta();
    }

    private void atualizarPergunta() {
        if (noAtual == null) {
            adicionarConhecimento();
            return;
        }

        if (noAtual.isEhAnimal()) {
            outputArea.append("O animal em que você pensou é " + noAtual.getTexto() + "?\n");
        } else {
            outputArea.append(noAtual.getTexto() + "\n");
        }
    }

    private void adicionarConhecimento() {
        String novoAnimal = JOptionPane.showInputDialog(this, "Eu não venci. Qual animal você pensou?");
        if (novoAnimal == null || novoAnimal.trim().isEmpty()) {
            iniciarJogo();
            return;
        }

        String novaPergunta = JOptionPane.showInputDialog(this,
                "Me dê uma pergunta que diferencie um(a) " + noAtual.getTexto() + " de um(a) " + novoAnimal + ".");
        if (novaPergunta == null || novaPergunta.trim().isEmpty()) {
            iniciarJogo();
            return;
        }

        int respostaNova = JOptionPane.showConfirmDialog(this,
                "Para um(a) " + novoAnimal + ", a resposta para '" + novaPergunta + "' é 'sim'?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        finalizarAprendizado(novoAnimal, novaPergunta, respostaNova == JOptionPane.YES_OPTION);
    }

    private void finalizarAprendizado(String novoAnimal, String novaPergunta, boolean respostaSim) {
        No novoNoPergunta = new No(novaPergunta, false);
        No novoNoAnimal = new No(novoAnimal, true);
        No noAntigo = noAtual;

        if (respostaSim) {
            novoNoPergunta.setSim(novoNoAnimal);
            novoNoPergunta.setNao(noAntigo);
        } else {
            novoNoPergunta.setSim(noAntigo);
            novoNoPergunta.setNao(novoNoAnimal);
        }

        if (noPai == null) {
            arvore.setRaiz(novoNoPergunta);
        } else if (foiRespostaSim != null && foiRespostaSim) {
            noPai.setSim(novoNoPergunta);
        } else {
            noPai.setNao(novoNoPergunta);
        }

        outputArea.append("Obrigado! Aprendi uma coisa nova!\n\n");
        arvore.salvarArvore();
        iniciarJogo();
    }

    // Gerar desenho arvore
    private void atualizarVisualizacaoArvore() {
        arvorePanelContainer.removeAll();
        arvorePanelContainer.setLayout(new BoxLayout(arvorePanelContainer, BoxLayout.Y_AXIS));

        JPanel raizPanel = buildNodePanel(arvore.getRaiz());
        arvorePanelContainer.add(raizPanel);

        arvorePanelContainer.revalidate();
        arvorePanelContainer.repaint();
    }

    // recursçao para construir os painéis da árvore
    private JPanel buildNodePanel(No no) {
        if (no == null) {
            return null;
        }

        JPanel nodePanel = new JPanel();
        nodePanel.setLayout(new BorderLayout());
        nodePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Cria a label para o nó atual
        JLabel label = new JLabel(no.getTexto());
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Define a cor de fundo com base no tipo do nó
        if (no.isEhAnimal()) {
            label.setBackground(new Color(173, 216, 230)); // Azul claro
        } else {
            label.setBackground(new Color(255, 255, 224)); // Amarelo claro
        }
        label.setOpaque(true);

        nodePanel.add(label, BorderLayout.NORTH);

        if (!no.isEhAnimal()) {
            JPanel childrenPanel = new JPanel(new GridLayout(1, 2));
            childrenPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

            // Painel para o filho "Sim"
            JPanel simPanel = new JPanel(new BorderLayout());
            simPanel.add(new JLabel("Sim"), BorderLayout.NORTH);
            simPanel.add(buildNodePanel(no.getSim()), BorderLayout.CENTER);

            // Painel para o filho "Não"
            JPanel naoPanel = new JPanel(new BorderLayout());
            naoPanel.add(new JLabel("Não"), BorderLayout.NORTH);
            naoPanel.add(buildNodePanel(no.getNao()), BorderLayout.CENTER);

            childrenPanel.add(simPanel);
            childrenPanel.add(naoPanel);

            nodePanel.add(childrenPanel, BorderLayout.CENTER);
        }

        return nodePanel;
    }

    private void listarAnimaisConhecidos() {
        Set<String> animais = new HashSet<>();
        listarAnimaisRecursivo(arvore.getRaiz(), animais);
        StringBuilder lista = new StringBuilder("Animais que conheço:\n");
        for (String animal : animais) {
            lista.append("- ").append(animal).append("\n");
        }
        outputArea.append("\n--- Lista de Animais ---\n" + lista.toString() + "\n");
    }

    private void listarAnimaisRecursivo(No no, Set<String> animais) {
        if (no != null) {
            if (no.isEhAnimal()) {
                animais.add(no.getTexto());
            } else {
                listarAnimaisRecursivo(no.getSim(), animais);
                listarAnimaisRecursivo(no.getNao(), animais);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == simButton) {
            handleResposta("s");
        } else if (e.getSource() == naoButton) {
            handleResposta("n");
        } else if (e.getSource() == verArvoreButton) {
            atualizarVisualizacaoArvore();
        }
    }

    private void handleResposta(String resposta) {
        if (noAtual != null) {
            if (noAtual.isEhAnimal()) {
                if (resposta.equalsIgnoreCase("s")) {
                    outputArea.append("Eu venci!\n\n");
                    iniciarJogo();
                } else {
                    adicionarConhecimento();
                }
            } else {
                noPai = noAtual;
                if (resposta.equalsIgnoreCase("s")) {
                    noAtual = noAtual.getSim();
                    foiRespostaSim = true;
                } else {
                    noAtual = noAtual.getNao();
                    foiRespostaSim = false;
                }
                atualizarPergunta();
            }
        }
    }
}