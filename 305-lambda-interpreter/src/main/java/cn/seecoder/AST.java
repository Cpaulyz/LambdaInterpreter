package cn.seecoder;

public abstract class AST {
    public abstract String toString();
    public boolean isValue() {
        return false;
    }
    public abstract String toLambda();
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
