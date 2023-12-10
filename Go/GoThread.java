import java.io.*;
import java.net.*;
/**Watek serwera */
public class GoThread extends Thread {
    private Socket socket;
    public GoThread(Socket socket) {
        this.socket = socket;
    }
    /**Glowna funkcja watku */
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            PrintWriter out = new PrintWriter(output, true);
            String command;
            while(true){
                command=in.readLine();
                if(command.equals("move")){
                    //TODO board.isLegal(command)
                    out.println("legal");
                    System.out.println("move accepted");
                }
                else if(command.equals("exit")){
                    out.println("disconnect");
                    System.out.println("client disconnected");
                    break;
                }
                else{
                    out.println("invalid");
                    System.out.println("invalid command");
                }
            }
        }
        catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}