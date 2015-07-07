package pvr3.tfg.domain;

/**
 * Created by Pablo on 30/06/2015.
 */
public class Coordinate {
    private float latitude;
    private float longitude;

    public Coordinate(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
