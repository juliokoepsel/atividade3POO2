package socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    
    private static Servidor instance; // Singleton
    private ServerSocket serverSocket;
    private List<Socket> clientes;

    private Servidor() {
        clientes = new ArrayList<>();
    }

    public static Servidor getInstance() {
        if (instance == null) {
            instance = new Servidor();
        }
        return instance;
    }

    public void iniciar(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("O Servidor do chat está sendo executado no port " + port);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                clientes.add(clienteSocket);
                ClienteHandler clienteHandler = new ClienteHandler(clienteSocket);
                Thread clienteThread = new Thread(clienteHandler);
                clienteThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void enviar(String mensagem, Socket origem) {
        for (Socket cliente : clientes) {
            if (!cliente.equals(origem) && !cliente.isClosed()) {
                try {
                    PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
                    saida.println(origem.getInetAddress() + ":" + String.valueOf(origem.getPort()) + ": " + mensagem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Servidor server = Servidor.getInstance();
        int port = 7000;
        server.iniciar(port);
    }
}
