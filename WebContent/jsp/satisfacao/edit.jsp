<%@ page import="javax.portlet.PortletURL,java.util.*" %>
<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="p"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<p:defineObjects />
<p:actionURL var="action" name="savePrefs" />

<div class="content-section section-services">
	<h4>Preferências</h4>
		<form action=${action}" method="post" class="form-d">
		
		  <ul class="form-set no-bullets">
		  
		  	<li class="form-row clearfix">
		  		<diV class="control-item control-span-6_12">
		  			<label class="label-d">Usuario de Servico  </label>
		  			<input class="field-d" value="${usuario_servico}" name="usuario_servico" placeholder="" disabled="disabled"/>
		  		</diV>
		  	</li>
		  	
		  	<li class="form-row clearfix">
		  		<diV class="control-item control-span-6_12">
		  			<label class="label-d">Senha do suario de Servico  </label>
		  			<input class="field-d" value="${senha_servico}" name="senha_servico" placeholder="" disabled="disabled"/>
		  		</diV>
		  	</li>
		  	
		  	
		  	<li class="form-row clearfix">
		  		<diV class="control-item control-span-6_12">
		  			<label class="label-d">Quantidade de dias para a busca de ocorrências</label>
		  			<input class="field-d" value="${qt_dias_busca}" name="qt_dias_busca" placeholder=""/>
		  		</diV>
		  	</li>
		  	
		  	
		  	<li class="form-row clearfix">
		  		<diV class="control-item control-span-6_12">
		  			<label class="label-d">Url da busca por CPF</label>
		  			<input class="field-d" value="${servico_busca_por_cpf}" name="servico_busca_por_cpf" placeholder=""/>
		  		</diV>
		  	</li>
		  	
		  	<li class="form-row clearfix">
		  		<diV class="control-item control-span-6_12">
		  			<label class="label-d">Url da busca por Protocolo</label>
		  			<input class="field-d" value="${servico_busca_por_protocolo}" name="servico_busca_por_protocolo" placeholder=""/>
		  		</diV>
		  	</li>
		  	
		  	<li class="form-row clearfix">
		  		<diV class="control-item control-span-6_12">
		  			<label class="label-d">Url do registro da pesquisa</label>
		  			<input class="field-d" value="${servico_registra_avaliacao}" name="servico_registra_avaliacao" placeholder=""/>
		  		</diV>
		  	</li>
		  	
		  	<li class="form-row clearfix">
		  		<diV class="control-item control-span-6_12">
		  			<input type="submit" value="Salvar"/>			
		  		</diV>
		  	</li>
		  </ul>
		  
		</form>
	</div>