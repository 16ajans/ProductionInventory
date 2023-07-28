package cgminterpreter;

import java.io.DataInput;
import java.io.IOException;

public class VDCIntegerPrecision extends Command {

	public VDCIntegerPrecision(int ec, int eid, int l, DataInput in, CGM cgm) throws IOException {

		super(ec, eid, l, in, cgm);
		int precision = makeInt();
		cgm.setVdcIntegerPrecision(precision);

		assert precision == 16 || precision == 24 || precision == 32;
	}
}