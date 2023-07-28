package cgminterpreter;

import java.awt.geom.Point2D;
import java.io.DataInput;
import java.io.IOException;

public class VDCExtent extends Command {
	Point2D.Double lowerLeftCorner;
	Point2D.Double upperRightCorner;

	public VDCExtent(int ec, int eid, int l, DataInput in, CGM cgm) throws IOException {
		super(ec, eid, l, in, cgm);
		this.lowerLeftCorner = makePoint();
		this.upperRightCorner = makePoint();
	}

	public Point2D.Double[] extent() {
		Point2D.Double points[] = new Point2D.Double[2];
		points[0] = this.lowerLeftCorner;
		points[1] = this.upperRightCorner;
		return points;
	}
}