package com.lunazstudios.axolib.math.molang.functions;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.NNFunction;
public class CosDegrees extends NNFunction {
    public CosDegrees(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public int getRequiredArguments() { return 1; }
    @Override public double doubleValue() { return Math.cos(this.getArg(0).doubleValue() / 180 * Math.PI); }
}
