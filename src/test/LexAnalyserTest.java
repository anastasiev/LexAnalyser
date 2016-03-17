package test;

import analizer.LexAnalyser;
import data.DataReceiver;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dmytro on 15.03.16.
 */
public class LexAnalyserTest {

    @Test
    public void testAnalyse() throws Exception {
        LexAnalyser analizer = new LexAnalyser();
        DataReceiver receiver = new DataReceiver("/home/dmytro/IdeaProjects/LexAnalizer/src/test.txt");
        receiver.open();
        analizer.analyse(receiver);
        receiver.close();
        System.out.println(analizer.getLexems());

        System.out.println(analizer.getConstantTable());
        System.out.println(analizer.getIdentTable());
        System.out.println(analizer.getKeyWordTable());
        System.out.println(analizer.getSingleSymTable());
        System.out.println(analizer.getDoubleSymTable());
    }
}