import java.io.File;
import java.io.RandomAccessFile;

public class Tar {
    // ATRIBUTOS
    private File ruta;
    private Headers[] headers;


    // Constructor
    public Tar(String filename) throws Exception{
        this.ruta = new File(filename);
        extractHeaders();
    }


    // Torna un array amb la llista de fitxers que hi ha dins el TAR
    public String[] list() {

        return null;
    }
    // Torna un array de bytes amb el contingut del fitxer que té per nom
    // igual a l'String «name» que passem per paràmetre
    public byte[] getBytes(String name) { return null; }
    // Expandeix el fitxer TAR dins la memòria
    public void expand() { }






    // Metodos propios
    private void extractHeaders() throws Exception {

        RandomAccessFile tar = new RandomAccessFile(this.ruta,"r");


        sacarInformacion(0, tar);
        sacarInformacion(15013, tar);




    }




    private void sacarInformacion(long inicio, RandomAccessFile tar) throws Exception{

        String fileName="";
        int finish=0;
        tar.seek(inicio);
        for (int i = 0; i < 99; i++) {
            int x = tar.readByte();
            if (x != 0){
                char c = (char) x;
                fileName+=c;
            }else {
                finish++;
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

        System.out.println("Tamaño: " + tamano);
        System.out.println("Nombre: " + fileName);

    }





    private int convertOctalToDecimal(String  octal) {
        int result = 0;

        for (int j = 0, i = octal.length()-1; j < octal.length(); j++, i--) {
            result += Character.getNumericValue(octal.charAt(i)) * (Math.pow(8,j));
        }
        return result;
    }






}


class Headers{
    // Atributos
    private String filename;
    private int tamano;
    private long checksum;
    private int inicioValor;

    // Constructor
    public Headers(String filename, int tamano, long checksum, int inicioValor){
        this.filename = filename;
        this.tamano = tamano;
        this.checksum = checksum;
        this.inicioValor=inicioValor;
    }


    // Getters

    public String getFilename() {
        return this.filename;
    }
    public int getTamano() {
        return this.tamano;
    }
    public long getChecksum() {
        return this.checksum;
    }
    public int getInicioValor(){return this.inicioValor;}

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



}
