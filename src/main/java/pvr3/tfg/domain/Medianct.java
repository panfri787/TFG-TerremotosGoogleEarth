package pvr3.tfg.domain;

/**
 * Created by Pablo on 20/07/2015.
 */
public class Medianct {
    private String geounit;
    private int none;
    private int slight;
    private int moderate;
    private int extensive;
    private int complete;

    public Medianct(String geounit, int none, int slight, int moderate, int extensive, int complete) {
        this.geounit = geounit;
        this.none = none;
        this.slight = slight;
        this.moderate = moderate;
        this.extensive = extensive;
        this.complete = complete;
    }
}
