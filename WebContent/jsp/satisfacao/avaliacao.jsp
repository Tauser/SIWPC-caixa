<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="p"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- versao 20160318 -->
<p:defineObjects />

<p:renderURL portletMode="view" var="url">
	<p:param name="cpf" value="${cpf}" />
</p:renderURL>

<p:actionURL var="urlAcao" name="gravarAvaliacao">
	<p:param name="cpf" value="${cpf}" />
	<p:param name="tipoBusca" value="${tipoBusca}" />

</p:actionURL>

<script type="text/javascript"
	src="${pageContext.servletContext.contextPath}/js/jquery-1.6.4.js"></script>
<script type="text/javascript"
	src="${pageContext.servletContext.contextPath}/js/jquery.maskedinput-1.3.js"></script>
<script type="text/javascript"
	src="${pageContext.servletContext.contextPath}/js/faleconosco.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.servletContext.contextPath}/css/faleconosco.css">

<div class="content-section section-services carregando">
	<form action="${urlAcao}" class="form-d" id="_form" method="POST">

		<input type="hidden" name="protocolo" value="${ocorrencia.protocolo}">
		<input type="hidden" name="nuMovimentacao" value="${nuMovimentacao}">

		<input type="hidden" name="numeroOcorrencia"
			value="${ocorrencia.protocolo}"> <input type="hidden"
			name="numeroFase" value="${ocorrencia.numeroFase}"> <input
			type="hidden" name="numeroTipoOcorrencia"
			value="${ocorrencia.numeroTipoOcorrencia}"> <input
			type="hidden" name="nuNotaAvaliacaoOuvidoria"
			id="nuNotaAvaliacaoOuvidoria" value="0"> <input type="hidden"
			name="nuNotaAvaliacaoOcorrencia" id="nuNotaAvaliacaoOcorrencia"
			value="0">
		<!--  Set inicio passo1 -->
		<fieldset class="fieldset-d clearfix form-step " id="passo1">
			<div class="legend clearfix pesquisa-satisfacao-legend">
				<div class="legend-info">
					<span>Protocolo:</span>
					<h2 class="legend-title delta">${ocorrencia.protocolo}</h2>
					<span class="legend-desc lighter milli">
						<ul>
							<li class="tipo"><b>Tipo:</b> ${ocorrencia.noNatureza }</li>
							<li class="origem"><b>Origem:</b> ${ocorrencia.origem }</li>
							<li class="assunto"><b>Assunto:</b> ${ocorrencia.noCategoria}</li>
							<li class="data"><b>Data:</b> ${ocorrencia.dhOcorenciaStr }</li>
							
						</ul> </span>
				</div>
			</div>
			<ul class="form-set no-bullets pesquisa-satisfacao">
				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-8_12">
						<p class="title">Pesquisa de Satisfação</p>
						<label class="label-d">Em uma escala de 1 a 5, sendo 1 o nível de satisfação mais baixo e 5 o nível de satisfação mais alto, avalie a <b>solução apresentada pela ouvidoria para a sua demanda</b>
						</label>
						<div>
							<span class="hint"></span>
						</div>
						<div>
							<span class="avaliacao"> <i class="fa fa-star-o" id="p1_1"
								style="cursor: pointer" onclick="registra('p1',1)"></i> <i
								class="fa fa-star-o" id="p1_2" style="cursor: pointer"
								onclick="registra('p1',2)"></i> <i class="fa fa-star-o"
								id="p1_3" style="cursor: pointer" onclick="registra('p1',3)"></i>
								<i class="fa fa-star-o" id="p1_4" style="cursor: pointer"
								onclick="registra('p1',4)"></i> <i class="fa fa-star-o"
								id="p1_5" style="cursor: pointer" onclick="registra('p1',5)"></i>
							</span>
						</div>
					</div></li>

				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-8_12">
						<label class="label-d">Em uma escala de 1 a 5, sendo 1 o nível de satisfação mais baixo e 5 o nível de satisfação mais alto, avalie a <b>qualidade do atendimento prestado pela ouvidoria</b>
						</label>
						<div>
							<span class="hint"></span>
						</div>
						<div>
							<span class="avaliacao"> <i class="fa fa-star-o" id="p2_1"
								style="cursor: pointer" onclick="registra('p2',1)"></i> <i
								class="fa fa-star-o" id="p2_2" style="cursor: pointer"
								onclick="registra('p2',2)"></i> <i class="fa fa-star-o"
								id="p2_3" style="cursor: pointer" onclick="registra('p2',3)"></i>
								<i class="fa fa-star-o" id="p2_4" style="cursor: pointer"
								onclick="registra('p2',4)"></i> <i class="fa fa-star-o"
								id="p2_5" style="cursor: pointer" onclick="registra('p2',5)"></i>
							</span>
						</div>
					</div></li>
				<li class="form-row clearfix">
					<div class="control-item control-span-6_12">
						<label class="label-d">Outras considerações (não
							obrigatório)</label>
						<textarea rows="4" cols="20" class="field-d"
							name="deObsroPesquisaSatisfacao" id="deObsroPesquisaSatisfacao"
							maxlength="500"></textarea>
					</div></li>
				<li class="form-row clearfix">
					<div class="control-item control-span-6_12">
						<span class="control-span-4_12">
							<button name="passo" value="1" type="button"
								class="submit-d submit-white" onclick="gravaAliacao()">
								<i class="font-icon i-down-dir"></i> Enviar
							</button> <span class="control-span-4_12"> ou </span> <span
							class="control-span-4_12"> <a href="${url}">Cancelar</a>
						</span> </span>
					</div></li>
			</ul>
		</fieldset>
	</form>
</div>
<script>
	var retorno = '${retorno}'

	function gravaAliacao() {
		
		if(document.getElementById("nuNotaAvaliacaoOuvidoria").value=='0' ||
			document.getElementById("nuNotaAvaliacaoOcorrencia").value=='0'){
				alert('Campo obrigatório não informado.');
				return false;
			}
		
		document.getElementById("_form").submit();

	}

	function registra(questao, nota) {
		for ( var i = 1; i <= 5; i++) {
			var item = $('#' + questao + '_' + i);
			if (i <= nota) {
				if (item.hasClass("fa-star-o"))
					item.removeClass("fa-star-o").addClass("fa-star")
			} else {
				if (item.hasClass("fa-star"))
					item.removeClass("fa-star").addClass("fa-star-o")
			}
		}

		if ('p1' == questao) {
			document.getElementById("nuNotaAvaliacaoOcorrencia").value = nota
		} else {
			document.getElementById("nuNotaAvaliacaoOuvidoria").value = nota
		}
	}
</script>