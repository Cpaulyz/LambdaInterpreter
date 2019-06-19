package cn.seecoder;

public class Application extends AST{
    AST lhs;//左树
    AST rhs;//右树

    Application(AST l, AST r){
        lhs = l;
        rhs = r;
    }

    public String toString(){
//        return toLambda();
        return "("+lhs.toString()+" "+rhs.toString()+")";
    }

    @Override
    public String toLambda() {
        return "("+lhs.toLambda()+" "+rhs.toLambda()+")";
    }

    @Override
    public boolean isValue() {
        if(lhs instanceof Abstraction){
            return false;
        }else if (lhs instanceof Identifier){
            if (rhs instanceof Identifier)
                return true;
            else
                return rhs.isValue();
        }else{
            return lhs.isValue()&&rhs.isValue();
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Application((AST)lhs.clone(),(AST)rhs.clone());
    }
}
