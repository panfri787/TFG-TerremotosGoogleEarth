package pvr3.tfg.domain.file_managers;

import de.micromata.opengis.kml.v_2_2_0.*;
import pvr3.tfg.domain.Coordinate;
import pvr3.tfg.domain.Medianct;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Pablo on 20/07/2015.
 */
public class MedianctManager extends AbstractFileManager{

    public MedianctManager(ArrayList<InputStream> streams, String kmlFileName, String additionalData){
        super.setKml_file_name(kmlFileName);
        super.setStreams(streams);
        super.setAdditionalData(additionalData);
    }

    @Override
    public String convertFromTextFile() {
        ArrayList<Medianct> mediancts = new ArrayList<>();
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
                    Medianct medianct = null;
                    for (int i = 0; st.hasMoreTokens(); i++) {
                        if (st.hasMoreTokens()) {
                            if (i == Integer.parseInt(getAdditionalData())) {
                                int none = Integer.parseInt(st.nextToken());
                                int slight = Integer.parseInt(st.nextToken());
                                int moderate = Integer.parseInt(st.nextToken());
                                int extensive = Integer.parseInt(st.nextToken());
                                int complete = Integer.parseInt(st.nextToken());
                                medianct = new Medianct(geounit, none, slight, moderate, extensive, complete);
                                break;
                            } else {
                                for (int j = 0; j < 5; j++) {
                                    st.nextToken();
                                }
                            }
                        }
                    }
                    if (medianct != null) {
                        mediancts.add(medianct);
                    }
                }
            }
            setAdditionalData("");
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!mediancts.isEmpty()){
            File f = generateKmlFile(mediancts, super.getStreams().get(1));
            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, super.getKml_file_name());
            return uri.toString();
        }
        return "";
    }

    public File generateKmlFile(ArrayList<Medianct> mediancts, InputStream centroidFileStream){
        HashMap<String, Coordinate> centroids = super.getCoordinatesFromCentroidFile(centroidFileStream);
        File f = new File("file.kml");
        Kml kmlFile = new Kml();
        Document doc = kmlFile.createAndSetDocument().withName("medianct.kml").withOpen(true);
        Folder populationFolder = doc.createAndAddFolder().withName("medianct").withOpen(true);
        int maxValue = Medianct.getMaxValueFromList(mediancts);

        for(Medianct mct : mediancts){
            if(centroids.containsKey(mct.getGeounit())){
                List<Placemark> placemarks = mct.getKmlRepresentation(centroids.get(mct.getGeounit()), maxValue);
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
