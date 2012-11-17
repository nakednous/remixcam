package remixlab.remixcam.geom;

public class Geom {
	public static float FLOAT_EPS = Float.MIN_VALUE;
  // Calculation of the Machine Epsilon for float precision. From:
  // http://en.wikipedia.org/wiki/Machine_epsilon#Approximation_using_Java
  static {
    float eps = 1.0f;

    do {
      eps /= 2.0f;
    } while ((float)(1.0 + (eps / 2.0)) != 1.0);

    FLOAT_EPS = eps;
  }
	
  //TODO check this throughout all the lib
	public static boolean same(float a, float b) {
    return Math.abs(a - b) < FLOAT_EPS;
  }

  //TODO check this throughout all the lib
  public static boolean diff(float a, float b) {
    return FLOAT_EPS <= Math.abs(a - b);
  }

  public static boolean zero(float a) {
    return Math.abs(a) < FLOAT_EPS;
  }

  public static boolean nonZero(float a) {
    return FLOAT_EPS <= Math.abs(a);
  }
}
