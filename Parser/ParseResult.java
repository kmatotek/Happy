package Parser;

import Errors.*;
import ASTNodes.Node;


public class ParseResult {
    private MyError error;
    private Node node;

    public ParseResult() {
        this.error = null;
        this.node = null;
    }

    public ParseResult register(ParseResult result) {
        if (result.error != null) {
            this.error = result.error;
        }
        return result;
    }

    public ParseResult success(Node node) {
        this.node = node;
        return this;
    }

    public ParseResult failure(MyError error) {  // Accepts Error or its subclasses
        this.error = error;
        return this;
    }

    public Error getError() {
        return this.error;
    }

    public Node getNode() {
        return node;
    }

}
