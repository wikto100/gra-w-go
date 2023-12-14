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

    String parseResponse(String response){
        return response.split("\\$")[0];
    }

    String getData(String response,int field){
        String data = response.split("\\$")[1];
        return data.split("\\|")[field];
    }

    void parseBoard(String response){
        String data = response.split("\\$")[1];
        String[] splitData = data.split("\\|");
        for (String splitDatum : splitData) {
            System.out.println(splitDatum);
        }
    }
}
