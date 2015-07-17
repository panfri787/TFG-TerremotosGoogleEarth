package pvr3.tfg.domain.file_managers;

import pvr3.tfg.domain.Coordinate;
import pvr3.tfg.domain.Earthquake;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Pablo on 16/07/2015.
 */
public class EarthquakeManager extends AbstractFileManager {

    private InputStream stream;

    public EarthquakeManager(InputStream stream, String kml_file_name) {
        super();
        this.stream = stream;
        this.setKml_file_name(kml_file_name);
    }

    //TODO: Posiblemente me interese refactorizar este metodo para adaptarlo a JAK
    public String convertFromTextFile(){
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {
            Scanner sc = new Scanner(this.stream);

            while(sc.hasNextLine()){
                //Elimino las lineas comentadas del fichero.
                if(sc.hasNext(Pattern.compile("%.*"))){
                    sc.nextLine();
                } else {
                    //Recojo los valores del fichero
                    Float weight_logic = Float.valueOf(sc.next());
                    Coordinate coordinate = new Coordinate(Double.valueOf(sc.next()), Double.valueOf(sc.next()));
                    Earthquake earthquake = new Earthquake(weight_logic, coordinate);
                    earthquakes.add(earthquake);
                    sc.nextLine();
                }
            }
            sc.close();

            if(!earthquakes.isEmpty()){
                File f = this.makeKML(earthquakes);
                AzureBlobManager abm = new AzureBlobManager();
                URI uri = abm.putAtKmlAzureBlob(f, this.getKml_file_name());
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
}
