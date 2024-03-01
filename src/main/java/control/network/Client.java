package control.network;

import java.io.*;
import java.net.Socket;

public class Client {
private static boolean LEAVE = false;
    public static void main(String[] args)throws IOException {
        BufferedReader serverRequest;
        BufferedWriter clientRequest;
        Socket socketOfClient;
        LEAVE = true; //Temporaire pour les tests
        try{
            socketOfClient = new Socket("localhost",9999);
            serverRequest = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
            clientRequest = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
            clientRequest.write("Hello ! Thanks for accepting me");
            clientRequest.newLine();
            clientRequest.flush();
            while(true){
                String serverResponse = serverRequest.readLine();
                System.out.println(serverResponse);
                if (LEAVE){
                    clientRequest.write("LEAVE");
                    clientRequest.flush();
                    System.out.println(serverRequest.readLine());
                    clientRequest.close();
                    serverRequest.close();
                    socketOfClient.close();
                    break;
                }
            }
        }catch (IOException e){
            System.out.println("Can't connect to server");
        }
    }
    public void PlayerHasToLeave(){
        LEAVE = true;
    }
}
