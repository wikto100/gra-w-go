package org.grawgo.klient;

//TODO parser wiadomosci z serwera
public class ClientCommandParser {
    String parseInputFromUser(String input) {
        String[] splitInput = input.split(" ");
        StringBuilder dataBuilder = new StringBuilder();
        String command = splitInput[0];
        int ARGNO;
        switch (command){
            case "place":
                ARGNO = 3; // place x1 y1
                break;
            default:
                ARGNO = 1; // komendy jak exit, disconnect, black, white etc
                break;
        }
        command += "$";
        for (int i = 1; i < ARGNO; i++) {
            dataBuilder.append(splitInput[i]);
            dataBuilder.append('|');
        }
        return (command + dataBuilder);
    }

    String parseCommandFromServer(String response) {
        return response.split("\\$")[0];
    }

    String parseDataFromServer(String response, int field) {
        String data = response.split("\\$")[1];
        return data.split("\\|")[field];
    }

    String parseBoardFromServer(String response) {
        StringBuilder dataBuilder = new StringBuilder();
        String data = response.split("\\$")[1];
        String[] splitData = data.split("\\|");
        for (String splitDatum : splitData) {
            dataBuilder.append(splitDatum);
            dataBuilder.append('\n');
        }
        return dataBuilder.toString();
    }
}
