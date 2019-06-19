package myversion;


import java.util.ArrayList;

public class NoMyParser {
    Lexer lexer;

    public NoMyParser(Lexer l) {
        lexer = l;
    }

    public AST parse() {
        ArrayList<String> ctx = new ArrayList<>();
        return term(ctx);
    }

    private AST term(ArrayList ctx) {
        if (lexer.skip(TokenType.LAMBDA)) {
            String id = lexer.token(TokenType.LCID).value;
            ctx.add(id);
            lexer.match(TokenType.DOT);
            AST item = term(ctx);
            if (ctx.indexOf(id)<0){
                return new Abstraction(new Identifier(id,-1),item);
            }else {
                return new Abstraction(new Identifier(id, ctx.size() - 1 - ctx.indexOf(id)), item);
            }
        } else {
            return application(ctx);
        }
    }

    private AST application(ArrayList ctx) {
        AST left = atom((ArrayList) ctx.clone());
        while (true) {
            AST right = atom((ArrayList) ctx.clone());
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
                return new Identifier(value, ctx.size() - 1 - ctx.lastIndexOf(value));
            }
        } else {
            return null;
        }
    }
}
