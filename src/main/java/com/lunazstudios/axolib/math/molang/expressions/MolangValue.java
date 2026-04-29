package com.lunazstudios.axolib.math.molang.expressions;

import com.lunazstudios.axolib.math.Constant;
import com.lunazstudios.axolib.math.IExpression;
import com.lunazstudios.axolib.math.molang.MolangParser;

public class MolangValue extends MolangExpression {
    public IExpression expression;
    public boolean returns;

    public MolangValue(MolangParser context, IExpression expression) {
        super(context);
        this.expression = expression;
    }

    public MolangExpression addReturn() { this.returns = true; return this; }

    @Override public double get() { return this.expression.get().doubleValue(); }

    @Override
    public String toString() {
        return (this.returns ? MolangParser.RETURN : "") + this.expression.toString();
    }

    @Override
    public String toData() {
        if (this.expression instanceof Constant) return String.valueOf(this.expression.get().doubleValue());
        return super.toData();
    }
}
