package br.gov.frameworkdemoiselle.timestamp;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class AppCallServlet {

	public static void main(String[] args) {
		System.out.println("--------------- CALL SERVLET ------------");

		HttpURLConnection connection = null;
		try {
			URL url = new URL("http://localhost:8080/certificate-applet-web/carimbo");
			
			byte[] content = "Serpro".getBytes();
			
		    connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setUseCaches(false);
		    connection.setDoOutput(true);
		    connection.setRequestProperty("Content-Type", "application/octet-stream");
		    connection.connect();
		    
		    OutputStream os = connection.getOutputStream();
		    os.write(content);
		    os.flush();
		    os.close();
		    
		    System.out.println("STATUS: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
		    
		    InputStream is = connection.getInputStream();
		    System.out.println(IOUtils.toString(is));
		    is.close();

		    
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
}
