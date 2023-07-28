package cgminterpreter;

import java.awt.geom.Point2D;
import java.io.DataInput;
import java.io.IOException;

public class MaximumVDCExtent extends Command {
	Point2D.Double firstCorner;
	Point2D.Double secondCorner;

	public MaximumVDCExtent(int ec, int eid, int l, DataInput in, CGM cgm) throws IOException {
		super(ec, eid, l, in, cgm);
		this.firstCorner = makePoint();
		this.secondCorner = makePoint();
	}
}