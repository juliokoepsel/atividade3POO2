package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorSocket {
    private static ServidorSocket instancia;
    private ServerSocket serverSocket;
    private List<Socket> clientes;

    private ServidorSocket() {
        clientes = new ArrayList<>();
    }

    public static ServidorSocket getInstancia() {
        if (instancia == null) {
            instancia = new ServidorSocket();
        }
        return instancia;
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("O Servidor do chat está sendo executado no port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientes.add(clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcast(String message, Socket sender) {
        for (Socket client : clientes) {
            if (!client.equals(sender)) {
                try {
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ServidorSocket server = ServidorSocket.getInstancia();
        int port = 7000;
        server.start(port);
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            while ((message = in.readLine()) != null) {
                ServidorSocket.getInstancia().broadcast(message, clientSocket);
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}