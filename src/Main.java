import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete PACKAGE_NAME
 * Proyecto Practica-Tar
 */

class Main {

    private static File ruta = null;
    private static Tar archivo = null;
    private static String[] nombres = null;
    private static boolean loaded = false;

    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        mostrarMenu();

        while (true) {
            execute(scan.nextLine());
        }


    }


    private static void mostrarMenu() {
        System.out.println("Tienes las siguientes opciones para interactuar:");
        System.out.println("---------------------");
        System.out.println("load");
        System.out.println("list");
        System.out.println("extract");
        System.out.println("extractAll");
        System.out.println("exit");
        System.out.println("---------------------");
        System.out.println("Has de introducir el nombre de la opcion que deseas");
    }


    private static void execute(String orden) throws Exception {
        switch (orden) {
            case "load":
                load();
                break;
            case "list":
                list();
                break;


            case "exit":
                System.exit(0);

        }
    }


    private static void load() throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Por favor introduce una ruta para el archivo tar que quieres cargar (ruta absoluta)");
        ruta = new File(scan.nextLine());
        archivo = new Tar(ruta.getAbsolutePath());
        archivo.expand();
        nombres = archivo.list();
        loaded = true;
        for (int i = 0; i < 15; i++) {
            Thread.sleep(100);
            System.out.print("-");
        }
        System.out.println("  Loaded !!");
    }


    private static void list() throws Exception {
        if (loaded) {
            for (int i = 0; i < nombres.length; i++) {
                System.out.println(nombres[i]);
            }
        } else {
            System.out.println("No has hecho un load antes de listar dicho archivo");
        }
    }


}
