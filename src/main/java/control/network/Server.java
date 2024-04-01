package control.network;

import java.io.*;
import java.net.Socket;

@Deprecated
public class Server {
    public static void main(String[] args) throws IOException {

    }

    public static class ServiceThread extends Thread {
        Socket clientSocket;
        int idClient;

        public ServiceThread(int idClient, Socket clientSocket) {
            this.idClient = idClient;
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader clientRequest = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter serverRequest = new BufferedWriter(
                        (new OutputStreamWriter(clientSocket.getOutputStream())));
                serverRequest.write("Hello ! Welcome to you player n°" + idClient + " !");
                while (true) {
                    serverRequest.newLine();
                    serverRequest.flush();
                    String clientResponse = clientRequest.readLine();
                    System.out.println(clientResponse);
                    if (clientResponse.equals("LEAVE")) {
                        serverRequest.write("Thanks for playing see you soon !");
                        serverRequest.flush();
                        clientRequest.close();
                        serverRequest.close();
                        clientSocket.close();
                        break;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(4);
            }
        }
    }
}
