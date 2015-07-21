package pvr3.tfg.domain.file_managers;

import de.micromata.opengis.kml.v_2_2_0.*;
import pvr3.tfg.domain.Coordinate;
import pvr3.tfg.domain.DamageAbsolute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Pablo on 20/07/2015.
 */
public class DamageAbsoluteManager extends AbstractFileManager{

    String type;

    public DamageAbsoluteManager(ArrayList<InputStream> streams, String kmlFileName, String additionalData, String type){
        super.setKml_file_name(kmlFileName);
        super.setStreams(streams);
        super.setAdditionalData(additionalData);
        this.type = type;
    }

    @Override
    public String convertFromTextFile() {
        ArrayList<DamageAbsolute> damageAbsolutes = new ArrayList<>();
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
                    DamageAbsolute damageAbsolute = null;
                    for (int i = 0; st.hasMoreTokens(); i++) {
                        if (st.hasMoreTokens()) {
                            if (i == Integer.parseInt(getAdditionalData())) {
                                int none = Integer.parseInt(st.nextToken());
                                int slight = Integer.parseInt(st.nextToken());
                                int moderate = Integer.parseInt(st.nextToken());
                                int extensive = Integer.parseInt(st.nextToken());
                                int complete = Integer.parseInt(st.nextToken());
                                damageAbsolute = new DamageAbsolute(geounit, none, slight, moderate, extensive, complete);
                                break;
                            } else {
                                for (int j = 0; j < 5; j++) {
                                    st.nextToken();
                                }
                            }
                        }
                    }
                    if (damageAbsolute != null) {
                        damageAbsolutes.add(damageAbsolute);
                    }
                }
            }
            setAdditionalData("");
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!damageAbsolutes.isEmpty()){
            File f = generateKmlFile(damageAbsolutes, super.getStreams().get(1));
            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, super.getKml_file_name());
            return uri.toString();
        }
        return "";
    }

    public File generateKmlFile(ArrayList<DamageAbsolute> damageAbsolutes, InputStream centroidFileStream){
        HashMap<String, Coordinate> centroids = super.getCoordinatesFromCentroidFile(centroidFileStream);
        File f = new File("file.kml");
        Kml kmlFile = new Kml();
        Document doc = kmlFile.createAndSetDocument().withName(this.type+".kml").withOpen(true);
        Folder populationFolder = doc.createAndAddFolder().withName(this.type).withOpen(true);
        int maxValue = DamageAbsolute.getMaxValueFromList(damageAbsolutes);

        for(DamageAbsolute mct : damageAbsolutes){
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
