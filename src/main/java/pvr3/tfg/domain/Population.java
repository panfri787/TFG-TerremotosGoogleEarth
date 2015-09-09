package pvr3.tfg.domain;

import de.micromata.opengis.kml.v_2_2_0.*;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

import java.util.ArrayList;

/**
 * Created by Pablo on 30/06/2015.
 */
public class Population {

    private String geounit;
    private int totalPopulation;

    public Population(String geounit, int totalPopulation) {
        this.geounit = geounit;
        this.totalPopulation = totalPopulation;
    }

    public Placemark getKmlRepresentation(Coordinate coordinate, int maxPopulationVal) {
        Placemark placemark = new Placemark();
        placemark.setName("Geounit: "+getGeounit()+", "+getTotalPopulation()+" personas");
        placemark.setStyleUrl("populationStyle");
        ArrayList<CoordinateKml> poligonCoordinates = this.calculateKmlCoordinates(coordinate, maxPopulationVal);
        Polygon bar = placemark.createAndSetPolygon();
        bar.setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);
        bar.setExtrude(true);
        Boundary boundary = new Boundary();
        LinearRing linearRing = boundary.createAndSetLinearRing();

        for(CoordinateKml ck : poligonCoordinates){
            linearRing.addToCoordinates(ck.getLongitude(),ck.getLatitude(),ck.getHigh());
        }

        bar.setOuterBoundaryIs(boundary);

        return placemark;
    }

    public ArrayList<CoordinateKml> calculateKmlCoordinates(Coordinate coordinate, int maxPopulationVal){
        double step = 0.002;
        double stepX = step * 0.5;
        double stepY = step;
        int k = 3;
        double highUnit;
        if(maxPopulationVal == 0){
            highUnit = 0.01;
        } else {
            highUnit = maxPopulationVal/2500d;
        }

        double factorReductionX = (step/50) * k;
        double factorReductionY = factorReductionX * 0.5;

        if(coordinate.getLatitude() < 0){
            factorReductionX = factorReductionX * -1;
            stepX = stepX * -1;
        }
        if(coordinate.getLongitude() < 0){
            factorReductionY = factorReductionY * -1;
            stepY = stepY * -1;
        }

        double high = 0;
        if(getTotalPopulation() > 0){
            high = getTotalPopulation()/highUnit;
        }

        ArrayList<CoordinateKml> coordinateKmlList = new ArrayList<>();
        double x = 0,y = 0;
        CoordinateKml originPoint = null;

        for(int j=0;j<4;j++){
            switch (j){
                case 0:
                    x = coordinate.getLatitude() - stepX - factorReductionX;
                    y = coordinate.getLongitude();
                    break;
                case 1:
                    x = coordinate.getLatitude();
                    y = coordinate.getLongitude() + stepY + factorReductionY;
                    break;
                case 2:
                    x = coordinate.getLatitude() + stepX + factorReductionX;
                    y = coordinate.getLongitude();
                    break;
                case 3:
                    x = coordinate.getLatitude();
                    y = coordinate.getLongitude() - stepY - factorReductionY;
                    break;
            }
            if(j == 0){
                originPoint = new CoordinateKml(x,y,high);
            }
            coordinateKmlList.add(new CoordinateKml(x,y,high));
        }
        coordinateKmlList.add(originPoint);

        return coordinateKmlList;
    }

    public static int getMaxPopulationValue(ArrayList<Population> poulations){
        int maxVal = 0;

        for(Population p : poulations){
            if(p.getTotalPopulation() > maxVal){
                maxVal = p.getTotalPopulation();
            }
        }

        return maxVal;
    }

    public String getGeounit() {
        return geounit;
    }

    public void setGeounit(String geounit) {
        this.geounit = geounit;
    }

    public int getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(int totalPopulation) {
        this.totalPopulation = totalPopulation;
    }
}
