package com.lunazstudios.axolib.math.molang.functions;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.trig.Acos;
public class AcosDegrees extends Acos {
    public AcosDegrees(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public double doubleValue() { return super.doubleValue() / Math.PI * 180; }
}
