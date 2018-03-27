package codingnomads;

import org.junit.Test;

import static org.junit.Assert.*;

public class FirstTestTest {

    FirstTest tester = new FirstTest();
    @Test
    public void multiply() throws Exception {

        assertEquals(4,tester.multiply(2,2));

    }
}