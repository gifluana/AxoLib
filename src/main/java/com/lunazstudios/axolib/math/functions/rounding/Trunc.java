package com.lunazstudios.axolib.math.functions.rounding;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.NNFunction;
public class Trunc extends NNFunction {
    public Trunc(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public int getRequiredArguments() { return 1; }
    @Override public double doubleValue() { double v = this.getArg(0).doubleValue(); return v < 0 ? Math.ceil(v) : Math.floor(v); }
}
