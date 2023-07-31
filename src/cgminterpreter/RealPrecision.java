package cgminterpreter;

import java.io.DataInput;
import java.io.IOException;

import cgminterpreter.VDCRealPrecision.Type;

public class RealPrecision extends Command {
	enum Precision {
		FLOATING_32, FLOATING_64, FIXED_32, FIXED_64
	}

	public RealPrecision(int ec, int eid, int l, DataInput in, CGM cgm) throws IOException {

		super(ec, eid, l, in, cgm);
		int representation = makeEnum();
		int p2 = makeInt();
		int p3 = makeInt();
		Type vdcPrecision = Type.FIXED_POINT_32BIT;
		Precision precision = Precision.FIXED_32;
		if (representation == 0) {
			if (p2 == 9 && p3 == 23) {
				precision = Precision.FLOATING_32;
				vdcPrecision = Type.FLOATING_POINT_32BIT;
			} else if (p2 == 12 && p3 == 52) {
				precision = Precision.FLOATING_64;
				vdcPrecision = Type.FLOATING_POINT_32BIT;
			} else {
				assert false : "unsupported combination";
			}
		} else if (representation == 1) {
			if (p2 == 16 && p3 == 16) {
				precision = Precision.FIXED_32;
				vdcPrecision = Type.FIXED_POINT_32BIT;
			} else if (p2 == 32 && p3 == 32) {
				precision = Precision.FIXED_64;
				vdcPrecision = Type.FIXED_POINT_64BIT;
			} else {
				assert false : "unsupported combination";
			}
		} else {
			assert false : "unsupported representation";
		}

		cgm.setRealPrecision(precision);
		cgm.setRealPrecisionProcessed(true);

		if (!cgm.hasVDCRealPrecisionBeenProcessed()) {
			cgm.setVdcRealPrecision(vdcPrecision);
		}
	}
}