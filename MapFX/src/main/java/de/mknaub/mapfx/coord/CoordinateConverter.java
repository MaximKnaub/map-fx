package de.knaubmaxim.mapfx.coord;

import static de.knaubmaxim.mapfx.coord.CoordinateConverter.LAENGEN_BREITEN_GRAD.BREITENGRAD;
import static de.knaubmaxim.mapfx.coord.CoordinateConverter.LAENGEN_BREITEN_GRAD.LAENGENGRAD;
import static java.lang.Math.floor;
import static java.lang.Math.round;

/**
 *
 * @author maka
 * @date 07.02.13
 */
public class CoordinateConverter {

    /**
     * Umwandlung von gradieller nach dezimaler Darstellung. Es muss
     * beruecksichtigt werden, dass bei der Umwandlung von geographischen
     * Koordinaten - insbesondere die Laenge - das spezielle Format
     * beruecksichtigt wird --> 0xxExxxx. Die fuehrende Null muss entfernt
     * werden. 03.11.2004, me11
     *
     * @param value
     * @return
     */
    public static double WGS84ToDez(String value) {
        String modifiedValue = value;

        // Null bzw. "" ist nicht gestattet. Des Weiteren ebenfalls keine negativen
        // Eingabe (07.11.2004, me11)
        if (value == null || value.length() == 0 || value.indexOf('-', 0) != -1) {
            return 0;
        }

        int index = value.indexOf('E');

        if (index == -1) {
            index = value.indexOf('W');
        }

        if (index == -1) {
            index = value.indexOf('N');
        }
        if (index == -1) {
            index = value.indexOf('S');
        }

        // Geographischen Laengengrad umwandeln
        if (index != -1) {
            modifiedValue = value.substring(value.substring(index, index + 1).equals("E") ? 1 : 0, index).trim();	// Vorkommaanteil

            String fractions = value.substring(index + 1).trim();

            while (fractions.length() < 4) {
                fractions += "0";
            }
            modifiedValue += fractions;	// Minuten/Sekunden
        }

        while (modifiedValue.trim().length() < 6) {
            modifiedValue = "0" + modifiedValue;
        }

        // seda 18.08.2011 - Rundung auf 4 Nachkommastellen
//        DecimalFormat df = new DecimalFormat("#0.000000000000");
        return ((Double.valueOf(modifiedValue.substring(4, 6)) / 60)
                + Double.valueOf(modifiedValue.substring(2, 4))) / 60
                + Double.valueOf(modifiedValue.substring(0, 2));

//        return parseDouble(df.format(((new Double(modifiedValue.substring(4, 6)) / 60)
//                + new Double(modifiedValue.substring(2, 4))) / 60
//                + new Double(modifiedValue.substring(0, 2))).replace(",", "."));
        //    double res = ((new Double(modifiedValue.substring(4, 6)).doubleValue()/60)
        //            + new Double(modifiedValue.substring(2, 4)).doubleValue())/60
        //            + new Double(modifiedValue.substring(0, 2)).doubleValue();
    }

    public static String dez2grad(double value, LAENGEN_BREITEN_GRAD laengen_breiten_grad, boolean round) {
        String result;
        String minuten;
        String sekunden;

        String laenge_breite = "";

        // seda
        if (laengen_breiten_grad == LAENGENGRAD) {
            if (value >= 0) {
                laenge_breite = "E";
            } else {
                laenge_breite = "W";
            }
        } else if (laengen_breiten_grad == BREITENGRAD) {
            if (value >= 0) {
                laenge_breite = "N";
            } else {
                laenge_breite = "S";
            }
        }

        // Grad
        double grad = floor(value);

        value -= grad;

        // Minuten
        value *= 60;
        double min = floor(value);
        value -= min;

        // Sekunden
        value *= 60;

        int seconds;

        if (false) { // kaufmännische Rundung, das eigentlich korrekte Vorgehen
            seconds = (int) round(value);
            if (seconds == 60) { //Bei 60sec Umrechnung in Minuten
                seconds = 0;
                min = min + 1;
                if (min == 60) { //Bei 60min Umrechnung in Grad
                    grad = grad + 1;
                }
            }//seconds = 59;
        } else { // Abrundung aufgrund Kompatibilität zu FAST
            seconds = (int) floor(value);
        }

        result = "" + (int) grad;
        // fuer Bessel, ohne E/N
        int addiereEineNull = 0;
        if (laenge_breite.equalsIgnoreCase("E")) {
            addiereEineNull = 1;
        }

        while (result.trim().length() < 2 + addiereEineNull) {
            result = "0" + result;
        }

        result += laenge_breite;

        minuten = "" + (int) min;
        while (minuten.trim().length() < 2) {
            minuten = "0" + minuten;
        }
        result += minuten;

        while (minuten.trim().length() < 2) {
            minuten = "0" + minuten;
        }

        sekunden = "" + seconds;
        while (sekunden.trim().length() < 2) {
            sekunden = "0" + sekunden;
        }
        result += sekunden;

        return result.replace('-', ' ').trim();
    }

    /**
     *
     */
    public enum LAENGEN_BREITEN_GRAD {

        LAENGENGRAD,
        BREITENGRAD;
    }
}
