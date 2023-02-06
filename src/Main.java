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
    }
}