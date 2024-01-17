package org.grawgo.serwer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ServerParserTest 
{
    private final ServerCommandParser testParser = ServerCommandParser.getInstance();

    @Test
    public void commandTest1()
    {
        String result;
        String expected="place";
        result=testParser.parseCommand("place$1|1");
        assertEquals(result,expected);
    }

    @Test
    public void commandTest2()
    {
        String result;
        String expected="exit";
        result=testParser.parseCommand("exit$");
        assertEquals(result,expected);
    }

    @Test
    public void placeTest()
    {
        String command="place$7|9";
        int res1,res2;
        int ex1=7,ex2=9;
        res1=testParser.parseCoords(command)[0];
        res2=testParser.parseCoords(command)[1];
        assertEquals(res1,ex1);
        assertEquals(res2,ex2);
    }
}
