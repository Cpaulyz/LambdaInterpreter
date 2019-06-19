package myversion;

public class Identifier extends AST {

    String name; //名字
    int value;
    int dbi;//De Bruijn index值

    public Identifier(String n,int v){

        name = n;
        value = v;
    }
    public String toString(){
//        return toGraph();
        if (value>=0)
            return String.valueOf(value);
        else
            return name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Identifier(name,value);
    }

    @Override
    public String toGraph() {
        return name+value;
    }

    public boolean ASTequal(Identifier iden){
        return name.equals(iden.name)&&value==iden.value;
    }

    @Override
    public boolean isValue() {
        return true;
    }

}
