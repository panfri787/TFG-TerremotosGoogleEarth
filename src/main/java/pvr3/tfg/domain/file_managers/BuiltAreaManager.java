package pvr3.tfg.domain.file_managers;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import pvr3.tfg.domain.BuiltArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Created by Pablo on 16/07/2015.
 */
public class BuiltAreaManager extends AbstractFileManager {

    private ArrayList<InputStream> streams;
    private String kml_file_name;
    private String additionalData;

    public BuiltAreaManager(ArrayList<InputStream> streams, String kml_file_name, String additionalData) {
        super();
        this.streams = streams;
        this.kml_file_name = kml_file_name;
        this.additionalData = additionalData;
    }

    public String convertFromTextFile() {
        ArrayList<BuiltArea> builtAreas = new ArrayList<>();
        Scanner sc = new Scanner(this.streams.get(0));
        URI uri;
        try {
            while (sc.hasNextLine()) {
                if (sc.hasNext(Pattern.compile("%.*"))) {
                    sc.nextLine();
                } else {
                    String line = sc.nextLine();
                    StringTokenizer t = new StringTokenizer(line);
                    String geounit = t.nextToken();
                    BuiltArea b = new BuiltArea(geounit);
                    for (int i = 0; t.hasMoreTokens(); i++) {
                        float area = Float.parseFloat(t.nextToken());
                        if (t.hasMoreTokens()) {
                            b.setTotalBuiltArea(b.getTotalBuiltArea() + area);
                            if (i == Integer.parseInt(this.additionalData)) {
                                b.setSelectedBuiltArea(area);
                            }
                        }
                    }
                    builtAreas.add(b);
                }
            }
            setAdditionalData("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!builtAreas.isEmpty()) {
            File f = generateKmlFile(builtAreas, this.streams.get(1));
            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, kml_file_name);
            return uri.toString();
        }
        return "";
    }

    public File generateKmlFile(List<BuiltArea> builtAreas, InputStream polytractFile){
        Kml polyTract = Kml.unmarshal(polytractFile);
        Document document = (Document)polyTract.getFeature();
        Folder polyFolder = null;
        File f = new File("file.kml");
        for(int i=0; i<document.getFeature().size(); i++){
            if(document.getFeature().get(i) instanceof Folder){
                polyFolder = (Folder) document.getFeature().get(i);
                break;
            }
        }
        Folder soilFolder = new Folder().withName("builtarea");

        for(int i = 0; i < polyFolder.getFeature().size() && i < builtAreas.size(); i++){

            if(polyFolder.getFeature().get(i) instanceof Placemark){
                Placemark placemark = (Placemark) polyFolder.getFeature().get(i);
                if(placemark.getName().equals(builtAreas.get(i).getGeounit()) ||
                        placemark.getId().equals(builtAreas.get(i).getGeounit())) {
                    placemark.createAndAddStyle().withPolyStyle(builtAreas.get(i).getKmlStyle());
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

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public String getKml_file_name() {
        return kml_file_name;
    }

    public String getAdditionalData() {
        return additionalData;
    }
}
