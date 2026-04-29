package com.lunazstudios.axolib.math.functions.trig;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.NNFunction;
public class Asin extends NNFunction {
    public Asin(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public int getRequiredArguments() { return 1; }
    @Override public double doubleValue() { return Math.asin(this.getArg(0).doubleValue()); }
}
