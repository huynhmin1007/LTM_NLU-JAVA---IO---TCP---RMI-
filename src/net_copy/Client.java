package net_copy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client {
	
	private Socket socket;
	private DataOutputStream netOut;
	
	public Client(Socket socket) throws IOException {
		super();
		this.socket = socket;
		netOut = new DataOutputStream(socket.getOutputStream());
	}
	
	public void run() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(true) {
			StringTokenizer line = new StringTokenizer(br.readLine());
			
			String request = line.nextToken();
			
			if("EXIT".equalsIgnoreCase(request)) {
				System.out.println("GG <3");
				break;
			}
			else if("UPLOAD".equalsIgnoreCase(request)) {
				String src  = line.nextToken();
				String dest = line.nextToken();

				if(!upload(src, dest)) {
					System.out.println("Upload khong thanh cong!");
					continue;
				}
				DataInputStream netIn = new DataInputStream(socket.getInputStream());
				String respone = netIn.readUTF();
				System.out.println(respone);
				netIn.close();
			}
			else {
				System.out.println("Yeu cau khong xac dinh!");
			}
		}
		br.close();
		socket.close();
	}
	
	public boolean upload(String filePath, String destPath) throws IOException {
		File file = new File(filePath);

		if(!file.exists()) return false;
		
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		
		netOut.writeUTF(destPath);
		netOut.flush();
		
		copy(is, netOut, file.length());
		netOut.flush();
		is.close();
		socket.close();
		return true;
	}
	
	private void copy(InputStream is, OutputStream os, long size) throws IOException {
		byte[] data = new byte[8 * 1024];
		while(size > 0) {
			int byteRead = (int) Math.min(data.length, size);
			if(is.read(data, 0, byteRead) != -1) {
				os.write(data, 0, byteRead);
			}
			size -= byteRead;
		}
	}
	
	public static void main(String[] args) {
		try {
			Client client = new Client(new Socket("localhost", 2000));
			client.run();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
