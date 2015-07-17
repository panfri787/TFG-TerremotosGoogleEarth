package pvr3.tfg.domain;

/**
 * Created by Pablo on 30/06/2015.
 */
public class Coordinate {
    private double latitude;
    private double longitude;

    public Coordinate(){
        this.latitude=0;
        this.longitude=0;
    }

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
