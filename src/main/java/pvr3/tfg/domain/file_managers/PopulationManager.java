package pvr3.tfg.domain.file_managers;

import de.micromata.opengis.kml.v_2_2_0.*;
import pvr3.tfg.domain.Coordinate;
import pvr3.tfg.domain.Population;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Pablo on 17/07/2015.
 */
public class PopulationManager extends AbstractFileManager {

    public PopulationManager(ArrayList<InputStream> streams, String kmlFileName) {
        super.setStreams(streams);
        super.setKml_file_name(kmlFileName);
    }

    @Override
    public String convertFromTextFile() {
        ArrayList<Population> populations = new ArrayList<>();
        Scanner sc = new Scanner(super.getStreams().get(0));
        URI uri;
        try {
            while(sc.hasNextLine()){
                if(sc.hasNext(Pattern.compile("%.*"))){
                    sc.nextLine();
                } else {
                    String geounit = sc.next();
                    int value = Integer.parseInt(sc.next());
                    Population p = new Population(geounit,value);
                    populations.add(p);
                    if(sc.hasNextLine()){
                        sc.nextLine();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if(!populations.isEmpty()){
            File f = generateKmlFile(populations, super.getStreams().get(1));
            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, super.getKml_file_name());
            return uri.toString();
        }

        return null;
    }

    private File generateKmlFile(ArrayList<Population> populations, InputStream centroidFileStream){
        HashMap<String, Coordinate> centroids = super.getCoordinatesFromCentroidFile(centroidFileStream);
        File f = new File("file.kml");
        Kml kmlFile = new Kml();
        Document doc = kmlFile.createAndSetDocument().withName("population.kml").withOpen(true);
        Folder populationFolder = doc.createAndAddFolder().withName("population").withOpen(true);
        Style barStyle = doc.createAndAddStyle().withId("populationStyle");
        barStyle.createAndSetPolyStyle().withColor("ff0000ff").withOutline(true);

        int maxPopulationVal = Population.getMaxPopulationValue(populations);
        for(Population p : populations){
            if(centroids.containsKey(p.getGeounit())){
                Placemark placemark = p.getKmlRepresentation(centroids.get(p.getGeounit()), maxPopulationVal);
                populationFolder.addToFeature(placemark);
            }
        }
        try {
            kmlFile.marshal(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return f;
    }
}
