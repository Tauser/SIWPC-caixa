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
		  		  <label class="label-d">Natureza do Formulário:</label>
				  <select class="select-d" name="natureza" id="tipo">
				  	<option value="2">Sugestão</option>
				  	<option value="3">Elogios</option>
				  	<option value="4">Denúncias</option>
				  	<option value="48">Dúvidas</option>
				  	<option value="1">Reclamações</option>
		  </select>	
		  		</diV>
		  	</li>
		  	<li class="form-row clearfix">
		  		<diV class="control-item control-span-6_12">
		  			<label class="label-d">URL de geração de Token: </label>
		  			<input class="field-d" value="${tokenCaptchaURL}" name="tokenCaptchaURL" placeholder="http:///Service1:/GerarToken"/>
		  		</diV>
		  	</li>
		  	<li class="form-row clearfix">
		  		<diV class="control-item control-span-6_12">
		  			<label class="label-d">URL de geração de Imagem: </label>
		  			<input class="field-d" value="${imgCaptchaURL}"  name="imgCaptchaURL" placeholder="http:///Service1:/GerarImagem"/>
		  		</diV>
		  	</li>
		  	<li class="form-row clearfix">
		  		<diV class="control-item control-span-6_12">
		  			<label class="label-d">URL de geração de audio: </label>
		  			<input class="field-d" value="${soundCaptchaURL}" name="soundCaptchaURL" placeholder="http:///Service1:/GerarAudio"/>
		  		</diV>
		  	</li>
		  	<li class="form-row clearfix">
		  		<diV class="control-item control-span-6_12">
		  			<labe class="label-d">URL de validação do catpcha: </label>
			  		<input class="field-d" value="${validateCaptchaURL}" name="validateCaptchaURL" placeholder="http:///Service1:/ValidarCaptcha"/>
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
	
<script type="text/javascript">
	$("#tipo").val(${natureza});
</script>