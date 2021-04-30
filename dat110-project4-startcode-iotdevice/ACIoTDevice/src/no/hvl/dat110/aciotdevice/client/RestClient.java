package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.google.gson.Gson;

public class RestClient {

	public RestClient() {
		// TODO Auto-generated constructor stub
	}
	
	
	private static String logpath = "/accessdevice/log";

	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message
		try (Socket s = new Socket(Configuration.host, Configuration.port)){
			
			Gson gson = new Gson();
			AccessMessage msg = new AccessMessage(message);
			
			//Construct the http request
			String jsonbody = gson.toJson(msg);
			
			String httppostreq = "POST " + logpath + " HTTP/1.1\r\n" + "Host: " + Configuration.host
					+ "\r\n" + "Content-type: application/json\r\n" + "Content-length: " + jsonbody.length()
					+ "\r\n" + "Connection: close\r\n" + "\r\n" + jsonbody + "\r\n";
			
			// Send the response over the TCP connection
			OutputStream output = s.getOutputStream();
			
			PrintWriter pw = new PrintWriter(output, false);
			pw.print(httppostreq);
			pw.flush();
			
			// Read the http response
			InputStream in = s.getInputStream();
			
			Scanner sc = new Scanner(in);
			StringBuilder jsonresp = new StringBuilder();
			boolean header = true;
			
			while(sc.hasNext()) {
				
				String nl = sc.nextLine();
				
				if(header) {
					System.out.println(nl);
				} else {
					jsonresp.append(nl);
				}
				
				if(nl.isEmpty()) {
					header = false;
				}
			}
			
			System.out.println("BODY:");
			System.out.println(jsonresp.toString());
			
			sc.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {

		AccessCode code = null;
		
		// TODO: implement a HTTP GET on the service to get current access code
		try (Socket s = new Socket(Configuration.host, Configuration.port)) {
			
			// Construct the get request
			Gson gson = new Gson();
			String httpgetreq = "GET " + codepath + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
						+ "Host: localhost\r\n" + "Connection: close\r\n" + "\r\n";
			
			// Send the http request
			OutputStream output = s.getOutputStream();
			
			PrintWriter pw = new PrintWriter(output, false);
			
			pw.print(httpgetreq);
			pw.flush();
			
			// Read the http response
			InputStream in = s.getInputStream();
			
			Scanner sc = new Scanner(in);
			StringBuilder jsonresp = new StringBuilder();
			boolean header = true;
			
			while(sc.hasNext()) {
				
				String nl = sc.nextLine();
				
				if(header) {
					System.out.println(nl);
				} else {
					jsonresp.append(nl);
				}
				
				if(nl.isEmpty()) {
					header = false;
				}
			}
			
			System.out.println("BODY:");
			System.out.println(jsonresp.toString());
			code = gson.fromJson(jsonresp.toString(), AccessCode.class);
			
			sc.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return code;
	}
}
