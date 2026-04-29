package com.lunazstudios.axolib.math.functions.utility;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
public class DieRollInteger extends DieRoll {
    public DieRollInteger(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public double doubleValue() { return (int) super.doubleValue(); }
}
