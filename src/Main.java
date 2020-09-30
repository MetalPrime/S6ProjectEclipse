import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

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
	private ArrayList<User> userBase;
	private boolean onActive;
	
	public void settings() {
		size(500, 500);
	}
	
	public void setup() {
		selectedText = 0;
		onActive = true;
		initServer();
		userBase = new ArrayList<User>();
		userBase.add(new User("Hillary","5985",UUID.randomUUID().toString()));
		userBase.add(new User("Ingrid","6454",UUID.randomUUID().toString()));
		userBase.add(new User("Julieth","7654",UUID.randomUUID().toString()));
		userBase.add(new User("Jhon","1234",UUID.randomUUID().toString()));
		userBase.add(new User("Isabella","2302",UUID.randomUUID().toString()));
		userBase.add(new User("Angela","0810",UUID.randomUUID().toString()));

		

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
				sendMessage("Usuario o Contraseña Inválidos");
			break;
			case 2:
				textSize(24);
				fill(0);
				textAlign(CENTER);
				text("Bienvenido", width/2, height/2);
				sendMessage("Bienvenido");
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
                        
                        Thread.sleep(1000);
                        System.out.println("Esperando...");
						
						 
						
						while(onActive) {
							//No se define hasta que el cliente manda un elemento
							String line = reader.readLine();
							System.out.println("Recibido" + line + '\n');
							 Gson gson = new Gson();
							 User valitedUser = gson.fromJson(line, User.class);
							
							Thread.sleep(2000);
							if(line!=null) {
								if(selectedText==1 || selectedText==0) {
									 for(int i = 0; i<userBase.size() ; i++) {
										 if(userBase.get(i).getUsername().equals(valitedUser.getUsername()) && userBase.get(i).getPassword().equals(valitedUser.getPassword())) {
											 selectedText = 2;
											 System.out.println("funciona");
											 onActive = false;

										 } else if(selectedText!=2){
											 selectedText = 1;
											 System.out.println("no funciona");
										 }
										
									 }
								 }
							}
							 
							 System.out.println("que esta pasando?");

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
