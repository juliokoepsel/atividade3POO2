package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteSocket {
    public static void main(String[] args) {
        String serverEndereco = "localhost";
        int serverPort = 7000;

        try {
            Socket socket = new Socket(serverEndereco, serverPort);

            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

            Thread respostaThread = new Thread(new ClienteResposta(socket));
            respostaThread.start();

            String input;
            while ((input = entrada.readLine()) != null) {
                saida.println(input);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClienteResposta implements Runnable {
    private Socket socket;

    public ClienteResposta(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String mensagem;
            while ((mensagem = entrada.readLine()) != null) {
                System.out.println("Recebido: " + mensagem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}