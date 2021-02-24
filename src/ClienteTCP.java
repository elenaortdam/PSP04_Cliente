import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTCP {

	public void conectarServidor() throws IOException, ClassNotFoundException {
		String Host = "localhost";
		int Puerto = 6000;
		Socket cliente;
		try {
			cliente = new Socket(Host, Puerto);
		} catch (ConnectException c) {
			System.out.println("SERVIDOR DESCONECTADO, no se ejecutara el cliente.....");
			return;
		}
		System.out.println("PROGRAMA CLIENTE INICIADO...");
		ObjectOutputStream fsalida = new ObjectOutputStream(cliente.getOutputStream());
		ObjectInputStream fentrada = new ObjectInputStream(cliente.getInputStream());
		int id = (int) fentrada.readObject();
		System.out.printf("SOY EL CLIENTE %d%n", id);
		System.out.println("==============================================");

		BufferedReader entradaDatos = new BufferedReader(new InputStreamReader(System.in));
		String cadena = "";

		boolean cadenaValida;

		while (!cadena.equals("*")) {
			do {
				try {
					cadenaValida = true;
					System.out.println("Introduce identificador a consultar (* para salir):");
					cadena = entradaDatos.readLine();
					validarCadena(cadena);
				} catch (Exception e) {
					System.out.printf("%s. Introduce la cadena de nuevo", e.getLocalizedMessage());
					cadenaValida = false;
				}
			} while (!cadenaValida);
			fsalida.reset();
			fsalida.writeObject(cadena);

			//OBTENER PRIMER OBJETO ENVIADO POR EL SERVIDOR
			Profesor profesor = (Profesor) fentrada.readObject();
			System.out.println(profesor.toString());
		}

//		fsalida.close();
//		fentrada.close();
		System.out.println("Fin de proceso... ");
		entradaDatos.close();
		cliente.close();
	}

	private void validarCadena(String cadena) {
		if (cadena == null) {
			throw new IllegalArgumentException("La cadena no puede ser nula");
		}
		//Quitamos los posibles espacion que puede tener
		cadena = cadena.trim();
		if (cadena.isEmpty()) {
			throw new IllegalArgumentException("La cadena no puede estar vacía");
		}
		if (cadena.equals("*")) {
			return;
		}
		Integer identificador = null;
		try {
			identificador = Integer.parseInt(cadena);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("El identificador tiene que ser un número");
		}

	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ClienteTCP clienteTCP = new ClienteTCP();
		clienteTCP.conectarServidor();
	}

}
