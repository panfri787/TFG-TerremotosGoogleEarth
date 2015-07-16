package pvr3.tfg.domain.file_manager;


import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * File management. The main propose of this class is read the "txt files" which were
 * uploaded to the website and convert it to "kml files" which can be load it on the Google Earth plugin
 */
public class FileManagerAbstractFactory {

    /**
     * The txt file
     */
    private InputStream stream;
    private ArrayList<InputStream> streams;
    private String kml_file_name;
    private String conversion_type;
    private String additionalData;

    /**
     * Constructor for multiple File inputs
     * @param streams
     * @param name
     */
    public FileManagerAbstractFactory(ArrayList<InputStream> streams, String name){
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
    public FileManagerAbstractFactory(ArrayList<InputStream> streams, String name, String additionalData){
        this.stream = null;
        this.streams = streams;
        this.conversion_type = name;
        this.additionalData = additionalData;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        this.kml_file_name = name+dateFormat.format(date)+".kml";
    }

    public AbstractFileManager getInstance(){
        AbstractFileManager instance=null;
        switch(this.conversion_type){
            case "earthquake":
                instance = new EarthquakeManager(this.streams.get(0),this.kml_file_name);
                break;
            case "soilcenter":
                instance = new SoilcenterManager(this.streams,this.kml_file_name,this.additionalData);
                break;
            case "shakecenter":
                instance = new ShakeCenterManager(this.streams,this.kml_file_name,this.additionalData);
                break;
            case "builtarea":
                instance = new BuiltAreaManager(this.streams,this.kml_file_name,this.additionalData);
                break;
            case "numbuild":
                instance = new NumBuildManager(this.streams,this.kml_file_name,this.additionalData);
                break;
            default:
                //url = "";
                break;
        }
        return instance;
    }
}
