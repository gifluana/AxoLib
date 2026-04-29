package com.lunazstudios.axolib.math.functions.utility;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.NNFunction;
import com.lunazstudios.axolib.utils.interps.Lerps;
public class LerpRotate extends NNFunction {
    public LerpRotate(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public int getRequiredArguments() { return 3; }
    @Override public double doubleValue() { return Lerps.lerpYaw(this.getArg(0).doubleValue(), this.getArg(1).doubleValue(), this.getArg(2).doubleValue()); }
}
