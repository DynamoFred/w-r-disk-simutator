
package simuladore_s;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class PlanificacionDeDisco extends JFrame{
    private CustomMenuBar menu;
    private JPanel canvas;
    private JMenu archivos, algoritmos;
    private JMenuItem fcfs, c_scan, sstf, scan, look, c_look, guardarEjercicio, cargarEjercicio;
    private JButton add, reset;
    private JLabel textnPistas, textCabezal, textElementos, text, text1, total, promedio, textAlgo;
    private JTextField nCabezal, nElementos, nPistas;
    private Polygon arrow;
    private ArrayList<Ejercicio> cad, fcfsList, c_scanList, sstfList, scanList, lookList, c_lookList;
    private Archivos fileManager;
    
    PlanificacionDeDisco(){
        initComponents();
    }
    
    public class CustomMenuBar extends JMenuBar {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.LIGHT_GRAY); // Cambia el color de fondo del menú aquí
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    private void initComponents(){
        //Propiedades JFrame
        setSize(900, 600);
        setTitle("Planificacion De Disco");
        setResizable(false); // Bloquea el redimensionamiento de la ventana
        setLayout(null);
        this.setLocationRelativeTo(null);//inicia programa en el centro de la pantalla
        getContentPane().setBackground(Color.GRAY);
        //Fuentes
        Font arial = new Font("Arial", Font.BOLD, 15);
        Font consolas = new Font("Consolas", Font.CENTER_BASELINE, 15);
        //ArrayList
        cad = new ArrayList<>();
        fcfsList = new ArrayList<>();
        c_scanList = new ArrayList<>();
        sstfList = new ArrayList<>();
        scanList = new ArrayList<>();
        lookList = new ArrayList<>();
        c_lookList = new ArrayList<>();
        //Archivos
        guardarEjercicio = new JMenuItem("Guardar");
        cargarEjercicio = new JMenuItem("Cargar");
        
        archivos = new JMenu("Archivos");
        archivos.add(guardarEjercicio);
        archivos.addSeparator();
        archivos.add(cargarEjercicio);
        //Algoritmos
        fcfs = new JMenuItem("FCFS");
        sstf = new JMenuItem("SSTF");
        c_scan = new JMenuItem("C-SCAN");
        scan = new JMenuItem("SCAN");
        c_look = new JMenuItem("C-LOOK");
        look = new JMenuItem("LOOK");
        
        algoritmos = new JMenu("Algoritmos");
        algoritmos.add(fcfs);
        algoritmos.addSeparator();
        algoritmos.add(sstf);
        algoritmos.addSeparator();
        algoritmos.add(c_scan);
        algoritmos.addSeparator();
        algoritmos.add(scan);
        algoritmos.addSeparator();
        algoritmos.add(c_look);
        algoritmos.addSeparator();
        algoritmos.add(look);
        
        //Menu
        menu = new CustomMenuBar();
        menu.add(archivos);
        menu.add(algoritmos);
        menu.setBounds(0, 0, 1200, 30);
        add(menu);
        
        //Textos
        textAlgo = new JLabel();
        textAlgo.setForeground(Color.BLACK);
        textAlgo.setFont(consolas);
        textAlgo.setBounds(420, 25, 300, 40);
        add(textAlgo);
        
        text = new JLabel("Limite 200 Pistas");
        text.setForeground(Color.YELLOW);
        text.setFont(consolas);
        text.setBounds(15, 60, 300, 40);
        add(text);
        
        text1 = new JLabel("Limite 13 Elementos");
        text1.setForeground(Color.YELLOW);
        text1.setFont(consolas);
        text1.setBounds(10, 80, 300, 40);
        add(text1);
        
        textCabezal = new JLabel("Cabezal");
        textCabezal.setFont(arial);
        textCabezal.setBounds(45, 125, 150, 20);
        add(textCabezal);
        
        total = new JLabel("Total : ");
        total.setForeground(Color.BLACK);
        total.setFont(consolas);
        total.setBounds(25, 400, 100, 20);
        add(total);

        promedio = new JLabel("Promedio : ");
        promedio.setForeground(Color.BLACK);
        promedio.setFont(consolas);
        promedio.setBounds(25, 425, 200, 20);
        add(promedio);
        
        nCabezal = new JTextField();
        nCabezal.setBounds(25, 150, 100, 20);
        nCabezal.setBackground(Color.LIGHT_GRAY);
        nCabezal.setHorizontalAlignment(JTextField.CENTER);
        add(nCabezal);
        
        textElementos = new JLabel("Elementos");
        textElementos.setFont(arial);
        textElementos.setBounds(35, 175, 200, 20);
        add(textElementos);
        
        nElementos = new JTextField();
        nElementos.setBounds(25, 200, 100, 20);
        nElementos.setBackground(Color.LIGHT_GRAY);
        nElementos.setHorizontalAlignment(JTextField.CENTER);
        add(nElementos);
        
        textnPistas = new JLabel("Pistas");
        textnPistas.setFont(arial);
        textnPistas.setBounds(50, 225, 200, 20);
        add(textnPistas);
        
        nPistas = new JTextField();
        nPistas.setBounds(25, 250, 100, 20);
        nPistas.setBackground(Color.LIGHT_GRAY);
        nPistas.setHorizontalAlignment(JTextField.CENTER);
        add(nPistas);
        //Reset
        reset = new JButton("Reset");
        reset.setBounds(25, 350, 100, 20);
        add(reset);
        //add
        add = new JButton("Agregar");
        add.setBounds(25, 300, 100, 20);
        add(add);
        //Vista Grafica
        arrow = new Polygon();
        arrow.addPoint(0, -15);
        arrow.addPoint(-5, -28);
        arrow.addPoint(0, -22);
        arrow.addPoint(5, -28);
        
        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                AffineTransform at = new AffineTransform();
                at.setTransform(g2.getTransform());

                if (cad.isEmpty()) {
                    g.setColor(Color.GRAY);
                    g.fillRect(50, 10, 600, 20);
                } else {
                    if (("FCFS".equals(textAlgo.getText()))) {
                        int canvasWidth = canvas.getWidth();
                        int rectWidth = (cad.get(0).getPistas() * 600) / 200;
                        int x = (canvasWidth - rectWidth) / 2;
                        int y = 10;
                        ArrayList<Integer> posicionesXArraw = new ArrayList<>();
                        g.setColor(Color.BLACK);
                        g.fillRect(x, y, rectWidth, 20);

                        for (Ejercicio fcfsList1 : fcfsList) {
                            // Empieza del cabezal
                            y += 50;
                            int x1 = ((fcfsList1.getCabezal() * 600) / 200) + x;
                            String numeroStr = Integer.toString(fcfsList1.getCabezal());
                            g2.drawString(numeroStr, x1 + 10, y - 15);
                            g2.translate(x1, y);
                            posicionesXArraw.add(x1);
                            g2.fill(arrow);
                            g2.setTransform(at);
                            for (int i = 0; i < fcfsList1.getElementos().length; i++) {
                                y += 30;
                                x1 = ((fcfsList1.getElementos()[i] * 600) / 200) + x;
                                numeroStr = Integer.toString(fcfsList1.getElementos()[i]);
                                g2.drawString(numeroStr, x1 + 10, y - 15);
                                g2.translate(x1, y);
                                posicionesXArraw.add(x1);
                                g2.fill(arrow);
                                g2.setTransform(at);
                            }
                        }
                        
                        y = 25;
                        for (int i = 0; i < posicionesXArraw.size() - 1; i++) {
                            if (posicionesXArraw.get(i) > posicionesXArraw.get(i + 1)) {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i+1), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            } else {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            }
                        }
                    }
                    
                    if (("SSTF".equals(textAlgo.getText()))) {
                        int canvasWidth = canvas.getWidth();
                        int rectWidth = (cad.get(0).getPistas() * 600) / 200;
                        int x = (canvasWidth - rectWidth) / 2;
                        int y = 10;
                        ArrayList<Integer> posicionesXArraw = new ArrayList<>();
                        g.setColor(Color.BLACK);
                        g.fillRect(x, y, rectWidth, 20);

                        for (Ejercicio sstfList1 : sstfList) {
                            // Empieza del cabezal
                            y += 50;
                            int x1 = ((sstfList1.getCabezal() * 600) / 200) + x;
                            String numeroStr = Integer.toString(sstfList1.getCabezal());
                            g2.drawString(numeroStr, x1 + 10, y - 15);
                            g2.translate(x1, y);
                            posicionesXArraw.add(x1);
                            g2.fill(arrow);
                            g2.setTransform(at);
                            for (int i = 0; i < sstfList1.getElementos().length; i++) {
                                y += 30;
                                x1 = ((sstfList1.getElementos()[i] * 600) / 200) + x;
                                numeroStr = Integer.toString(sstfList1.getElementos()[i]);
                                g2.drawString(numeroStr, x1 + 10, y - 15);
                                g2.translate(x1, y);
                                posicionesXArraw.add(x1);
                                g2.fill(arrow);
                                g2.setTransform(at);
                            }
                        }

                        y = 25;
                        for (int i = 0; i < posicionesXArraw.size() - 1; i++) {
                            if (posicionesXArraw.get(i) > posicionesXArraw.get(i + 1)) {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i + 1), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            } else {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            }
                        }
                    }
                    if (("C-SCAN".equals(textAlgo.getText()))) {
                        int canvasWidth = canvas.getWidth();
                        int rectWidth = (cad.get(0).getPistas() * 600) / 200;
                        int x = (canvasWidth - rectWidth) / 2;
                        int y = 10;
                        ArrayList<Integer> posicionesXArraw = new ArrayList<>();
                        g.setColor(Color.BLACK);
                        g.fillRect(x, y, rectWidth, 20);

                        for (Ejercicio c_scanList1 : c_scanList) {
                            // Empieza del cabezal
                            y += 50;
                            int x1 = ((c_scanList1.getCabezal() * 600) / 200) + x;
                            String numeroStr = Integer.toString(c_scanList1.getCabezal());
                            g2.drawString(numeroStr, x1 + 10, y - 15);
                            g2.translate(x1, y);
                            posicionesXArraw.add(x1);
                            g2.fill(arrow);
                            g2.setTransform(at);
                            for (int i = 0; i < c_scanList1.getElementos().length; i++) {
                                y += 30;
                                x1 = ((c_scanList1.getElementos()[i] * 600) / 200) + x;
                                numeroStr = Integer.toString(c_scanList1.getElementos()[i]);
                                g2.drawString(numeroStr, x1 + 10, y - 15);
                                g2.translate(x1, y);
                                posicionesXArraw.add(x1);
                                g2.fill(arrow);
                                g2.setTransform(at);
                            }
                        }

                        y = 25;
                        for (int i = 0; i < posicionesXArraw.size() - 1; i++) {
                            if (posicionesXArraw.get(i) > posicionesXArraw.get(i + 1)) {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i + 1), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            } else {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            }
                        }
                    }

                    if (("SCAN".equals(textAlgo.getText()))) {
                        int canvasWidth = canvas.getWidth();
                        int rectWidth = (cad.get(0).getPistas() * 600) / 200;
                        int x = (canvasWidth - rectWidth) / 2;
                        int y = 10;
                        ArrayList<Integer> posicionesXArraw = new ArrayList<>();
                        g.setColor(Color.BLACK);
                        g.fillRect(x, y, rectWidth, 20);

                        for (Ejercicio scanList1 : scanList) {
                            // Empieza del cabezal
                            y += 50;
                            int x1 = ((scanList1.getCabezal() * 600) / 200) + x;
                            String numeroStr = Integer.toString(scanList1.getCabezal());
                            g2.drawString(numeroStr, x1 + 10, y - 15);
                            g2.translate(x1, y);
                            posicionesXArraw.add(x1);
                            g2.fill(arrow);
                            g2.setTransform(at);
                            for (int i = 0; i < scanList1.getElementos().length; i++) {
                                y += 30;
                                x1 = ((scanList1.getElementos()[i] * 600) / 200) + x;
                                numeroStr = Integer.toString(scanList1.getElementos()[i]);
                                g2.drawString(numeroStr, x1 + 10, y - 15);
                                g2.translate(x1, y);
                                posicionesXArraw.add(x1);
                                g2.fill(arrow);
                                g2.setTransform(at);
                            }
                        }

                        y = 25;
                        for (int i = 0; i < posicionesXArraw.size() - 1; i++) {
                            if (posicionesXArraw.get(i) > posicionesXArraw.get(i + 1)) {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i + 1), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            } else {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            }
                        }
                    }

                    if (("C-LOOK".equals(textAlgo.getText()))) {
                        int canvasWidth = canvas.getWidth();
                        int rectWidth = (cad.get(0).getPistas() * 600) / 200;
                        int x = (canvasWidth - rectWidth) / 2;
                        int y = 10;
                        ArrayList<Integer> posicionesXArraw = new ArrayList<>();
                        g.setColor(Color.BLACK);
                        g.fillRect(x, y, rectWidth, 20);

                        for (Ejercicio c_lookList1 : c_lookList) {
                            // Empieza del cabezal
                            y += 50;
                            int x1 = ((c_lookList1.getCabezal() * 600) / 200) + x;
                            String numeroStr = Integer.toString(c_lookList1.getCabezal());
                            g2.drawString(numeroStr, x1 + 10, y - 15);
                            g2.translate(x1, y);
                            posicionesXArraw.add(x1);
                            g2.fill(arrow);
                            g2.setTransform(at);
                            for (int i = 0; i < c_lookList1.getElementos().length; i++) {
                                y += 30;
                                x1 = ((c_lookList1.getElementos()[i] * 600) / 200) + x;
                                numeroStr = Integer.toString(c_lookList1.getElementos()[i]);
                                g2.drawString(numeroStr, x1 + 10, y - 15);
                                g2.translate(x1, y);
                                posicionesXArraw.add(x1);
                                g2.fill(arrow);
                                g2.setTransform(at);
                            }
                        }

                        y = 25;
                        for (int i = 0; i < posicionesXArraw.size() - 1; i++) {
                            if (posicionesXArraw.get(i) > posicionesXArraw.get(i + 1)) {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i + 1), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            } else {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            }
                        }
                    }

                    if (("LOOK".equals(textAlgo.getText()))) {
                        int canvasWidth = canvas.getWidth();
                        int rectWidth = (cad.get(0).getPistas() * 600) / 200;
                        int x = (canvasWidth - rectWidth) / 2;
                        int y = 10;
                        ArrayList<Integer> posicionesXArraw = new ArrayList<>();
                        g.setColor(Color.BLACK);
                        g.fillRect(x, y, rectWidth, 20);

                        for (Ejercicio lookList1 : lookList) {
                            // Empieza del cabezal
                            y += 50;
                            int x1 = ((lookList1.getCabezal() * 600) / 200) + x;
                            String numeroStr = Integer.toString(lookList1.getCabezal());
                            g2.drawString(numeroStr, x1 + 10, y - 15);
                            g2.translate(x1, y);
                            posicionesXArraw.add(x1);
                            g2.fill(arrow);
                            g2.setTransform(at);
                            for (int i = 0; i < lookList1.getElementos().length; i++) {
                                y += 30;
                                x1 = ((lookList1.getElementos()[i] * 600) / 200) + x;
                                numeroStr = Integer.toString(lookList1.getElementos()[i]);
                                g2.drawString(numeroStr, x1 + 10, y - 15);
                                g2.translate(x1, y);
                                posicionesXArraw.add(x1);
                                g2.fill(arrow);
                                g2.setTransform(at);
                            }
                        }

                        y = 25;
                        for (int i = 0; i < posicionesXArraw.size() - 1; i++) {
                            if (posicionesXArraw.get(i) > posicionesXArraw.get(i + 1)) {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i + 1), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            } else {
                                y += 30;
                                g.setColor(Color.BLACK);
                                g.fillRect(posicionesXArraw.get(i), y, Math.abs(posicionesXArraw.get(i) - posicionesXArraw.get(i + 1)), 5);
                            }
                        }
                    }
                }
            }
        };

        canvas.setBounds(175, 55, 700, 500);
        add(canvas);
        
        add.addActionListener((ActionEvent e) -> {
            try {
                if (cad.isEmpty()) {
                    boolean repetirCiclo = false;
                    String elementosInput = "";
                    int pistas, elementos, cabezal, intElemento = -1;
                    elementos = Integer.parseInt(nElementos.getText());
                    pistas = Integer.parseInt(nPistas.getText());
                    cabezal = Integer.parseInt(nCabezal.getText());
                    Ejercicio el = new Ejercicio();
                    
                    if (elementos > 0 && elementos <= 13 && pistas > 0 && pistas <= 200) {
                        if (cabezal > 0 && cabezal <= pistas) {
                            int[] elem = new int[elementos];
                            el.setCabezal(cabezal);
                            el.setPistas(pistas);
                            for (int i = 0; i < elementos; i++) {
                                do {
                                    try {
                                        repetirCiclo = false;
                                        elementosInput = JOptionPane.showInputDialog("Elemento " + (i + 1));
                                        if (elementosInput == null) {
                                            break;
                                        }
                                        intElemento = Integer.parseInt(elementosInput);
                                        if (intElemento > 0 && intElemento < pistas) {
                                            for (int j = 0; j < elem.length; j++) {
                                                if (elem[j] == intElemento) {
                                                    JOptionPane.showMessageDialog(null, "Ese elemento ya fue ingresado");
                                                    repetirCiclo = true;
                                                }
                                            }
                                        }else{
                                            JOptionPane.showMessageDialog(null, "Elemento fuera de los límites");
                                            repetirCiclo = true;
                                        }
                                    } catch (HeadlessException | NumberFormatException E) {
                                        JOptionPane.showMessageDialog(null, "Ingrese Datos Numéricos o No Deje Espacios Vacíos");
                                        intElemento = -1; // Reiniciar el valor para volver a pedirlo
                                    }
                                } while (repetirCiclo || intElemento == -1);
                                if (elementosInput == null) {
                                    break;
                                }
                                elem[i] = intElemento;
                            }
                            
                            if (elementosInput != null) {
                                el.setElementos(elem);
                                cad.add(el);
                            }else{
                                cad.clear();
                            }
                            
                            nElementos.setText("");
                            nPistas.setText("");
                            nCabezal.setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "Valor cabezal incorrecto");
                        }
                    } else {
                        if (elementos <= 0 || elementos > 13) {
                            JOptionPane.showMessageDialog(null, "Limite de 13 elementos");
                        }
                        if (pistas <= 0 || pistas > 200) {
                            JOptionPane.showMessageDialog(null, "Limite de 200 Pistas");
                        }
                    }
                }
            } catch (HeadlessException | NumberFormatException E) {
                JOptionPane.showMessageDialog(null, "Ingrese Datos Numericos o No Deje Espacios Vacios");
            }
        });
        
        fcfs.addActionListener((ActionEvent e) -> {
            if (!cad.isEmpty()) {
                textAlgo.setText("FCFS");
                text.setText("");
                text1.setText("");
                fcfsList.clear();
                for (Ejercicio cad1 : cad) {
                    Ejercicio ele = new Ejercicio();
                    ele.setCabezal(cad1.getCabezal());
                    ele.setPistas(cad1.getPistas());
                    ele.setElementos(cad1.getElementos());
                    fcfsList.add(ele);
                }
                //calcular diferencias}
                
                int index = 1;
                int[] diferencias = new int[fcfsList.get(0).getElementos().length];
                
                for (Ejercicio fcfsList1 : fcfsList) {
                    diferencias[0] = Math.abs(fcfsList1.getCabezal() - fcfsList1.getElementos()[0]);
                    for (int i = 0; i < fcfsList1.getElementos().length; i++) {
                        if (index < fcfsList1.getElementos().length) {
                            diferencias[index] = Math.abs(fcfsList1.getElementos()[i] - fcfsList1.getElementos()[i + 1]);
                            index++;
                        } else {
                            break;
                        }
                    }
                    fcfsList1.setDiferencia(diferencias);
                }
                int tot = 0;
                double prom = 0;
                for (Ejercicio fcfsList1 : fcfsList) {
                    
                    for (int i = 0; i < fcfsList1.getDiferencia().length; i++) {
                        tot += fcfsList1.getDiferencia()[i];
                    }
                    prom = (double) tot / fcfsList1.getDiferencia().length;
                }
                
                DecimalFormat df = new DecimalFormat("#.##");
                String promedioFormato = df.format(prom);
                
                total.setText("Total : " + tot);
                promedio.setText("Promedio : " + (promedioFormato));
                canvas.repaint();
            }else
                JOptionPane.showMessageDialog(null, "Ingrese datos");
        });
        
        sstf.addActionListener((ActionEvent e) -> {
            if (!cad.isEmpty()) {
                Ejercicio ele = new Ejercicio();
                textAlgo.setText("SSTF");
                text.setText("");
                text1.setText("");
                sstfList.clear();
                
                int cabezal = cad.get(0).getCabezal();
                ele.setCabezal(cabezal);
                
                int[] pistassstf = new int[cad.get(0).getElementos().length];
                int[] diferencias = new int[cad.get(0).getElementos().length];
                ArrayList<Integer> pistas = new ArrayList<>();
                
                for (Ejercicio cad1 : cad) {
                    for (int i = 0; i < cad1.getElementos().length; i++) {
                        pistas.add(cad1.getElementos()[i]);
                    }
                }
                
                // Ordenar
                
                int ind = 0;  // Inicializar ind fuera del bucle
                while (!pistas.isEmpty()) {
                    int movimientoMinimo = Integer.MAX_VALUE;
                    int pistaElegida = -1;
                    
                    for (int pista : pistas) {
                        int movimiento = Math.abs(cabezal - pista);
                        if (movimiento < movimientoMinimo) {
                            movimientoMinimo = movimiento;
                            
                            pistaElegida = pista;
                        }
                    }
                    
                    cabezal = pistaElegida;
                    pistassstf[ind] = pistaElegida;
                    diferencias[ind] = movimientoMinimo;
                    ind++;
                    
                    pistas.remove(Integer.valueOf(pistaElegida));
                }
                ele.setDiferencia(diferencias);
                ele.setElementos(pistassstf);
                sstfList.add(ele);
                //Calcular
                
                int tot = 0;
                double prom = 0;
                for (Ejercicio sstfList1 : sstfList) {
                    
                    for (int i = 0; i < sstfList1.getDiferencia().length; i++) {
                        tot += sstfList1.getDiferencia()[i];
                    }
                    prom = (double) tot / sstfList1.getDiferencia().length;
                }
                
                DecimalFormat df = new DecimalFormat("#.##");
                String promedioFormato = df.format(prom);
                
                total.setText("Total : " + tot);
                promedio.setText("Promedio : " + (promedioFormato));
                canvas.repaint();
            } else
                JOptionPane.showMessageDialog(null, "Ingrese datos");
        });
        
        c_scan.addActionListener((ActionEvent e) -> {
            if (!cad.isEmpty()) {
                Ejercicio ele = new Ejercicio();
                textAlgo.setText("C-SCAN");
                text.setText("");
                text1.setText("");
                c_scanList.clear();
                
                int cabezal = cad.get(0).getCabezal();
                ele.setCabezal(cabezal);
                
                ArrayList<Integer> pistas = new ArrayList<>();
                
                for (Ejercicio cad1 : cad) {
                    for (int i = 0; i < cad1.getElementos().length; i++) {
                        pistas.add(cad1.getElementos()[i]);
                    }
                }
                if (!pistas.contains(0)) {
                    pistas.add(0);
                }
                if (!pistas.contains(cad.get(0).getPistas() - 1)) {
                    pistas.add(cad.get(0).getPistas() - 1);
                }
                
                int[] pistasc_scan = new int[pistas.size()];
                int[] diferencias = new int[pistas.size()];
                // Ordenar las pistas de manera ascendente
                Collections.sort(pistas);
                int ind = 0;
                while (!pistas.isEmpty()) {
                    int movimientoMinimo = Integer.MAX_VALUE;
                    int pistaElegida = -1;
                    
                    for (int pista : pistas) {
                        if (pista >= cabezal) {
                            int movimiento = pista - cabezal;
                            if (movimiento < movimientoMinimo) {
                                movimientoMinimo = movimiento;
                                pistaElegida = pista;
                            }
                        }
                    }
                    
                    if (pistaElegida == -1) {
                        pistaElegida = pistas.get(0);
                        movimientoMinimo = Math.abs(pistaElegida - cabezal); // Agregar el movimiento hacia el principio
                    }
                    
                    cabezal = pistaElegida;
                    pistasc_scan[ind] = pistaElegida;
                    diferencias[ind] = movimientoMinimo;
                    ind++;
                    
                    pistas.remove(Integer.valueOf(pistaElegida));
                }
                
                ele.setDiferencia(diferencias);
                ele.setElementos(pistasc_scan);
                c_scanList.add(ele);
                //Calcular
                
                int tot = 0;
                double prom = 0;
                for (Ejercicio c_scanList1 : c_scanList) {
                    
                    for (int i = 0; i < c_scanList1.getDiferencia().length; i++) {
                        tot += c_scanList1.getDiferencia()[i];
                    }
                    prom = (double) tot / c_scanList1.getDiferencia().length;
                }
                
                DecimalFormat df = new DecimalFormat("#.##");
                String promedioFormato = df.format(prom);
                
                total.setText("Total : " + tot);
                promedio.setText("Promedio : " + (promedioFormato));
                canvas.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese datos");
            }
        });
        
        scan.addActionListener((ActionEvent e) -> {
            if (!cad.isEmpty()) {
                Ejercicio ele = new Ejercicio();
                textAlgo.setText("SCAN");
                text.setText("");
                text1.setText("");
                scanList.clear();
                int cabezal = cad.get(0).getCabezal();
                ele.setCabezal(cabezal);
                ArrayList<Integer> pistas = new ArrayList<>();
                
                for (Ejercicio cad1 : cad) {
                    for (int i = 0; i < cad1.getElementos().length; i++) {
                        pistas.add(cad1.getElementos()[i]);
                    }
                }
                if (!pistas.contains(0)) {
                    pistas.add(0);
                }
                if (!pistas.contains(cad.get(0).getPistas() - 1)) {
                    pistas.add(cad.get(0).getPistas() - 1);
                }
                
                
                // Ordenar las pistas de manera ascendente
                Collections.sort(pistas);
                
                ArrayList<Integer> pistasHaciaAdelante = new ArrayList<>();
                ArrayList<Integer> pistasHaciaAtras = new ArrayList<>();
                
                for (int pista : pistas) {
                    if (pista >= cabezal) {
                        pistasHaciaAdelante.add(pista);
                    } else {
                        pistasHaciaAtras.add(pista);
                    }
                }
                
                // Ordenar la lista hacia adelante de manera ascendente
                Collections.sort(pistasHaciaAdelante);
                
                // Ordenar la lista hacia atrás de manera descendente
                Collections.sort(pistasHaciaAtras, Collections.reverseOrder());
                
                // Fusionar las listas
                ArrayList<Integer> pistasOrdenadas = new ArrayList<>(pistasHaciaAdelante);
                pistasOrdenadas.addAll(pistasHaciaAtras);
                
                int[] pistasscan = new int[pistasOrdenadas.size()];
                for (int i = 0; i < pistasOrdenadas.size(); i++) {
                    pistasscan[i] = pistasOrdenadas.get(i);
                }
                
                ele.setElementos(pistasscan);
                scanList.add(ele);
                
                //calcular diferencias
                int index = 1;
                int[] diferencias = new int[pistasOrdenadas.size()];
                for (Ejercicio scanList1 : scanList) {
                    diferencias[0] = Math.abs(scanList1.getCabezal() - scanList1.getElementos()[0]);
                    for (int i = 0; i < scanList1.getElementos().length; i++) {
                        if (index < scanList1.getElementos().length) {
                            diferencias[index] = Math.abs(scanList1.getElementos()[i] - scanList1.getElementos()[i + 1]);
                            index++;
                        } else {
                            break;
                        }
                    }
                    scanList1.setDiferencia(diferencias);
                }
                
                // Asignar los resultados a tu objeto 'ele'
                ele.setDiferencia(diferencias);
                
                //Calcular
                int tot = 0;
                double prom = 0;
                for (Ejercicio scanList1 : scanList) {
                    
                    for (int i = 0; i < scanList1.getDiferencia().length; i++) {
                        tot += scanList1.getDiferencia()[i];
                    }
                    prom = (double) tot / scanList1.getDiferencia().length;
                }
                
                DecimalFormat df = new DecimalFormat("#.##");
                String promedioFormato = df.format(prom);
                
                total.setText("Total : " + tot);
                promedio.setText("Promedio : " + (promedioFormato));
                canvas.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese datos");
            }
        });
        
        c_look.addActionListener((ActionEvent e) -> {
            if (!cad.isEmpty()) {
                Ejercicio ele = new Ejercicio();
                textAlgo.setText("C-LOOK");
                text.setText("");
                text1.setText("");
                c_lookList.clear();
                int cabezal = cad.get(0).getCabezal();
                ele.setCabezal(cabezal);
                ArrayList<Integer> pistas = new ArrayList<>();
                
                for (Ejercicio cad1 : cad) {
                    for (int i = 0; i < cad1.getElementos().length; i++) {
                        pistas.add(cad1.getElementos()[i]);
                    }
                }
                
                // Ordenar las pistas de manera ascendente
                Collections.sort(pistas);
                
                // Inicializar arrays con el tamaño correcto
                ArrayList<Integer> pistasHaciaAdelante = new ArrayList<>();
                ArrayList<Integer> pistasHaciaAtras = new ArrayList<>();
                
                for (int pista : pistas) {
                    if (pista >= cabezal) {
                        pistasHaciaAdelante.add(pista);
                    } else {
                        pistasHaciaAtras.add(pista);
                    }
                }
                
                // Ordenar la lista hacia adelante de manera ascendente
                Collections.sort(pistasHaciaAdelante);
                
                // Ordenar la lista hacia atrás de manera ascendente
                Collections.sort(pistasHaciaAtras);
                
                // Fusionar las listas
                ArrayList<Integer> pistasOrdenadas = new ArrayList<>(pistasHaciaAdelante);
                pistasOrdenadas.addAll(pistasHaciaAtras);
                
                int[] pistasc_look = new int[pistasOrdenadas.size()];
                
                for (int i = 0; i < pistasOrdenadas.size(); i++) {
                    pistasc_look[i] = pistasOrdenadas.get(i);
                }
                
                ele.setElementos(pistasc_look);
                c_lookList.add(ele);
                
                //calcular diferencias
                int index = 1;
                int[] diferencias = new int[pistasOrdenadas.size()];
                for (Ejercicio c_lookList1 : c_lookList) {
                    diferencias[0] = Math.abs(c_lookList1.getCabezal() - c_lookList1.getElementos()[0]);
                    for (int i = 0; i < c_lookList1.getElementos().length; i++) {
                        if (index < c_lookList1.getElementos().length) {
                            diferencias[index] = Math.abs(c_lookList1.getElementos()[i] - c_lookList1.getElementos()[i + 1]);
                            index++;
                        } else {
                            break;
                        }
                    }
                    c_lookList1.setDiferencia(diferencias);
                }
                // Asignar los resultados a tu objeto 'ele'
                ele.setDiferencia(diferencias);
                
                //Calcular
                int tot = 0;
                double prom = 0;
                for (Ejercicio c_lookList1 : c_lookList) {
                    
                    for (int i = 0; i < c_lookList1.getDiferencia().length; i++) {
                        tot += c_lookList1.getDiferencia()[i];
                    }
                    prom = (double) tot / c_lookList1.getDiferencia().length;
                }
                
                DecimalFormat df = new DecimalFormat("#.##");
                String promedioFormato = df.format(prom);
                
                total.setText("Total : " + tot);
                promedio.setText("Promedio : " + (promedioFormato));
                canvas.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese datos");
            }
        });
        
        look.addActionListener((ActionEvent e) -> {
            if(!cad.isEmpty()){
                Ejercicio ele = new Ejercicio();
                textAlgo.setText("LOOK");
                text.setText("");
                text1.setText("");
                lookList.clear();
                int cabezal = cad.get(0).getCabezal();
                ele.setCabezal(cabezal);
                ArrayList<Integer> pistas = new ArrayList<>();
                
                for (Ejercicio cad1 : cad) {
                    for (int i = 0; i < cad1.getElementos().length; i++) {
                        pistas.add(cad1.getElementos()[i]);
                    }
                }
                
                // Ordenar las pistas de manera ascendente
                Collections.sort(pistas);
                
                ArrayList<Integer> pistasHaciaAdelante = new ArrayList<>();
                ArrayList<Integer> pistasHaciaAtras = new ArrayList<>();
                
                for (int pista : pistas) {
                    if (pista >= cabezal) {
                        pistasHaciaAdelante.add(pista);
                    } else {
                        pistasHaciaAtras.add(pista);
                    }
                }
                
                // Ordenar la lista hacia adelante de manera ascendente
                Collections.sort(pistasHaciaAdelante);
                
                // Ordenar la lista hacia atrás de manera descendente
                Collections.sort(pistasHaciaAtras, Collections.reverseOrder());
                
                // Fusionar las listas
                ArrayList<Integer> pistasOrdenadas = new ArrayList<>(pistasHaciaAdelante);
                pistasOrdenadas.addAll(pistasHaciaAtras);
                
                int[] pistasc_look = new int[pistasOrdenadas.size()];
                
                for (int i = 0; i < pistasOrdenadas.size(); i++) {
                    pistasc_look[i] = pistasOrdenadas.get(i);
                }
                
                ele.setElementos(pistasc_look);
                lookList.add(ele);
                
                int index = 1;
                int[] diferencias = new int[pistasOrdenadas.size()];
                for (Ejercicio lookList1 : lookList) {
                    diferencias[0] = Math.abs(lookList1.getCabezal() - lookList1.getElementos()[0]);
                    for (int i = 0; i < lookList1.getElementos().length; i++) {
                        if (index < lookList1.getElementos().length) {
                            diferencias[index] = Math.abs(lookList1.getElementos()[i] - lookList1.getElementos()[i + 1]);
                            index++;
                        } else {
                            break;
                        }
                    }
                    lookList1.setDiferencia(diferencias);
                }
                // Asignar los resultados a tu objeto 'ele'
                ele.setDiferencia(diferencias);
                
                //Calcular
                int tot = 0;
                double prom = 0;
                for (Ejercicio lookList1 : lookList) {
                    
                    for (int i = 0; i < lookList1.getDiferencia().length; i++) {
                        tot += lookList1.getDiferencia()[i];
                    }
                    prom = (double) tot / lookList1.getDiferencia().length;
                }
                
                DecimalFormat df = new DecimalFormat("#.##");
                String promedioFormato = df.format(prom);
                
                total.setText("Total : " + tot);
                promedio.setText("Promedio : " + (promedioFormato));
                canvas.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese datos");
            }
        });
        
        reset.addActionListener((ActionEvent e) -> {
            textAlgo.setText("");
            total.setText("Total :");
            promedio.setText("Promedio :");
            text.setText("Limite 200 Pistas");
            text1.setText("Limite 13 Elementos");
            cad.clear();
            fcfsList.clear();
            c_scanList.clear();
            sstfList.clear();
            scanList.clear();
            lookList.clear();
            c_lookList.clear();
            canvas.repaint();
        });
        
        guardarEjercicio.addActionListener((ActionEvent e) -> {
            if (cad.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay datos que guardar");
            }
            else{
                fileManager = new Archivos(cad);
                fileManager.guardar();
            }
        });
        
        cargarEjercicio.addActionListener((ActionEvent e) -> {
            if (cad.isEmpty()) {
                fileManager = new Archivos(cad);
                fileManager.cargar();
            }else{
                JOptionPane.showMessageDialog(null, "Ya esta ejecutando un ejercicio");
            }
        });
    }
    
    public static void main(String[] args) {
        PlanificacionDeDisco frame = new PlanificacionDeDisco();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
