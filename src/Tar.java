import java.io.File;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete PACKAGE_NAME
 * Proyecto Practica-Tar
 */


public class Tar {
    // ATRIBUTOS
    private File ruta;
    private LinkedList<Header> headers = new LinkedList<>();


    // Constructor
    public Tar(String filename) throws Exception{
        this.ruta = new File(filename);
    }


    // Torna un array amb la llista de fitxers que hi ha dins el TAR
    public String[] list() {
        String[] dev = new String[headers.size()];
        for (int i = 0; i < dev.length; i++) {
            dev[i] = headers.get(i).getFilename();
        }

        return dev;
    }

    // Torna un array de bytes amb el contingut del fitxer que té per nom
    // igual a l'String «name» que passem per paràmetre
    public byte[] getBytes(String name) throws Exception {
        String[] nombres = list();
        RandomAccessFile tar = new RandomAccessFile(this.ruta, "r");
        int index = -1;

        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i].equals(name)) {
                index = i;
                break;
            }
        }

        byte[] devolver;
        if (index == -1) {
            return null;
        } else {
            Header h = headers.get(index);
            long tamaño = h.getTamañoOriginal();
            long inicioContenido = h.getInicioValor();
            devolver = new byte[(int) tamaño];
            tar.seek(inicioContenido);
            for (int i = 0; i < tamaño; i++) {
                devolver[i] = tar.readByte();
            }

        }


        return devolver;
    }


    // Expandeix el fitxer TAR dins la memòria
    public void expand() throws Exception {
        extractHeaders();
    }



    /*      METODOS PORPIOS     */


    private void extractHeaders() throws Exception {
        RandomAccessFile tar = new RandomAccessFile(this.ruta,"r");
        // El tamaño de los archivos son multiplos de 512
        // el header es 512
        long inicio = 0;
        Header head;
        while (true) {
            head = sacarInformacion(inicio, tar);
            if (head.getFilename().length() == 0) {
                break;
            }
            headers.addLast(head);
            inicio += 512 + head.getTamano();
        }
    }


    /**
     * @param inicio Valor que nos indica donde empezar a leer
     * @param tar    Archivo tar que tenemos que leer
     * @return Devuelve un Header
     * @throws Exception Puede lanzar una excepcion a la hora de leer el tar
     * Este metodo lo que hace es desglosar y leer toda la informacion que necesitamos
     * de un header, una vez finalizado este metodo crea un objeto header
     * (Clase que he implementado al final del codigo) y una vez creado lo retornamos.
     */
    private Header sacarInformacion(long inicio, RandomAccessFile tar) throws Exception{


        /* Aqui sacamos el nombre del header */
        String fileName="";
        tar.seek(inicio);
        for (int i = 0; i < 99; i++) {
            int x = tar.readByte();
            if (x != 0){
                char c = (char) x;
                fileName+=c;
            }
        }

        /* Aqui extrameos el tamaño del header*/
        tar.seek(124+inicio);
        String numero = "";
        for (int i = 0; i < 11; i++) {
            int ayuda = tar.readByte();
            int s=0;
            if (ayuda!=0){
                s = ayuda - 48;
            }
            numero += s;
        }
        long tamano = convertOctalToDecimal(numero);
        long tamañoOriginal = tamano;

        /*Dicho tamaño ha de ser multiplo de 512 asi que si no lo es, vamos
        aumentandolo 1 a 1 hasta que encontremos dicho multiplo*/
        while (true){
            if (tamano%512 == 0){
                break;
            }else {
                tamano++;
            }
        }


        return new Header(fileName, tamano, inicio + 512, tamañoOriginal);
    }


    /**
     * @param octal Recibimos una string del numero en octal
     * @return el numero en decimal del octal que hemos recibido
     *
     * Este simple metodo transforma de octal a decimal
     */
    private int convertOctalToDecimal(String  octal) {
        int result = 0;
        for (int j = 0, i = octal.length()-1; j < octal.length(); j++, i--) {
            result += Character.getNumericValue(octal.charAt(i)) * (Math.pow(8, j));
        }
        return result;
    }

}



/**
 * Esta clase llamada Header la usamos para guardar
 * informacion que nos interesa de cada header de nuestro archivo tar.
 *
 * Contiene informacion como el nombre, el tamaño, el checksum etc
 */
class Header{
    // Atributos
    private String filename;
    private long tamano;
    private long inicioValor;
    private long tamañoOriginal;

    // Constructor
    Header(String filename, long tamano, long inicioValor, long tamañoOriginal) {
        this.filename = filename;
        this.tamano = tamano;
        this.inicioValor = inicioValor;
        this.tamañoOriginal = tamañoOriginal;
    }

    /*
     *               Getters
     *
     * Estos metodos son getters que nos permiten recibir
     * informacion de dicho header desde fuera de la clase
     * ya que los atributos del header son privados
     * */
    public String getFilename() {
        return this.filename;
    }
    public long getTamano() {
        return this.tamano;
    }

    public long getInicioValor() {
        return this.inicioValor;
    }

    public long getTamañoOriginal() {
        return this.tamañoOriginal;
    }


    /**
     * @return Devulve un String con informacion.
     *
     * Este metodo es override de la clase Object,
     * nos permite ver toda la informacion que necesitamos
     * del objeto en un simple string
     */
    @Override
    public String toString() {
        return "Nombre: " + this.filename + "\nTamano: " + this.tamano + "\nInicio valor del archivo: " + this.inicioValor;
    }

}