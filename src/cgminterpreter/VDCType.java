package cgminterpreter;

import java.io.DataInput;
import java.io.IOException;

public class VDCType extends Command {
	enum Type {
		INTEGER, REAL
	}

	public VDCType(int ec, int eid, int l, DataInput in, CGM cgm) throws IOException {
		super(ec, eid, l, in, cgm);
		int p1 = makeInt();
		Type type = Type.INTEGER;
		if (p1 == 0)
			type = Type.INTEGER;
		else if (p1 == 1)
			type = Type.REAL;

		cgm.setVdcType(type);
	}
}