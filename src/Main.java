import javax.swing.*;
import java.io.*;
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
            case "extract":
                extract();
                break;

            case "exit":
                exit();

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
        cargar("  Loaded !!");
    }


    private static void list() throws Exception {
        if (loaded) {
            for (String nombre : nombres) {
                System.out.println(nombre);
            }
        } else {
            System.out.println("No has hecho un load antes de listar dicho tar");
        }
    }

    private static void extract() throws Exception {
        if (loaded) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Introduce un nombre de un archivo que este dentro del tar: ");
            String nombre = scan.nextLine();
            boolean result = comprovarNombre(nombre);

            if (result) {
                // Nombre encontrado
                System.out.println("Introduce una ruta (absoluta) donde quieres extraer (sin el nombre del archivo) \nque acabe con / donde quieras que se extraiga el archivo");
                String rute = scan.nextLine();
                File ruta = new File(rute);
                if (ruta.exists()) {

                    if (rute.charAt(rute.length() - 1) != '/') {
                        rute += "/";
                    }
                    rute += nombre;
                    File archivoExtraer = new File(rute);


                    if (!archivoExtraer.createNewFile()) {
                        System.out.println("Ha habido un error al crear el archivo");
                        exit();
                    }


                    FileOutputStream ou = new FileOutputStream(archivoExtraer.getAbsolutePath());
                    ou.write(archivo.getBytes(nombre));
                    cargar("  Extraido!!");
                    System.out.println("El archivo se ha extraido correctamente en la siguiente ruta: " + rute);


                } else {
                    System.out.println("Ruta introducida no valida, la ruta no existe");
                    exit();
                }


            } else {
                // Nombre no encontrado
                System.out.println("Ese nombre que has introducido no existe!!!");
                exit();
            }


        } else {
            System.out.println("No has hecho un load antes de extraer dicho tar");
        }
    }


    public static boolean comprovarNombre(String name) {
        boolean find = false;
        for (String nom : nombres) {
            if (name.equals(nom)) {
                find = true;
                break;
            }
        }
        return find;

    }

    public static void exit() {
        System.exit(0);
    }


    public static void cargar(String string) throws Exception {
        for (int i = 0; i < 15; i++) {
            Thread.sleep(100);
            System.out.print("-");
        }
        System.out.println(string);
        Thread.sleep(100);
    }

}
