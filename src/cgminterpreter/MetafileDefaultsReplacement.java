package cgminterpreter;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

public class MetafileDefaultsReplacement extends Command {
	public MetafileDefaultsReplacement(int ec, int eid, int l, DataInput in, CGM cgm) throws IOException {
		super(ec, eid, l, in, cgm);

		int k = makeUInt(16);

		// the element class
		int elementClass = k >> 12;
		int elementId = (k >> 5) & 127;

		int nArgs = k & 31;
		if (nArgs == 31) {
			// it's a long form command
			nArgs = makeUInt(16);

			// TODO: we don't support partitioned data here
			assert ((nArgs & (1 << 15)) == 0);
		}

		// copy all the remaining arguments in an array
		byte commandArguments[] = new byte[nArgs];
		int c = 0;
		while (c < nArgs) {
			commandArguments[c++] = makeByte();
		}

		readCommand(new DataInputStream(new ByteArrayInputStream(commandArguments)), elementClass, elementId,
				commandArguments.length, cgm);
	}
}