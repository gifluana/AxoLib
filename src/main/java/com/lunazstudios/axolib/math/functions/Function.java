package com.lunazstudios.axolib.math.functions;

import com.lunazstudios.axolib.math.Constant;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.MathBuilder;

public abstract class Function implements IExpression {
    protected MathBuilder builder;
    protected IExpression[] args;
    protected String name;
    protected IExpression result = new Constant(0);

    public Function(MathBuilder builder, IExpression[] expressions, String name) throws Exception {
        this.builder = builder;
        this.args    = expressions;
        this.name    = name;
        if (expressions.length < this.getRequiredArguments()) {
            throw new Exception(String.format("Function '%s' requires at least %s arguments. %s are given!", this.getName(), this.getRequiredArguments(), expressions.length));
        }
        for (int i = 0; i < expressions.length; i++) this.verifyArgument(i, expressions[i]);
    }

    protected void verifyArgument(int index, IExpression expression) {}

    @Override public void set(double value) {}
    @Override public void set(String value) {}

    public IExpression getArg(int index) {
        if (index < 0 || index >= this.args.length) {
            throw new IllegalStateException("Index should be within the argument's length range! Given " + index + ", arguments length: " + this.args.length);
        }
        return this.args[index].get();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.args.length; i++) {
            sb.append(this.args[i].toString());
            if (i < this.args.length - 1) sb.append(", ");
        }
        return this.getName() + "(" + sb + ")";
    }

    public String getName() { return this.name; }
    public int getRequiredArguments() { return 0; }
}
