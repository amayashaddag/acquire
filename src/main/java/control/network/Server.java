package control.network;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args)throws IOException{
        ServerSocket listener = null;
        //ArrayList<Client> clientHandler = new ArrayList<>();
        int client = 0;
        try{
            listener = new ServerSocket(9999);
            System.out.println("Waiting for players...");
        }catch (IOException e){
            e.printStackTrace();
            System.exit(4);
        }
        try{
            while(true){
                Socket socketForClient = listener.accept();
                new ServiceThread(client,socketForClient).start();
                System.out.println("Player n°"+client+" joined the game.");
                client++;
            }
        }finally {
            listener.close();
        }

    }
    public static class ServiceThread extends Thread{
        Socket clientSocket;
        int idClient;
        public ServiceThread(int idClient,Socket clientSocket){
            this.idClient = idClient;
            this.clientSocket = clientSocket;
        }
        @Override
        public void run(){
            try{
                BufferedReader clientRequest = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter serverRequest = new BufferedWriter((new OutputStreamWriter(clientSocket.getOutputStream())));
                serverRequest.write("Hello ! Welcome to you player n°"+idClient+" !");
                while(true){
                    serverRequest.newLine();
                    serverRequest.flush();
                    String clientResponse = clientRequest.readLine();
                    System.out.println(clientResponse);
                    if(clientResponse.equals("LEAVE")){
                        serverRequest.write("Thanks for playing see you soon !");
                        serverRequest.flush();
                        clientRequest.close();
                        serverRequest.close();
                        clientSocket.close();
                        break;
                    }

                }
            }catch (IOException e){
                e.printStackTrace();
                System.exit(4);
            }
        }
    }
}
