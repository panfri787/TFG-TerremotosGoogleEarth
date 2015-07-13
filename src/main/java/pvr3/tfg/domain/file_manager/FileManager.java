package pvr3.tfg.domain.file_manager;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import pvr3.tfg.domain.Coordinate;
import pvr3.tfg.domain.Earthquake;
import pvr3.tfg.domain.SoilcenterFile;

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
 * File management. The main propose of this class is read the "txt files" which were
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
    private String additionalData;

    /**
     * Constructor for one single File input.
     * @param stream
     * @param name
     */
    public FileManager(InputStream stream, String name){
        this.streams = null;
        this.additionalData = null;
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
        this.additionalData = null;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        this.kml_file_name = name+dateFormat.format(date)+".kml";
    }

    /**
     * Constructor for multiple File inputs with additional data.
     * @param streams
     * @param name
     */
    public FileManager(ArrayList<InputStream> streams, String name, String additionalData){
        this.stream = null;
        this.streams = streams;
        this.conversion_type = name;
        this.additionalData = additionalData;
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
                url = this.convertFromSoilCenter();
                break;

            default:
                url = "";
                break;
        }
        return url;
    }

    private String convertFromSoilCenter() {
        ArrayList<SoilcenterFile> soilcenterFiles = new ArrayList<>();
        Scanner scSoil = new Scanner(this.streams.get(0));
        URI uri;
        try {
            while (scSoil.hasNextLine()) {
                if (scSoil.hasNext(Pattern.compile("%.*"))) {
                    scSoil.nextLine();
                } else {
                    String geounit = scSoil.next();
                    scSoil.next();
                    scSoil.next();
                    int soilType = Integer.parseInt(scSoil.next());
                    SoilcenterFile soilcenterFile = new SoilcenterFile(geounit, soilType);
                    soilcenterFiles.add(soilcenterFile);
                    scSoil.nextLine();
                }
            }
            scSoil.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!soilcenterFiles.isEmpty()) {
            File f = this.modifyPolyTract(soilcenterFiles);
            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, kml_file_name);
            return uri.toString();
        }
        return "";
    }

    private File modifyPolyTract(ArrayList<SoilcenterFile> soilcenterFiles) {
        Kml polyTract = Kml.unmarshal(this.streams.get(1));
        Document document = (Document)polyTract.getFeature().withName("PolyTract.kml");
        Folder polyFolder = null;
        File f = new File("file.kml");
        for(int i=0; i<document.getFeature().size(); i++){
            if(document.getFeature().get(i) instanceof Folder){
                polyFolder = (Folder) document.getFeature().get(i);
                break;
            }
        }
        Folder soilFolder = new Folder().withName("soilcenter1");

        for(int i = 0; i < polyFolder.getFeature().size() && i < soilcenterFiles.size(); i++){

            if(polyFolder.getFeature().get(i) instanceof Placemark){
                Placemark placemark = (Placemark) polyFolder.getFeature().get(i);
                if(placemark.getName().equals(soilcenterFiles.get(i).getGeounit())) {
                    placemark.createAndAddStyle().withPolyStyle(soilcenterFiles.get(i).getKMLStyle());
                    soilFolder.addToFeature(placemark);
                }
            }
        }
        polyTract.setFeature(soilFolder);
        try {
            polyTract.marshal(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return f;
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
