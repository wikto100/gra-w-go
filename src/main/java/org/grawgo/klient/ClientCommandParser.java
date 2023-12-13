package org.grawgo.klient;
//TODO parser wiadomosci z serwera
public class ClientCommandParser {
    String parseInput(String input){
        String[] splitInput=input.split("\\ ");
        String parsedInput=splitInput[0];
        parsedInput+="$";
        if(parsedInput.equals("place$")){
            parsedInput+=splitInput[1];
            parsedInput+="|";
            parsedInput+=splitInput[2];
        }
        return parsedInput;
    }
}
