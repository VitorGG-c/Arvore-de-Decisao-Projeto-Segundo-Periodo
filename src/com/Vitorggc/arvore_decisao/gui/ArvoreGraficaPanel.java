package com.Vitorggc.arvore_decisao.gui;

import com.Vitorggc.arvore_decisao.model.No;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ArvoreGraficaPanel extends JPanel {

    private No raiz;
    private Map<No, Point> nodePositions;
    private Map<No, Dimension> subtreeSizes;
    private int parentChildSpacing = 60;
    private int siblingSpacing = 20;
    private int nodeWidth = 160;
    private int nodeHeight = 50;

    public ArvoreGraficaPanel(No raiz) {
        this.raiz = raiz;
        this.nodePositions = new HashMap<>();
        this.subtreeSizes = new HashMap<>();
        setBackground(Color.WHITE);
    }

    public void setRaiz(No novaRaiz) {
        this.raiz = novaRaiz;
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (raiz == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        calculatePositions();
        drawTree(g2d, raiz, null);
    }

    private void calculatePositions() {
        nodePositions.clear();
        subtreeSizes.clear();
        if (raiz != null) {
            calculateSubtreeSize(raiz);
            calculateNodePosition(raiz, getWidth() / 2, parentChildSpacing);
        }
    }

    private Dimension calculateSubtreeSize(No node) {
        if (node == null) return new Dimension(0, 0);

        Dimension simSize = calculateSubtreeSize(node.getSim());
        Dimension naoSize = calculateSubtreeSize(node.getNao());

        int width = Math.max(nodeWidth, simSize.width + naoSize.width + siblingSpacing);
        int height = nodeHeight + (Math.max(simSize.height, naoSize.height) > 0 ? parentChildSpacing + Math.max(simSize.height, naoSize.height) : 0);

        Dimension size = new Dimension(width, height);
        subtreeSizes.put(node, size);
        return size;
    }

    private void calculateNodePosition(No node, int x, int y) {
        if (node == null) return;
        nodePositions.put(node, new Point(x, y));

        Dimension simSize = subtreeSizes.getOrDefault(node.getSim(), new Dimension(0, 0));
        Dimension naoSize = subtreeSizes.getOrDefault(node.getNao(), new Dimension(0, 0));

        int totalChildrenWidth = simSize.width + naoSize.width + siblingSpacing;

        int simX = x - totalChildrenWidth / 2 + simSize.width / 2;
        int naoX = x + totalChildrenWidth / 2 - naoSize.width / 2;
        int childrenY = y + parentChildSpacing;

        calculateNodePosition(node.getSim(), simX, childrenY);
        calculateNodePosition(node.getNao(), naoX, childrenY);
    }

    private void drawTree(Graphics2D g, No node, No parent) {
        if (node == null) return;

        Point currentPos = nodePositions.get(node);
        Point parentPos = nodePositions.get(parent);

        // Desenha a linha de conexão com o pai e a etiqueta "Sim"/"Não"
        if (parent != null && currentPos != null && parentPos != null) {
            g.setColor(Color.DARK_GRAY);
            g.drawLine(parentPos.x, parentPos.y, currentPos.x, currentPos.y);

            // Lógica CORRIGIDA para a etiqueta
            String label = (parent.getSim() == node) ? "Sim" : "Não";
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            int labelX = (parentPos.x + currentPos.x) / 2;
            int labelY = (parentPos.y + currentPos.y) / 2;
            g.drawString(label, labelX, labelY);
        }

        // Desenha o nó (círculo/oval)
        if (node.isEhAnimal()) {
            g.setColor(new Color(220, 255, 220)); // Verde claro para animais
        } else {
            g.setColor(new Color(255, 255, 224)); // Amarelo claro para perguntas
        }
        g.fillOval(currentPos.x - nodeWidth / 2, currentPos.y - nodeHeight / 2, nodeWidth, nodeHeight);
        g.setColor(Color.BLACK);
        g.drawOval(currentPos.x - nodeWidth / 2, currentPos.y - nodeHeight / 2, nodeWidth, nodeHeight);

        // Desenha o texto do nó centralizado
        drawCenteredString(g, node.getTexto(), new Rectangle(currentPos.x - nodeWidth / 2, currentPos.y - nodeHeight / 2, nodeWidth, nodeHeight));

        // Desenha as sub-árvores recursivamente
        drawTree(g, node.getSim(), node);
        drawTree(g, node.getNao(), node);
    }

    private void drawCenteredString(Graphics g, String text, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setColor(Color.BLACK);
        g.drawString(text, x, y);
    }

    @Override
    public Dimension getPreferredSize() {
        if (raiz == null) {
            return new Dimension(800, 600);
        }
        calculateSubtreeSize(raiz);
        Dimension treeSize = subtreeSizes.getOrDefault(raiz, new Dimension(nodeWidth, nodeHeight));
        // Adiciona um padding para a visualização não ficar cortada
        return new Dimension(treeSize.width + 100, treeSize.height + 100);
    }
}