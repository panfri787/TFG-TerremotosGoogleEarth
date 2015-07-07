package pvr3.tfg.domain;

/**
 * Created by Pablo on 30/06/2015.
 */
public class Shakecenter {
    private int geounit;
    private Coordinate coordinate;
    //TODO: Decidir como represento la propiedad soil
    private int soil;
    private float pga;
    private float sa_3;
    private float sa_10;

    public int getGeounit() {
        return geounit;
    }

    public void setGeounit(int geounit) {
        this.geounit = geounit;
    }

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

    //TODO: Getters y setters de la propiedad soil cuando decida como la represento.
}
