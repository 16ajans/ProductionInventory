package cgminterpreter;

import java.io.DataInput;
import java.io.IOException;

public class VDCRealPrecision extends Command {
	enum Type {
		FLOATING_POINT_32BIT, FLOATING_POINT_64BIT, FIXED_POINT_32BIT, FIXED_POINT_64BIT,
	}

	public VDCRealPrecision(int ec, int eid, int l, DataInput in, CGM cgm) throws IOException {

		super(ec, eid, l, in, cgm);
		int p1 = makeEnum();
		int p2 = makeInt();
		int p3 = makeInt();

		Type precision = Type.FIXED_POINT_32BIT;

		if (p1 == 0) {
			if (p2 == 9 && p3 == 23) {
				precision = Type.FLOATING_POINT_32BIT;
			} else if (p2 == 12 && p3 == 52) {
				precision = Type.FLOATING_POINT_64BIT;
			} else {
				// use default
				precision = Type.FIXED_POINT_32BIT;
			}
		} else if (p1 == 1) {
			if (p2 == 16 && p3 == 16) {
				precision = Type.FIXED_POINT_32BIT;
			} else if (p2 == 32 && p3 == 32) {
				precision = Type.FIXED_POINT_64BIT;
			} else {
				// use default
				precision = Type.FIXED_POINT_32BIT;
			}
		}

		cgm.setVdcRealPrecision(precision);
	}
}