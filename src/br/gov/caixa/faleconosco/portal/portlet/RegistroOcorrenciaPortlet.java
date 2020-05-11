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

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import br.gov.caixa.faleconosco.portal.dto.CategoriaDTO;
import br.gov.caixa.faleconosco.portal.dto.OcorrenciaDTO;
import br.gov.caixa.faleconosco.portal.service.FaleConoscoService;
import br.gov.caixa.faleconosco.portal.util.CriptpUtil;
import br.gov.caixa.faleconosco.portal.util.Util;
import br.gov.caixa.faleconosco.portal.util.XMLGen;
import br.gov.caixa.faleconosco.portal.util.XMLGen.ResponseXML;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class RegistroOcorrenciaPortlet extends javax.portlet.GenericPortlet {
	
	private static final String CATEGORIA_SIOUV_OUTROS_SERVICOS = "21";
	private static final String TOKEN_CAPTCHA = "tokenCaptcha";
	private static final String EXCEPTION = "exception";
	private static final String JSP_ERRO = "/jsp/faleconosco/erro.jsp";
	private static final String OCORRENCIA = "ocorrencia";
	private static final String PROTOCOLO = "protocolo";
	private static final String PROTOCOLOSTR = "protocoloStr";
	private static final String TITULO_PASSO2 = "tituloPasso2";
	private static final String JSP_FORMULARIO = "/jsp/faleconosco/form.jsp";
	private static final String JSP_FORMULARIO_EDIT = "/jsp/faleconosco/edit.jsp";
	private static final String JSP_SUCESSO = "/jsp/faleconosco/sucesso.jsp";
	private static final String JSP_FORMULARIO_RECLAMACAO = "/jsp/faleconosco/formReclamacao.jsp";
	
	private static final String TOKEN_CAPTCHA_URL="tokenCaptchaURL";
	private static final String IMG_CAPTCHA_URL="imgCaptchaURL";
	private static final String SOUND_CAPTCHA_URL="soundCaptchaURL";
	private static final String VALIDATE_CAPTCHA_URL="validateCaptchaURL"; 

	
	private static final String NATUREZA = "natureza";
	
	private static final PortletMode EDITDEFAULTSMODE = new PortletMode("edit_defaults");
	private static final String NOME_NATUREZA = "nomeNatureza";
	
	private static enum NaturezaFormulario{
		SUGESTOES(2,"Sugestões","sugestão",3), 
		ELOGIOS(3,"Elogios","elogio",3), 
		DUVIDAS(48,"Dúvidas","dúvida",3), 
		RECLAMACAO(1,"Reclamações","reclamação",3);	
		
		final int natureza;
		final String titulo;
		final String nome;
		final int tipoOcorrencia;
		
		NaturezaFormulario(int valorNarureza, String tituloForm , String nomeForm,int tpOcorrencia){
			natureza = valorNarureza;
			titulo = tituloForm; 
			nome = nomeForm;
			tipoOcorrencia = tpOcorrencia;
		}
	}
	
	/**
	 * @see javax.portlet.Portlet#init()
	 */
	public void init() throws PortletException{
		super.init();
	}
	
	@ProcessAction(name="savePrefs")
	public void savePrefs(ActionRequest request, ActionResponse response)throws PortletException, IOException{
		PortletPreferences pref = request.getPreferences();
		
		pref.setValue(NATUREZA, request.getParameter(NATUREZA));
		
		pref.setValue(TOKEN_CAPTCHA_URL, request.getParameter(TOKEN_CAPTCHA_URL));
		pref.setValue(IMG_CAPTCHA_URL, request.getParameter(IMG_CAPTCHA_URL));
		pref.setValue(SOUND_CAPTCHA_URL, request.getParameter(SOUND_CAPTCHA_URL));
		pref.setValue(VALIDATE_CAPTCHA_URL, request.getParameter(VALIDATE_CAPTCHA_URL));
		
		
		pref.store();
		response.setPortletMode(PortletMode.VIEW);
	}
	
	
	private String getTokenCaptcha(String urlToken) throws IOException {
		
		URL url = new URL(urlToken);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if(conn.getResponseCode()!=200){
			return "captcharServiceErro:"+conn.getResponseCode();
		}
		
		
		try{
			JSONObject object = JSONObject.parse(conn.getInputStream());
			String token = (String) object.get("Token");
			return token;
		}catch(Exception e){
			return "ErroParserJson:"+e.getMessage();
		}
	}
	
	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
		String resourceID = request.getResourceID();
		
		try {
		 	Method method = this.getClass().getDeclaredMethod(resourceID, ResourceRequest.class, ResourceResponse.class);
		 	method.invoke(this, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortletException(e);
		}
		super.serveResource(request, response);
	}
	
	public void validaManifesto(ResourceRequest request, ResourceResponse response)throws PortletException, IOException{
		try{
			OcorrenciaDTO dto = new OcorrenciaDTO();
			configureConverters();
			BeanUtils.populate(dto, request.getParameterMap());
			
			List<String> errors = new ArrayList<String>();
			
			if(Util.isEmpty(dto.getCategoria())){
				errors.add("Categoria");
			}
			if(Util.isEmpty(dto.getMensagem())){
				errors.add("Mensagem");
			}
			
			
			Boolean isResponseCorrect =Boolean.FALSE;
	           
	        String code = request.getParameter("captcha_response");
	        String token =  request.getParameter(TOKEN_CAPTCHA);  
	        System.out.println("Recebendo a validacao(Manifesto) para o token: "+token);
	        
	        try {
	                isResponseCorrect = validateCaptcha(token,code,request);
	        } catch (Exception e) {
	                 e.printStackTrace();
	                 throw new PortletException(e);
	        }
	            
	        if(!isResponseCorrect){
	            	errors.add("Caracteres não foram digitados corretamente");
	        }
			
			ResponseXML responseXML = new ResponseXML();
			responseXML.addNode("passo", "validaManifesto");
			if(Util.isEmpty(errors)){
				responseXML.setSucesso(true);
				responseXML.addNode("process", true);
			}else{
				String msgErro = null;
				for (String error : errors) {
					if(msgErro ==null){
						msgErro = error;
					}else{
						msgErro+=", "+error;
					}
				}
				responseXML.setMsgError(msgErro);
			}
			
			PrintWriter writer = response.getWriter();
			
			writer.print(XMLGen.generateResponse(responseXML));
			
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new PortletException(e);
		}
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws PortletException
	 * @throws IOException
	 */
	public void buscaReclamacao(ResourceRequest request, ResourceResponse response)throws PortletException, IOException{
		try{
			FaleConoscoService service = new FaleConoscoService();
			String pedidoCPF = request.getParameter("protocoloCPF");
			ResponseXML responseXML = new ResponseXML();
			if(Util.isEmpty(pedidoCPF)){
				responseXML.setMsgError("Número do Pedido ou CPF obrigatório");
			}else{
				OcorrenciaDTO ocorrencia = service.buscaReclamacao(pedidoCPF);
				if(ocorrencia==null){
					responseXML.setMsgError("Nenhum registro encontrado.");
				}else{
					switch(ocorrencia.getNaturezaOcor()){
						case 2:
							responseXML.setMsgError("Apenas reclamações poderão ser reabertas. Para sugestões favor abrir outro pedido.");
							break;
						case 3:
							responseXML.setMsgError("Apenas reclamações poderão ser reabertas. Para reclamações favor abrir outro pedido.");
							break;
						case 48:
							responseXML.setMsgError("Apenas reclamações poderão ser reabertas. Para dúvidas favor abrir outro pedido.");
							break;
						case 4:
							responseXML.setMsgError("Apenas reclamações poderão ser reabertas. Para denúncias favor abrir outro pedido.");
							break;
					}
				}
				
				
				if(Util.isEmpty(responseXML.getMsgError())){
						request.getPortletSession().setAttribute(OCORRENCIA, ocorrencia);
						responseXML.setSucesso(true);
						
						responseXML.addNode("protocolo", ocorrencia.getProtocolo());
						
						responseXML.addNode("nomeCategoria", ocorrencia.getNoCategoria());
						
						Date dataEnvio = ocorrencia.getDataEnvio();
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
						responseXML.addNode("dataEnvio", df.format(dataEnvio));
						String noSituacaoOcorrencia = ocorrencia.getNoSituacaoOcorrencia();
						if(Util.isEmpty(noSituacaoOcorrencia)||"Enviada".equalsIgnoreCase(noSituacaoOcorrencia)){
							responseXML.addNode("situacao", "Em atendimento");
						}else if("cancelada".equalsIgnoreCase(noSituacaoOcorrencia)||"respondida".equalsIgnoreCase(noSituacaoOcorrencia)){
							responseXML.addNode("situacao", "Finalizado");
						}else{
							responseXML.addNode("situacao", noSituacaoOcorrencia);
						}
						
						responseXML.addNode("icRelacionamento", ocorrencia.getIcRelacionamento());
						
						int indexOfSpace = ocorrencia.getNome().indexOf(" ");
						
						String nomeOcorrencia = "";
						
						if(indexOfSpace!=-1){
							nomeOcorrencia = ocorrencia.getNome().substring(0,indexOfSpace);
						}else{
							nomeOcorrencia = ocorrencia.getNome();
						}
						
						responseXML.addNode("nome", nomeOcorrencia);
						responseXML.addNode("categoria",ocorrencia.getCategoria());
				}
				
			}
			
			
			responseXML.addNode("passo", "buscaReclamacao");
			PrintWriter writer = response.getWriter();
			writer.print(XMLGen.generateResponse(responseXML));
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new PortletException(e);
		}
	}
	
	public void passo0(ResourceRequest request, ResourceResponse response)throws PortletException, IOException{
		try {
			OcorrenciaDTO dto = new OcorrenciaDTO();
			
			BeanUtils.populate(dto, request.getParameterMap());
			ResponseXML responseXML = new XMLGen.ResponseXML();
			
			responseXML.addNode("passo", 0);
			
			List<String> errors = new ArrayList<String>();
			
			if(Util.isEmpty(dto.getTipo())){
				errors.add("Tipo Ocorrência");
			}
			
			if(Util.isEmpty(errors)){
				responseXML.setSucesso(true);
			}else{
				String msgErro = null;
				for (String error : errors) {
					if(msgErro ==null){
						msgErro = error;
					}else{
						msgErro+=", "+error;
					}
				}
				responseXML.setMsgError(msgErro);
			}
			
			PrintWriter writer = response.getWriter();
			
			writer.print(XMLGen.generateResponse(responseXML));
			
		}catch(Exception e){
			e.printStackTrace();
			throw new PortletException(e);
		}
	}
	
	public void passo1(ResourceRequest request, ResourceResponse response)throws PortletException, IOException{
		try{
			OcorrenciaDTO dto = new OcorrenciaDTO();
			
			BeanUtils.populate(dto, request.getParameterMap());
			ResponseXML responseXML = new XMLGen.ResponseXML();
			
			responseXML.addNode("passo", 1);
			
			List<String> errors = new ArrayList<String>();
			
			if(Util.isEmpty(dto.getIcRelacionamento())){
				errors.add("Relacionamento Caixa");
			}
			
			boolean correntista = "S".equalsIgnoreCase(dto.getIcRelacionamento());
			boolean anonimo = "A".equalsIgnoreCase(dto.getIcRelacionamento());
			
			
			if(!anonimo){
				if(Util.isEmpty(dto.getNome())){
					errors.add("Nome");
				}
			}
			
			
			if(correntista){
				if(Util.isEmpty(dto.getAgencia())){
					errors.add("Agência");
				}
				if(Util.isEmpty(dto.getConta())){
					errors.add("Conta");
				}
				if(Util.isEmpty(dto.getOperacao())){
					errors.add("Operação");
				}
			}
			
			if(!anonimo){
				if(Util.isEmpty(dto.getCpf())){
					errors.add("CPF");
				}else{
					if(!Util.isCPF(Util.onlyNumbers(dto.getCpf()))){
						errors.add("Número de CPF inválido");
					}
				}
				
				
				
				if(Util.isEmpty(dto.getEmail())){
					errors.add("Email");
				}
				
				if(Util.isEmpty(dto.getDdd())||
						Util.isEmpty(dto.getTel())){
					errors.add("Telefone");
				}
			}
			
			
			if(Util.isEmpty(dto.getNoCidade())){
				errors.add("Cidade");
			}
			
			if(Util.isEmpty(dto.getSgUf())){
				errors.add("UF");
			}
			
			if(Util.isEmpty(errors)){
				responseXML.setSucesso(true);
			}else{
				String msgErro = null;
				for (String error : errors) {
					if(msgErro ==null){
						msgErro = error;
					}else{
						msgErro+=", "+error;
					}
				}
				responseXML.setMsgError(msgErro);
			}
			
			PrintWriter writer = response.getWriter();
			
			writer.print(XMLGen.generateResponse(responseXML));
			
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new PortletException(e);
		}
	}
	
	public void passo2(ResourceRequest request, ResourceResponse response)throws PortletException, IOException{
		try{
			OcorrenciaDTO dto = new OcorrenciaDTO();
			configureConverters();
			BeanUtils.populate(dto, request.getParameterMap());
			String token = request.getParameter(TOKEN_CAPTCHA);
			System.out.println("Recebendo a validacao para o token: "+token);
			String natureza = request.getPreferences().getValue(NATUREZA, Integer.toString(NaturezaFormulario.SUGESTOES.natureza));
			if(natureza.equalsIgnoreCase(Integer.toString(NaturezaFormulario.RECLAMACAO.natureza))){
				
				/* na pagina de reclamacoes existem 2 campos referentes ao categoria e Mensagem
				 * neste caso o parametro destes campos está dobrado, por isso deve sempre capturar o segundo valor do array
				 */
				String[] categoria = request.getParameterValues("categoria");
				String[] mensagem = request.getParameterValues("mensagem");
				
				dto.setCategoria(categoria[1]);
				dto.setMensagem(mensagem[1]);
				
			}
			
			List<String> errors = new ArrayList<String>();
			
			if(Util.isEmpty(dto.getCategoria())){
				errors.add("Categoria");
			}
			if(Util.isEmpty(dto.getMensagem())){
				errors.add("Mensagem");
			}
			
			Boolean isResponseCorrect =Boolean.FALSE;
	           
	           String code = request.getParameter("captcha_response");
	           
	            try {
	                isResponseCorrect = validateCaptcha(token,code,request);
	            } catch (Exception e) {
	                 e.printStackTrace();
	                 throw new PortletException(e);
	            }
	            
	            if(!isResponseCorrect){
	            	errors.add("Caracteres não foram digitados corretamente");
	            }
			
			
			
			ResponseXML responseXML = new ResponseXML();
			responseXML.addNode("passo", 2);
			if(Util.isEmpty(errors)){
				responseXML.setSucesso(true);
				responseXML.addNode("process", true);
			}else{
				String msgErro = null;
				for (String error : errors) {
					if(msgErro ==null){
						msgErro = error;
					}else{
						msgErro+=", "+error;
					}
				}
				responseXML.setMsgError(msgErro);
			}
			
			PrintWriter writer = response.getWriter();
			
			writer.print(XMLGen.generateResponse(responseXML));
			
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new PortletException(e);
		}
	}
	
	
	

	private void configureConverters() {
		Converter stringConverter = new Converter(){
			
			@Override
			public Object convert(Class clazz, Object value) {
				if(Util.isEmpty(value)){
					return null;
				}
				return value.toString();
			}
		}; 
		ConvertUtils.register(stringConverter, String.class);
		
	}
	
	public void  download(ResourceRequest request, ResourceResponse response)throws PortletException, IOException{
		
		
		try {
			InputStream report = request.getPortletSession().getPortletContext().getResourceAsStream("/pdf/recibo.jasper");
			InputStream logo = request.getPortletSession().getPortletContext().getResourceAsStream("/images/logo_caixa.gif");
			String protocolo = request.getParameter("protocolo");
			protocolo = CriptpUtil.decriptografar(protocolo);
			String natureza = request.getPreferences().getValue(NATUREZA, Integer.toString(NaturezaFormulario.SUGESTOES.natureza));
			String nomeNatureza = getNomeNatureza(Integer.valueOf(natureza));
			ArrayList<String> lista = new ArrayList<String>();
			lista.add("empty");
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);
			Map<String,Object> parametros = new HashMap<String, Object>();
			parametros.put("logo", logo);
			parametros.put("titulo", nomeNatureza.toUpperCase()+" NÚMERO: "+protocolo);
			
			FaleConoscoService service = new FaleConoscoService();
			
			OcorrenciaDTO ocorrencia = service.recuperar(protocolo);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			parametros.put("data", sdf.format(ocorrencia.getDhOcorrencia()));
			
			
			parametros.put("conteudo", "Nome: "+ocorrencia.getNome()+"\n"+ocorrencia.getMensagem());
			
			JasperPrint fillReport = JasperFillManager.fillReport(report, parametros,ds);
			byte[] conteudo = JasperExportManager.exportReportToPdf(fillReport);
			response.setContentType("application/octet-stream");
			response.addProperty("Content-Disposition", "attachment; filename=\"recibo.pdf\"");
			response.getPortletOutputStream().write(conteudo);
		} catch (Exception e) {
			throw new PortletException(e);
		}
	}
	
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		//Verifica o preenchimento dos servicos de captcha
		// sem esses valores o portlet nao funciona
		String urlToken = request.getPreferences().getValue(TOKEN_CAPTCHA_URL, null);
		String urlImg = request.getPreferences().getValue(IMG_CAPTCHA_URL, null);
		String soundUrl = request.getPreferences().getValue(SOUND_CAPTCHA_URL, null);
		String validateUrl = request.getPreferences().getValue(VALIDATE_CAPTCHA_URL, null);
		if(urlToken==null || urlImg == null || soundUrl==null || validateUrl==null){
			response.getWriter().write("Entre no modo de edição defina os serviços de captcha");
			return;
		}
		String natureza = request.getPreferences().getValue(NATUREZA, Integer.toString(NaturezaFormulario.SUGESTOES.natureza));
		String exception = (String) request.getParameter(EXCEPTION);
		if("true".equalsIgnoreCase(exception)){
			request.setAttribute("nome", getNomeNatureza(Integer.valueOf(natureza)));
			PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(JSP_ERRO);
			rd.include(request,response);
			return;
		}
		
		String paramProtocolo = request.getParameter(PROTOCOLO);
		Integer protocolo = null;
		if(paramProtocolo!=null){
			protocolo = Integer.valueOf(paramProtocolo);
		}
		
		
		response.setContentType(request.getResponseContentType());
		FaleConoscoService service = new FaleConoscoService();
		
		try{
			if(Util.isEmpty(protocolo)){
				String tokenCaptcha = getTokenCaptcha(urlToken);
				request.setAttribute(TOKEN_CAPTCHA, tokenCaptcha);
				
				PortletRequestDispatcher rd = null;
					if(natureza.equalsIgnoreCase(Integer.toString(NaturezaFormulario.RECLAMACAO.natureza))){
						rd = getPortletContext().getRequestDispatcher(JSP_FORMULARIO_RECLAMACAO);
					}else{
						rd = getPortletContext().getRequestDispatcher(JSP_FORMULARIO);
					}
				
				String tituloFormPasso2 = getTituloForm(Integer.valueOf(natureza));
				String nomeNatureza = getNomeNatureza(Integer.valueOf(natureza));
				request.setAttribute(NOME_NATUREZA, nomeNatureza);
				request.setAttribute(TITULO_PASSO2, tituloFormPasso2);
				request.setAttribute(NATUREZA, natureza);
				
				
				List<CategoriaDTO>categorias;
				//if(NaturezaFormulario.DEUNCIAS.natureza == Integer.valueOf(natureza)){
//				if(natureza.equalsIgnoreCase(Integer.toString(NaturezaFormulario.DEUNCIAS.natureza))){
//					categorias = service.listarCategoriasParaDenuncias();
//				}else{
//					categorias = service.listarCategorias();
//				}
				
				categorias = service.listarCategorias();
				
				request.setAttribute("categorias", categorias);
				
				rd.include(request,response);
			}else{
				PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(JSP_SUCESSO);
				request.setAttribute(PROTOCOLO, CriptpUtil.criptografar(Integer.toString(protocolo)));
				request.setAttribute(PROTOCOLOSTR, protocolo);
				request.setAttribute("nome", getNomeNatureza(Integer.valueOf(natureza)));
				rd.include(request,response);
			}
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("nome", getNomeNatureza(Integer.valueOf(natureza)) + "<!--"+ e.getMessage() + e.toString() +" -->");
			PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(JSP_ERRO);
			rd.include(request,response);
			//throw new PortletException(e);
		}
	}
	
	private String getTituloForm(int tipo) {
		String titulo = null;
		
		if(tipo == NaturezaFormulario.SUGESTOES.natureza){
			titulo = NaturezaFormulario.SUGESTOES.titulo;
			
		}else if(tipo == NaturezaFormulario.ELOGIOS.natureza){
			titulo = NaturezaFormulario.ELOGIOS.titulo;
		
		}else if(tipo == NaturezaFormulario.DUVIDAS.natureza){
			titulo = NaturezaFormulario.DUVIDAS.titulo;
			
		}else if(tipo == NaturezaFormulario.RECLAMACAO.natureza){
			titulo = NaturezaFormulario.RECLAMACAO.titulo;
		}
		
		
		return titulo;
	}
	
	private int getTipoOCorrecia(int tipo) {
		int tipoOcorrencia = 0;
		
		if(tipo == NaturezaFormulario.SUGESTOES.natureza){
			tipoOcorrencia = NaturezaFormulario.SUGESTOES.tipoOcorrencia;
			
		}else if(tipo == NaturezaFormulario.ELOGIOS.natureza){
			tipoOcorrencia = NaturezaFormulario.ELOGIOS.tipoOcorrencia;
			
		}else if(tipo == NaturezaFormulario.DUVIDAS.natureza){
			tipoOcorrencia = NaturezaFormulario.DUVIDAS.tipoOcorrencia;
			
		}else if(tipo == NaturezaFormulario.RECLAMACAO.natureza){
			tipoOcorrencia = NaturezaFormulario.RECLAMACAO.tipoOcorrencia;
		}
		
		
		return tipoOcorrencia;
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
	
	private boolean validateCaptcha(String token, String code, ResourceRequest request) throws IOException {
		
		String validateURL = request.getPreferences().getValue(VALIDATE_CAPTCHA_URL, null);
		
		URL url = new URL(validateURL+"?token="+token+"&palavra="+code);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if(conn.getResponseCode()!=200){
			System.out.println("ERRO AO SE CONECTAR COM O CAPTCHA");
			return false;
		}
		
		try{
			JSONObject object = JSONObject.parse(conn.getInputStream());
			return  (Boolean) object.get("Validated");
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
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
	
	@ProcessAction(name="gravar")
	public void gravar (ActionRequest request, ActionResponse response)throws PortletException, IOException{
		try{
			OcorrenciaDTO dto = new OcorrenciaDTO();
			BeanUtils.populate(dto, request.getParameterMap());
			
			
			
			FaleConoscoService service = new FaleConoscoService();
			
			String natureza = request.getPreferences().getValue(NATUREZA, Integer.toString(NaturezaFormulario.SUGESTOES.natureza));
			if(natureza.equalsIgnoreCase(Integer.toString(NaturezaFormulario.RECLAMACAO.natureza))){
				
				/* na pagina de reclamacoes existem 2 campos referentes ao categoria e Mensagem
				 * neste caso o parametro destes campos está dobrado, por isso deve sempre capturar o segundo valor do array
				 */
				String[] categoria = request.getParameterValues("categoria");
				String[] mensagem = request.getParameterValues("mensagem");
				
				dto.setCategoria(categoria[1]);
				dto.setMensagem(mensagem[1]);
				
			}
			
			String tipo = request.getPreferences().getValue(NATUREZA, Integer.toString(NaturezaFormulario.SUGESTOES.natureza));
			
			
			
			
			dto.setNaturezaOcor(Integer.valueOf(tipo));
			dto.setTipo(Integer.valueOf(getTipoOCorrecia(Integer.valueOf(tipo))));
			
			Integer protocolo = service.gravaFormulario(dto);
			//request.getPortletSession().setAttribute(PROTOCOLO, protocolo);
			response.setRenderParameter(PROTOCOLO, protocolo.toString());
			
		}catch(Exception e){
			e.printStackTrace();
			response.setRenderParameter(EXCEPTION, "true");
		}
		
		
	}
	
	private String getNomeNatureza(int natureza) {
		String nome;
		
		if(natureza == NaturezaFormulario.SUGESTOES.natureza){
			nome = NaturezaFormulario.SUGESTOES.nome;
			
		}else if(natureza == NaturezaFormulario.ELOGIOS.natureza){
			nome = NaturezaFormulario.ELOGIOS.nome;
			
		}else if(natureza == NaturezaFormulario.DUVIDAS.natureza){
			nome = NaturezaFormulario.DUVIDAS.nome;
			
		}else {
			nome = NaturezaFormulario.RECLAMACAO.nome;
		}
		
		
		return nome;
	}

	public void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		response.setContentType(request.getResponseContentType());
		String tipo = request.getPreferences().getValue(NATUREZA, Integer.toString(NaturezaFormulario.SUGESTOES.natureza));

		String tokenUrl = request.getPreferences().getValue(TOKEN_CAPTCHA_URL,null);
		String imgUrl = request.getPreferences().getValue(IMG_CAPTCHA_URL, null);
		String audioUrl = request.getPreferences().getValue(SOUND_CAPTCHA_URL, null);
		String validateUrl = request.getPreferences().getValue(VALIDATE_CAPTCHA_URL, null);
		
		request.setAttribute(NATUREZA, tipo);

		request.setAttribute(TOKEN_CAPTCHA_URL, tokenUrl);
		request.setAttribute(IMG_CAPTCHA_URL, imgUrl);
		request.setAttribute(SOUND_CAPTCHA_URL, audioUrl);
		request.setAttribute(VALIDATE_CAPTCHA_URL, validateUrl);
		
		PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(JSP_FORMULARIO_EDIT);
		rd.include(request,response);
	}
}
