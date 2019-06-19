package myversion;

public class Token {
    TokenType type;
    String value;

    public Token(TokenType type, String value){
        this.type = type;
        this.value = value;
    }
    public Token(TokenType type, char value){
        this.type = type;
        this.value = String.valueOf(value);
    }

    public TokenType getType(){
        return type;
    }
}
