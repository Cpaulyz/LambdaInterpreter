package cn.seecoder;

public class Identifier extends AST {

    String name; //名字
    int value;//De Bruijn index值

    public Identifier(String n,int v){

        name = n;
        value = v;
    }

    @Override
    public String toLambda() {
        return name;
    }

    public String toString(){
//        return toLambda();
//        if (value>=0)
            return String.valueOf(value);
//        else
//            return name;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Identifier(name,value);
    }
    @Override
    public boolean isValue() {
        return true;
    }

}
