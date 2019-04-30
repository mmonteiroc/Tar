import java.io.File;
import java.io.RandomAccessFile;
import java.util.LinkedList;

public class Tar {
    // ATRIBUTOS
    private File ruta;
    private LinkedList<Header> headers = new LinkedList<>();


    // Constructor
    public Tar(String filename) throws Exception{
        this.ruta = new File(filename);
        extractHeaders();
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
            } else {
                continue;
            }
        }


        byte[] dev;
        if (index == -1) {
            return null;
        } else {
            Header h = headers.get(index);
            long tamaño = h.getTamañoOriginal();
            long inicioContenido = h.getInicioValor();
            dev = new byte[(int) tamaño];
            tar.seek(inicioContenido);
            for (int i = 0; i < tamaño; i++) {
                dev[i] = tar.readByte();
            }

        }


        return dev;
    }


    // Expandeix el fitxer TAR dins la memòria
    public void expand() { }






    // Metodos propios
    private void extractHeaders() throws Exception {
        RandomAccessFile tar = new RandomAccessFile(this.ruta,"r");
        // El tamaño de los archivos son multiplos de 512
        // el header es 512
        long inicio = 0;
        Header head = null;
        while (true) {
            head = sacarInformacion(inicio, tar);
            if (head.getFilename().length() == 0) {
                break;
            }
            System.out.println(head.toString());
            headers.addLast(head);
            inicio += 512 + head.getTamano();
        }
    }




    private Header sacarInformacion(long inicio, RandomAccessFile tar) throws Exception{


        // Nombre
        String fileName="";
        tar.seek(inicio);
        for (int i = 0; i < 99; i++) {
            int x = tar.readByte();
            if (x != 0){
                char c = (char) x;
                fileName+=c;
            }
        }

        // Tamaño
        System.out.println();
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
        while (true){
            if (tamano%512 == 0){
                break;
            }else {
                tamano++;
            }
        }

        // Checksum
        int checksum=0;


        return new Header(fileName, tamano, checksum, inicio + 512, tamañoOriginal);
    }





    private int convertOctalToDecimal(String  octal) {
        int result = 0;
        for (int j = 0, i = octal.length()-1; j < octal.length(); j++, i--) {
            result += Character.getNumericValue(octal.charAt(i)) * (Math.pow(8,j));
        }
        return result;
    }






}


class Header{
    // Atributos
    private String filename;
    private long tamano;
    private long checksum;
    private long inicioValor;
    private long tamañoOriginal;

    // Constructor
    Header(String filename, long tamano, long checksum, long inicioValor, long tamañoOriginal) {
        this.filename = filename;
        this.tamano = tamano;
        this.checksum = checksum;
        this.inicioValor=inicioValor;
        this.tamañoOriginal = tamañoOriginal;
    }

    // Getters
    public String getFilename() {
        return this.filename;
    }
    public long getTamano() {
        return this.tamano;
    }
    public long getChecksum() {
        return this.checksum;
    }
    public long getInicioValor(){return this.inicioValor;}

    public long getTamañoOriginal() {
        return this.tamañoOriginal;
    }
    // Setters
    public void setTamano(int tamano) {
        this.tamano = tamano;
    }
    public void setChecksum(long checksum) {
        this.checksum = checksum;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "Nombre: " + this.filename + "\nTamano: " + this.tamano + "\nchecksum: " + this.checksum + "\nInicio valor del archivo: " + this.inicioValor;
    }

}
