package cgminterpreter;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CGM {
	List<Command> commands;

	private int integerPrecision = 16;
	private RealPrecision.Precision realPrecision = RealPrecision.Precision.FIXED_32;

	private VDCType.Type vdcType = VDCType.Type.INTEGER;
	private int vdcIntegerPrecision = 16;
	private VDCRealPrecision.Type vdcRealPrecision = VDCRealPrecision.Type.FIXED_POINT_32BIT;
	private boolean realPrecisionProcessed = false;

	public CGM(File cgmFile) throws IOException {
		InputStream inputStream = new FileInputStream(cgmFile);
//		String cgmFilename = cgmFile.getName();
		DataInputStream in = new DataInputStream(new BufferedInputStream(inputStream));
		read(in);
		in.close();
	}

	public void read(DataInput in) throws IOException {
		commands = new ArrayList<>();

		while (true) {
			Command c = Command.read(in, this);

			if (c == null) {
				break;
			}

			commands.add(c);
		}
	}
	
	public Dimension getSize() {
		Point2D.Double[] extent = extent();
		if (extent == null)
			return null;

		int width = (int)Math.ceil((Math.abs(extent[1].x - extent[0].x)));
		int height = (int)Math.ceil((Math.abs(extent[1].y - extent[0].y)));

		return new Dimension(width, height);
	}
	
	public Point2D.Double[] extent() {
		for (Command c : this.commands) {
			if (c instanceof VDCExtent) {
				Point2D.Double[] extent = ((VDCExtent) c).extent();
				return extent;
			}
		}
		return null;
	}
	
//	private ScalingMode getScalingMode() {
//		for (Command c : this.commands) {
//			if (c instanceof ScalingMode) {
//				return (ScalingMode)c;
//			}
//		}
//		return null;
//	}

	int getIntegerPrecision() {
		return this.integerPrecision;
	}

	void setIntegerPrecision(int integerPrecision) {
		this.integerPrecision = integerPrecision;
	}

	VDCType.Type getVdcType() {
		return this.vdcType;
	}

	void setVdcType(VDCType.Type vdcType) {
		this.vdcType = vdcType;
	}

	RealPrecision.Precision getRealPrecision() {
		return this.realPrecision;
	}

	void setRealPrecision(RealPrecision.Precision realPrecision) {
		this.realPrecision = realPrecision;
	}

	boolean hasRealPrecisionBeenProcessed() {
		return this.realPrecisionProcessed;
	}

	void setRealPrecisionProcessed(boolean realPrecisionProcessed) {
		this.realPrecisionProcessed = realPrecisionProcessed;
	}

	public VDCRealPrecision.Type getVdcRealPrecision() {
		return this.vdcRealPrecision;
	}

	public void setVdcRealPrecision(VDCRealPrecision.Type vdcRealPrecision) {
		this.vdcRealPrecision = vdcRealPrecision;
	}

	int getVdcIntegerPrecision() {
		return this.vdcIntegerPrecision;
	}

	void setVdcIntegerPrecision(int vdcIntegerPrecision) {
		this.vdcIntegerPrecision = vdcIntegerPrecision;
	}
}
