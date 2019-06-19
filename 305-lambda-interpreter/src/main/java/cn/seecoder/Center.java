package cn.seecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Center {
    Map<String, String> map = new HashMap();
    static String ZERO = "(\\f.\\x.x)";
    static String SUCC = "(\\n.\\f.\\x.f (n f x))";
    static String ONE = "(\\f.\\x.f x)";
    static String TWO = "(\\f.\\x.f (f x))";
    static String THREE = "(\\f.\\x.f (f (f x)))";
    static String PLUS = "(\\m.\\n.((m " + SUCC + ") n))";
    static String POW = "(\\b.\\e.e b)";       // POW not ready
    static String PRED = "(\\n.\\f.\\x.n(\\g.\\h.h(g f))(\\u.x)(\\u.u))";
    static String SUB = "(\\m.\\n.n " + PRED + " m)";
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


    public void printInfo() {
        System.out.println("1.在>>>后进行输入，请使用\\符号代替lambda符号，括号省略为标准省略方式");
        System.out.println("2.不同变量间括号隔开，变量名为小写字母开头");
        System.out.println("3.如需定义表达式，请使用大写字母开头，等号连接。如 ONE = \\f.x.f x");
        System.out.println("4.定义变量后可使用变量进行输入，如 POW TWO TWO");
        System.out.println("5.内置常用表达式，输入 showmap 可查看");
        System.out.println("6.使用时需要帮忙可输入 help");
        System.out.println("7.输入Q以退出程序");
    }

    public void printMap() {
        for (String key : map.keySet()) {
            System.out.println(key +" = "+map.get(key));
        }
    }

    public Center() {
        map.put("ZERO", ZERO);
        map.put("SUCC", SUCC);
        map.put("ONE", ONE);
        map.put("TWO", TWO);
        map.put("THREE", THREE);
        map.put("PLUS", PLUS);
        map.put("POW", POW);
        map.put("PRED", PRED);
        map.put("SUB", SUB);
        map.put("TRUE", TRUE);
        map.put("FALSE", FALSE);
        map.put("AND", AND);
        map.put("OR", OR);
        map.put("NOT", NOT);
        map.put("IF", IF);
        map.put("ISZERO", ISZERO);
        map.put("LEQ", LEQ);
        map.put("EQ", EQ);
        map.put("MAX", MAX);
        map.put("MIN", MIN);
    }

    private static String app(String func, String x) {
        return "(" + func + x + ")";
    }

    private static String app(String func, String x, String y) {
        return "(" + "(" + func + x + ")" + y + ")";
    }

    private static String app(String func, String cond, String x, String y) {
        return "(" + func + cond + x + y + ")";
    }

    private static String input() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        try {
            return bf.readLine();
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 定义键值对
     *
     * @param s
     * @return
     */
    private void identify(String s) {
        String[] strings = s.split("=");
        if (strings.length != 2) {
            System.out.println("Invalid");
        } else {
            String name = strings[0].trim();
            String value = strings[1].trim();
            if (map.containsKey(name)) {
                System.out.print(name + "has been identified. Do you want to replace? (Y/N)");
                String temp = input();
                if ("Y".equals(temp) || "y".equals(temp)) {
                    map.put(name,this.analyse(value));
                }
            } else {
                map.put(name, this.analyse(value));
            }
        }
    }

    /**
     * 解析输入，返回一个字符串
     *
     * @param s
     * @return
     */
    public String analyse(String s) throws NullPointerException {
        String[] strings = s.split("\\(|\\)|\\.|\\s");
        for (String temp : strings) {
            if ((!"".equals(temp)) && (temp.charAt(0) <= 'Z' && temp.charAt(0) >= 'A')&&temp!=null) {
                if (!map.containsKey(temp)) {
                    throw new NullPointerException(temp);
                }
                s = s.replaceAll(temp, "("+map.get(temp).replaceAll("\\\\", "\\\\\\\\")+")");
            }
        }

        return s.trim();
    }

    public static void main(String[] args) {
        Center center = new Center();
        System.out.println("====================================================================");
        center.printInfo();
        System.out.println("====================================================================");
        while (true) {
            System.out.print(">>>");
            String in = input();
            if ("Q".equals(in)) {
                break;
            } else if (in.contains("=")) {
                center.identify(in);
            } else if ("showmap".equals(in)) {
                center.printMap();
            } else if ("help".equals(in)) {
                center.printInfo();
            } else {
//                try {
                    String source = center.analyse(in);
                    Lexer lexer = new Lexer(source, true);
                    System.out.println("约简前：" + source);
                    System.out.println("约简后：" + new Interpreter(lexer).evalToLambda());
//                } catch (NullPointerException e) {
//                    System.out.println("未找到定义符" + e.getMessage());
//                }

            }
        }
    }
}
