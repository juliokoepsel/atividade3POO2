package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {

        String serverEndereco = "localhost";
        int serverPort = 7000;

        try {
            Socket socket = new Socket(serverEndereco, serverPort);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in)); // BufferedReader ou Scanner?
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true); // PrintWriter ou PrintStream?
            Thread respostaThread = new Thread(new ClienteRespostas(socket));
            respostaThread.start();

            System.out.println("Conectado ao servidor (" + serverEndereco + ":" + serverPort + ")");
            System.out.println("Olá, seja bem-vindo ao chat!");
            System.out.println("Digite \"/sair\" para desconectar-se do chat");

            String mensagem;
            do {
                mensagem = entrada.readLine();
                saida.println(mensagem);
            } while (!mensagem.equals("/sair"));

            socket.close();
        } catch (ConnectException e) {
            System.out.println("Não foi possível conectar ao servidor!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
