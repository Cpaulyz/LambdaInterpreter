package myversion;

public class Application extends AST {
    AST lhs;//左树
    AST rhs;//右树

    Application(AST l, AST s){
        lhs = l;
        rhs = s;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Application((AST)lhs.clone(),(AST)rhs.clone());
    }

    public String toString(){
//        return toGraph();
        return "("+lhs.toString()+" "+rhs.toString()+")";
    }

    public String toGraph(){
        return "("+lhs.toGraph()+" "+rhs.toGraph()+")";
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
    public void replace(AST toReplace, AST item) {
        if(lhs instanceof Identifier){
            if (((Identifier)lhs).ASTequal((Identifier)toReplace)){
                try {
                    lhs = (AST) item.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }else if(lhs instanceof Application){
            lhs.replace(toReplace,item);
        }
        if(rhs instanceof Identifier){
            if (((Identifier)rhs).ASTequal((Identifier)toReplace)){
                try {
                    rhs = (AST) item.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }else if(rhs instanceof Application){
            rhs.replace(toReplace,item);
        }
    }
}
