package com.Vitorggc.arvore_decisao.main;

import com.Vitorggc.arvore_decisao.gui.InterfaceArvoreDecisao;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceArvoreDecisao());
    }
}