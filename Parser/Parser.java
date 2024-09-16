package Parser;

import Errors.*;
import java.util.List;
import ASTNodes.*;
import Lexer.*;

public class Parser {
    private List<Token> tokens;
    private int tokIdx;
    private Token currentTok;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.tokIdx = -1;
        advance();
    }

    public void advance() {
        tokIdx += 1;
        if (tokIdx < tokens.size()) {
            currentTok = tokens.get(tokIdx);
        }
    }

    public ParseResult parse() {
        ParseResult res = expr();
        if (res.getError() == null && currentTok.type != Token.TT_EOF) {
            return res.failure(new InvalidSyntaxError(currentTok.posStart, currentTok.posEnd, "Expected '+', '-', '*' or '/'"));
        }
        return res;
    }

    public ParseResult factor() {
        ParseResult res = new ParseResult();
        Token tok = currentTok;

        if (tok.type.equals(Token.TT_PLUS) || tok.type.equals(Token.TT_MINUS)) {
            advance();
            ParseResult factorResult = factor();  // Note: factor() returns ParseResult
            Node factorNode = res.register(factorResult);
            if (res.getError() != null) return res;
            return res.success(new UnaryOpNode(tok, factorNode));
        } else if (tok.type.equals(Token.TT_INT) || tok.type.equals(Token.TT_FLOAT)) {
            advance();
            return res.success(new NumberNode(tok));
        } else if (tok.type.equals(Token.TT_LPAREN)) {
            advance();
            ParseResult exprResult = expr();  // Note: expr() returns ParseResult
            if (res.getError() != null) return res;
            if (currentTok.type.equals(Token.TT_RPAREN)) {
                advance();
                return res.success(exprResult.getNode());
            } else {
                return res.failure(new InvalidSyntaxError(currentTok.posStart, currentTok.posEnd, "Expected ')'"));
            }
        }

        return res.failure(new InvalidSyntaxError(tok.posStart, tok.posEnd, "Expected int or float"));
}


    public ParseResult term() {
        return binOp(this::factor, new String[]{Token.TT_MUL, Token.TT_DIV});
    }

    public ParseResult expr() {
        return binOp(this::term, new String[]{Token.TT_PLUS, Token.TT_MINUS});
    }

    public ParseResult binOp(ParserFunction func, String[] ops) {
        ParseResult res = new ParseResult();
        Node left = (Node) res.register(func.apply());
        if (res.getError() != null) return res;

        while (List.of(ops).contains(currentTok.type)) {
            Token opToken = currentTok;
            advance();
            Node right = (Node) res.register(func.apply());
            if (res.getError() != null) return res;
            left = new BinOpNode(left, opToken, right);
        }

        return res.success(left);
    }

    @FunctionalInterface
    public interface ParserFunction {
        ParseResult apply();
    }
}
