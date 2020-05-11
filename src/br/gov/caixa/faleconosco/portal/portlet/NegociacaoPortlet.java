package br.gov.caixa.faleconosco.portal.portlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.ProcessAction;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import br.gov.caixa.faleconosco.portal.dto.OcorrenciaDTO;
import br.gov.caixa.faleconosco.portal.util.CriptpUtil;
import br.gov.caixa.faleconosco.portal.util.HttpUtil;
import br.gov.caixa.faleconosco.portal.util.Util;
import br.gov.caixa.faleconosco.portal.util.XMLGen;
import br.gov.caixa.faleconosco.portal.util.XMLGen.ResponseXML;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class NegociacaoPortlet extends GenericPortlet {

	private static final String DATA_NASCIMENTO = "dtNascimento";
	private static final String NOME_DA_MAE = "noMae";
	private static final String MENSAGEM="mensagem";
	private static final String PROTOCOLO = "protocolo";
	private static final String PROTOCOLOSTR = "protocoloStr";
	private static final String NOME="nome"; 
	private static final String CPF_CNPJ="cpf";
	private static final String AGENCIA="agencia";
	private static final String EMAIL="email";
	private static final String TEL="tel";
	private static final String NOME_CIDADE="noCidade";
	private static final String NOME_NATUREZA="noNatureza";
	private static final String NOME_CATEGORIA="noCategoria";
	private static final String SIGLA_UF="sgUf";
	
	
	private static final String JSP_FORMULARIO = "/jsp/negociacao/form.jsp";
	private static final String JSP_ERRO = "/jsp/negociacao/erro.jsp";	
	private static final String JSP_SUCESSO = "/jsp/negociacao/sucesso.jsp";
	
	private static final String TOKEN_CAPTCHA = "tokenCaptcha";
	private static final String TOKEN_CAPTCHA_URL="tokenCaptchaURL";
	private static final String IMG_CAPTCHA_URL="imgCaptchaURL";
	private static final String SOUND_CAPTCHA_URL="soundCaptchaURL";
	private static final String VALIDATE_CAPTCHA_URL="validateCaptchaURL"; 
	
	
	private static final String GRAVAR_NEGOCIACAO = "gravarNegociacao";
	
	private static final String SIOUV_USUARIO_SERVICO = "siouv_usuario_servico";
	private static final String SIOUV_SENHA_SERVICO = "siouv_senha_servico";
	
	private static final String USUARIO_SERVICO = "usuario_servico";
	private static final String SENHA_SERVICO = "senha_servico";
	
	private static final String URL_BUSCA_POR_PROTOCOLO = "servico_busca_por_protocolo";
	private static final String URL_REGISTRA_NEGOCIACAO = "servico_registra_negociacao";
	private static final String MSG_SUCESSO_SIVOU = "Registro realizado com sucesso";
	private static final String JSP_FORMULARIO_EDIT = "/jsp/negociacao/edit.jsp";
	private static final PortletMode EDITDEFAULTSMODE = new PortletMode("edit_defaults");
	
	@Override
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {

	
		
		String urlToken = request.getPreferences().getValue(TOKEN_CAPTCHA_URL, null);
		String urlImg = request.getPreferences().getValue(IMG_CAPTCHA_URL, null);
		String soundUrl = request.getPreferences().getValue(SOUND_CAPTCHA_URL, null);
		String validateUrl = request.getPreferences().getValue(VALIDATE_CAPTCHA_URL, null);
		if (urlToken == null || urlImg == null || soundUrl == null || validateUrl == null) {
			response.getWriter().write("Entre no modo de edição defina os serviços de captcha");
			return;
		}
		
		String paramProtocolo = request.getParameter(PROTOCOLO);
		
		System.out.println("-------------------------Protocolo :"+paramProtocolo);
		Integer protocolo = null;
		if(paramProtocolo!=null){
			protocolo = Integer.valueOf(paramProtocolo);
		}
		System.out.println("--------------------Protocolo :"+protocolo);
		
		
		try{
			if(Util.isEmpty(protocolo)){
				
				String tokenCaptcha = getTokenCaptcha(urlToken);
				request.setAttribute(TOKEN_CAPTCHA, tokenCaptcha);
				
				PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(JSP_FORMULARIO);
				
				rd.include(request, response);
				
			}else{
				System.out.println("-----------------------Protocolo 2:"+protocolo);
				
				PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(JSP_SUCESSO);
				request.setAttribute(PROTOCOLO, CriptpUtil.criptografar(Integer.toString(protocolo)));
				request.setAttribute(PROTOCOLOSTR, protocolo);
				request.setAttribute("nome", NaturezaFormulario.NEGOCIACAO.nomeCategoria);
				rd.include(request,response);
				
			}
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("nome", NaturezaFormulario.NEGOCIACAO.nomeCategoria+ "<!--"+ e.getMessage() + e.toString() +" -->");
			PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(JSP_ERRO);
			rd.include(request,response);
			throw new PortletException(e);
		}
	}
	
//	public void init() throws PortletException{
//		super.init();
//	}
	
	
	public void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		response.setContentType(request.getResponseContentType());
		
		String tokenUrl = request.getPreferences().getValue(TOKEN_CAPTCHA_URL,null);
		String imgUrl = request.getPreferences().getValue(IMG_CAPTCHA_URL, null);
		String audioUrl = request.getPreferences().getValue(SOUND_CAPTCHA_URL, null);
		String validateUrl = request.getPreferences().getValue(VALIDATE_CAPTCHA_URL, null);
		String urlRegistro = request.getPreferences().getValue(URL_REGISTRA_NEGOCIACAO, null);
		String urlBusca = request.getPreferences().getValue(URL_BUSCA_POR_PROTOCOLO, null);
		
		request.setAttribute(TOKEN_CAPTCHA_URL, tokenUrl);
		request.setAttribute(IMG_CAPTCHA_URL, imgUrl);
		request.setAttribute(SOUND_CAPTCHA_URL, audioUrl);
		request.setAttribute(VALIDATE_CAPTCHA_URL, validateUrl);
		request.setAttribute(URL_REGISTRA_NEGOCIACAO, urlRegistro);
		request.setAttribute(URL_BUSCA_POR_PROTOCOLO, urlBusca);
		
		PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(JSP_FORMULARIO_EDIT);
		rd.include(request,response);
	}
	
	@ProcessAction(name="savePrefs")
	public void savePrefs(ActionRequest request, ActionResponse response)throws PortletException, IOException{
		PortletPreferences pref = request.getPreferences();
		
		//pref.setValue(USUARIO_SERVICO, request.getParameter(USUARIO_SERVICO));
		//pref.setValue(SENHA_SERVICO, request.getParameter(SENHA_SERVICO));
		
		
		
		pref.setValue(TOKEN_CAPTCHA_URL, request.getParameter(TOKEN_CAPTCHA_URL));
		pref.setValue(IMG_CAPTCHA_URL, request.getParameter(IMG_CAPTCHA_URL));
		pref.setValue(SOUND_CAPTCHA_URL, request.getParameter(SOUND_CAPTCHA_URL));
		pref.setValue(VALIDATE_CAPTCHA_URL, request.getParameter(VALIDATE_CAPTCHA_URL));
		
		pref.setValue(URL_BUSCA_POR_PROTOCOLO, request.getParameter(URL_BUSCA_POR_PROTOCOLO));
		pref.setValue(URL_REGISTRA_NEGOCIACAO, request.getParameter(URL_REGISTRA_NEGOCIACAO));
		
		pref.store();
		response.setPortletMode(PortletMode.VIEW);
	}
	
	
	@Override
	protected void doDispatch(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		PortletMode portletMode = request.getPortletMode();
		if(portletMode.equals(EDITDEFAULTSMODE)){
			doEdit(request, response);
			return;
		}
		super.doDispatch(request, response);
	}	

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {

		String resourceID = request.getResourceID();

		try {
			Method method = this.getClass().getDeclaredMethod(resourceID, ResourceRequest.class,
					ResourceResponse.class);
			method.invoke(this, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortletException(e);
		}
		super.serveResource(request, response);
	}

	public void passo1(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
		try {
			OcorrenciaDTO dto = new OcorrenciaDTO();

			BeanUtils.populate(dto, request.getParameterMap());
			ResponseXML responseXML = new XMLGen.ResponseXML();

			responseXML.addNode("passo", 1);

			List<String> errors = new ArrayList<String>();

			if (Util.isEmpty(dto.getNome())) {
				errors.add("Nome");
			}
			
			if (Util.isEmpty(dto.getNoMae())) {
				errors.add("Nome da Mãe");
			}
			
			if (Util.isEmpty(dto.getDtNascimento())) {
				errors.add("Data de Nascimento");
			}else {
				if(!Util.validaData(dto.getDtNascimento())) {
					errors.add("Data de Nascimento inválida");
				}
			}
			
			
			
			if (Util.isEmpty(dto.getAgencia())) {
				errors.add("Agência");
			}
			
			
			if (Util.isEmpty(dto.getCpfCnpj())) {
				errors.add("CPF/CNPJ");
			} else {
				if(Util.onlyNumbers(dto.getCpfCnpj()).length() <= 11) {
					if (!Util.isCPF(Util.onlyNumbers(dto.getCpfCnpj()))) {
						errors.add("Número de CPF/CNPJ inválido");
					}
				}else {
					if (!Util.isCNPJ(Util.onlyNumbers(dto.getCpfCnpj()))) {
						errors.add("Número de CPF/CNPJ inválido");
					}
				}
			
				
			}

			if (Util.isEmpty(dto.getEmail())) {
				errors.add("Email");
			}

			if (Util.isEmpty(dto.getDdd()) || Util.isEmpty(dto.getTel())) {
				errors.add("Telefone");
			}

			if (Util.isEmpty(dto.getNoCidade())) {
				errors.add("Cidade");
			}

			if (Util.isEmpty(dto.getSgUf())) {
				errors.add("UF");
			}
			if (Util.isEmpty(errors)) {
				responseXML.setSucesso(true);
			} else {
				String msgErro = null;
				for (String error : errors) {
					if (msgErro == null) {
						msgErro = error;
					} else {
						msgErro += ", " + error;
					}
				}
				responseXML.setMsgError(msgErro);
			}

			PrintWriter writer = response.getWriter();

			writer.print(XMLGen.generateResponse(responseXML));

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortletException(e);
		}
	}

	public void passo2(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
		try {
			
			
			
			OcorrenciaDTO dto = new OcorrenciaDTO();
			configureConverters();
			BeanUtils.populate(dto, request.getParameterMap());
			String token = request.getParameter(TOKEN_CAPTCHA);
			System.out.println("Recebendo a validacao para o token: " + token);
			String[] mensagem = request.getParameterValues("mensagem");

			dto.setMensagem(mensagem[0]);

			List<String> errors = new ArrayList<String>();

			if (Util.isEmpty(dto.getMensagem())) {
				errors.add("Mensagem");
			}

			Boolean isResponseCorrect = Boolean.FALSE;

			String code = request.getParameter("captcha_response");

			try {
				isResponseCorrect = validateCaptcha(token, code, request);
			} catch (Exception e) {
				e.printStackTrace();
				throw new PortletException(e);
			}

			if (!isResponseCorrect) {
				errors.add("Caracteres não foram digitados corretamente");
			}
			
			ResponseXML responseXML = new ResponseXML();
			responseXML.addNode("passo", 2);
			if (Util.isEmpty(errors)) {
				responseXML.setSucesso(true);
				responseXML.addNode("process", true);
			} else {
				String msgErro = null;
				for (String error : errors) {
					if (msgErro == null) {
						msgErro = error;
					} else {
						msgErro += ", " + error;
					}
				}
				responseXML.setMsgError(msgErro);
			}

			PrintWriter writer = response.getWriter();

			writer.print(XMLGen.generateResponse(responseXML));

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortletException(e);
		}
	}

	private void configureConverters() {
		Converter stringConverter = new Converter() {

			@Override
			public Object convert(Class clazz, Object value) {
				if (Util.isEmpty(value)) {
					return null;
				}
				return value.toString();
			}
		};
		ConvertUtils.register(stringConverter, String.class);

	}
	
	private static enum NaturezaFormulario{
		NEGOCIACAO(1,2,2,5,"Solicitação","NEGOCIAÇÃO DE DÍVIDAS");
		
		
		final int tipoDocumentoCPF;
		final int tipoDocumentoCNPJ;
		final int origem;
		final int natureza;
		final String nomeCategoria;
		final String nomeNatureza;
		
		NaturezaFormulario(int valorTipoDocumentoCPF,int valorTipoDocumentoCNPJ, int valorOrigem,int valorNarureza,String nome_Natureza, String nome_Categoria ){
			tipoDocumentoCPF =valorTipoDocumentoCPF;
			tipoDocumentoCNPJ = valorTipoDocumentoCNPJ;
			origem =valorOrigem;
			natureza = valorNarureza;
			nomeNatureza = nome_Natureza;
			nomeCategoria = nome_Categoria;
		}
		
	
	}
	
	private String getNomeNatureza(int natureza) {
		String nome = null;
		
		if(natureza == NaturezaFormulario.NEGOCIACAO.natureza){
			nome = NaturezaFormulario.NEGOCIACAO.nomeNatureza;
			
		}
		
		
		return nome;
	}
	
	@ProcessAction(name=GRAVAR_NEGOCIACAO)
	public void gravaNegociacao (ActionRequest request, ActionResponse response)throws PortletException, IOException{
		try{
			
			
			OcorrenciaDTO dto = new OcorrenciaDTO();

			BeanUtils.populate(dto, request.getParameterMap());
			
			//remove Máscara do cpf
			String cpfCnpj = Util.onlyNumbers(dto.getCpfCnpj());
			
					
			String manifesto = "CATEGORIA: "+NaturezaFormulario.NEGOCIACAO.nomeCategoria+"\nCONTATO: ("+dto.getDdd()+") "+dto.getTel()+"\nNOME DA MÃE: "+dto.getNoMae()+"\nDATA NASCIMENTO: "+dto.getDtNascimento()+"\nAGÊNCIA ATENDIMENTO: "+dto.getAgencia()
			+"\nEMAIL: "+dto.getEmail()+"\nCIDADE: "+dto.getNoCidade()+"\nUF: "+dto.getSgUf()+"\nMENSAGEM: "+dto.getMensagem();
			
			
			System.out.println("MANIFESTO: "+manifesto);
			
			
			int tipoDocumento;
			
			if(cpfCnpj.length() == 11) {
				tipoDocumento =  NaturezaFormulario.NEGOCIACAO.tipoDocumentoCPF;
			}else {
				tipoDocumento =  NaturezaFormulario.NEGOCIACAO.tipoDocumentoCNPJ;
			}
			
					
			JsonObject obj = new JsonObject();
			
			
			obj.addProperty("nomeSolicitante",dto.getNome());
			obj.addProperty( "cpfCnpj",cpfCnpj);
			obj.addProperty("tipoDocumento", tipoDocumento);
			obj.addProperty("origem",NaturezaFormulario.NEGOCIACAO.origem);
			obj.addProperty("natureza",NaturezaFormulario.NEGOCIACAO.natureza);
			obj.addProperty("manifesto",manifesto);
			
			
			
			String url = request.getPreferences().getValue(URL_REGISTRA_NEGOCIACAO, null);
			
			String usuario = System.getProperty(SIOUV_USUARIO_SERVICO); //request.getPreferences().getValue(USUARIO_SERVICO, null);
			String senha = System.getProperty(SIOUV_SENHA_SERVICO); //request.getPreferences().getValue(SENHA_SERVICO, null);
			
			
			String resposta = HttpUtil.sendPostRequest(url, obj.toString(), usuario, senha);
		
			JsonObject retorno = new JsonParser().parse(resposta).getAsJsonObject();
			
			JsonElement nuOcorrencia = retorno.get("nuOcorrencia");

			
//			if(!Util.isEmpty(nuOcorrencia)){
//				MSG_SUCESSO_SIVOU.equalsIgnoreCase(nuOcorrencia.getAsString());
//				response.setRenderParameter(MENSAGEM, "Sua negociação foi enviada com sucesso");
//			}
			
			
			//Provisório até serviço rest estiver ok
			
			response.setRenderParameter(PROTOCOLO, nuOcorrencia.getAsString());
			response.setRenderParameter(NOME,dto.getNome());
			response.setRenderParameter(NOME_CATEGORIA, NaturezaFormulario.NEGOCIACAO.nomeCategoria);
			response.setRenderParameter(NOME_NATUREZA, NaturezaFormulario.NEGOCIACAO.nomeNatureza);
			response.setRenderParameter(NOME_CIDADE, dto.getNoCidade());
			response.setRenderParameter(SIGLA_UF,dto.getSgUf());
			response.setRenderParameter(AGENCIA, dto.getAgencia());
			response.setRenderParameter(MENSAGEM, dto.getMensagem());
			response.setRenderParameter(NOME_DA_MAE, dto.getNoMae());
			response.setRenderParameter(DATA_NASCIMENTO, dto.getDtNascimento());
					
			
		}catch(Exception e){
			e.printStackTrace();
			//response.setRenderParameter(ERRO, ERRO);
		}
	}
	
public void  download(ResourceRequest request, ResourceResponse response)throws PortletException, IOException{
		
		
		try {
			System.out.println("Entrou Download");
			
			String protocolo = request.getParameter("protocolo");
			protocolo = CriptpUtil.decriptografar(protocolo);
			
			OcorrenciaDTO dto = new OcorrenciaDTO();

			BeanUtils.populate(dto, request.getParameterMap());
			

			
			
			System.out.println("DEBUGXX:Nome : "+dto.getNome()+"\n"+"Nome da Mãpae:"+dto.getNoMae()+"\nNascimento: "+dto.getDtNascimento()
					+"\nCategoria : "+dto.getNoCategoria()+"\n"+"Natureza : "+dto.getNoNatureza()+"\n"+
					"Cidade : "+dto.getNoCidade()+"\n"+"UF:"+dto.getSgUf()+"\n"+"Aagência de Atendimento : "+dto.getAgencia()+"\n"
					+"Mensagem : "+dto.getMensagem());
			
			
//			String url = request.getPreferences().getValue(URL_BUSCA_POR_PROTOCOLO, null);
//			String usuario = request.getPreferences().getValue(USUARIO_SERVICO, null);
//			String senha = request.getPreferences().getValue(SENHA_SERVICO, null);
//			
//				JsonObject obj = new JsonObject();
//				obj.addProperty("protocolo", protocolo);
//				obj.addProperty("anonimo", "S");
//				
//			String resposta = HttpUtil.sendPostRequest(url, obj.toString(), usuario, senha);
				
				
			
			
			InputStream report = request.getPortletSession().getPortletContext().getResourceAsStream("/pdf/recibo.jasper");
			InputStream logo = request.getPortletSession().getPortletContext().getResourceAsStream("/images/logo_caixa.gif");
			
			ArrayList<String> lista = new ArrayList<String>();
			lista.add("empty");
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);
			Map<String,Object> parametros = new HashMap<String, Object>();
			parametros.put("logo", logo);
			
			;
			parametros.put("titulo","PROPOSTA DE "+ dto.getNoCategoria().toUpperCase()+" NÚMERO: "+protocolo);

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			parametros.put("data", sdf.format(new Date()));
			
			
			parametros.put("conteudo", "Nome : "+dto.getNome()+"\n"+"Nome da Mãe:"+dto.getNoMae()+"\nNascimento: "+dto.getDtNascimento()+"\n"
					+"Categoria : "+dto.getNoCategoria()+"\n"+"Natureza : "+dto.getNoNatureza()+"\n"+
					"Cidade : "+dto.getNoCidade()+"\n"+"UF:"+dto.getSgUf()+"\n"+"Agência de Atendimento : "+dto.getAgencia()+"\n"
					+"Mensagem : "+dto.getMensagem());
			
			JasperPrint fillReport = JasperFillManager.fillReport(report, parametros,ds);
			byte[] conteudo = JasperExportManager.exportReportToPdf(fillReport);
			response.setContentType("application/octet-stream");
			response.addProperty("Content-Disposition", "attachment; filename=\"recibo.pdf\"");
			response.getPortletOutputStream().write(conteudo);
		
		} catch (Exception e) {
			
			throw new PortletException(e);
		}
	}
	
	private boolean validateCaptcha(String token, String code, ResourceRequest request) throws IOException {

		String validateURL = request.getPreferences().getValue(VALIDATE_CAPTCHA_URL, null);

		URL url = new URL(validateURL + "?token=" + token + "&palavra=" + code);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != 200) {
			System.out.println("ERRO AO SE CONECTAR COM O CAPTCHA");
			return false;
		}

		try {
			JSONObject object = JSONObject.parse(conn.getInputStream());
			return (Boolean) object.get("Validated");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private String getTokenCaptcha(String urlToken) throws IOException {

		URL url = new URL(urlToken);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != 200) {
			return "captcharServiceErro:" + conn.getResponseCode();
		}

		try {
			JSONObject object = JSONObject.parse(conn.getInputStream());
			String token = (String) object.get("Token");
			return token;
		} catch (Exception e) {
			return "ErroParserJson:" + e.getMessage();
		}
	}


	public void  imageCaptcha(ResourceRequest request, ResourceResponse response)throws PortletException, IOException{
		
		String token = request.getParameter(TOKEN_CAPTCHA);
		
		try {
			
			System.out.println("Imagem para o token:  "+token);
			String imgUrl = request.getPreferences().getValue(IMG_CAPTCHA_URL, null);
			URL url = new URL(imgUrl+"?token="+token);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if(conn.getResponseCode()!=200){
				throw new RuntimeException("Erro ao recuperar imagem captcha"+conn.getResponseCode());
			}
			
			
			JSONObject resultado = JSONObject.parse(conn.getInputStream());
			
			
			JSONArray JbyteArray = (JSONArray) resultado.get("ImgTobyte");
			byte[] conteudo = new byte[JbyteArray.size()];
			for (int i=0; i < JbyteArray.size();i++) {
				Long value = (Long) JbyteArray.get(i);
				conteudo[i]=value.byteValue();
			}
			
			
			response.setContentType("image/jpeg");
			response.getPortletOutputStream().write(conteudo);
		} catch (Exception e) {
			System.out.println("erro ao recuperar a imagem do captcha: "+token);
			e.printStackTrace();
			//throw new PortletException(e);
			response.getWriter().write(e.getMessage());
		}
	}

	public void  refreshCaptcha(ResourceRequest request, ResourceResponse response)throws PortletException, IOException{
			try{
				
				String urlToken = request.getPreferences().getValue(TOKEN_CAPTCHA_URL, null);
				String tokenCaptcha = getTokenCaptcha(urlToken);
				
				ResourceURL imgUrl = response.createResourceURL();
				imgUrl.setParameter(TOKEN_CAPTCHA, tokenCaptcha);
				imgUrl.setResourceID("imageCaptcha");
				
				ResourceURL audioURL = response.createResourceURL();
				audioURL.setParameter(TOKEN_CAPTCHA, tokenCaptcha);
				audioURL.setResourceID("audioCaptcha");
				
				JSONObject retorno = new JSONObject();
				retorno.put(TOKEN_CAPTCHA, tokenCaptcha);
				retorno.put(IMG_CAPTCHA_URL,imgUrl.toString());
				retorno.put(SOUND_CAPTCHA_URL,audioURL.toString());
				response.getWriter().write(retorno.serialize());
				
			}catch(Exception e){
				response.getWriter().write(e.getMessage());
			}
	}


	public void  audioCaptcha(ResourceRequest request, ResourceResponse response)throws PortletException, IOException{
		
		String token = request.getParameter(TOKEN_CAPTCHA);
		
		try {
			
			System.out.println("Audio para o token:  "+token);
			String urlSound = request.getPreferences().getValue(SOUND_CAPTCHA_URL,null);
			URL url = new URL(urlSound+"?token="+token);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if(conn.getResponseCode()!=200){
				throw new RuntimeException("Erro ao recuperar imagem audio code: "+conn.getResponseCode());
			}
			
			JSONObject resultado = JSONObject.parse(conn.getInputStream());
			
			JSONArray JbyteArray = (JSONArray) resultado.get("SoundTobyte");
			byte[] conteudo = new byte[JbyteArray.size()];
			for (int i=0; i < JbyteArray.size();i++) {
				Long value = (Long) JbyteArray.get(i);
				conteudo[i]=value.byteValue();
			}
			
			response.setContentType("audio/mpeg3");
			response.getPortletOutputStream().write(conteudo);
		} catch (Exception e) {
			System.out.println("Erro ao recuperar o audio: "+token);
			e.printStackTrace();
			response.getWriter().write(e.getMessage());
		}
	}

}

