package app;
import control.network.*;

import javax.imageio.IIOException;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello bitches");
        Server.main(args);
        Client.main(args);

    }
}
