package pvr3.tfg.domain;

/**
 * Created by Pablo on 02/07/2015.
 */
public class Earthquake {
    /**
     * Weight of the earthquake for the locigal tree scheme
     */
    private float weight;

    /**
     * Latitude and longitude of the epicenter.
     */
    private Coordinate coordinate;

    /**
     * Constructor for the class 'Earthquake'.
     * @param weight Weight of the earthquake for the logical tree scheme.
     * @param coordinate Coordinate object with the latitude and the longitude of the epicenter.
     */
    public Earthquake(float weight, Coordinate coordinate) {
        this.weight = weight;
        this.coordinate = coordinate;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Put an earthquake in KML representation.
     * @return The String with the KML representation of the earthquake.
     */
    public String toKML(){
        String kml;

        kml = "<Placemark> \n" +
                "    <Style>\n" +
                "      <IconStyle>\n" +
                "        <color>FF1400FF</color>\n" +
                "        <scale>"+ this.getWeight()*4 +"</scale>\n" +
                "        <Icon>\n" +
                "          <href>http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png</href>\n" +
                "        </Icon>\n" +
                "      </IconStyle>\n" +
                "    </Style>\n" +
                "    <Point>\n" +
                "      <coordinates>"+ this.getCoordinate().getLongitude() +","+ this.getCoordinate().getLatitude() +"</coordinates>\n" +
                "    </Point>\n" +
                "</Placemark>";

        return kml;
    }
}
