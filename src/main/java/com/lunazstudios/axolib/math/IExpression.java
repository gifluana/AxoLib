package com.lunazstudios.axolib.math;

public interface IExpression {
    public IExpression get();
    public boolean isNumber();
    public void set(double value);
    public void set(String value);
    public double doubleValue();
    public boolean booleanValue();
    public String stringValue();
}
