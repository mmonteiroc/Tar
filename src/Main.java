import java.io.*;
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
            case "extractAll":
                extractAll();
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
        cargar("  Loaded !!", 100);
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


    /**
     * @throws Exception Posible excepcion a la hora de crear el archivo
     *                   <p>
     *                   Este metodo lo que hace es primero de todo comprovar si tenemos
     *                   algun archivo tar loaded, si es asi, nos deja continuar
     *                   Despues nos pide un nombre que tiene que ser un archivo que este
     *                   dentro del tar y despues, nos pedira una ruta absoluta donde
     *                   queremos extraer dicho tar. Dicho metodo extraera el tar con el
     *                   nombre original en path que le hayamos pasado
     */
    private static void extract() throws Exception {
        if (loaded) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Introduce un nombre de un archivo que este dentro del tar: ");
            String nombre = scan.nextLine();
            boolean result = comprovarNombre(nombre);

            if (result) {
                // Nombre encontrado
                System.out.println("Introduce una ruta (absoluta) donde quieres extraer el archivo (sin el nombre del archivo)");
                String rute = scan.nextLine();
                if (rute.charAt(rute.length() - 1) != '/') {
                    rute += "/";
                }
                File ruta = new File(rute);
                if (ruta.exists()) {

                    rute += nombre;
                    File archivoExtraer = new File(rute);

                    if (!archivoExtraer.createNewFile()) {
                        System.out.println("Ha habido un error al crear el archivo");
                        exit();
                    }

                    FileOutputStream ou = new FileOutputStream(archivoExtraer.getAbsolutePath());
                    ou.write(archivo.getBytes(nombre));
                    cargar("  Extraido!!", 100);
                    System.out.println("El archivo se ha extraido correctamente en la siguiente ruta: " + rute);
                    exit();
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


    /**
     * @throws Exception Posible excepcion a la hora de crear un archivo
     *
     * Este metodo lo que hace es extraer todos los archivos que tenemos
     * dentro el tar en una ruta que nosotros le pasemos
     */
    private static void extractAll() throws Exception {
        if (loaded) {

            // Pedimos la ruta donde extraerlo
            Scanner scan = new Scanner(System.in);
            System.out.println("Introduce una ruta (absoluta) donde quieres extraer todo el tar");
            String rute = scan.nextLine();
            if (rute.charAt(rute.length() - 1) != '/') {
                rute += "/";
            }

            File ruta = new File(rute);
            if (ruta.exists()) {
                // si la ruta existe
                for (String nombre : nombres) {
                    File archivoExtraer = new File(rute + nombre);
                    if (!archivoExtraer.createNewFile()) {
                        // en el caso de que haya habido un error a la
                        // hora de crear uno de los archivos (se parara el programa)
                        System.out.println("Ha habido un error al crear el archivo");
                        exit();
                    }

                    FileOutputStream output = new FileOutputStream(archivoExtraer.getAbsolutePath());
                    output.write(archivo.getBytes(nombre));
                    cargar("    archivo " + nombre + " extraido", 30);
                }

                System.out.println("Todos los archivos han sido extraidos en la siguiente ruta: " + rute);
                exit();
            } else {
                // si la ruta no existe
                System.out.println("Ruta introducida no valida, la ruta no existe");
                exit();
            }

        } else {
            System.out.println("No has hecho un load antes de extraer dicho tar");
        }
    }


    /**
     * @param name nombre del archivo a buscar
     * @return true/false
     *
     * Este metodo recibe un nombre de un archivo y su cometido
     * es buscar si ese nombre existe dentro del archivo tar
     */
    private static boolean comprovarNombre(String name) {
        boolean find = false;
        for (String nom : nombres) {
            if (name.equals(nom)) {
                find = true;
                break;
            }
        }
        return find;
    }


    /**
     * Este metodo simplemente hace un exit del programa
     */
    private static void exit() {
        System.exit(0);
    }


    /**
     * Este simple metodo lo uso para que visualmente en
     * el terminal parezca que esta cargando una barra
     */
    private static void cargar(String string, int time) throws Exception {
        for (int i = 0; i < 15; i++) {
            Thread.sleep(time);
            System.out.print("-");
        }
        System.out.println(string);
        Thread.sleep(time);
    }

}
