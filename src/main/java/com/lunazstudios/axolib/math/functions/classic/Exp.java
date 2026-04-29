package com.lunazstudios.axolib.math.functions.classic;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.NNFunction;
public class Exp extends NNFunction {
    public Exp(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public int getRequiredArguments() { return 1; }
    @Override public double doubleValue() { return Math.exp(this.getArg(0).doubleValue()); }
}
