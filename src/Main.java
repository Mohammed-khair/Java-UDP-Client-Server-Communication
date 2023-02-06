public class Main {
    public static void main(String[] args) {

        Server server = new Server();
        IntermediateHost host = new IntermediateHost();
        Client client = new Client();

        Thread serverThread = new Thread(server, "Server");
        Thread hostThread = new Thread(host, "Host");
        Thread clientThread = new Thread(client, "Client");

        serverThread.start();
        hostThread.start();
        clientThread.start();

        byte data[] = new byte[100];
        data[0] = 0;
        data[1] = 1;
        String filename = "test.txt";
        data[2] = filename.getBytes(); //filename
        String mode = "octet";
        data[3] = mode.getBytes();
        data[4] = 0;

    }
}