package com.lunazstudios.axolib.math;

public class Operator implements IExpression {
    public static boolean DEBUG = false;

    public Operation operation;
    public IExpression a;
    public IExpression b;
    private IExpression result = new Constant(0);

    public Operator(Operation op, IExpression a, IExpression b) {
        this.operation = op;
        this.a = a;
        this.b = b;
    }

    @Override
    public IExpression get() {
        if (!this.isNumber() && this.operation == Operation.ADD) {
            this.result.set(this.stringValue());
        } else {
            this.result.set(this.doubleValue());
        }
        return this.result;
    }

    @Override public boolean isNumber() { return this.a.isNumber() || this.b.isNumber(); }

    @Override public void set(double value) {}
    @Override public void set(String value) {}

    @Override
    public double doubleValue() {
        if (!this.isNumber() && this.operation == Operation.EQUALS) {
            return this.a.stringValue().equals(this.b.stringValue()) ? 1 : 0;
        }
        return this.operation.calculate(this.a.doubleValue(), this.b.doubleValue());
    }

    @Override public boolean booleanValue() { return Operation.isTrue(this.doubleValue()); }

    @Override
    public String stringValue() {
        if (this.operation == Operation.ADD) return this.a.stringValue() + this.b.stringValue();
        return this.a.stringValue();
    }

    @Override
    public String toString() {
        if (DEBUG) return "(" + this.a + " " + this.operation.sign + " " + this.b + ")";
        return this.a + " " + this.operation.sign + " " + this.b;
    }
}
