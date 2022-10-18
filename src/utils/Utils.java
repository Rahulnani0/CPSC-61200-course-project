package utils;

public class Utils {

    public static double clamp( double value, double min, double max) {

    	if( Double.compare(value, min) < 0)
    		return min;

    	if( Double.compare(value, max) > 0)
    		return max;

    	return value;
    }
}
