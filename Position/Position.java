package Position;

public class Position {
    public int index;
    public int line;
    public int col;
    public String fileName;
    private String fileText;

    public Position(int index, int line, int col, String fileName, String fileText){
        this.index = index;
        this.line = line;
        this.col = col;
        this.fileName = fileName;
        this.fileText = fileText;
    }

    public Position(int index, int line, int col, String fileText){
        this.index = index;
        this.line = line;
        this.col = col;
        this.fileText = fileText;
    }

    public void advance(char currChar){
        this.index += 1;
        this.col += 1;

        if(currChar == '\n'){
            this.line += 1;
            this.col = 0;
        }
    }

    public void advance(){
        this.index += 1;
        this.col += 1;

    }

    public Position copy(){
        return new Position(this.index, this.line, this.col, this.fileName, this.fileText);
    }
}