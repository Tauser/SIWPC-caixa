package br.gov.caixa.faleconosco.portal.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Date;

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
import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ws.portletcontainer.portlet.PortletUtils;

import br.gov.caixa.faleconosco.portal.dto.OcorrenciaDTO;
import br.gov.caixa.faleconosco.portal.util.HttpUtil;
import br.gov.caixa.faleconosco.portal.util.Util;


public class PesquisaSatisfacaoPortlet extends GenericPortlet{

	
	private static final String NU_MOVIMENTACAO = "nuMovimentacao";
	private static final String MENSAGEM = "mensagem";
	private static final String MSG_SUCESSO_SIVOU = "Registro realizado com sucesso";
	private static final String ANONIMO = "anonimo";
	private static final String ERRO = "erro";
	private static final String ABRIR_AVALIACAO = "abrirAvaliacao";
	private static final String DESTINO = "destino";
	private static final String CPF = "cpf";
	private static final String TIPO_BUSCA = "tipoBusca";
	private static final String PROTOCOLO = "protocolo";
	private static final String URL_BUSCA_POR_CPF = "servico_busca_por_cpf";
	private static final String URL_BUSCA_POR_PROTOCOLO = "servico_busca_por_protocolo";
	private static final String URL_REGISTRA_AVALIACAO = "servico_registra_avaliacao";
	private static final String USUARIO_SERVICO = "usuario_servico";
	private static final String SENHA_SERVICO = "senha_servico";

	private static final String SIOUV_USUARIO_SERVICO = "siouv_usuario_servico";
	private static final String SIOUV_SENHA_SERVICO = "siouv_senha_servico";
	private static final String QT_DIAS_BUSCA = "qt_dias_busca";
	private static final String JSP_ERRO = "/jsp/satisfacao/erro.jsp";
	private static final String JSP_FORMULARIO_EDIT = "/jsp/satisfacao/edit.jsp";
	private static final String GRAVAR_AVALIACAO = "gravarAvaliacao";
	private static final PortletMode EDITDEFAULTSMODE = new PortletMode("edit_defaults");
	
	@Override
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		HttpServletRequest httpRequest = PortletUtils.getHttpServletRequest(request);
		
		String protocolo = getParameter(PROTOCOLO,httpRequest,request); //httpRequest.getParameter(PROTOCOLO);
		String nuMovimentacao = getParameter(NU_MOVIMENTACAO, httpRequest, request);
		String tipoBusca = getParameter(TIPO_BUSCA,httpRequest,request);
		String destino = getParameter(DESTINO,httpRequest,request);
		String cpf = getParameter(CPF,httpRequest,request);
		String erro = getParameter(ERRO,httpRequest,request);
		String anonimo = getParameter(ANONIMO,httpRequest,request);
		String mensagem = getParameter(MENSAGEM,httpRequest,request);
		
		PortletRequestDispatcher rd;
		
		System.out.println("POSSUI ERRO: ("+erro+"): "+!Util.isEmpty(erro));
		
		
		/*
		Enumeration<String> httpNames = httpRequest.getParameterNames();
		System.out.println(":::::::::::::::::::::PARAMETROS http :::::::::::::");
		while(httpNames.hasMoreElements()) {
			String name = httpNames.nextElement();
			System.out.println("ATRIBUTO/VALOR ");
			System.out.println(name+"//"+httpRequest.getParameter(name));
		}
		
		
		Enumeration<String> names = request.getParameterNames();
		System.out.println(":::::::::::::::::::::PARAMETROS RENDER:::::::::::::");
		while(names.hasMoreElements()) {
			String name = names.nextElement();
			System.out.println("ATRIBUTO/VALOR ");
			System.out.println(name+"//"+request.getParameter(name));
		}
		*/
		
		if(!Util.isEmpty(erro)){
			rd = getPortletContext().getRequestDispatcher(JSP_ERRO);
			rd.include(request,response);
			return;
		}
		
		
		
		
		
		if(ABRIR_AVALIACAO.equalsIgnoreCase(destino)){
			System.out.println("ABRINDO AVALIACAO: protocolo"+protocolo);
			rd = getPortletContext().getRequestDispatcher("/jsp/satisfacao/avaliacao.jsp");
		
			String url = request.getPreferences().getValue(URL_BUSCA_POR_PROTOCOLO, null);
			String usuario = System.getProperty(SIOUV_USUARIO_SERVICO); //request.getPreferences().getValue(USUARIO_SERVICO, null);
			String senha = System.getProperty(SIOUV_SENHA_SERVICO); //request.getPreferences().getValue(SENHA_SERVICO, null);
			try{
				JsonObject obj = new JsonObject();
				
				obj.addProperty("protocolo", protocolo);
				//obj.addProperty(NU_MOVIMENTACAO, nuMovimentacao);
				obj.addProperty(ANONIMO, anonimo);
				
				String resultado = HttpUtil.sendPostRequest(url, obj.toString(), usuario, senha);
				
				JsonObject retorno = new JsonParser().
						parse(resultado).
						getAsJsonObject();
				
				System.out.println("RETORNO DA PESQUISA POR PROTOCOLO: "+retorno.toString());
				
				JsonArray ocorrencias = retorno.getAsJsonArray("ocorrencias");
				
				OcorrenciaDTO ocorrencia = recuperaOcorrenciaPorMovimentacao(ocorrencias,nuMovimentacao);
				
				request.setAttribute("ocorrencia", ocorrencia);
				request.setAttribute(NU_MOVIMENTACAO, nuMovimentacao);
				request.setAttribute("retorno", retorno);
				
			}catch(Exception e){
				e.printStackTrace();
				
				rd = getPortletContext().getRequestDispatcher(JSP_ERRO);
			}
			
		}else{
			rd = getPortletContext().getRequestDispatcher("/jsp/satisfacao/pesquisa.jsp");
		}
		
		request.setAttribute(TIPO_BUSCA, tipoBusca);
		if(!Util.isEmpty(cpf)){
			request.setAttribute(CPF, cpf);
			
		}
		System.out.println("MENSAGEM:    "+mensagem);
		if(!Util.isEmpty(mensagem)){
			request.setAttribute(MENSAGEM, mensagem);
		}
		
		rd.include(request,response);
	}
	
	private OcorrenciaDTO recuperaOcorrenciaPorMovimentacao(JsonArray ocorrencias, String nuMovimentacao) {
		
		
		for (JsonElement obj : ocorrencias) {
			OcorrenciaDTO ocorrenciaOBJ = mapOcorrencia((JsonObject) obj);
			if(nuMovimentacao
					.equals(ocorrenciaOBJ
					.getNuMovimentacao())) {
				
				return ocorrenciaOBJ;
			}
		}
		
		return null;
		
		
	}

	private String getParameter(String name, HttpServletRequest httpRequest, RenderRequest request) {
		String value = httpRequest.getParameter(name);
		if(value==null) {
			value = request.getParameter(name);
			if(value==null) {
				System.out.println("parametro: "+name+" recuperado render: "+value);
			}else {
				System.out.println("parametro: "+name+" nulo");
			}
		}else {
			System.out.println("parametro: "+name+" recuperado http: "+value);
		}
		return value;
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
	
	public void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		response.setContentType(request.getResponseContentType());
		
		
		String usuarioServico = "DESTATIVADO utilize a custom property: " +SIOUV_USUARIO_SERVICO; //request.getPreferences().getValue(USUARIO_SERVICO,null);
		String senhaServico = "DESTATIVADO utilize a custom property: " + SIOUV_SENHA_SERVICO; //request.getPreferences().getValue(SENHA_SERVICO,null);
		String qtDiasBusca = request.getPreferences().getValue(QT_DIAS_BUSCA,null);
		
		String urlBuscaPorCPF = request.getPreferences().getValue(URL_BUSCA_POR_CPF,null);
		String urlBuscaProtocolo = request.getPreferences().getValue(URL_BUSCA_POR_PROTOCOLO,null);
		String urlRegistraAvaliacao = request.getPreferences().getValue(URL_REGISTRA_AVALIACAO,null);
		
		request.setAttribute(USUARIO_SERVICO, usuarioServico);
		request.setAttribute(SENHA_SERVICO, senhaServico);
		request.setAttribute(QT_DIAS_BUSCA, qtDiasBusca);
		request.setAttribute(URL_BUSCA_POR_CPF, urlBuscaPorCPF);
		request.setAttribute(URL_BUSCA_POR_PROTOCOLO, urlBuscaProtocolo);
		request.setAttribute(URL_REGISTRA_AVALIACAO, urlRegistraAvaliacao);
		
		PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(JSP_FORMULARIO_EDIT);
		rd.include(request,response);
	}
	
	@ProcessAction(name="savePrefs")
	public void savePrefs(ActionRequest request, ActionResponse response)throws PortletException, IOException{
		PortletPreferences pref = request.getPreferences();
		
		//pref.setValue(USUARIO_SERVICO, request.getParameter(USUARIO_SERVICO));
		//pref.setValue(SENHA_SERVICO, request.getParameter(SENHA_SERVICO));
		pref.setValue(QT_DIAS_BUSCA, request.getParameter(QT_DIAS_BUSCA));
		pref.setValue(URL_BUSCA_POR_CPF, request.getParameter(URL_BUSCA_POR_CPF));
		pref.setValue(URL_BUSCA_POR_PROTOCOLO, request.getParameter(URL_BUSCA_POR_PROTOCOLO));
		pref.setValue(URL_REGISTRA_AVALIACAO, request.getParameter(URL_REGISTRA_AVALIACAO));
		
		
		pref.store();
		response.setPortletMode(PortletMode.VIEW);
	}
	
	
	private OcorrenciaDTO mapOcorrencia(JsonObject obj) {
		OcorrenciaDTO ocorrencia = new OcorrenciaDTO();
		JsonElement protocolo = obj.get("numeroOcorrencia");
		//JsonElement noNatureza = obj.get("noNatura");
		JsonElement noNatureza = obj.get("noNatureza");
		JsonElement noTipoOrigem = obj.get("noTipoOrigem");
		JsonElement deCategoria = obj.get("noAssunto");
		JsonElement dhOcorrencia = obj.get("dhOcorrencia");
		JsonElement noSituacaoOcorrencia = obj.get("status");
		JsonElement numeroFase = obj.get("numeroFase");
		JsonElement numeroTipoOcorrencia = obj.get("numeroTipoOcorrencia");
		JsonElement nuMovimentacao = obj.get("nuMovimentacao");
		
		
		ocorrencia.setProtocolo((!protocolo.isJsonNull())?protocolo.getAsLong():null);
		ocorrencia.setNoNatureza((!noNatureza.isJsonNull())?noNatureza.getAsString():null);
		ocorrencia.setOrigem((!noTipoOrigem.isJsonNull())?noTipoOrigem.getAsString():null);
		ocorrencia.setNoCategoria((!deCategoria.isJsonNull())?deCategoria.getAsString():null);
		ocorrencia.setDhOcorenciaStr((!dhOcorrencia.isJsonNull())?dhOcorrencia.getAsString():null);
		ocorrencia.setNoSituacaoOcorrencia((!noSituacaoOcorrencia.isJsonNull())?noSituacaoOcorrencia.getAsString():null);
		ocorrencia.setNumeroFase((!numeroFase.isJsonNull())?numeroFase.getAsString():null);
		ocorrencia.setNumeroTipoOcorrencia((!numeroTipoOcorrencia.isJsonNull())?numeroTipoOcorrencia.getAsString():null);
		ocorrencia.setNuMovimentacao((!nuMovimentacao.isJsonNull())?nuMovimentacao.getAsString():null);
		
		return ocorrencia;
	}
	
	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
		String resourceID = request.getResourceID();
		System.out.println("solicitado resourceID: "+resourceID);
		try {
		 	Method method = this.getClass().getDeclaredMethod(resourceID, ResourceRequest.class, ResourceResponse.class);
		 	method.invoke(this, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortletException(e);
		}
		super.serveResource(request, response);
	}
	
	
	@ProcessAction(name=ABRIR_AVALIACAO)
	public void abrirAvaliacao (ActionRequest request, ActionResponse response)throws PortletException, IOException{
		try{
			
			response.removePublicRenderParameter(MENSAGEM);
			String tipoBusca = request.getParameter(TIPO_BUSCA);
			String protocolo = request.getParameter(PROTOCOLO);
			String cpf = request.getParameter(CPF);
			String anonimo = request.getParameter(ANONIMO);
			String nuMovimentacao = request.getParameter(NU_MOVIMENTACAO);
			
			response.setRenderParameter(NU_MOVIMENTACAO, nuMovimentacao);
			response.setRenderParameter(PROTOCOLO, protocolo);
			response.setRenderParameter(TIPO_BUSCA, tipoBusca);
			response.setRenderParameter(CPF, cpf);
			response.setRenderParameter(ANONIMO, anonimo);
			
			response.setRenderParameter(DESTINO,ABRIR_AVALIACAO);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new PortletException(e);
		}
	}
	
	
	@ProcessAction(name=GRAVAR_AVALIACAO)
	public void gravaAvaliacao (ActionRequest request, ActionResponse response)throws PortletException, IOException{
		try{
			
			String tipoBusca = request.getParameter(TIPO_BUSCA);
			String protocolo = request.getParameter(PROTOCOLO);
			String cpf = request.getParameter(CPF);
			String erro = request.getParameter(ERRO);
			
			if(!Util.isEmpty(erro)){
				
			}
			
			
			String numeroOcorrencia = request.getParameter("numeroOcorrencia");
			String numeroTipoOcorrencia = request.getParameter("numeroTipoOcorrencia");
			String numeroFase = request.getParameter("numeroFase");
			String nuMovimentacao = request.getParameter(NU_MOVIMENTACAO);
			
			String dtPesquisaSatisfacao = Util.formataData(new Date(System.currentTimeMillis()));
			
			String nuNotaAvaliacaoOuvidoria = request.getParameter("nuNotaAvaliacaoOuvidoria");
			String nuNotaAvaliacaoOcorrencia = request.getParameter("nuNotaAvaliacaoOcorrencia");
			String deObsroPesquisaSatisfacao = request.getParameter("deObsroPesquisaSatisfacao");
			
			JsonObject obj = new JsonObject();
			
			obj.addProperty("numeroOcorrencia",numeroOcorrencia);
			obj.addProperty( "numeroTipoOcorrencia",numeroTipoOcorrencia);
			obj.addProperty("numeroFase",numeroFase);
			obj.addProperty("dtPesquisaSatisfacao",dtPesquisaSatisfacao);
			obj.addProperty("nuNotaAvaliacaoOuvidoria",nuNotaAvaliacaoOuvidoria);
			obj.addProperty("nuNotaAvaliacaoOcorrencia",nuNotaAvaliacaoOcorrencia);
			obj.addProperty("deObsroPesquisaSatisfacao",deObsroPesquisaSatisfacao);
			obj.addProperty(NU_MOVIMENTACAO, nuMovimentacao);
			
			
			
			
			System.out.println("Realizando avaliacao : protocolo: "+protocolo);
			
			String url = request.getPreferences().getValue(URL_REGISTRA_AVALIACAO, null);
			
			String usuario = System.getProperty(SIOUV_USUARIO_SERVICO); //request.getPreferences().getValue(USUARIO_SERVICO, null);
			String senha = System.getProperty(SIOUV_SENHA_SERVICO); //request.getPreferences().getValue(SENHA_SERVICO, null);
			
			
			String resposta = HttpUtil.sendPostRequest(url, obj.toString(), usuario, senha);
		
			JsonObject retorno = new JsonParser().parse(resposta).getAsJsonObject();
			System.out.println(retorno);
			
			JsonElement mensagem = retorno.get("mensagem");
			
			if(!Util.isEmpty(mensagem)){
				MSG_SUCESSO_SIVOU.equalsIgnoreCase(mensagem.getAsString());
				response.setRenderParameter(MENSAGEM, "Sua pesquisa de satisfação foi enviada com sucesso");
			}
			response.removePublicRenderParameter(PROTOCOLO);
			if(!Util.isEmpty(tipoBusca)) {
				response.setRenderParameter(TIPO_BUSCA, tipoBusca);
			}
			
			
			if(!Util.isEmpty(cpf))
				response.setRenderParameter(CPF, cpf);
			
			
			if(retorno.getAsJsonObject().get("erro")!=null){
				response.setRenderParameter(ERRO, ERRO);
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			response.setRenderParameter(ERRO, ERRO);
		}
	}
	
	public void pesquisaPorCPF(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
		String cpf = request.getParameter(CPF);
		String url = request.getPreferences().getValue(URL_BUSCA_POR_CPF, null);
		
		String usuario = System.getProperty(SIOUV_USUARIO_SERVICO); //request.getPreferences().getValue(USUARIO_SERVICO, null);
		String senha = System.getProperty(SIOUV_SENHA_SERVICO); //request.getPreferences().getValue(SENHA_SERVICO, null);
		
		String qtdDiasCorridos = request.getPreferences().getValue(QT_DIAS_BUSCA, "10");
		
		if(!Util.isCPF(cpf)){
			JsonObject obj = new JsonObject();
			obj.addProperty("erro", "CPF inválido");
			PrintWriter writer = response.getWriter();
			
			writer.print(obj.toString());
			return;
			
		}
		
		
		try {
			
			JsonObject obj = new JsonObject();
			obj.addProperty(CPF, cpf);
			
			// FIXME REMOVER COMENTARIO ABAIXO 
			
			obj.addProperty("qtdDiasCorridos",qtdDiasCorridos);
			
			
			String resposta = HttpUtil.sendPostRequest(url, obj.toString(), usuario, senha);
			
			PrintWriter writer = response.getWriter();
			
			writer.print(resposta);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortletException(e);
		}
		super.serveResource(request, response);
	}
	
	
	public void pesquisaProtocoloAnonimo(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
		
		String protocolo = request.getParameter(PROTOCOLO);
		String url = request.getPreferences().getValue(URL_BUSCA_POR_PROTOCOLO, null);
		String usuario = request.getPreferences().getValue(USUARIO_SERVICO, null);
		String senha = request.getPreferences().getValue(SENHA_SERVICO, null);
		
		try {
			
			JsonObject obj = new JsonObject();
			obj.addProperty("protocolo", protocolo);
			obj.addProperty("anonimo", "S");
			
			String resposta = HttpUtil.sendPostRequest(url, obj.toString(), usuario, senha);
			
			PrintWriter writer = response.getWriter();
			
			writer.print(resposta);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortletException(e);
		}
		super.serveResource(request, response);
	}
	
	
	
}