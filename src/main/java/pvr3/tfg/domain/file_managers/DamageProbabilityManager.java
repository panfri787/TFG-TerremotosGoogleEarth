package pvr3.tfg.domain.file_managers;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import pvr3.tfg.domain.Coordinate;
import pvr3.tfg.domain.DamageProbability;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Pablo on 20/11/2015.
 */
public class DamageProbabilityManager extends AbstractFileManager{

    public DamageProbabilityManager(ArrayList<InputStream> streams, String kmlFileName, String additionalData){
        super.setKml_file_name(kmlFileName);
        super.setStreams(streams);
        super.setAdditionalData(additionalData);
    }

    @Override
    public String convertFromTextFile() {
        ArrayList<DamageProbability> damageProbabilities = new ArrayList<>();
        Scanner sc = new Scanner(super.getStreams().get(0));
        URI uri;
        try {
            while (sc.hasNextLine()) {
                if (sc.hasNext(Pattern.compile("%.*"))) {
                    sc.nextLine();
                } else {
                    String line = sc.nextLine();
                    StringTokenizer st = new StringTokenizer(line);
                    String geounit = st.nextToken();
                    for(int i=0; i<3; i++){
                        st.nextToken();
                    }
                    DamageProbability damageProbability = null;
                    for (int i = 0; st.hasMoreTokens(); i++) {
                        if (st.hasMoreTokens()) {
                            if (i == Float.parseFloat(getAdditionalData())) {
                                float none = Float.parseFloat(st.nextToken());
                                float slight = Float.parseFloat(st.nextToken());
                                float moderate = Float.parseFloat(st.nextToken());
                                float extensive = Float.parseFloat(st.nextToken());
                                float complete = Float.parseFloat(st.nextToken());
                                damageProbability = new DamageProbability(geounit, none, slight, moderate, extensive, complete);
                                break;
                            } else {
                                for (int j = 0; j < 5; j++) {
                                    st.nextToken();
                                }
                            }
                        }
                    }
                    if (damageProbability != null) {
                        damageProbabilities.add(damageProbability);
                    }
                }
            }
            setAdditionalData("");
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!damageProbabilities.isEmpty()){
            File f = generateKmlFile(damageProbabilities, super.getStreams().get(1));
            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, super.getKml_file_name());
            return uri.toString();
        }
        return "";
    }

    public File generateKmlFile(ArrayList<DamageProbability> damageProbabilities, InputStream centroidFileStream){
        HashMap<String, Coordinate> centroids = super.getCoordinatesFromCentroidFile(centroidFileStream);
        File f = new File("file.kml");
        Kml kmlFile = new Kml();
        Document doc = kmlFile.createAndSetDocument().withName("dout.kml").withOpen(true);
        Folder populationFolder = doc.createAndAddFolder().withName("dout").withOpen(true);
        float maxValue = DamageProbability.getMaxValueFromList(damageProbabilities);

        for(DamageProbability dout : damageProbabilities){
            if(centroids.containsKey(dout.getGeounit())){
                List<Placemark> placemarks = dout.getKmlRepresentation(centroids.get(dout.getGeounit()), maxValue);
                for(Placemark placemark: placemarks){
                    populationFolder.addToFeature(placemark);
                }
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
