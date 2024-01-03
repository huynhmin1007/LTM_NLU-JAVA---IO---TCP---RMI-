package giaide_2021.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import giaide_2021.helper.RafCandidate;
import giaide_2021.model.Candidate;

public class ServerThread extends Thread {
	
	private Socket socket;
	private BufferedReader netIn;
	private PrintWriter netOut;
	
	private RafCandidate rafCandidate;
	
	private final String DIR = System.getProperty("user.dir") + "\\data\\data";

	public static final String END_MARKER = "OVER";
	
	public ServerThread(Socket socket) throws IOException {
		super();
		this.socket = socket;
		netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		netOut = new PrintWriter(socket.getOutputStream(), true);
		
		File folder = new File(DIR);
		if(!folder.exists()) folder.mkdir();
		
		rafCandidate = new RafCandidate(new File(folder.getAbsoluteFile() + "\\thisinh.dat"));
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		String welcome = "__Welcome__";
		netOut.println(welcome);
		netOut.println(END_MARKER);
		
		try {
			Candidate lastCandidate = null;
			while(true) {
				String line = netIn.readLine();
				
				if(line == null) {
					break;
				}
				
				StringTokenizer request = new StringTokenizer(line, "\t");
				String com = request.nextToken().toUpperCase();
				String[] params = line.substring(com.length()).trim().split("\t");;
				String respone;
				
				switch(com) {
				case "REGISTER":
					if(params.length != 3) {
						respone = "Error: invalid command";
					}
					else {
						Candidate candidate = register(params[0].trim(), params[1].trim(), params[2].trim());
						if(candidate == null) {
							respone = "Error";
						}
						else {
							respone = String.format("MSDT: %03d", candidate.getId());
							lastCandidate = candidate;
						}
					}
					break;
				
				case "FOTO":
					if(lastCandidate != null && params.length == 1) {
						if(!uploadImg(lastCandidate, params[0])) {
							respone = "Error";
						}
						else {
							respone = "Upload successful" + "\n" + lastCandidate.toString();
						}
						break;
					}
					
				case "VIEW":
					if(params.length == 1) {
						try {
							int id = Integer.parseInt(params[0]);
							if(id > 0 && id <= rafCandidate.getCount()) {
								Candidate candidate = view(id);
								respone = candidate.toString();
							}
							else {
								respone = "Candidate not found";
							}
						}
						catch (NumberFormatException e) {
							respone = "ERROR";
						}
						break;
					}
					
				case "UPDATE":
					if(params.length == 2) {
						int id = Integer.parseInt(params[0]);
						String address = params[1];
						
						if(update(id, address)) {
							respone = "UPDATE SUCCESSFUL";
						}
						else {
							respone = "ERROR";
						}
						break;
					}
					
					default:
						respone = "Error: invalid command";
						break;
				}
				
				netOut.println(respone);
				netOut.println(END_MARKER);
			}
			closeConnection();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean update(int id, String address) throws IOException {
		return rafCandidate.update(id, address);
	}
	
	private Candidate view(int id) throws IOException {
		return rafCandidate.getCandidate(id);
	}
	
	private boolean uploadImg(Candidate candidate, String path) throws IOException {
		File img = new File(path);
		
		if(!img.exists() || img.length() > 100 * 1024 || !img.getName().endsWith(".jpg")) return false;
		
		rafCandidate.register(candidate, img);
		
		return true;
	}
	
	private Candidate register(String name, String date, String address) throws IOException {
		if(date.length() != 10 || name.length() > 25) return null;
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date dateFormat;
		try {
			dateFormat = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		if(new Date().getYear() - dateFormat.getYear() > 10) return null;
		
		return new Candidate(rafCandidate.getCount() + 1, name, dateFormat, address);
	}
	
	public void closeConnection() throws IOException {
		netIn.close();
		netOut.close();
		socket.close();
		rafCandidate.close();
	}
}
