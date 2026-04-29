package com.lunazstudios.axolib.math;

import com.lunazstudios.axolib.math.functions.Function;
import com.lunazstudios.axolib.math.functions.classic.Abs;
import com.lunazstudios.axolib.math.functions.classic.Exp;
import com.lunazstudios.axolib.math.functions.classic.Ln;
import com.lunazstudios.axolib.math.functions.classic.Mod;
import com.lunazstudios.axolib.math.functions.classic.Pow;
import com.lunazstudios.axolib.math.functions.classic.Sqrt;
import com.lunazstudios.axolib.math.functions.limit.Clamp;
import com.lunazstudios.axolib.math.functions.limit.Max;
import com.lunazstudios.axolib.math.functions.limit.Min;
import com.lunazstudios.axolib.math.functions.rounding.Ceil;
import com.lunazstudios.axolib.math.functions.rounding.Floor;
import com.lunazstudios.axolib.math.functions.rounding.Round;
import com.lunazstudios.axolib.math.functions.rounding.Trunc;
import com.lunazstudios.axolib.math.functions.string.StringContains;
import com.lunazstudios.axolib.math.functions.string.StringEndsWith;
import com.lunazstudios.axolib.math.functions.string.StringStartsWith;
import com.lunazstudios.axolib.math.functions.trig.Acos;
import com.lunazstudios.axolib.math.functions.trig.Asin;
import com.lunazstudios.axolib.math.functions.trig.Atan;
import com.lunazstudios.axolib.math.functions.trig.Atan2;
import com.lunazstudios.axolib.math.functions.trig.Cos;
import com.lunazstudios.axolib.math.functions.trig.Sin;
import com.lunazstudios.axolib.math.functions.utility.DieRoll;
import com.lunazstudios.axolib.math.functions.utility.DieRollInteger;
import com.lunazstudios.axolib.math.functions.utility.HermiteBlend;
import com.lunazstudios.axolib.math.functions.utility.Lerp;
import com.lunazstudios.axolib.math.functions.utility.LerpRotate;
import com.lunazstudios.axolib.math.functions.utility.Random;
import com.lunazstudios.axolib.math.functions.utility.RandomInteger;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathBuilder {
    public Map<String, Variable> variables = new HashMap<>();
    public Map<String, Class<? extends Function>> functions = new HashMap<>();
    protected boolean strict = true;

    public MathBuilder() {
        this.register("PI", Math.PI);
        this.register("E", Math.E);

        this.functions.put("floor", Floor.class);
        this.functions.put("round", Round.class);
        this.functions.put("ceil", Ceil.class);
        this.functions.put("trunc", Trunc.class);

        this.functions.put("clamp", Clamp.class);
        this.functions.put("max", Max.class);
        this.functions.put("min", Min.class);

        this.functions.put("abs", Abs.class);
        this.functions.put("exp", Exp.class);
        this.functions.put("ln", Ln.class);
        this.functions.put("sqrt", Sqrt.class);
        this.functions.put("mod", Mod.class);
        this.functions.put("pow", Pow.class);

        this.functions.put("cos", Cos.class);
        this.functions.put("sin", Sin.class);
        this.functions.put("acos", Acos.class);
        this.functions.put("asin", Asin.class);
        this.functions.put("atan", Atan.class);
        this.functions.put("atan2", Atan2.class);

        this.functions.put("lerp", Lerp.class);
        this.functions.put("lerprotate", LerpRotate.class);
        this.functions.put("random", Random.class);
        this.functions.put("randomi", RandomInteger.class);
        this.functions.put("roll", DieRoll.class);
        this.functions.put("rolli", DieRollInteger.class);
        this.functions.put("hermite", HermiteBlend.class);

        this.functions.put("str_contains", StringContains.class);
        this.functions.put("str_starts", StringStartsWith.class);
        this.functions.put("str_ends", StringEndsWith.class);
    }

    public MathBuilder lenient() {
        this.strict = false;
        return this;
    }

    public Variable register(String name) { return this.register(name, 0D); }

    public Variable register(String name, double value) {
        Variable variable = new Variable(name, value);
        this.register(variable);
        return variable;
    }

    public void register(Variable variable) {
        this.variables.put(variable.getName(), variable);
    }

    public IExpression parse(String expression) throws Exception {
        String trimmed = expression.trim();
        if (trimmed.equals("-") || trimmed.equals("+")) return new Constant(0D);
        return this.parseSymbols(this.breakdownChars(this.breakdown(expression)));
    }

    public String[] breakdown(String expression) throws Exception {
        if (this.strict && !expression.matches("^[\\w\\d\\s_+-/*%^&|<>=!?:.,()\"'@~\\[\\]]+$")) {
            throw new Exception("Given expression '" + expression + "' contains illegal characters!");
        }
        String[] chars = expression.split("(?!^)");
        int left = 0, right = 0;
        for (String s : chars) {
            if (s.equals("(")) left++;
            else if (s.equals(")")) right++;
        }
        if (left != right) {
            throw new Exception("Given expression '" + expression + "' has more uneven amount of parenthesis, there are " + left + " open and " + right + " closed!");
        }
        return chars;
    }

    public List<Object> breakdownChars(String[] chars) {
        List<Object> symbols = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        int len = chars.length;
        boolean string = false;

        for (int i = 0; i < len; i++) {
            String s = chars[i];
            boolean longOperator = i < chars.length - 1 && this.isOperator(s + chars[i + 1]);

            if (s.equals("\"")) string = !string;

            if (string) {
                buffer.append(s);
            } else if (this.isOperator(s) || longOperator || s.equals(",")) {
                if (s.equals("-")) {
                    int size = symbols.size();
                    boolean isEmpty = buffer.toString().trim().isEmpty();
                    boolean isFirst = size == 0 && isEmpty;
                    boolean isOperatorBehind = size > 0 && (this.isOperator(symbols.get(size - 1)) || symbols.get(size - 1).equals(",")) && isEmpty;
                    if (isFirst || isOperatorBehind) { buffer.append(s); continue; }
                }

                if (buffer.length() > 0) { symbols.add(buffer.toString()); buffer = new StringBuilder(); }

                if (longOperator) { symbols.add(s + chars[i + 1]); i += 1; }
                else { symbols.add(s); }
            } else if (s.equals("(")) {
                if (buffer.length() > 0) { symbols.add(buffer.toString()); buffer = new StringBuilder(); }
                int counter = 1;
                for (int j = i + 1; j < len; j++) {
                    String c = chars[j];
                    if (c.equals("(")) counter++;
                    else if (c.equals(")")) counter--;
                    if (counter == 0) {
                        symbols.add(this.breakdownChars(buffer.toString().split("(?!^)")));
                        i = j; buffer = new StringBuilder(); break;
                    } else { buffer.append(c); }
                }
            } else if (!s.equals(" ")) {
                buffer.append(s);
            }
        }

        if (buffer.length() > 0) symbols.add(buffer.toString());
        return this.trimSymbols(symbols);
    }

    private List<Object> trimSymbols(List<Object> symbols) {
        List<Object> newSymbols = new ArrayList<>();
        for (int i = 0; i < symbols.size(); i++) {
            Object value = symbols.get(i);
            if (value instanceof String) {
                String s = ((String) value).trim();
                if (!s.isEmpty()) newSymbols.add(s);
            } else {
                newSymbols.add(this.trimSymbols((List) value));
            }
        }
        return newSymbols;
    }

    @SuppressWarnings("unchecked")
    public IExpression parseSymbols(List<Object> symbols) throws Exception {
        IExpression ternary = this.tryTernary(symbols);
        if (ternary != null) return ternary;

        int size = symbols.size();

        if (size == 1) return this.expressionFromObject(symbols.get(0));

        if (size == 2) {
            Object first = symbols.get(0);
            Object second = symbols.get(1);
            if ((this.isVariable(first) || first.equals("-")) && second instanceof List) {
                return this.createFunction((String) first, (List<Object>) second);
            }
        }

        int lastOp = this.seekLastOperator(symbols);
        int op = lastOp;

        while (op != -1) {
            int leftOp = this.seekLastOperator(symbols, op - 1);
            if (leftOp != -1) {
                Operation left  = this.operationForOperator((String) symbols.get(leftOp));
                Operation right = this.operationForOperator((String) symbols.get(op));
                if (right.value > left.value) {
                    return new Operator(left, this.parseSymbols(symbols.subList(0, leftOp)), this.parseSymbols(symbols.subList(leftOp + 1, size)));
                } else if (left.value > right.value) {
                    Operation initial = this.operationForOperator((String) symbols.get(lastOp));
                    if (initial.value < left.value) {
                        return new Operator(initial, this.parseSymbols(symbols.subList(0, lastOp)), this.parseSymbols(symbols.subList(lastOp + 1, size)));
                    }
                    return new Operator(right, this.parseSymbols(symbols.subList(0, op)), this.parseSymbols(symbols.subList(op + 1, size)));
                }
            }
            op = leftOp;
        }

        Operation operation = this.operationForOperator((String) symbols.get(lastOp));
        return new Operator(operation, this.parseSymbols(symbols.subList(0, lastOp)), this.parseSymbols(symbols.subList(lastOp + 1, size)));
    }

    protected int seekLastOperator(List<Object> symbols) {
        return this.seekLastOperator(symbols, symbols.size() - 1);
    }

    protected int seekLastOperator(List<Object> symbols, int offset) {
        for (int i = offset; i >= 0; i--) {
            Object o = symbols.get(i);
            if (this.isOperator(o)) {
                if (o.equals("-")) {
                    Object next = i < symbols.size() - 1 ? symbols.get(i + 1) : null;
                    Object prev = i > 0 ? symbols.get(i - 1) : null;
                    if (next instanceof List && (this.isOperator(prev) || prev == null)) continue;
                }
                return i;
            }
        }
        return -1;
    }

    protected IExpression tryTernary(List<Object> symbols) throws Exception {
        int question = -1, questions = 0, colon = -1, colons = 0;
        int size = symbols.size();
        for (int i = 0; i < size; i++) {
            Object object = symbols.get(i);
            if (object instanceof String) {
                if (object.equals("?")) { if (question == -1) question = i; questions++; }
                else if (object.equals(":")) { if (colons + 1 == questions && colon == -1) colon = i; colons++; }
            }
        }
        if (questions == colons && question > 0 && question + 1 < colon && colon < size - 1) {
            return new Ternary(
                this.parseSymbols(symbols.subList(0, question)),
                this.parseSymbols(symbols.subList(question + 1, colon)),
                this.parseSymbols(symbols.subList(colon + 1, size))
            );
        }
        return null;
    }

    protected IExpression createFunction(String first, List<Object> args) throws Exception {
        if (first.equals("!")) return new Negate(this.parseSymbols(args));
        if (first.startsWith("!") && first.length() > 1) return new Negate(this.createFunction(first.substring(1), args));
        if (first.equals("-")) return new Negative(new Group(this.parseSymbols(args)));
        if (first.startsWith("-") && first.length() > 1) return new Negative(this.createFunction(first.substring(1), args));

        if (!this.functions.containsKey(first)) throw new Exception("Function '" + first + "' couldn't be found!");

        List<IExpression> values = new ArrayList<>();
        List<Object> buffer = new ArrayList<>();
        for (Object o : args) {
            if (o.equals(",")) { values.add(this.parseSymbols(buffer)); buffer.clear(); }
            else { buffer.add(o); }
        }
        if (!buffer.isEmpty()) values.add(this.parseSymbols(buffer));

        Class<? extends Function> function = this.functions.get(first);
        Constructor<? extends Function> ctor = function.getConstructor(MathBuilder.class, IExpression[].class, String.class);
        return ctor.newInstance(this, values.toArray(new IExpression[0]), first);
    }

    @SuppressWarnings("unchecked")
    public IExpression expressionFromObject(Object object) throws Exception {
        if (object instanceof String) {
            String symbol = (String) object;
            if (symbol.startsWith("!")) return new Negate(this.expressionFromObject(symbol.substring(1)));
            if (symbol.startsWith("\"") && symbol.endsWith("\"")) return new Constant(symbol.substring(1, symbol.length() - 1));
            if (this.isDecimal(symbol)) return new Constant(Double.parseDouble(symbol));
            else if (this.isVariable(symbol)) {
                if (symbol.startsWith("-")) {
                    symbol = symbol.substring(1);
                    Variable value = this.getVariable(symbol);
                    if (value != null) return new Negative(value);
                } else {
                    IExpression expression = this.getVariable(symbol);
                    if (expression != null) return expression;
                }
            }
        } else if (object instanceof List) {
            return new Group(this.parseSymbols((List<Object>) object));
        }
        throw new Exception("Given object couldn't be converted to value! " + object);
    }

    protected Variable getVariable(String name) { return this.variables.get(name); }

    protected Operation operationForOperator(String op) throws Exception {
        for (Operation operation : Operation.values()) {
            if (operation.sign.equals(op)) return operation;
        }
        throw new Exception("There is no such operator '" + op + "'!");
    }

    protected boolean isVariable(Object o) {
        return o instanceof String && !this.isDecimal((String) o) && !this.isOperator((String) o);
    }

    protected boolean isOperator(Object o) { return o instanceof String && this.isOperator((String) o); }

    protected boolean isOperator(String s) {
        return Operation.OPERATORS.contains(s) || s.equals("?") || s.equals(":");
    }

    protected boolean isDecimal(String s) { return s.matches("^-?\\d+(\\.\\d+)?$"); }
}
