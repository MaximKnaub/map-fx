package de.knaubmaxim.mapfx.coord;

import static de.knaubmaxim.mapfx.coord.CoordinateUtils.bearingTo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maka
 * @date 17.10.2013
 */
public class PolygonUtils {

    private static final DecimalFormat DECIMAL_FORMAT_3_DIGITS = new DecimalFormat("000");

    public static boolean isInsidePolygon(List<Coordinate> coordList, Coordinate coord) {
        boolean inside = false;
        double x1 = coordList.get(coordList.size() - 1).getLongitude();
        double y1 = coordList.get(coordList.size() - 1).getLatitude();
        double x2 = coordList.get(0).getLongitude();
        double y2 = coordList.get(0).getLatitude();

        boolean startUeber = y1 >= coord.getLatitude();
        for (int i = 1; i < coordList.size(); i++) {
            boolean endUeber = y2 >= coord.getLatitude();
            if (startUeber != endUeber) {
                if ((y2 - coord.getLatitude()) * (x2 - x1) <= (y2 - y1) * (x2 - coord.getLongitude())) {
                    if (endUeber) {
                        inside = !inside;
                    }
                } else {
                    if (!endUeber) {
                        inside = !inside;
                    }
                }
            }

            startUeber = endUeber;
            y1 = y2;
            x1 = x2;
            x2 = coordList.get(i).getLongitude();
            y2 = coordList.get(i).getLatitude();
        }
        return inside;
    }

    /**
     *
     * @param geoCoordList
     * @param distance in meter
     * @return
     */
    public static List<Coordinate> expandPolygon(List<Coordinate> geoCoordList, int distance) {
        List<Coordinate> resultList = new ArrayList<>();
        List<Coordinate> tempList = new ArrayList<>();
        if (geoCoordList.isEmpty()) {
            return resultList;
        }

        final List<Coordinate> list = fillGaps(geoCoordList);
        for (int i = 0; i < list.size(); i++) {
            Coordinate previous = list.get(i - 1 < 0 ? list.size() - 1 : i - 1);
            Coordinate coord = list.get(i);
            Coordinate next = list.get((i + 1) > list.size() - 1 ? 0 : i + 1);
            int startWinkel = (int) bearingTo(coord, previous);
            int endWinkel = (int) bearingTo(coord, next);

            for (int angle = startWinkel; angle != endWinkel;) {
                if (angle > 360) {
                    angle = 0;
                    continue;
                } else {
                    angle++;
                }
                Coordinate gk = coord.destinationPoint(angle, distance);
                if (isInsidePolygon(list, gk) == false) {
                    tempList.add(gk);
                }
            }
        }

        tempList.stream().filter((Coordinate t) -> {
            return list.stream().noneMatch((geoCoord) -> (geoCoord.distanceTo(t) < distance /*|| PolygonUtils.isInsidePolygon(list, t)*/ == true));
        }).forEach(coord -> {
            resultList.add(coord);
        });
        if (resultList.isEmpty() == false) {
            resultList.add(resultList.get(0));
        }
        return resultList;
    }

    private static List<Coordinate> getClosestCoordinateVector(List<Coordinate> geoCoordlist, Coordinate exitCoord, Coordinate enterCoord) {
        List<Coordinate> data = new ArrayList<>();
        Coordinate closestExitCoordinate;
        Double distance = 40_075.0;
        boolean swap = false;
        for (Coordinate geoCoord : geoCoordlist) {
            double tmpDistance = swap ? enterCoord.distanceTo(geoCoord) : exitCoord.distanceTo(geoCoord);
            if (tmpDistance < distance) {
                distance = tmpDistance;
            } else {
                swap = !swap;
            }
            if (swap) {
                data.add(geoCoord);
            }
        }
        return data;
    }

    public static Coordinate getAVGCoordinate(List<Coordinate> coordinateList) {
        if (coordinateList.isEmpty()) {
            return null;
        }
        double lon, lat;
        lon = coordinateList.stream().mapToDouble(val -> val.getLongitude()).sum() / coordinateList.size();
        lat = coordinateList.stream().mapToDouble(val -> val.getLatitude()).sum() / coordinateList.size();
        return new Coordinate(lon, lat);
    }

    /* ************* UTILS ************** */
    /**
     *
     * @param list
     * @return
     */
    public static List<Coordinate> fillGaps(List<Coordinate> list) {
        double gapSize = 3_000;
        List<Coordinate> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Coordinate coord = list.get(i);
            Coordinate next = list.get((i + 1) < list.size() ? i + 1 : 0);
            double angle = bearingTo(coord, next);
            result.add(coord);
            while (coord.distanceTo(next) > gapSize) {
                coord = coord.destinationPoint(angle, gapSize);
                result.add(coord);
            }
        }
        return result;
    }

}
