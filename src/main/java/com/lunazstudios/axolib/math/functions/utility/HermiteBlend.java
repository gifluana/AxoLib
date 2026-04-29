package com.lunazstudios.axolib.math.functions.utility;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.NNFunction;
public class HermiteBlend extends NNFunction {
    public HermiteBlend(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public int getRequiredArguments() { return 1; }
    @Override public double doubleValue() { double x = this.getArg(0).doubleValue(); return 3 * x * x - 2 * x * x * x; }
}
