package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClienteHandler implements Runnable {
    
    private Socket clienteSocket;

    public ClienteHandler(Socket clienteSocket) {
        this.clienteSocket = clienteSocket;
    }

    @Override
    public void run() {
        try {
            Servidor server = Servidor.getInstance();

            BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));

            System.out.println("Usuário conectado: " + clienteSocket.getInetAddress() + ":" + String.valueOf(clienteSocket.getPort()));
            server.enviar("Conectou ao chat!", clienteSocket);

            String mensagem;
            while ((mensagem = entrada.readLine()) != null) {
                if (!mensagem.equals("/sair"))
                    server.enviar(mensagem, clienteSocket);
            }

            System.out.println("Usuário desconectado: " + clienteSocket.getInetAddress() + ":" + String.valueOf(clienteSocket.getPort()));
            server.enviar("Desconectou do chat!", clienteSocket);
            clienteSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
