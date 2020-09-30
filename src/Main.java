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
	private int selectedText;
	
	public void settings() {
		size(500, 500);
	}
	
	public void setup() {
		selectedText = 0;
		initServer();
	}
	
	public void draw() {
		background(180);
		
		switch(selectedText) {
		
			case 0:
				textSize(24);
				fill(0);
				textAlign(CENTER);
				text("Ingrese su usuario y " + "\n" + "contraseña desde" + "\n" + "su teléfono móvil", width/2, height/2);
			break;
			case 1:
				textSize(24);
				fill(0);
				textAlign(CENTER);
				text("Usuario o Contraseña" + "\n" + "Inválidos, por favor" + "\n" + "intente de nuevo", width/2, height/2);
			break;
			case 2:
				textSize(24);
				fill(0);
				textAlign(CENTER);
				text("Bienvenido", width/2, height/2);
			break;
		}
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
							 Gson gson = new Gson();
							 User valitedUser = gson.fromJson(line, User.class);

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
