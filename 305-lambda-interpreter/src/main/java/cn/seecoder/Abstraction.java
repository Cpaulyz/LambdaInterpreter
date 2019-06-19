package cn.seecoder;

public class Abstraction extends AST {
    Identifier param;//变量
    AST body;//表达式

    Abstraction(Identifier p, AST b){
        param = p;
        body = b;
    }

    public String toString(){
//        return toLambda();
        return "\\."+body.toString();
//        return "\\"+param.toString()+"."+body.toString();
    }

    @Override
    public String toLambda() {
        return "\\"+param.toLambda()+"."+body.toLambda();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Abstraction((Identifier)param.clone(),(AST)body.clone());
    }
    @Override
    public boolean isValue() {
        return body.isValue();
    }

}
