import java.net.*;
import java.io.*;

public class GoClient {
 
    public static void main(String[] args) {
        try{
            Socket socket = new Socket("localhost", 4444); 
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Console console = System.console();
            String text;
            String response;
        do {
            text = console.readLine("Input command: ");
            out.println(text);
            response=in.readLine();
            if(response.equals("legal")){
                System.out.println("moved");
            }
            else if(response.equals("disconnect")){
                System.out.println("disconnecting...");
            }
            else if(response.equals("invalid")){
                System.out.println("invalid command");
            }
            else{
                System.out.println("encountered error");
            }
        } while (!text.equals("exit"));
            socket.close();
        }
        catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        }
        catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}