package br.gov.caixa.faleconosco.portal.util;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLGen {

	public static class ResponseXML{
		
		private Map<String,Object>nodes;
		
		private boolean sucesso;
		private String  msgError;
		
		
		
		public void addNode(String name, Object value){
			if(nodes==null){
				nodes = new HashMap<String, Object>();
			}
			nodes.put(name, value);
		}
		
		
		
		
		public void setMsgError(String msgError) {
			this.msgError = msgError;
		}




		public boolean isSucesso() {
			return sucesso;
		}
		public void setSucesso(boolean sucesso) {
			this.sucesso = sucesso;
		}
		
		
		public Map<String,Object> getNodes() {
			return nodes;
		}
		public void setNodes(Map<String,Object> nodes) {
			this.nodes = nodes;
		}

		public String getMsgError() {
			return msgError;
		}
		
		
		
		
		
	}
	
	
	public static String generateResponse(ResponseXML response) throws Exception{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("root");
		Element sucesso = doc.createElement("sucesso");
		sucesso.setTextContent(Boolean.toString(response.sucesso));
		root.appendChild(sucesso);
		
		if(response.msgError!=null && !response.msgError.isEmpty()){
			Element msgErro = doc.createElement("msgErro");
			msgErro.setTextContent(response.msgError);
			root.appendChild(msgErro);
		}
	
		//populate nodes from map
		
		if(response.nodes!=null && !response.nodes.isEmpty()){
			Set<String> keySet = response.nodes.keySet();
			for (String key : keySet) {
				Object value = response.nodes.get(key);
				Element nodeKey = doc.createElement(key);
				nodeKey.setTextContent((value!=null)?value.toString():null);
				root.appendChild(nodeKey);
			}
		}
		
		
		doc.appendChild(root);
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		return result.getWriter().toString();
	}
}