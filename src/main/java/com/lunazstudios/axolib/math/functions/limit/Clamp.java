package com.lunazstudios.axolib.math.functions.limit;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.NNFunction;
import com.lunazstudios.axolib.utils.MathUtils;
public class Clamp extends NNFunction {
    public Clamp(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public int getRequiredArguments() { return 3; }
    @Override public double doubleValue() { return MathUtils.clamp(this.getArg(0).doubleValue(), this.getArg(1).doubleValue(), this.getArg(2).doubleValue()); }
}
