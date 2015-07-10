package pvr3.tfg.domain.file_manager;

import pvr3.tfg.domain.Coordinate;
import pvr3.tfg.domain.Earthquake;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * File management. The prinicipal propose of this class is read the "txt files" which were
 * uploaded to the website and convert it to "kml files" which can be load it on the Google Earth plugin
 */
public class FileManager {

    /**
     * The txt file
     */
    private InputStream stream;
    private ArrayList<InputStream> streams;
    private String kml_file_name;
    private String conversion_type;

    /**
     * Constructor for one single File input.
     * @param stream
     * @param name
     */
    public FileManager(InputStream stream, String name){
        this.streams = null;
        this.stream = stream;
        this.conversion_type = name;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        this.kml_file_name = name+dateFormat.format(date)+".kml";
    }

    /**
     * Constructor for multiple File inputs
     * @param streams
     * @param name
     */
    public FileManager(ArrayList<InputStream> streams, String name){
        this.stream = null;
        this.streams = streams;
        this.conversion_type = name;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        this.kml_file_name = name+dateFormat.format(date)+".kml";
    }

    public String readAndConvertToKML(){
        String url;
        switch(this.conversion_type){
            case "earthquake":
                url = this.convertFromEarthQuake();
                break;
            case "soilcenter":
                url ="";
                break;

            default:
                url = "";
                break;
        }
        return url;
    }

    //TODO: Posiblemente me interese refactorizar este metodo para adaptarlo a JAK
    private String convertFromEarthQuake(){
        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();

        try {
            Scanner sc = new Scanner(this.stream);

            while(sc.hasNextLine()){
                //Elimino las lineas comentadas del fichero.
                if(sc.hasNext(Pattern.compile("%.*"))){
                    sc.nextLine();
                } else {
                    //Recojo los valores del fichero
                    Float weight_logic = Float.valueOf(sc.next());
                    Coordinate coordinate = new Coordinate(Float.valueOf(sc.next()), Float.valueOf(sc.next()));
                    Earthquake earthquake = new Earthquake(weight_logic, coordinate);
                    earthquakes.add(earthquake);
                    sc.nextLine();
                }
            }
            sc.close();


            if(!earthquakes.isEmpty()){
                File f = this.makeKML(earthquakes);
                AzureBlobManager abm = new AzureBlobManager();
                URI uri = abm.putAtKmlAzureBlob(f, kml_file_name);
                return uri.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private File makeKML(ArrayList<Earthquake> earthquakes) {
        try {
            File f = new File("file.kml");
            PrintStream ps = new PrintStream(f);
            ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            ps.println("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
            ps.println("<Document>");
            for(Earthquake e : earthquakes){
                ps.println(e.toKML());
            }
            ps.println("</Document>");
            ps.println("</kml>");
            ps.close();
            return f;
        } catch (FileNotFoundException e) {
            System.err.println(e.toString());
        }
        return null;
    }

    public String getKml_file_name() {
        return kml_file_name;
    }
}
