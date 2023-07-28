package cgminterpreter;

import java.io.DataInput;
import java.io.IOException;

public class ScalingMode extends Command {
	enum Mode {
		ABSTRACT, METRIC
	}

	private Mode mode;
	private double metricScalingFactor;

	public ScalingMode(int ec, int eid, int l, DataInput in, CGM cgm) throws IOException {
		super(ec, eid, l, in, cgm);

		int mod = l > 0 ? makeEnum() : 0;
		if (mod == 0) {
			this.mode = Mode.ABSTRACT;
		} else if (mod == 1) {
			this.mode = Mode.METRIC;
			this.metricScalingFactor = makeFloatingPoint();

			assert (this.currentArg == this.args.length);
		}
	}

	public Mode getMode() {
		return this.mode;
	}

	public double getMetricScalingFactor() {
		return this.metricScalingFactor;
	}
}
