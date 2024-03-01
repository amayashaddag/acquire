package control.network;

import java.io.*;
import java.net.Socket;

public class Client {
    private boolean leave = false;
    public static void main(String[] args)throws IOException {
        BufferedReader serverRequest;
        BufferedWriter clientRequest;
        Socket socketOfClient;
        try{
            socketOfClient = new Socket("localhost",9999);
            serverRequest = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
            clientRequest = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));

        }catch (IOException e){
            System.out.println("Can't connect to server");
        }
        try{

        }


    }
}
