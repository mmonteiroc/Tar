import java.io.File;
import java.io.RandomAccessFile;

public class Tar {
    // ATRIBUTOS
    private File ruta;
    private Header[] headers;


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
        // El tamaño de los archivos son multiplos de 512
        // el header es 512

        long inicio = 0;
        //Header h1 = sacarInformacion(inicio,tar);
        //inicio += 512 + h1.getTamano();
        /*Header h2 = sacarInformacion(inicio,tar);
        inicio+= h2.getTamano()+512;
        Header h3 = sacarInformacion(inicio,tar);
        inicio+=h3.getTamano()+512;
        Header h4 = sacarInformacion(inicio,tar);*/


        for (int i = 0; i < 6; i++) {
            Header h1 = sacarInformacion(inicio,tar);
            inicio += 512 + h1.getTamano();
        }





    }




    private Header sacarInformacion(long inicio, RandomAccessFile tar) throws Exception{

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

        while (true){
            if (tamano%512 == 0){
                break;
            }else {
                tamano++;
            }
        }

        System.out.println("Tamaño: " + tamano);
        System.out.println("Nombre: " + fileName);
        System.out.println(fileName.length());

        int checksum=0;
        Header h = new Header(fileName,tamano,checksum,inicio+512);
        return h;
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

    // Constructor
    public Header(String filename, long tamano, long checksum, long inicioValor){
        this.filename = filename;
        this.tamano = tamano;
        this.checksum = checksum;
        this.inicioValor=inicioValor;
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
