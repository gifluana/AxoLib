package com.lunazstudios.axolib.math.functions.string;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.SNFunction;
public class StringEndsWith extends SNFunction {
    public StringEndsWith(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public int getRequiredArguments() { return 2; }
    @Override public double doubleValue() { return this.getArg(0).stringValue().endsWith(this.getArg(1).stringValue()) ? 1 : 0; }
}
