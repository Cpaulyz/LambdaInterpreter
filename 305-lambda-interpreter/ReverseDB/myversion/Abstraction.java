package myversion;

public class Abstraction extends AST {
    Identifier param;//变量
    AST body;//表达式

    Abstraction(Identifier p, AST b){
        param = p;
        body = b;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Abstraction((Identifier)param.clone(),(AST)body.clone());
    }

    public String toString(){
//        return "\\"+param.toGraph()+"."+body.toGraph();
        return "\\."+body.toString();
//        return "\\"+param.toString()+"."+body.toString();
    }

    @Override
    public String toGraph() {
        return "\\"+param.toGraph()+ "" +body.toGraph();
    }
    //    String para;
//    AST body; // 可能是一个抽象也可能是一个应用，也可能是Identifier
//    ItemType type = ItemType.ABS;
//
//    public Abstraction(String para, AST body) {
//        this.para = para;
//        this.body = body;
//    }
//
//    @Override
//    public ItemType getType() {
//        return type;
//    }
//
    @Override
    public boolean isValue() {
        return body.isValue();
    }
//
//    @Override
//    public String toString() {
//        return "\\" + para + "." + body.toString();
//    }
//
//    @Override
//    public AST simplify() {
//        if (!isValue()) {
//            body = body.simplify();
//        }
//        return this;
//    }

    /**
     * 传入一个item参数，将para相应的替换成item，注意绑定
     *
     * @param item
     * @return
     */
    public AST applicate(AST item) {
        if (body instanceof Identifier) {
            if (((Identifier) body).ASTequal(param)) {
                return item;
            }else {
                return body;
            }
        } else if (body instanceof Application){
            body.replace(param,item);
            return body;
        }else {
            body.replace(param,item);
            return body;
        }
    }

    @Override
    public void replace(AST toReplace, AST item) {
        if (body instanceof Identifier) {
            if (((Identifier) body).ASTequal((Identifier)toReplace)) {
                try {
                    body = (AST) item.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(body instanceof Abstraction){
            ((Abstraction)body).body.replace(toReplace,item);
        }else {
            body.replace(toReplace,item);
        }
    }
}
