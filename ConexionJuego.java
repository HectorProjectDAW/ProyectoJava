package com.proyecto.mi_proyecto;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ConexionJuego {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean esServidor;
    private Consumer<String> onMensajeRecibido;

    public ConexionJuego(boolean esServidor) {
        this.esServidor = esServidor;
    }

    public void setOnMensajeRecibido(Consumer<String> onMensajeRecibido) {
        this.onMensajeRecibido = onMensajeRecibido;
    }

    public void iniciarServidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        socket = serverSocket.accept();
        iniciarFlujos();
    }

    public void conectarCliente(String ip, int puerto) throws IOException {
        socket = new Socket(ip, puerto);
        iniciarFlujos();
    }

    private void iniciarFlujos() throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(() -> {
            try {
                String linea;
                while ((linea = in.readLine()) != null) {
                    if (onMensajeRecibido != null) {
                        onMensajeRecibido.accept(linea);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error en la conexión: " + e.getMessage());
            }
        }).start();
    }

    public void enviarMensaje(String mensaje) {
        if (out != null) {
            out.println(mensaje);
        }
    }

    public void cerrar() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            if (esServidor && serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
