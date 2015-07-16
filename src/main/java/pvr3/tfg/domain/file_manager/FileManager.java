package pvr3.tfg.domain.file_manager;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import pvr3.tfg.domain.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
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
            case "shakecenter":
                url = this.convertFromShakeCenter();
                break;
            case "builtarea":
                url = this.convertFromBuiltArea();
                break;
            case "numbuild":
                url = this.convertFromNumbuild();
                break;
            default:
                url = "";
                break;
        }
        return url;
    }

    private String convertFromNumbuild() {
        ArrayList<NumBuild> numBuilds = new ArrayList<>();
        Scanner sc = new Scanner(this.streams.get(0));
        URI uri;
        try{
            while(sc.hasNextLine()){
                if(sc.hasNext(Pattern.compile("%.*"))){
                    sc.nextLine();
                } else {
                    String line = sc.nextLine();
                    StringTokenizer t = new StringTokenizer(line);
                    String geounit = t.nextToken();
                    NumBuild numBuild = new NumBuild(geounit);
                    for(int i=0; t.hasMoreTokens(); i++){
                        float builds = Float.parseFloat(t.nextToken());
                        if(t.hasMoreTokens()){
                            numBuild.setTotalBuilds(numBuild.getTotalBuilds() + builds);
                            if(i == Integer.parseInt(this.additionalData)){
                                numBuild.setSelectedBuild(builds);
                            }
                        }
                    }
                    numBuilds.add(numBuild);
                }
            }
            setAdditionalData("");
        } catch (Exception e){
            e.printStackTrace();
        }

        if(!numBuilds.isEmpty()){
            File f = NumBuild.generateKmlFile(numBuilds, this.streams.get(1));
            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, kml_file_name);
            return uri.toString();
        }
        return "";
    }

    private String convertFromBuiltArea() {
        ArrayList<BuiltArea> builtAreas = new ArrayList<>();
        Scanner sc = new Scanner(this.streams.get(0));
        URI uri;
        try{
            while(sc.hasNextLine()){
                if(sc.hasNext(Pattern.compile("%.*"))){
                    sc.nextLine();
                } else {
                    String line = sc.nextLine();
                    StringTokenizer t = new StringTokenizer(line);
                    String geounit = t.nextToken();
                    BuiltArea b = new BuiltArea(geounit);
                    for(int i=0; t.hasMoreTokens(); i++){
                        float area = Float.parseFloat(t.nextToken());
                        if(t.hasMoreTokens()){
                            b.setTotalBuiltArea(b.getTotalBuiltArea()+ area);
                            if(i == Integer.parseInt(this.additionalData)){
                                b.setSelectedBuiltArea(area);
                            }
                        }
                    }
                    builtAreas.add(b);
                }
            }
            setAdditionalData("");
        } catch (Exception e){
            e.printStackTrace();
        }

        if(!builtAreas.isEmpty()){
            File f = BuiltArea.generateKmlFile(builtAreas, this.streams.get(1));
            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, kml_file_name);
            return uri.toString();
        }
        return "";
    }

    private String convertFromShakeCenter() {
        ArrayList<Shakecenter> shakecenters = new ArrayList<>();
        Scanner sc = new Scanner(this.streams.get(0));
        URI uri;
        try{
            while(sc.hasNextLine()){
                if (sc.hasNext(Pattern.compile("%.*"))) {
                    sc.nextLine();
                } else {
                    String geounit = sc.next();
                    for(int i=0; i<4; i++)
                        sc.next();
                    float pga = Float.parseFloat(sc.next());
                    float sa3 = Float.parseFloat(sc.next());
                    float sa10 = Float.parseFloat(sc.next());
                    Shakecenter shakecenter = new Shakecenter(geounit, pga, sa3, sa10);
                    shakecenters.add(shakecenter);
                    sc.nextLine();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if(!shakecenters.isEmpty()){
            File f = Shakecenter.generateKmlFile(shakecenters, this.streams.get(1), this.additionalData);

            //Logica para calcular la leyenda a mostrar
            float biggest=-1;
            switch(this.additionalData){
                case "pga":
                    biggest = Shakecenter.findBiggestPgaAcceleration(shakecenters);
                    break;
                case "sa3":
                    biggest = Shakecenter.findBiggestSa3Acceleration(shakecenters);
                    break;
                case "sa10":
                    biggest = Shakecenter.findBiggestSa10Acceleration(shakecenters);
                    break;
            }
            if(biggest <= 0.3){
                setAdditionalData("1");
            } else {
                if(biggest > 0.3 && biggest <= 0.6){
                    setAdditionalData("2");
                } else {
                    setAdditionalData("3");
                }
            }

            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, kml_file_name);
            return uri.toString();
        }

        return "";
    }

    private String convertFromSoilCenter() {
        ArrayList<Soilcenter> soilcenters = new ArrayList<>();
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
                    Soilcenter soilcenter = new Soilcenter(geounit, soilType);
                    soilcenters.add(soilcenter);
                    scSoil.nextLine();
                }
            }
            scSoil.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!soilcenters.isEmpty()) {
            File f = Soilcenter.generateKmlFile(soilcenters, this.streams.get(1));
            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, kml_file_name);
            return uri.toString();
        }
        return "";
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

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }
}
