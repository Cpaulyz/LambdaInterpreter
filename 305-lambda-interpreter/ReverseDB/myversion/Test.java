package myversion;

public class Test {
    static String ZERO = "(\\f.\\x.x)";
    static String SUCC = "(\\n.\\f.\\x.f (n f x))";
    static String ONE = "(\\f.\\x.f x)";
    static String TWO = "(\\f.\\x.f (f x))";
    static String THREE = "(\\f.\\x.f (f (f x)))";
    static String FIVE = "(\\f.\\x.f (f (f (f (f x)))))";
    static String PLUS = "(\\m.\\n.((m "+SUCC+") n))";
    static String POW = "(\\b.\\e.e b)";       // POW not ready
    static String PRED = "(\\n.\\f.\\x.n(\\g.\\h.h(g f))(\\u.x)(\\u.u))";
    static String SUB = "(\\m.\\n.n "+PRED+" m)";
    static String TRUE = "(\\x.\\y.x)";
    static String FALSE = "(\\x.\\y.y)";
    static String AND = "(\\p.\\q.p q p)";
    static String OR = "(\\p.\\q.p p q)";
    static String NOT = "(\\p.\\a.\\b.p b a)";
    static String IF = "(\\p.\\a.\\b.p a b)";
    static String ISZERO = "(\\n.n(\\x."+FALSE+")"+TRUE+")";
    static String LEQ = "(\\m.\\n."+ISZERO+"("+SUB+"m n))";
    static String EQ = "(\\m.\\n."+AND+"("+LEQ+"m n)("+LEQ+"n m))";
    static String MAX = "(\\m.\\n."+IF+"("+LEQ+" m n)n m)";
    static String MIN = "(\\m.\\n."+IF+"("+LEQ+" m n)m n)";
    public static void main(String[] args) {
        String s = SUB+FIVE+THREE;
        System.out.println(s);
        Lexer lexer = new Lexer(s);
        Parser parser = new Parser(lexer);
        Interpreter interpreter = new Interpreter(parser);
        AST result = interpreter.eval();
        System.out.println(result.toGraph());
    }
}