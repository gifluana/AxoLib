package com.lunazstudios.axolib.math.functions.string;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;
import com.lunazstudios.axolib.math.functions.SNFunction;
public class StringStartsWith extends SNFunction {
    public StringStartsWith(MathBuilder b, IExpression[] e, String n) throws Exception { super(b, e, n); }
    @Override public int getRequiredArguments() { return 2; }
    @Override public double doubleValue() { return this.getArg(0).stringValue().startsWith(this.getArg(1).stringValue()) ? 1 : 0; }
}
