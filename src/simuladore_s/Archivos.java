
package simuladore_s;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.prefs.Preferences;

public class Archivos {

    private final Preferences preferences;
    private final ArrayList<Ejercicio> cad;
    
    public Archivos(ArrayList<Ejercicio> cad) {
        preferences = Preferences.userNodeForPackage(this.getClass());
        this.cad = cad;
    }

    public void guardar() {
        JFileChooser selectarch = new JFileChooser(preferences.get("lastSaveLocation", "C:\\Users\\USER\\Desktop"));
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos Enlazados (*.pdd)", "pdd");
        selectarch.setFileFilter(filtro);
        selectarch.setDialogTitle("Guardar Archivo");
        PrintWriter pw;
        int select = selectarch.showSaveDialog(null);
        if (select == JFileChooser.APPROVE_OPTION) {
            File saveArch = selectarch.getSelectedFile();
            // Verifica si la extensión ya está presente en el nombre del archivo
            if (!saveArch.getName().toLowerCase().endsWith(".pdd")) {
                // Agrega la extensión al nombre del archivo
                saveArch = new File(saveArch.getAbsolutePath() + ".pdd");
            }
            try ( FileWriter archivo = new FileWriter(saveArch);) {
                pw = new PrintWriter(archivo);
                for (Ejercicio cad1 : cad) {
                    pw.println(cad1.getCabezal());
                    pw.println(cad1.getPistas());
                    pw.println(Arrays.toString(cad1.getElementos()));
                }
                JOptionPane.showMessageDialog(null, "Ejercicio Guardado");
                // Guarda la ubicación seleccionada en las preferencias
                preferences.put("lastSaveLocation", saveArch.getParentFile().getAbsolutePath());
            } catch (IOException e) {
                
            }
        }
    }

    public void cargar() {
        File archselect;
        JFileChooser selectarch = new JFileChooser(preferences.get("lastSaveLocation", "C:\\Users\\USER\\Desktop"));
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.pdd", "pdd");
        selectarch.setFileFilter(filtro);
        selectarch.setDialogTitle("Cargar Archivo");
        int result = selectarch.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return; // El usuario canceló la selección
        }
        archselect = selectarch.getSelectedFile();
        try ( BufferedReader br = new BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(archselect), StandardCharsets.UTF_8))) {
            String elementosBrLine, cabezal;
                int[] elementosArray;
                while ((cabezal = br.readLine()) != null) {
                    Ejercicio ejer = new Ejercicio();
                    ejer.setCabezal(Integer.parseInt(cabezal));
                    ejer.setPistas(Integer.parseInt(br.readLine()));
                    elementosBrLine = br.readLine();
                    elementosBrLine = elementosBrLine.substring(1, elementosBrLine.length() - 1); // Elimina corchetes al principio y al final
                    String[] elementos = elementosBrLine.split(", "); // Divide la cadena en elementos individuales
                    elementosArray = new int[elementos.length];
                    for (int i = 0; i < elementos.length; i++) {
                        elementosArray[i] = Integer.parseInt(elementos[i].trim());
                    }
                    ejer.setElementos(elementosArray);
                    cad.add(ejer);
                }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer en el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al convertir un número: " + e.getMessage());
        }
    }
}
