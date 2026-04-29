package com.lunazstudios.axolib.math.functions.trig;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.NNFunction;
public class Atan2 extends NNFunction {
    public Atan2(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public int getRequiredArguments() { return 2; }
    @Override public double doubleValue() { return Math.atan2(this.getArg(0).doubleValue(), this.getArg(1).doubleValue()); }
}
