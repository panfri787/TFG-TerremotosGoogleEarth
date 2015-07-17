package pvr3.tfg.domain;

/**
 * Created by Pablo on 17/07/2015.
 */
public class CoordinateKml extends Coordinate {

    private double high;

    public CoordinateKml(){
        super();
        this.high = 0;
    }

    public CoordinateKml(double latitude, double longitude, double high){
        super.setLatitude(latitude);
        super.setLongitude(longitude);
        this.high = high;
    }

    public double getHigh() {
        return high;
    }
}
