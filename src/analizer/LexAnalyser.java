package analizer;

import constants.ComentSymbols;
import constants.Delimeters;
import constants.KeyWords;
import data.Receiver;
import exceptions.InvalidIDException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by dmytro on 06.03.16.
 */

//Tests!!!!!!!

public class LexAnalyser {

    private HashMap<String, Integer> keyWordTable;
    private HashMap<String, Integer> constantTable;
    private HashMap<String, Integer> identTable;
    private HashMap<String, Integer> singleSymTable;
    private HashMap<String, Integer> doubleSymTable;

    public HashMap<String, Integer> getKeyWordTable() {
        return keyWordTable;
    }

    public HashMap<String, Integer> getConstantTable() {
        return constantTable;
    }

    public HashMap<String, Integer> getIdentTable() {
        return identTable;
    }

    public HashMap<String, Integer> getSingleSymTable() {
        return singleSymTable;
    }

    public HashMap<String, Integer> getDoubleSymTable() {
        return doubleSymTable;
    }



    private int lastConstantCode = 500;
    private int lastIdentCode = 1000;

    private int lastKeyWordCode = 400;
    private int lastDoubleSymCode = 300;

    private boolean commentFlag = false;

    private ArrayList<Lexem> lexems = new ArrayList<>();

    public ArrayList<Lexem> getLexems() {
        return lexems;
    }

    public LexAnalyser(){
        keyWordTable = new HashMap<>();
        constantTable = new HashMap<>();
        identTable = new HashMap<>();
        singleSymTable = new HashMap<>();
        doubleSymTable = new HashMap<>();

        keyWordTable.put(KeyWords.BEGIN, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.END, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.VAR, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.PROGRAM, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.CONST, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.SIGNAL, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.COMPLEX, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.INTEGER, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.FLOAT, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.BLOCKFLOAT, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.EXT, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.LOOP, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.ENDLOOP, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.FOR, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.ENDFOR, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.CASE, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.OF, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.ENDCASE, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.TO, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.DO, ++lastKeyWordCode);
        keyWordTable.put(KeyWords.MOD, ++lastKeyWordCode);

        doubleSymTable.put(Delimeters.APPROPRIATE, ++lastDoubleSymCode);
        doubleSymTable.put(Delimeters.GREATER_OR_EQUAL, ++lastDoubleSymCode);
        doubleSymTable.put(Delimeters.LESS_OR_EQUAL, ++lastDoubleSymCode);
        doubleSymTable.put(Delimeters.RANGE, ++lastDoubleSymCode);

        singleSymTable.put(Delimeters.LESS, Integer.valueOf(Delimeters.LESS.charAt(0)));
        singleSymTable.put(Delimeters.GREATER, Integer.valueOf(Delimeters.GREATER.charAt(0)));
        singleSymTable.put(Delimeters.ADD, Integer.valueOf(Delimeters.ADD.charAt(0)));
        singleSymTable.put(Delimeters.COLON, Integer.valueOf(Delimeters.COLON.charAt(0)));
        singleSymTable.put(Delimeters.DIV, Integer.valueOf(Delimeters.DIV.charAt(0)));
        singleSymTable.put(Delimeters.EQUAL, Integer.valueOf(Delimeters.EQUAL.charAt(0)));
        singleSymTable.put(Delimeters.MULTIPLY, Integer.valueOf(Delimeters.MULTIPLY.charAt(0)));
        singleSymTable.put(Delimeters.STOP_LINE, Integer.valueOf(Delimeters.STOP_LINE.charAt(0)));
        singleSymTable.put(Delimeters.SUB, Integer.valueOf(Delimeters.SUB.charAt(0)));
        singleSymTable.put(Delimeters.COMA, Integer.valueOf(Delimeters.COMA.charAt(0)));
        singleSymTable.put(Delimeters.EXCLAMATION, Integer.valueOf(Delimeters.EXCLAMATION.charAt(0)));
        singleSymTable.put(Delimeters.AMPERSAND, Integer.valueOf(Delimeters.AMPERSAND.charAt(0)));
        singleSymTable.put(Delimeters.LBRACKET, Integer.valueOf(Delimeters.LBRACKET.charAt(0)));
        singleSymTable.put(Delimeters.RBRACKET, Integer.valueOf(Delimeters.RBRACKET.charAt(0)));
        singleSymTable.put(Delimeters.POWER, Integer.valueOf(Delimeters.POWER.charAt(0)));
        singleSymTable.put(Delimeters.GRID, Integer.valueOf(Delimeters.GRID.charAt(0)));
    }
    //
    private boolean isLet(char c){
        if(c >= 65 && c<=90)
            return true;
        return false;
    }
    //
    private boolean isNum(char c){
        if(c >= 48 && c <=57)
            return true;
        return false;
    }
    //
    private boolean isSpaces(char c){
        if(c == 32 || c == 9 || c == 11)
            return true;
        return false;
    }

    private boolean isSingleDelimiter(String del){
        return singleSymTable.containsKey(del);
    }

    private boolean isDoubleDelimiter(String del){
        return doubleSymTable.containsKey(del);
    }

    //
    private String makeWord(ArrayList<Character> line){
        StringBuilder builder = new StringBuilder();

        while(!line.isEmpty() && (isLet(line.get(0)) || isNum(line.get(0))))
            builder.append(line.remove(0));


        return builder.toString();
    }

    //
    private String makeNumber(ArrayList<Character> line) throws InvalidIDException{
        String res = makeWord(line);
        for(int i = 0; i<res.length(); i++)
            if(isLet(res.charAt(i))){
                InvalidIDException ex = new InvalidIDException(res + " is invalid identificator");
                ex.setInvalidId(res);
                throw ex;
            }

        return res;
    }
    //
    private String makeDelimiter(ArrayList<Character> line){
        if(line.isEmpty())return null;

        StringBuilder builder = new StringBuilder();
        builder.append(line.remove(0));
        if (!line.isEmpty()){
            builder.append(line.remove(0));
            if (isDoubleDelimiter(builder.toString())) return builder.toString();
            line.add(0,builder.charAt(1));
            builder.deleteCharAt(1);
        }

        if (isSingleDelimiter(builder.toString())) return builder.toString();
        line.add(0,builder.charAt(0));
        return null;
    }
    //
    private boolean isCommentSymbol(ArrayList<Character> line, String symbol){
        StringBuilder builder = new StringBuilder();
        builder.append(line.remove(0));
        if (!line.isEmpty()){
            builder.append(line.remove(0));
            if(symbol.equalsIgnoreCase(builder.toString()))
                return true;
            line.add(0,builder.charAt(1));
            line.add(0,builder.charAt(0));
            return false;
        }
        line.add(0,builder.charAt(0));
        return false;
    }
    //
    private void deleteComment(ArrayList<Character> line){
        boolean endCom = false;
        while (!line.isEmpty()){
            if(isCommentSymbol(line, ComentSymbols.ECOM)){
                endCom = true;
                break;
            }
            line.remove(0);
        }
        if(endCom)commentFlag = false;
    }


    private void seperateLexems(String line){
        ArrayList<Character> lineChar = new ArrayList<>();

        for(Character c: line.toCharArray())
            lineChar.add(c);

        if(commentFlag)deleteComment(lineChar);

        while (!lineChar.isEmpty()){
            //seperate words
            if(isLet(lineChar.get(0))){
                String word = makeWord(lineChar);
                if(keyWordTable.containsKey(word))
                    lexems.add(new Lexem(word, keyWordTable.get(word)));
                else{
                    if(!identTable.containsKey(word))
                        identTable.put(word, ++lastIdentCode);
                    lexems.add(new Lexem(word, identTable.get(word)));
                }
                continue;
            }
            //seperate constants
            if(isNum(lineChar.get(0))){
                try {
                    String number = makeNumber(lineChar);
                    if(!constantTable.containsKey(number))
                        constantTable.put(number, ++lastConstantCode);
                    lexems.add(new Lexem(number, constantTable.get(number)));
                } catch (InvalidIDException e) {
                    lexems.add(new Lexem(e.getInvalidId(), -1));
                }finally {
                    continue;
                }
            }
            //seperate spaces
            if(isSpaces(lineChar.get(0))){
                lineChar.remove(0);
                continue;
            }

            //search comment
            commentFlag = isCommentSymbol(lineChar, ComentSymbols.BCOM);
            if(commentFlag){
                deleteComment(lineChar);
                continue;
            }


            //seperate delimiters
            String delim = makeDelimiter(lineChar);
            if(delim != null) {
                if (delim.length() == 2)
                    lexems.add(new Lexem(delim, doubleSymTable.get(delim)));
                else
                    lexems.add(new Lexem(delim, singleSymTable.get(delim)));
                continue;
            }

            //unknown symbol

            lexems.add(new Lexem(String.valueOf(lineChar.remove(0)) , -1));

        }
    }


    public void analyse(Receiver receiver) throws IOException {
        String str = null;
        while((str=receiver.getString()) != null){
            str = str.toUpperCase();
            seperateLexems(str);
        }


        if(commentFlag)
            lexems.add(new Lexem(KeyWords.ERROR, -1));
    }


}
