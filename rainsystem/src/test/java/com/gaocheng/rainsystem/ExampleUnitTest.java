package com.gaocheng.rainsystem;

import com.gaocheng.baselibrary.util.StringUtil;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println(StringUtil.getPercent(640,789));
        System.out.println(1.0*640/789);
    }
}