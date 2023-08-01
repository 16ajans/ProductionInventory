package cgminterpreter;

import java.awt.Dimension;
import java.text.DecimalFormat;

public class DoubleDimension extends Dimension {

	DecimalFormat df = new DecimalFormat("0.00");

	/**
	 * 
	 */
	private static final long serialVersionUID = 3080293633433066398L;

	double height;
	double width;

	public DoubleDimension() {
		height = 0;
		width = 0;
	}

	public DoubleDimension(double x, double y) {
		if (x < 36 && y < 36) {
			if (x > y) {
				width = x;
				height = y;
			} else {
				width = y;
				height = x;
			}
		} else {
			if (x > y) {
				width = y;
				height = x;
			} else {
				width = x;
				height = y;
			}
		}
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public String toString() {
		return String.format("%" + 6 + "s", df.format(width)) + " " + String.format("%" + 6 + "s", df.format(height));
	}
}
