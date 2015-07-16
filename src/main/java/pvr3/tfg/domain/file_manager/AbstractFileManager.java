package pvr3.tfg.domain.file_manager;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Pablo on 16/07/2015.
 */
public abstract class AbstractFileManager {
    private ArrayList<InputStream> streams;
    private String kml_file_name;
    private String additionalData;

    public abstract String convertFromTextFile();

    public String getKml_file_name() {
        return kml_file_name;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public ArrayList<InputStream> getStreams() {
        return streams;
    }

    public void setStreams(ArrayList<InputStream> streams) {
        this.streams = streams;
    }

    public void setKml_file_name(String kml_file_name) {
        this.kml_file_name = kml_file_name;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }
}
