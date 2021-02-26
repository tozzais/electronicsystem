package com.gaocheng.riversystem;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        String s = "";
        for (int i=0;i<10;i++){
            if (i == 0){
                s = s + "rid = '" + i + "'";
            }else {
                s = s + " or rid = '" + i + "'";
            }
        }
        String s2 = "RLevel = '市管' OR RLevel = '区管' OR RLevel = '镇管' ";
        String s3 = "RTown = '"+1001+"'";
        System.out.println(s3);
    }
}