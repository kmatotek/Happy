package Parser;

import Errors.MyError;
import ASTNodes.*;

public class ParseResult {
    private MyError error;
    private Node node;

    public MyError getError() {
        return error;
    }

    public Node getNode() {
        return node;
    }

    public ParseResult success(Node node) {
        this.node = node;
        this.error = null;
        return this;
    }

    public ParseResult failure(MyError error) {
        this.error = error;
        this.node = null;
        return this;
    }

    public Node register(ParseResult res) {
        if (res.getError() != null) {
            this.error = res.getError();
            return null;
        }
        return res.getNode();
    }

    
}
