package test;

import analizer.LexAnalyser;
import analizer.SyntaxAnalyser;
import data.CharReader;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.*;

/**
 * Created by dmytro on 16.04.16.
 */
public class SyntaxAnalyserTest {
    @Test
    public void listTest()throws Exception{
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        ListIterator<Integer> it = list.listIterator();
        System.out.println(it.next());
        System.out.println(it.next());
        System.out.println(it.previous());
        System.out.println(it.previous());
        System.out.println(it.next());

    }
    @Test
    public void analyseTest()throws Exception{
        LexAnalyser analyser = new LexAnalyser();
        InputStreamReader inp = new InputStreamReader(
                new FileInputStream("/home/dmytro/IdeaProjects/LexAnalyserServer/src/test.sgn"));
        CharReader reader = new CharReader(inp);
        analyser.analyse(reader);
        reader.close();

        SyntaxAnalyser syntaxAnalyser = new SyntaxAnalyser(analyser.getLexems());
        syntaxAnalyser.analyse();
    }

}