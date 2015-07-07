package pvr3.tfg.domain.file_manager;

import pvr3.tfg.domain.Coordinate;
import pvr3.tfg.domain.Earthquake;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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
    private File file;
    private String kml_file_name;

    public FileManager(File file){
        this.file = file;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        this.kml_file_name = this.file.getName()+dateFormat.format(date)+".kml";
    }

    public void readAndConvertToKML(){
        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();

        try {
            Scanner sc = new Scanner(this.file);

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
                this.makeKML(earthquakes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeKML(ArrayList<Earthquake> earthquakes) {
        try {
            PrintStream ps = new PrintStream(new File("src/main/resources/static/kml_files/"+this.kml_file_name));
            ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            ps.println("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
            ps.println("<Document>");
            for(Earthquake e : earthquakes){
                ps.println(e.toKML());
            }
            ps.println("</Document>");
            ps.println("</kml>");
            ps.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.toString());
        }
    }

    public String getKml_file_name() {
        return kml_file_name;
    }
}
