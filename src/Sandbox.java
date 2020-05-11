import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.caixa.faleconosco.portal.util.Util;
import br.gov.caixa.faleconosco.portal.util.XMLGen;
import br.gov.caixa.faleconosco.portal.util.XMLGen.ResponseXML;


public class Sandbox {

	
	public static void main(String[] arg) throws Exception{
		
		/*
		for (char i = 'A'; i != 'Z'; i++) {
			System.out.println(Character.valueOf(i));
		}
		
		String string = "00 13216 54";
		System.out.println(string.substring(0,string.indexOf(" ")));
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		System.out.println(df.format(date));
		*/
		
		/*
		File file = new File("C:\\Users\\rogerioso\\Desktop\\teste_arquivo.pdf");
		FileInputStream fot = new FileInputStream(file);
		byte bytes[] = new byte[(int)file.length()];
		fot.read(bytes);
		fot.close();
		
		System.out.println(fot);
		
		*/
		
		
		System.out.println(Util.validaData("16/78/6786"));
		
		
		
	}

	private static void responseXML() throws Exception {
		ResponseXML response = new XMLGen.ResponseXML();
		response.setMsgError("ERROR");
		response.addNode("passo", "1");
		response.addNode("rosca", null);
		System.out.println(XMLGen.generateResponse(response));
	}
	
}
