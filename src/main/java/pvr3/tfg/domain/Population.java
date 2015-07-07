package pvr3.tfg.domain;

/**
 * Created by Pablo on 30/06/2015.
 */
public class Population {

    private int geounit;

    /**Total census tract population*/
    private int pop;

    /**Daytime residential population inferred from census data*/
    private int dres;

    /**Nighttime residential population inferred from census data*/
    private int nres;

    /**Number of people employed in the comercial sector*/
    private int comw;

    /**Number of people employed in the industrial sector*/
    private int indw;

    /**Number of people commuting inferred from census data*/
    private int comm;

    /**Number of students in grade school (Usually <17 years)*/
    private int grade;

    /**Number of students on college and university campuses in the census tract (>17 years)*/
    private int college;

    /**Number of people staying in hotels in the census tract*/
    private int hotel;

    /*TODO: Seria mejor representarlo como un enumerado
      En la documentacion hablan de este valor como "(0.60 for dense urban areas, 0.80 for less dense urban or suburban
      areas and 0.85 for rural)where the default value is 0.80"
     */
    /**Factor representing the proportion of commuters using automobiles inferred from profile of community*/
    private float prfil;

    /**Number of regional residents who do not live in the study area. Visiting the census tract for shopping and
     * entertaiment (Default value 0)*/
    private float visit;

    public int getGeounit() {
        return geounit;
    }

    public void setGeounit(int geounit) {
        this.geounit = geounit;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public int getDres() {
        return dres;
    }

    public void setDres(int dres) {
        this.dres = dres;
    }

    public int getNres() {
        return nres;
    }

    public void setNres(int nres) {
        this.nres = nres;
    }

    public int getComw() {
        return comw;
    }

    public void setComw(int comw) {
        this.comw = comw;
    }

    public int getIndw() {
        return indw;
    }

    public void setIndw(int indw) {
        this.indw = indw;
    }

    public int getComm() {
        return comm;
    }

    public void setComm(int comm) {
        this.comm = comm;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getCollege() {
        return college;
    }

    public void setCollege(int college) {
        this.college = college;
    }

    public int getHotel() {
        return hotel;
    }

    public void setHotel(int hotel) {
        this.hotel = hotel;
    }

    //TODO: Añadir metodos getter y setter para prfil cuando decida su representacion

    public float getVisit() {
        return visit;
    }

    public void setVisit(float visit) {
        this.visit = visit;
    }
}
