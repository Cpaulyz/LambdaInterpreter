package myversion;

public class Lexer {
    /**
     * 处理 token 的辅助方法：(可以自行定义)
     * next(Token): 返回下一个 token 是否匹配 Token
     * skip(Token): 和 next 一样, 但如果匹配的话会跳过
     * match(Token): 断言 next 方法返回 true 并 skip
     * token(Token): 断言 next 方法返回 true 并返回 token
     * Lexer向控制台输出每次检测到的Token类型+换行。
     */
    public String source;
    public int index = 0;
    private boolean hasEnd = false;
//    public Token token;

    public Lexer(String s) {
        source = s;
    }

    /**
     * 返回下一个字符的Token
     *
     * @return Token
     */
    private Token nextToken() {
        Token result;
        if (!hasEnd) {
            if (index < source.length()) {
                switch (source.charAt(index)) {
                    case '.':
                        result = new Token(TokenType.DOT, source.charAt(index));
//                    System.out.println("DOT");
                        break;
                    case '\\':
                        result = new Token(TokenType.LAMBDA, source.charAt(index));
//                    System.out.println("LAMBDA");
                        break;
                    case '(':
                        result = new Token(TokenType.LPAREN, source.charAt(index));
//                    System.out.println("LPAREN");
                        break;
                    case ')':
                        result = new Token(TokenType.RPAREN, source.charAt(index));
//                    System.out.println("RPAREN");
                        break;
                    case ' ':
                        result = new Token(TokenType.BLANK, source.charAt(index));
                        break;
                    default:
                        if ('a' <= source.charAt(index) && source.charAt(index) <= 'z') {
                            int temp = index + 1;
                            while (temp < source.length() && (('a' <= source.charAt(temp) && source.charAt(temp) <= 'z') || ('A' <= source.charAt(temp) && source.charAt(temp) <= 'Z'|| ('0' <= source.charAt(temp) && source.charAt(temp) <= '9'))))
                                temp++;
                            result = new Token(TokenType.LCID, source.substring(index, temp));
//                        System.out.println("LCID");
                        } else {
                            result = new Token(TokenType.EOF, "");
//                        System.out.println("EOF");
                        }
                }
            } else {
                result = new Token(TokenType.EOF, "");
                System.out.println("EOF");
                hasEnd = true;
            }
        } else
            result = new Token(TokenType.EOF, "");
        return result;
    }

    /**
     * skip(Token): 和 next 一样, 但如果匹配的话会跳过
     *
     * @param token
     * @return
     */
    public boolean skip(TokenType token) {
        if (nextToken().getType() == token) {
            System.out.println(nextToken().getType());
            index += nextToken().value.length();
            if (nextToken().getType() == TokenType.BLANK)
                index++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * match(Token): 断言 next 方法返回 true 并 skip
     *
     * @param token
     */
    public void match(TokenType token) {
        if (next(token)) {
            skip(token);
        }
    }

    /**
     * next(Token): 返回下一个 token 是否匹配 Token
     *
     * @return
     */
    public boolean next(TokenType token) {
        if (nextToken().getType() == token) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * token(Token): 断言 next 方法返回 true 并返回 token
     * 并且调整index
     *
     * @return
     */
    public Token token(TokenType t) {
        Token result = nextToken();
        assert next(t);
        skip(t);
        return result;
    }

    public boolean hasNext() {
        if (index < source.length()) {
            return true;
        } else {
            return false;
        }
    }
}
