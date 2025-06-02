package com.proyecto.mi_proyecto;

import javax.swing.*;
import java.awt.*;

public class PanelAhorcado extends JPanel {
    private int intentosFallidos = 0;

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.drawLine(50, 250, 150, 250); // base
        g.drawLine(100, 250, 100, 50); // poste vertical
        g.drawLine(100, 50, 200, 50);  // poste horizontal
        g.drawLine(200, 50, 200, 80);  // soga

        if (intentosFallidos >= 1) g.drawOval(180, 80, 40, 40); // cabeza
        if (intentosFallidos >= 2) g.drawLine(200, 120, 200, 180); // cuerpo
        if (intentosFallidos >= 3) g.drawLine(200, 140, 170, 160); // brazo izq
        if (intentosFallidos >= 4) g.drawLine(200, 140, 230, 160); // brazo der
        if (intentosFallidos >= 5) g.drawLine(200, 180, 170, 210); // pierna izq
        if (intentosFallidos >= 6) g.drawLine(200, 180, 230, 210); // pierna der
    }
}
