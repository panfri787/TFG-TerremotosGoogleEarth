package pvr3.tfg.domain;

/**
 * Created by Pablo on 30/06/2015.
 */
public class RealtimeGrid {

    private Coordinate coordinate;
    private float pga;
    private float sa_3;
    private float sa_10;

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public float getPga() {
        return pga;
    }

    public void setPga(float pga) {
        this.pga = pga;
    }

    public float getSa_3() {
        return sa_3;
    }

    public void setSa_3(float sa_3) {
        this.sa_3 = sa_3;
    }

    public float getSa_10() {
        return sa_10;
    }

    public void setSa_10(float sa_10) {
        this.sa_10 = sa_10;
    }
}
