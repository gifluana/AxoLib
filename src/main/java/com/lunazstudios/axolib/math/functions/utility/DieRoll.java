package com.lunazstudios.axolib.math.functions.utility;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.NNFunction;
public class DieRoll extends NNFunction {
    public static double rollDie(int num, double min, double max) {
        double m = Math.max(max, min), n = Math.min(max, min), sum = 0;
        for (int i = 0; i < num; i++) sum += Math.random() * (m - n) + n;
        return sum;
    }
    public DieRoll(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public int getRequiredArguments() { return 3; }
    @Override public double doubleValue() { return rollDie((int) this.getArg(0).doubleValue(), this.getArg(1).doubleValue(), this.getArg(2).doubleValue()); }
}
