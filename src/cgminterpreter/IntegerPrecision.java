package cgminterpreter;

import java.io.DataInput;
import java.io.IOException;

public class IntegerPrecision extends Command {

	public IntegerPrecision(int ec, int eid, int l, DataInput in, CGM cgm) throws IOException {

		super(ec, eid, l, in, cgm);
		int precision = makeInt();
		cgm.setIntegerPrecision(precision);

		assert precision == 8 || precision == 16 || precision == 24 || precision == 32
				: "unsupported INTEGER PRECISION";
	}
}