package org.grawgo.klient;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClientParserTest 
{
    private ClientCommandParser testParser = new ClientCommandParser();

    @Test
    public void inputTest1()
    {
        String result;
        String expected="place$1|1";
        result=testParser.parseInput("place 1 1");
        assertEquals(result,expected);
    }

    @Test
    public void inputTest2()
    {
        String result;
        String expected="exit$";
        result=testParser.parseInput("exit");
        assertEquals(result,expected);
    }

    @Test
    public void responseTest()
    {
        String result;
        String expected="DISCONNECT_RESPONSE";
        result=testParser.parseResponse("DISCONNECT_RESPONSE$0");
        assertEquals(result,expected);
    }
}
