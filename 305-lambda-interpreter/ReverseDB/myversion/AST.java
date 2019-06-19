package myversion;

public abstract class AST {
    public abstract String toString();
    public boolean isValue() {
        return false;
    }

    public ItemType getType() {
        return null;
    }

    public void replace(AST toReplace, AST item){}

    public String toGraph(){return null;}

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
