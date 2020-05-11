package br.gov.caixa.faleconosco.portal.util;

import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.logging.Level;

public class Util {

	
	public static boolean isEmpty(Object obj){
		if(obj==null)
				return true;
		
		if(obj instanceof String){
			String value = (String) obj;
			if(value.trim().length()==0){
				return true;
			}
		}
		
		if(obj instanceof Collection){
			Collection value = (Collection) obj;
			if(value.isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	public static String onlyNumbers(String value){
		if(isEmpty(value))return value;
		String numbers = value.replaceAll("[^\\d.]", "").replaceAll("\\.", "");
		return numbers;
	}
	
	public static String readerToString(Reader reader) throws IOException {
		  char[] arr = new char[8*1024]; // 8K at a time
		  StringBuffer buf = new StringBuffer();
		  int numChars;

		  while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
		      buf.append(arr, 0, numChars);
		  }

		  return buf.toString();
		    }
	
	
	public static boolean isCPF(String CPF) { 
		// considera-se erro CPF's formados por uma sequencia de numeros iguais 
		if (CPF.equals("00000000000") || 
			CPF.equals("11111111111") || 
			CPF.equals("22222222222") || 
			CPF.equals("33333333333") || 
			CPF.equals("44444444444") || 
			CPF.equals("55555555555") || 
			CPF.equals("66666666666") || 
			CPF.equals("77777777777") || 
			CPF.equals("88888888888") || 
			CPF.equals("99999999999") || 
			(CPF.length() != 11)) 
			return(false); 
		
			char dig10, dig11; 
			int sm, i, r, num, peso; 
			// "try" - protege o codigo para eventuais erros de conversao de tipo (int) 
			try { // Calculo do 1o. Digito Verificador 
				sm = 0; peso = 10; for (i=0; i<9; i++) { 
					// converte o i-esimo caractere do CPF em um numero: 
					// por exemplo, transforma o caractere '0' no inteiro 0 
					// (48 eh a posicao de '0' na tabela ASCII)
					num = (int)(CPF.charAt(i) - 48); 
					sm = sm + (num * peso); peso = peso - 1; 
					} 
				r = 11 - (sm % 11); if ((r == 10) || (r == 11)) dig10 = '0'; else dig10 = (char)(r + 48); 
				// converte no respectivo caractere numerico 
				// Calculo do 2o. Digito Verificador 
				sm = 0; peso = 11; 
				for(i=0; i<10; i++) { 
					num = (int)(CPF.charAt(i) - 48); 
					sm = sm + (num * peso); 
					peso = peso - 1; } 
				
				r = 11 - (sm % 11); 
				if ((r == 10) || (r == 11)) 
					dig11 = '0'; else dig11 = (char)(r + 48); 
				// Verifica se os digitos calculados conferem com os digitos informados. 
				if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) 
					return(true); 
				else 
					return(false); 
				} catch (InputMismatchException erro) { 
						return(false); 
				} 
			}
	
	public static boolean isCNPJ(String CNPJ) {
		    if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
		        CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
		        CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
		        CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
		        CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
		       (CNPJ.length() != 14))
		       return(false);
		 
		    char dig13, dig14;
		    int sm, i, r, num, peso;

		    try {
		      sm = 0;
		      peso = 2;
		      for (i=11; i>=0; i--) {
		        num = (int)(CNPJ.charAt(i) - 48);
		        sm = sm + (num * peso);
		        peso = peso + 1;
		        if (peso == 10)
		           peso = 2;
		      }
		 
		      r = sm % 11;
		      if ((r == 0) || (r == 1))
		         dig13 = '0';
		      else dig13 = (char)((11-r) + 48);
		 
		      sm = 0;
		      peso = 2;
		      for (i=12; i>=0; i--) {
		        num = (int)(CNPJ.charAt(i)- 48);
		        sm = sm + (num * peso);
		        peso = peso + 1;
		        if (peso == 10)
		           peso = 2;
		      }
		 
		      r = sm % 11;
		      if ((r == 0) || (r == 1))
		         dig14 = '0';
		      else dig14 = (char)((11-r) + 48);
	
		      if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
		         return(true);
		      else return(false);
		    } catch (InputMismatchException erro) {
		        return(false);
		    }
		  }

	public static String formataData(Date date){
		if(date==null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}
	
	
public static boolean validaData(String dataInformada){
		
		if(dataInformada==null){
			return false;
		}
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);
			sdf.parse(dataInformada);
		} catch (Exception e) {
			
			return false;
		}
	
		return true;
	}
	
}
