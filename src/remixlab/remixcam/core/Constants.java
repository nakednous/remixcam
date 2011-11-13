package remixlab.remixcam.core;

public interface Constants {	
	/**
   * PI is a mathematical constant with the value 3.14159265358979323846.
   * It is the ratio of the circumference of a circle to its diameter.
   * It is useful in combination with the trigonometric functions <b>sin()</b> and <b>cos()</b>.
   *  
   * @see #HALF_PI
   * @see #TWO_PI
   * @see #QUARTER_PI
   * 
   */
	static final float PI = (float) Math.PI;
  /**
   * HALF_PI is a mathematical constant with the value 1.57079632679489661923.
   * It is half the ratio of the circumference of a circle to its diameter.
   * It is useful in combination with the trigonometric functions <b>sin()</b> and <b>cos()</b>.
   * 
   * @see #PI
   * @see #TWO_PI
   * @see #QUARTER_PI
   */
  static final float HALF_PI    = PI / 2.0f;
  static final float THIRD_PI   = PI / 3.0f;
  /**
   * QUARTER_PI is a mathematical constant with the value 0.7853982.
   * It is one quarter the ratio of the circumference of a circle to its diameter.
   * It is useful in combination with the trigonometric functions <b>sin()</b> and <b>cos()</b>.
   * 
   * @see #PI
   * @see #TWO_PI
   * @see #HALF_PI
   */
  static final float QUARTER_PI = PI / 4.0f;
  /**
   * TWO_PI is a mathematical constant with the value 6.28318530717958647693.
   * It is twice the ratio of the circumference of a circle to its diameter.
   * It is useful in combination with the trigonometric functions <b>sin()</b> and <b>cos()</b>.
   * 
   * @see #PI
   * @see #HALF_PI
   * @see #QUARTER_PI
   */
  static final float TWO_PI     = PI * 2.0f;
}
