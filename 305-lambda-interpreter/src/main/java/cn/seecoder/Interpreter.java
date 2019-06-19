package cn.seecoder;

import java.util.ArrayList;

public class Interpreter {
    Parser parser;
    AST astAfterParser;
    private static String[] sub = new String[]{"f", "x", "n", "m", "p", "q", "g", "h", "e", "b", "c", "d", "j", "k", "r", "s", "t", "u", "v", "w", "y", "z"};
    private static ArrayList params = new ArrayList();
    public Interpreter(Parser p) {
        parser = p;
        astAfterParser = p.parse();
        //System.out.println("After parser:"+astAfterParser.toString());
    }
    public Interpreter(String s){
        Lexer lexer = new Lexer(s);
        parser = new Parser(lexer);
        astAfterParser = parser.parse();
    }
    public Interpreter(Lexer lexer){
        parser = new Parser(lexer);
        astAfterParser = parser.parse();
    }


    private boolean isAbstraction(AST ast) {
        return ast instanceof Abstraction;
    }
    private boolean isApplication(AST ast) {
        return ast instanceof Application;
    }
    private boolean isIdentifier(AST ast) {
        return ast instanceof Identifier;
    }

    public String evalToLambda(){
        return alpha(eval()).toLambda();
    }
    public AST eval() {
        return evalAST(astAfterParser);
    }

    private AST evalAST(AST ast) {
        while (true) {
            if (isIdentifier(ast)) {
                return ast;
            } else if (isApplication(ast)) {
                if (isAbstraction(((Application) ast).lhs)) {
                    ast = substitute(((Abstraction) ((Application) ast).lhs).body, ((Application) ast).rhs);
                } else {
                    ((Application) ast).lhs = evalAST(((Application) ast).lhs);
                    ((Application) ast).rhs = evalAST(((Application) ast).rhs);
                }
                if (ast.isValue()) {
                    return ast;
                }
            } else {
                ((Abstraction) ast).body = evalAST(((Abstraction) ast).body);
                return ast;
            }
        }
    }

    private AST substitute(AST node, AST value) {
        return shift(-1, subst(node, shift(1, value, 0), 0), 0);
    }

    /**
     * value替换node节点中的变量：
     * 如果节点是Applation，分别对左右树替换；
     * 如果node节点是abstraction，替入node.body时深度得+1；
     * 如果node是identifier，则替换De Bruijn index值等于depth的identifier（替换之后value的值加深depth）
     *
     * @param value 替换成为的value
     * @param node  被替换的整个节点
     * @param depth 外围的深度
     * @return AST
     * @throws  (方法有异常的话加)
     */
    private AST subst(AST node, AST value, int depth) {
        if (isIdentifier(node)) {
            if (((Identifier) node).value == depth) {
                try {
                    return shift(depth, (AST) value.clone(), 0); // clone是为了 1.“独立”  2.避免栈溢出（shift中）
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            } else {
                return node;
            }
        } else if (isAbstraction(node)) {
            ((Abstraction) node).body = subst(((Abstraction) node).body, value, depth + 1);
            try {
                return (AST) node.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else {
            ((Application) node).lhs = subst(((Application) node).lhs, value, depth);
            ((Application) node).rhs = subst(((Application) node).rhs, value, depth);
            try {
                return (AST) node.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * De Bruijn index值位移
     * 如果节点是Applation，分别对左右树位移；
     * 如果node节点是abstraction，新的body等于旧node.body位移by（from得+1）；
     * 如果node是identifier，则新的identifier的De Bruijn index值如果大于等于from则加by，否则加0（超出内层的范围的外层变量才要shift by位）.
     *    *@param by 位移的距离
     *
     * @param node 位移的节点
     * @param from 内层的深度
     * @return AST
     * @throws  (方法有异常的话加)
     */

    private AST shift(int by, AST node, int from) {
        if (isIdentifier(node)) {
            if (((Identifier) node).value >= from)
                return new Identifier(((Identifier) node).name, ((Identifier) node).value + by);
            else
                return new Identifier(((Identifier) node).name, ((Identifier) node).value);
        } else if (isApplication(node)) {
            return new Application(shift(by, ((Application) node).lhs, from), shift(by, ((Application) node).rhs, from));
        } else {
            ((Abstraction) node).body = shift(by, ((Abstraction) node).body, from + 1);
            return node;
        }
    }

    private String findSub (String para){
        for(String s:sub){
            if ((!s.equals(para))&&!params.contains(s)){
                params.add(s);
                return s;
            }
        }
        return null;
    }
    public AST alpha(AST ast) {
        ArrayList toAlpha = scan(ast);
        return doAlpha(ast,toAlpha);
    }

    private AST doAlpha(AST ast,ArrayList toAlpha){
        if (isIdentifier(ast)) {
            return ast;
        } else if (isApplication(ast)) {
            doAlpha(((Application)ast).lhs,toAlpha);
            doAlpha(((Application)ast).rhs,toAlpha);
        } else {
            if (toAlpha.contains(((Abstraction)ast).param.name)) {
                toAlpha.remove(((Abstraction) ast).param.name);
                String temp = findSub(((Abstraction) ast).param.name);
                ((Abstraction)ast).body = subst(((Abstraction)ast).body, new Identifier(temp, 0), 0);
                ((Abstraction) ast).param.name = temp;
            }
            doAlpha(((Abstraction)ast).body,toAlpha);

        }
        return ast;
    }
    private ArrayList scan(AST ast) {
        ArrayList<String> result = new ArrayList();
        scanParam(ast, result);
        ArrayList<String> record = new ArrayList();
        for (String s : result) {
            if (!record.contains(s))
                record.add(s);
        }
        params = record;
        for (String s:record){
            result.remove(s);
        }
        // 一通很奇怪的操作，其实只是为了把有无重复都-1
        return result;
    }

    private void scanParam(AST ast, ArrayList result) {
        if (isAbstraction(ast)) {
            result.add(((Abstraction) ast).param.name);
            scanParam(((Abstraction)ast).body,result);
        } else if (isApplication(ast)) {
            scanParam(((Application) ast).lhs, result);
            scanParam(((Application) ast).rhs, result);
        }
    }

    static String ZERO = "(\\f.\\x.x)";
    static String SUCC = "(\\n.\\f.\\x.f (n f x))";
    static String ONE = app(SUCC, ZERO);
    static String TWO = app(SUCC, ONE);
    static String THREE = app(SUCC, TWO);
    static String FOUR = app(SUCC, THREE);
    static String FIVE = app(SUCC, FOUR);
    static String PLUS = "(\\m.\\n.((m " + SUCC + ") n))";
    static String POW = "(\\b.\\e.e b)";       // POW not ready
    static String PRED = "(\\n.\\f.\\x.n(\\g.\\h.h(g f))(\\u.x)(\\u.u))";
    static String SUB = "(\\m.\\n.n" + PRED + "m)";
    static String TRUE = "(\\x.\\y.x)";
    static String FALSE = "(\\x.\\y.y)";
    static String AND = "(\\p.\\q.p q p)";
    static String OR = "(\\p.\\q.p p q)";
    static String NOT = "(\\p.\\a.\\b.p b a)";
    static String IF = "(\\p.\\a.\\b.p a b)";
    static String ISZERO = "(\\n.n(\\x." + FALSE + ")" + TRUE + ")";
    static String LEQ = "(\\m.\\n." + ISZERO + "(" + SUB + "m n))";
    static String EQ = "(\\m.\\n." + AND + "(" + LEQ + "m n)(" + LEQ + "n m))";
    static String MAX = "(\\m.\\n." + IF + "(" + LEQ + " m n)n m)";
    static String MIN = "(\\m.\\n." + IF + "(" + LEQ + " m n)m n)";


    private static String app(String func, String x) {
        return "(" + func + x + ")";
    }

    private static String app(String func, String x, String y) {
        return "(" + "(" + func + x + ")" + y + ")";
    }

    private static String app(String func, String cond, String x, String y) {
        return "(" + func + cond + x + y + ")";
    }

    public static void main(String[] args) {
        // write your code here


        String[] sources = {
                ZERO,//0
                ONE,//1
                TWO,//2
                THREE,//3
                app(PLUS, ZERO, ONE),//4
                app(PLUS, TWO, THREE),//5
                app(POW, TWO, TWO),//6
                app(PRED, ONE),//7
                app(PRED, TWO),//8
                app(SUB, FOUR, TWO),//9
                app(AND, TRUE, TRUE),//10
                app(AND, TRUE, FALSE),//11
                app(AND, FALSE, FALSE),//12
                app(OR, TRUE, TRUE),//13
                app(OR, TRUE, FALSE),//14
                app(OR, FALSE, FALSE),//15
                app(NOT, TRUE),//16
                app(NOT, FALSE),//17
                app(IF, TRUE, TRUE, FALSE),//18
                app(IF, FALSE, TRUE, FALSE),//19
                app(IF, app(OR, TRUE, FALSE), ONE, ZERO),//20
                app(IF, app(AND, TRUE, FALSE), FOUR, THREE),//21
                app(ISZERO, ZERO),//22
                app(ISZERO, ONE),//23
                app(LEQ, THREE, TWO),//24
                app(LEQ, TWO, THREE),//25
                app(EQ, TWO, FOUR),//26
                app(EQ, FIVE, FIVE),//27
                app(MAX, ONE, TWO),//28
                app(MAX, FOUR, TWO),//29
                app(MIN, ONE, TWO),//30
                app(MIN, FOUR, TWO),//31
        };

        int i = 6;
        String source = sources[i];
//        String source = "\\x.x y";
        System.out.println(i + ":" + source);
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Interpreter interpreter = new Interpreter(parser);
        AST result = interpreter.eval();
        System.out.println(i+":" + result.toLambda());
        System.out.println(interpreter.alpha(result).toLambda());
    }
     //  \\x.(\\p.p x)(\\u.x)
}
