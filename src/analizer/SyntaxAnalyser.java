package analizer;

import constants.Delimeters;
import constants.KeyWords;
import exceptions.SAException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Created by dmytro on 16.04.16.
 */
//next 6 paragraph
public class SyntaxAnalyser extends Analyser{
    public SyntaxAnalyser(ArrayList<Lexem> lexems){
        this.lexems = lexems;
    }

    public void analyse(){
        ListIterator<Lexem> li = lexems.listIterator();
        try {
            program(li);
            System.out.println("All right!!!");
        } catch (SAException e) {
            System.out.println("Error: " + e.toString());
        } catch (NoSuchElementException e1){
            System.out.println("Error: lexem excepted");
        }
    }

    private boolean isIdentifier(int code){
        if(code >= 1001)
            return true;
        return false;
    }

    private boolean isConstant(int code){
        if(code >= 501)
            return true;
        return false;
    }

    private void makeError(String message) throws SAException{
        SAException ex = new SAException(message);
        ex.setError(true);
        throw ex;
    }

    private void program(ListIterator<Lexem> it) throws SAException, NoSuchElementException {
        if(KeyWords.PROGRAM.equals(it.next().getName())){
            procIdentifier(it);
            if(Delimeters.STOP_LINE.equals(it.next().getName())){
                block(it);
            }else{
                makeError("; excepted");
            }
        }else{
            makeError("PROGRAM excepted");
        }

    }
    private void procIdentifier(ListIterator<Lexem> it)throws SAException{
        identifier(it);
    }
    private void block(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        declarations(it);
        if(KeyWords.BEGIN.equals(it.next().getName())){
            statementList(it);
            if(!KeyWords.END.equals(it.next().getName())){
                makeError("END excepted");
            }
        }

    }

    private void identifier(ListIterator<Lexem> it)throws SAException{
        if(it.hasNext()) {
            Lexem lex =  it.next();
            if (!isIdentifier(lex.getCode())) {
                throw new SAException(lex.getName() + " is not identifier");
            }
        }else{
            makeError("identifier excepted");
        }
    }

    private void constant(ListIterator<Lexem> it)throws SAException{
        if(it.hasNext()) {
            Lexem lex =  it.next();
            if (!isConstant(lex.getCode())) {
                if(!(it.hasNext() && Delimeters.SUB.equals(lex.getName()) && isConstant(it.next().getCode()))){
                    makeError("constant excepted");
                }
            }
        }else{
            makeError("constant excepted");
        }
    }

    private void declarations(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        constDeclarations(it);
        varDeclarations(it);
    }

    private void statementList(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        try{
            statement(it);
            statementList(it);
        }catch (SAException ex){
            if(!ex.isError()) {
                it.previous();
                return;
            }else
                throw ex;
        }
    }
    private void statement(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        Lexem lex = it.next();
        if(KeyWords.LOOP.equals(lex.getName())){
            statementList(it);
            if(!KeyWords.ENDLOOP.equals(lex.getName())){
                makeError("ENDLOOP excepted");
            }
        }else if(KeyWords.FOR.equals(lex.getName())){
            return;
        }else if(KeyWords.CASE.equals(lex.getName())){
            return;
        }else{
            throw new SAException("unknown statement");
        }
        if(!Delimeters.STOP_LINE.equals(lex.getName())){
            makeError("; excepted");
        }
    }

    private void constDeclarations(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        if(KeyWords.CONST.equals(it.next().getName())){
            try {
                constDeclarationsList(it);
            }catch (SAException ex){
                if(!ex.isError()) {
                    it.previous();
                    return;
                }else
                    throw ex;
            }
        }else{
            throw new SAException("CONST excepted");
        }
    }

    private void constDeclaration(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        constIdentifier(it);
        if(Delimeters.EQUAL.equals(it.next().getName())){
            constant(it);
            if(!Delimeters.STOP_LINE.equals(it.next().getName())){
                makeError("; excepted");
            }
        }else{
            makeError("= excepted");

        }
    }

    private void constDeclarationsList(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        constDeclaration(it);
        constDeclarationsList(it);
    }

    private void constIdentifier(ListIterator<Lexem> it)throws SAException{
        identifier(it);
    }

    private void varDeclarations(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        if(KeyWords.VAR.equals(it.next().getName())){
            try {
                declarationsList(it);
            }catch (SAException ex){
                if(!ex.isError()) {
                    it.previous();
                    return;
                }else
                    throw ex;
            }
        }else{
            throw new SAException("VAR excepted");
        }
    }

    private void declarationsList(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        declaration(it);
        declarationsList(it);
    }

    private void declaration(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        variableIdentifier(it);
        identifiersList(it);
        if(Delimeters.COLON.equals(it.next().getName())){
            attribute(it);
            if(!Delimeters.STOP_LINE.equals(it.next().getName())){
                makeError("; excepted");
            }
        }else{
            makeError(": excepted");
        }

    }

    private void variableIdentifier(ListIterator<Lexem> it)throws SAException{
        identifier(it);
    }

    private void identifiersList(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        if(Delimeters.COMA.equals(it.next().getName())){
            variableIdentifier(it);
            identifiersList(it);
        }else{
            it.previous();
        }
    }

    private void attribute(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        Lexem lex = it.next();
        if(KeyWords.SIGNAL.equals(lex.getName())){
            return;
        }else if(KeyWords.SIGNAL.equals(lex.getName())){
            return;
        }else if(KeyWords.COMPLEX.equals(lex.getName())){
            return;
        }else if(KeyWords.INTEGER.equals(lex.getName())){
            return;
        }else if(KeyWords.FLOAT.equals(lex.getName())){
            return;
        }else if(KeyWords.BLOCKFLOAT.equals(lex.getName())){
            return;
        }else if(KeyWords.EXT.equals(lex.getName())){
            return;
        }else{
            if(Delimeters.SQUARE_BRACKET_B.equals(lex.getName())){
                range(it);
                rangesList(it);
                if(!Delimeters.SQUARE_BRACKET_E.equals(it.next().getName())){
                    makeError("] excepted");
                }
            }else{
                makeError(lex.getName() + " unknown attribute");
            }
        }
    }


    private void range(ListIterator<Lexem> it)throws SAException, NoSuchElementException {
        unsignedInteger(it);
        if(Delimeters.RANGE.equals(it.next().getName())){
            unsignedInteger(it);
        }else{
            makeError(".. excepted");
        }
    }

    private void rangesList(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        if(Delimeters.COMA.equals(it.next().getName())){
            range(it);
            rangesList(it);
        }else{
            it.previous();
        }
    }

    //change it in future
    private void unsignedInteger(ListIterator<Lexem> it)throws SAException, NoSuchElementException{
        Lexem lex = it.next();
        if(!isConstant(lex.getCode())){
            makeError(lex.getName() + " is not constant");
        }
    }

}
