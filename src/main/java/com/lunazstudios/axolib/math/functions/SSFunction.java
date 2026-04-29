package com.lunazstudios.axolib.math.functions;

import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;

public abstract class SSFunction extends Function {
    public SSFunction(MathBuilder builder, IExpression[] expressions, String name) throws Exception {
        super(builder, expressions, name);
    }

    @Override
    protected void verifyArgument(int index, IExpression expression) {
        if (expression.isNumber()) throw new IllegalStateException("Function " + this.name + " cannot receive number arguments!");
    }

    @Override public IExpression get()      { this.result.set(this.stringValue()); return this.result; }
    @Override public boolean isNumber()     { return false; }
    @Override public double doubleValue()   { return 0; }
    @Override public boolean booleanValue() { return this.stringValue().equalsIgnoreCase("true"); }
}
