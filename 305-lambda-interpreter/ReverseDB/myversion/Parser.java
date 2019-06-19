package myversion;


import java.util.ArrayList;

public class Parser {
    Lexer lexer;
    int ptr=0;

    public Parser(Lexer l) {
        lexer = l;
    }

    public AST parse() {
        ArrayList<String> ctx = new ArrayList<>();
//        for (int i = 0; i < ptr; i++) {
//            ctx.add(null);
//        }
        return term(ctx);
    }

    private AST term(ArrayList ctx) {
        if (lexer.skip(TokenType.LAMBDA)) {
            String id = lexer.token(TokenType.LCID).value;
            ctx.add(id);
            ptr++;
            lexer.match(TokenType.DOT);
            AST item = term(ctx);
            if (ctx.indexOf(id)<0){
                return new Abstraction(new Identifier(id,-1),item);
            }else {
                return new Abstraction(new Identifier(id, ctx.lastIndexOf(id)), item);
            }
        } else {
            return application(ctx);
        }
    }

    private AST application(ArrayList ctx) {
        ArrayList temp1 = (ArrayList)ctx.clone();
        for (int i = ctx.size(); i < ptr; i++) {
            temp1.add(i,null);
        }
        AST left = atom(temp1);
        temp1 = null;
        while (true) {
            ArrayList temp2 = (ArrayList)ctx.clone();
            for (int i = ctx.size(); i < ptr; i++) {
                temp2.add(i,null);
            }
            AST right = atom(temp2);
            if (right == null) {
                return left;
            } else {
                left = new Application(left, right);
            }
        }
    }

    private AST atom(ArrayList ctx) {
        if (lexer.skip(TokenType.LPAREN)) {
            AST term;
//            if (lexer.next(TokenType.LAMBDA)) {
//                ArrayList<String> newctx = new ArrayList<>();
//                term = term(newctx);
//            }else {
            term = term(ctx);
//            }
            lexer.match(TokenType.RPAREN);
            return term;
        } else if (lexer.next(TokenType.LCID)) {
            String value = lexer.token(TokenType.LCID).value;
            if (ctx.indexOf(value) < 0) {
                return new Identifier(value, -1);
            } else {
                return new Identifier(value, ctx.lastIndexOf(value));
            }
        } else {
            return null;
        }
    }
}
