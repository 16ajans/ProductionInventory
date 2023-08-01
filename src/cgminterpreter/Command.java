package cgminterpreter;

import java.awt.geom.Point2D;
import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

public class Command {

	protected int args[];

	protected int currentArg = 0;

	private int posInArg = 0;

	protected CGM cgm;

	public static Command read(DataInput in, CGM cgm) throws IOException {

		int k;

		try { // NOT A LOOP
			k = in.readUnsignedByte();
			k = (k << 8) | in.readUnsignedByte();
		} catch (EOFException e) {
			return null;
		}

		int ec = k >> 12;
		int eid = (k >> 5) & 127;
		int l = k & 31; // preserves only the last 5 bits
		return readCommand(in, ec, eid, l, cgm);
	}

	protected static Command readCommand(DataInput in, int ec, int eid, int l, CGM cgm) throws IOException {

		switch (ec) {

		case 1:
			return Command.readMetaFileDescriptorElements(in, ec, eid, l, cgm);
		case 2:
			return Command.readPictureDescriptorElements(in, ec, eid, l, cgm);
		case 3:
			return Command.readControlElements(in, ec, eid, l, cgm);
		default:
			return new Command(ec, eid, l, in, cgm);
		}

	}

	private static Command readMetaFileDescriptorElements(DataInput in, int ec, int eid, int l, CGM cgm)
			throws IOException {
		switch (eid) {

		case 3:
			return new VDCType(ec, eid, l, in, cgm);
		case 4:
			return new IntegerPrecision(ec, eid, l, in, cgm);
		case 5:
			return new RealPrecision(ec, eid, l, in, cgm);
		case 12:
			return new MetafileDefaultsReplacement(ec, eid, l, in, cgm);
		case 17:
			return new MaximumVDCExtent(ec, eid, l, in, cgm);
		default:
			return new Command(ec, eid, l, in, cgm);
		}
	}

	private static Command readPictureDescriptorElements(DataInput in, int ec, int eid, int l, CGM cgm)
			throws IOException {
		switch (eid) {

		case 1:
			return new ScalingMode(ec, eid, l, in, cgm);
		case 6:
			return new VDCExtent(ec, eid, l, in, cgm);
		case 8:
			System.out.println("Dev Viewport");
			break;
		case 9:
			System.out.println("Dev Viewport Spec Mode");
			break;
		case 10:
			System.out.println("Dev Viewport Mapping");
			break;
		}
		return new Command(ec, eid, l, in, cgm);

	}

	private static Command readControlElements(DataInput in, int ec, int eid, int l, CGM cgm) throws IOException {
		switch (eid) {

		case 1:
			return new VDCIntegerPrecision(ec, eid, l, in, cgm);
		case 2:
			return new VDCRealPrecision(ec, eid, l, in, cgm);
		}
		return new Command(ec, eid, l, in, cgm);

	}

	public Command(int ec, int eid, int l, DataInput in, CGM cgm) throws IOException {

		this.cgm = cgm;
		if (l != 31) {
			this.args = new int[l];
			for (int i = 0; i < l; i++)
				this.args[i] = in.readUnsignedByte();
			if (l % 2 == 1) {
				try {
					in.readUnsignedByte();
				} catch (EOFException e) {
					// we've reached the end of the data input. Since we're only
					// skipping data here, the exception can be ignored.
				}
			}
		} else {
			// this is a long form command
			boolean done = true;
			int a = 0;
			do {
				l = read16(in);
				if (l == -1)
					break;
				if ((l & (1 << 15)) != 0) {
					// data is partitioned and it's not the last partition
					done = false;
					// clear bit 15
					l = l & ~(1 << 15);
				} else {
					done = true;
				}
				if (this.args == null) {
					this.args = new int[l];
				} else {
					// resize the args array
					this.args = Arrays.copyOf(this.args, this.args.length + l);
				}
				for (int i = 0; i < l; i++)
					this.args[a++] = in.readUnsignedByte();

				// align on a word if necessary
				if (l % 2 == 1) {
					int skip = in.readUnsignedByte();
					assert (skip == 0) : "skipping data";
				}
			} while (!done);
		}
	}

	private int read16(DataInput in) throws IOException {
		return (in.readUnsignedByte() << 8) | in.readUnsignedByte();
	}

	final protected int makeInt() {
		int precision = this.cgm.getIntegerPrecision();
		return makeInt(precision);
	}

	private int makeInt(int precision) {
		skipBits();
		if (precision == 8) {
			return makeSignedInt8();
		}
		if (precision == 16) {
			return makeSignedInt16();
		}
		if (precision == 24) {
			return makeSignedInt24();
		}
		if (precision == 32) {
			return makeSignedInt32();
		}

		// return default
		return makeSignedInt16();
	}

	final protected int makeSignedInt8() {
		skipBits();
		assert (this.currentArg < this.args.length);
		return (byte) this.args[this.currentArg++];
	}

	final protected int makeSignedInt16() {
		skipBits();
		assert (this.currentArg + 1 < this.args.length);
		return ((short) (this.args[this.currentArg++] << 8) + this.args[this.currentArg++]);
	}

	final protected int makeSignedInt24() {
		skipBits();
		assert (this.currentArg + 2 < this.args.length);
		return (this.args[this.currentArg++] << 16) + (this.args[this.currentArg++] << 8)
				+ this.args[this.currentArg++];
	}

	final protected int makeSignedInt32() {
		skipBits();
		assert (this.currentArg + 3 < this.args.length);
		return (this.args[this.currentArg++] << 24) + (this.args[this.currentArg++] << 16)
				+ (this.args[this.currentArg++] << 8) + this.args[this.currentArg++];
	}

	private void skipBits() {
		if (this.posInArg % 8 != 0) {
			// we read some bits from the current arg but aren't done, skip the rest
			this.posInArg = 0;
			this.currentArg++;
		}
	}

	final protected int makeEnum() {
		return makeSignedInt16();
	}

	final protected Point2D.Double makePoint() {
		return new Point2D.Double(makeVdc(), makeVdc());
	}

	final protected double makeVdc() {

		if (this.cgm.getVdcType().equals(VDCType.Type.REAL)) {
			VDCRealPrecision.Type precision = this.cgm.getVdcRealPrecision();
			if (precision.equals(VDCRealPrecision.Type.FIXED_POINT_32BIT)) {
				return makeFixedPoint32();
			}
			if (precision.equals(VDCRealPrecision.Type.FIXED_POINT_64BIT)) {
				return makeFixedPoint64();
			}
			if (precision.equals(VDCRealPrecision.Type.FLOATING_POINT_32BIT)) {
				return makeFloatingPoint32();
			}
			if (precision.equals(VDCRealPrecision.Type.FLOATING_POINT_64BIT)) {
				return makeFloatingPoint64();
			}

			return makeFixedPoint32();
		}

		int precision = this.cgm.getVdcIntegerPrecision();
		if (precision == 16) {
			return makeSignedInt16();
		}
		if (precision == 24) {
			return makeSignedInt24();
		}
		if (precision == 32) {
			return makeSignedInt32();
		}

		return makeSignedInt16();
	}

	private double makeFixedPoint32() {
		double wholePart = makeSignedInt16();
		double fractionPart = makeUInt16();

		return wholePart + (fractionPart / (2 << 15));
	}

	private double makeFixedPoint64() {
		double wholePart = makeSignedInt32();
		double fractionPart = makeUInt32();

		return wholePart + (fractionPart / (2 << 31));
	}

	protected final double makeFloatingPoint32() {
		skipBits();
		int bits = 0;
		for (int i = 0; i < 4; i++) {
			bits = (bits << 8) | makeChar();
		}
		return Float.intBitsToFloat(bits);
	}

	private double makeFloatingPoint64() {
		skipBits();
		long bits = 0;
		for (int i = 0; i < 8; i++) {
			bits = (bits << 8) | makeChar();
		}
		return Double.longBitsToDouble(bits);
	}

	final protected char makeChar() {
		skipBits();
		assert (this.currentArg < this.args.length);
		return (char) this.args[this.currentArg++];
	}

	final protected byte makeByte() {
		skipBits();
		assert (this.currentArg < this.args.length);
		return (byte) this.args[this.currentArg++];
	}

	final protected double makeFloatingPoint() {
		RealPrecision.Precision precision = this.cgm.getRealPrecision();
		if (precision.equals(RealPrecision.Precision.FLOATING_32)) {
			return makeFloatingPoint32();
		}
		if (precision.equals(RealPrecision.Precision.FLOATING_64)) {
			return makeFloatingPoint64();
		}
		return makeFloatingPoint32();
	}

	final protected int makeUInt(int precision) {
		if (precision == 1) {
			return makeUInt1();
		}
		if (precision == 2) {
			return makeUInt2();
		}
		if (precision == 4) {
			return makeUInt4();
		}
		if (precision == 8) {
			return makeUInt8();
		}
		if (precision == 16) {
			return makeUInt16();
		}
		if (precision == 24) {
			return makeUInt24();
		}
		if (precision == 32) {
			return makeUInt32();
		}

		// return default
		return makeUInt8();
	}

	private int makeUInt32() {
		skipBits();
		assert this.currentArg + 3 < this.args.length;
		return (char) (this.args[this.currentArg++] << 24) + (char) (this.args[this.currentArg++] << 16)
				+ (char) (this.args[this.currentArg++] << 8) + (char) this.args[this.currentArg++];
	}

	private int makeUInt24() {
		skipBits();
		assert this.currentArg + 2 < this.args.length;
		return (char) (this.args[this.currentArg++] << 16) + (char) (this.args[this.currentArg++] << 8)
				+ (char) this.args[this.currentArg++];
	}

	private int makeUInt16() {
		skipBits();

		if (this.currentArg + 1 < this.args.length) {
			// this is the default, two bytes
			return (char) (this.args[this.currentArg++] << 8) + (char) this.args[this.currentArg++];
		}

		// some CGM files request a 16 bit precision integer when there are only 8 bits
		// left
		if (this.currentArg < this.args.length) {
			return (char) this.args[this.currentArg++];
		}

		assert false;
		return 0;
	}

	private int makeUInt8() {
		skipBits();
		assert this.currentArg < this.args.length;
		return (char) this.args[this.currentArg++];
	}

	private int makeUInt4() {
		return makeUIntBit(4);
	}

	private int makeUInt2() {
		return makeUIntBit(2);
	}

	private int makeUInt1() {
		return makeUIntBit(1);
	}

	private int makeUIntBit(int numBits) {
		assert (this.currentArg < this.args.length);

		int bitsPosition = 8 - numBits - this.posInArg;
		int mask = ((1 << numBits) - 1) << bitsPosition;
		int ret = (char) ((this.args[this.currentArg] & mask) >> bitsPosition);
		this.posInArg += numBits;
		if (this.posInArg % 8 == 0) {
			// advance to next byte
			this.posInArg = 0;
			this.currentArg++;
		}
		return ret;
	}
}
