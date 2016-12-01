package view.helpers;

/**
 * Geometry math helper methods
 */
public final class Geometry {

    /**
     * Calculates the ArcAngle alpha of the circular sector by the given start and end angle.
     * @param startAngle
     * @param endAngle
     * @return The ArcAngle alpha
     */
    public static double calculateAngleClockwise(double startAngle, double endAngle){

        if(startAngle < 0){
            throw new IllegalArgumentException("startAngle must be > 0, current: " + startAngle);
        }else if (endAngle < 0){
            throw new IllegalArgumentException("endAngle must be > 0, current: " + endAngle);
        }

        double alpha = 0;

        if(endAngle > startAngle){
            alpha = endAngle - startAngle;
        }
        else if(startAngle > endAngle){
            alpha = 360 - (startAngle - endAngle);
        }

        return alpha;
    }

}
