package net_copy;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	
	private Socket socket;
	private DataOutputStream netOut;
	
	public ServerThread(Socket socket) throws IOException {
		super();
		this.socket = socket;
		netOut = new DataOutputStream(socket.getOutputStream());
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		try {
			DataInputStream netIn = new DataInputStream(socket.getInputStream());
			
			String destPath = netIn.readUTF();
			File file = new File(destPath);
			
			OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
			
			copy(netIn, os);
			netOut.writeUTF("Upload thanh cong!");
			netOut.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void copy(InputStream is, OutputStream os) throws IOException {
		byte[] data = new byte[8 * 1024];
		int res;
		while((res = is.read(data)) != -1) {
			os.write(data, 0, res);
		}
	}
}
