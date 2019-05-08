import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;

/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete PACKAGE_NAME
 * Proyecto provasJava
 */
public class MainScreen extends JFrame {
    /*Atributos*/
    private JPanel panel1;
    private JTable TablaTar;
    private Tar tar;


    MainScreen() {
        // Extablece panel principal
        this.setContentPane(panel1);

        /*Barra menus*/
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        /*Menu File*/
        JMenu File = new JMenu("File");
        menuBar.add(File);

        /*Items del menu File*/
        JMenuItem abrirFichero = new JMenuItem("Abrir ..");
        final JMenuItem extraerFichero = new JMenuItem("Extraer ..");
        extraerFichero.setEnabled(false);
        JMenuItem salirApp = new JMenuItem("Cerrar aplicación");

        File.add(abrirFichero);
        File.add(extraerFichero);
        File.add(salirApp);


        /*Acciones*/
        abrirFichero.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser("Selecciona tu fichero tar");
                chooser.showOpenDialog(null);
                String path = chooser.getSelectedFile().getAbsolutePath();

                tar = new Tar(path);
                try {
                    tar.expand();
                    rellenarTabla();
                    extraerFichero.setEnabled(true);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        extraerFichero.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    extraerFicheros();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        salirApp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainScreen.this.dispose();
            }
        });



        /*Modelo tabla*/
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("File name");
        tableModel.addColumn("File size");
        TablaTar.setModel(tableModel);


    }


    private void extraerFicheros() throws Exception {
        JFileChooser fileChooser = new JFileChooser("Choose where to extract");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showOpenDialog(null);
        String path = fileChooser.getSelectedFile().getAbsolutePath();
        if (TablaTar.getSelectedRowCount() == 0) {
            for (String s : tar.list()) {
                byte[] contents = tar.getBytes(s);
                FileOutputStream fos = new FileOutputStream(path + "/" + s);
                fos.write(contents);
                fos.close();
            }
        } else {
            int[] indices = TablaTar.getSelectedRows();
            for (int i : indices) {
                String s = (String) TablaTar.getValueAt(i, 0);
                byte[] contents = tar.getBytes(s);
                FileOutputStream fos = new FileOutputStream(path + "/" + s);
                fos.write(contents);
                fos.close();
            }
        }


        JOptionPane.showMessageDialog(null, "Extracted successful");
    }


    private void rellenarTabla() throws Exception {
        DefaultTableModel tabla = (DefaultTableModel) TablaTar.getModel();
        String[] nombres = tar.list();
        for (String nombre : nombres) {
            tabla.addRow(new String[]{
                    nombre,
                    Long.toString(tar.getBytes(nombre).length)
            });
        }
    }

}
