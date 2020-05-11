package br.gov.caixa.faleconosco.portal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;

public class HttpUtil {

	public static String sendPostRequest(String requestUrl, String payload,String usuario, String senha) {
		String jsonString = new String();
		
		System.out.println("Executando pesquisa na URL:"+requestUrl);
		System.out.println("Payload enviado: "+payload);
		HttpURLConnection connection = null;
		try {
			URL url = new URL(requestUrl);
	        
			connection = (HttpURLConnection) url.openConnection();
	        
	        String authenticationString = com.lowagie.text.pdf.codec.Base64.encodeBytes((usuario+":"+senha).getBytes());
	        
	        System.out.println("auth String:"+authenticationString);
	        
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        connection.setRequestProperty("Authorization", "Basic "+authenticationString);
	        
	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	        writer.write(payload);
	        writer.close();
	        
	        
	        if(connection.getResponseCode()!=200) {
	        		System.out.println("Status do Retorno: "+connection.getResponseCode());
	        		JsonObject retorno = new JsonObject();
	            retorno.addProperty("erro","Erro em sua Solicitação");
	            retorno.addProperty("errorMSG", responseErrorToString(connection));
	            System.out.println("RETORNO:"+retorno.toString());
	            
	            return retorno.toString();
	        }
	        
	        
	        
	        jsonString =  responseToString(connection);
	        System.out.println("resposta webservice: "+jsonString);
	    } catch (Throwable e) {
	            e.printStackTrace();
	            JsonObject retorno = new JsonObject();
	            retorno.addProperty("erro",e.toString());
	            return retorno.toString();
	    }finally {
	    		if(connection!=null)
	    			connection.disconnect();
	    }
	    return jsonString.toString();
	}

	private static String responseToString( HttpURLConnection connection) throws IOException {
		StringBuffer retorno = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
		        retorno.append(line);
		}
		br.close();
		return retorno.toString();
	}
	
	private static String responseErrorToString( HttpURLConnection connection) throws IOException {
		StringBuffer retorno = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
		String line;
		while ((line = br.readLine()) != null) {
		        retorno.append(line);
		}
		br.close();
		return retorno.toString();
	}
	
	
}
