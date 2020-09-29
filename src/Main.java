import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

import processing.core.PApplet;

public class Main extends PApplet{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main(Main.class.getName());
	}
	
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private Gson gson;
	
	public void settings() {
		size(500, 500);
	}
	
	public void setup() {
		initServer();
	}
	
	public void draw() {
		background(180);
	}
	
	public void mouseClicked() {
		sendMessage("Click desde servidor");
	}
	
	public void initServer() {
		new Thread(
				()->{
					try {
						// 1. esperar la conexión
						ServerSocket server = new ServerSocket(5000);
						System.out.println("Esperando conexión");
						socket = server.accept();
						
						// 3. conexión establecida
						System.out.println("Cliente conectado");
						InputStream  is = socket.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						reader = new BufferedReader(isr);
						
						OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        writer = new BufferedWriter(osw);
						 
						while(true) {
							System.out.println("Esperando...");
							//No se define hasta que el cliente manda un elemento
							String line = reader.readLine();
							System.out.println("Recibido");
							System.out.println("Recibido" + line + '\n');
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
				).start();
	}
	
	private void sendMessage(String msg) {
        new Thread(
                () -> {
                    try {
                        writer.write(msg+"\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }
	
	
	

}
