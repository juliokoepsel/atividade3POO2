package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ClienteRespostas implements Runnable {
    
    private Socket socket;

    public ClienteRespostas(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            String mensagem;
            while ((mensagem = entrada.readLine()) != null) {
                System.out.println(mensagem);
            }

            System.out.println("A conexão com o servidor foi encerrada!");
        } catch (SocketException e) {
            System.out.println("Desconectado");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
