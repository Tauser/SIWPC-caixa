<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="p"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- versao 20160318 -->
<p:defineObjects />
<p:actionURL var="urlAbreAvaliacao" name="abrirAvaliacao"/>
<p:resourceURL var="urlPesquisaPorCPF" id="pesquisaPorCPF"/>
<p:resourceURL var="urlPesquisaAnonimo" id="pesquisaProtocoloAnonimo"/>
<script type="text/javascript"
	src="${pageContext.servletContext.contextPath}/js/jquery-1.6.4.js"></script>
<script type="text/javascript"
	src="${pageContext.servletContext.contextPath}/js/jquery.maskedinput-1.3.js"></script>
<script type="text/javascript"
	src="${pageContext.servletContext.contextPath}/js/faleconosco.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.servletContext.contextPath}/css/faleconosco.css">

<div class="content-section section-services carregando">
	<form action="${urlAbreAvaliacao}" class="form-d" id="_form" method="POST">

		<!--  Set inicio passo1 -->
		<fieldset class="fieldset-d clearfix form-step fieldset-pesquisa-satisfacao" id="passo1">
			<div class="legend clearfix">
				<span class="legend-step delta">1</span>
				<div class="legend-info">
					<h2 class="legend-title delta">Protocolo</h2>
					<span class="legend-desc lighter milli">Acompanhe sua solicitação</span>
				</div>
			</div>
			<ul class="form-set no-bullets">
				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-6_12">
						<label class="label-d" id="labelCpfPrtcl">Digite seu CPF:</label>
						 <input id="cpfPrtcl" type="text" name="cpfPrtcl" value="${cpf}" class="field-d control-span-6_12" size="15">
						 <input type="hidden" type="text" name="cpf" id="cpf" value="${cpf}"/>
						 <input type="hidden" type="text" name="anonimo" id="anonimo" value="N"/>
						 <input type="hidden" type="text" name="tipoBusca" id="tipoBusca" value="${tipoBusca}"/>
						 <input type="hidden" type="text" name="protocolo" id="protocolo" />
						 <input type="hidden" type="text" name="nuMovimentacao" id="nuMovimentacao" />
					</div>
				</li>
				<li class="form-row clearfix">
					<div class="control-item control-span-6_12">
						<label class="label-d">Seu pedido foi anônimo &nbsp;&nbsp;
							<input id="anonimoCHK" type="checkbox" name="anonimoCHK" onclick="changeLabel()" size="15">
						</label>
					</div>
				</li>
				<li class=" clearfix control-span-6_12 msgErroPasso1 ">
					<div class="feedback feedback-error">
						<span class="feedback-text">
								<span id="msgErroPasso1"></span>.
						</span> <span class="dialog-close feedback-close"><i class="font-icon i-cancel"></i> </span>
					</div>
				</li>
				<li class="form-row clearfix">
					<div class="control-item control-span-3_12">
						<button name="passo" value="1" type="button"
							class="submit-d submit-blue js-form-next validate-user">
							<i class="font-icon i-down-dir"></i>
							Acompanhar
						</button>
					</div>
				</li>
			</ul>
		</fieldset>
		<!-- set fim passo 1 -->
		
		<!--  passo 2  -->
		<fieldset class="fieldset-d clearfix form-step fieldset-pesquisa-satisfacao" id="passo2">
			<div class="legend clearfix">
				<span class="legend-step delta">2</span>
				<div class="legend-info">
					<h2 class="legend-title delta">Resultado</h2>
					<span class="legend-desc lighter milli"></span>
				</div>
			</div>
			<ul class="form-set no-bullets tabela-widh-100">
				<li class="form-row clearfix indentificacao">
					<table>
						<thead>
							<tr>
								<td>Tipo de Ocorrência</td>
								<td>Tipo de Manifesta&ccedil;&atilde;o</td>
								<td>Protocolo</td>
								<td>Origem</td>
								<td>Assunto</td>
								<td>Data da Abertura</td>
								<td>Data da Solução</td>
								<td>Pesquisa de Satisfa&ccedil;&atilde;o</td>
							</th>
						</thead>
						<tbody id="dataTableResultado">
							</tr>
							<!-- final do loop de ocorrencias -->
							<span class="secondary-action form-row"> <a
							href="javascript:void(0);" class="js-form-prev" id="linkVoltaPasso2">Volte à etapa
								anterior</a> </span>							
						</tbody>
					</table>
					<!-- 
					<div class="legenda-tabela">
				  		<div class="periodo-ocorrencia">
				  			<span>Ocorrências registradas no período de 90 dias.</span> 
				  		</div>
				  		<br/>
				  		<div>
				  			<p>Pesquisa de Satisfação indisponível: sua ocorrência ainda não foi respondida pela Caixa. A Pesquisa de Satisfação estará disponível para preenchimento, por 5 dias úteis, após a data da resposta.</p>
				  		</div>
				  		<div>
				  			<p>Pesquisa de Satisfação com prazo expirado: a pesquisa fica disponível somente por 5 dias úteis após a data da resposta da sua ocorrência. </p>
				  		</div>
					</div>
					 -->
				</li>
			</ul>
		</fieldset>
	</form>
</div>

<script>
$('.msgErroPasso1').hide();
$("#cpfPrtcl").mask("9?9999999999");
$('#cpfPrtcl').bind('keyup keypress',function(e){
	var keyCode = e.keyCode || e.which;
	if(keyCode===13){
		e.preventDefault();
		return false;
	}
})
var msg = '${mensagem}';
if(msg!=''){
	alert(msg);
}

$('.fieldset-d').not(':first-child').addClass('fieldset-inactive');

$('.js-form-next').click(function(e) {
	
	$('.msgErroPasso1').hide();

	e.preventDefault();
	currentStep = $(this).parents('fieldset');
	nextStep = $(this).parents('fieldset').next();
	
	if(document.getElementById("cpfPrtcl").value==''){
		alert('Campo obrigatório não informado.')
		return;
	}
	
	var buscaProtocolo = document.getElementById("anonimoCHK").checked;
	
	if(buscaProtocolo){
		pesquisaProtocolo()
	}else{
		pesquisaCPF()
	}
});


function 	pesquisaProtocolo(){
	document.getElementById("protocolo").value = document.getElementById("cpfPrtcl").value;
	document.getElementById("tipoBusca").value="protocolo";
	
	$.ajax({
		type: "POST",
		url:  "${urlPesquisaAnonimo}",
		async: false,
		data : $("#_form").serialize(),
		success: function(response){
			//console.log(response);
			var resultado;
			try{
				resultado = JSON.parse(response)
			}catch(erro){
				resultado = {
					erro:'Serviço indisponível, tente mais tarde'
				}
			}
			
			if(resultado.erro){
				$('#msgErroPasso1')
				$('#msgErroPasso1')[0].innerHTML=resultado.erro
				$('.msgErroPasso1').show();
			}else{
				
				var tbody = document.getElementById('dataTableResultado');
				tbody.innerHTML='';
				
				if(resultado.ocorrencias!=null && resultado.ocorrencias.length>0){
					resultado.ocorrencias.sort(custom_sort);
					for(var i=0; i<resultado.ocorrencias.length; i++){
						
						var ocorrencia = resultado.ocorrencias[i];
						
						var tr = geraLinhaTable(ocorrencia)
						
						tbody.appendChild(tr)
						
					}
				executaRolagem(currentStep,nextStep)	
				}else{
				$('#msgErroPasso1')
				$('#msgErroPasso1')[0].innerHTML='Protocolo não econtrado. Esta opção é exclusiva para consulta de protocolos anônimos.\nCaso deseje consultar um protocolo não anônimo, a pesquisa deverá ser feita por meio do CPF'
				$('.msgErroPasso1').show();
			}
			}
		}
	})
	
}
//ordena inversamente a sequencia

function strToDate(str){
	if(str!=undefined && str!=null && str!=''){
		var val = str.split('/')
		if(val.length===3){
			return new Date(val[2], val[1] - 1, val[0])
		}
	}
	return new Date();
}
function custom_sort(a, b) {
	var dateA = strToDate(a.dhSolucao);
	var dateB = strToDate(b.dhSolucao);
	
	
	
    return dateB.getTime() - dateA.getTime();
}

function pesquisaCPF(){
	
	document.getElementById("cpf").value = document.getElementById("cpfPrtcl").value;
	document.getElementById("tipoBusca").value="cpf";
	
	$.ajax({
		type : "POST",
		url : '${urlPesquisaPorCPF}',
		data : {cpf:$('#cpfPrtcl')[0].value},
		async : false,
		success : function(response) {
			//console.log(response);
			var resultado;
			try{
				resultado = JSON.parse(response)
			}catch(erro){
				resultado = {
					erro:'Serviço indisponível, tente mais tarde'
				}
			}
			
			if(resultado.erro){
				$('#msgErroPasso1')
				$('#msgErroPasso1')[0].innerHTML=resultado.erro
				$('.msgErroPasso1').show();
			}else{
				
				var tbody = document.getElementById('dataTableResultado');
				tbody.innerHTML='';
				
				if(resultado.ocorrencias!=null && resultado.ocorrencias.length>0){
					
					resultado.ocorrencias.sort(custom_sort);
					
					for(var i=0; i<resultado.ocorrencias.length; i++){
						
						var ocorrencia = resultado.ocorrencias[i];
						
						var tr = geraLinhaTable(ocorrencia)
						
						tbody.appendChild(tr)
						
					}
				executaRolagem(currentStep,nextStep)	
				}else{
				$('#msgErroPasso1')
				$('#msgErroPasso1')[0].innerHTML='Não foram localizadas ocorrências para o CPF informado'
				$('.msgErroPasso1').show();
			}
			}
		}
	})
}


function abrirAvaliacao(event,nuProtocolo,nuMovimentacao,anonimo){
	event.preventDefault();
	$("#protocolo").val(nuProtocolo);
	$("#nuMovimentacao").val(nuMovimentacao);
	$("#anonimo").val(anonimo)
	$("#_form").submit();
}

function geraLinhaTable(ocorrencia){
	
	var tr = document.createElement("tr");
	appendTD(tr,ocorrencia.noTipoOcorrencia)
	appendTD(tr,ocorrencia.noNatureza)

	
	if(ocorrencia.protocolo!=null){
		appendTD(tr,ocorrencia.protocolo)
	}else{
		appendTD(tr,ocorrencia.numeroOcorrencia)
	}
	
	
	appendTD(tr,ocorrencia.noTipoOrigem)
	appendTD(tr,ocorrencia.noAssunto)
	appendTD(tr,ocorrencia.dhOcorrencia)
	
	if(ocorrencia.status=='Indisponível'){
		appendTD(tr,'Em Análise')
	}else{
		appendTD(tr,ocorrencia.dhSolucao)
	}
	
	if(ocorrencia.status=='Liberada'){
		var btn = document.createElement("button")
		btn.setAttribute('class','submit-d')
		var txt = document.createTextNode('Avaliar')
		btn.appendChild(txt)
		btn.setAttribute('onclick','abrirAvaliacao(event,'+ocorrencia.numeroOcorrencia+','+ocorrencia.nuMovimentacao+',\''+ocorrencia.anonimo+'\')')
		var td =  document.createElement('td');
		td.appendChild(btn);
		tr.appendChild(td);
	}else if(ocorrencia.status=='Avaliada'){
		//criar as estrelas
		var td = document.createElement("td");
		
		var avaliacao = new Array();
		
		var nota = parseInt(ocorrencia.nuNotaAvaliacaoOcorrencia)
		
		for(var i=1;i<=5;i++){
			var img = document.createElement("i")
			
			if(i<=nota){
				img.setAttribute('class','fa fa-star')
			}else{
				img.setAttribute('class','fa fa-star-o')
			}
			td.appendChild(img)
		}
			
		tr.appendChild(td);
	}else{
		appendTD(tr,ocorrencia.status)
	}
	
	return tr;
} 



function appendTD(tr,textValue){
	if(!textValue){
		textValue = ''
	}
	var td = document.createElement("td");
	var textNode = document.createTextNode(textValue);
	td.appendChild(textNode);
	tr.appendChild(td);
}


function executaRolagem(currentStep,nextStep){
	
	//efetua a abertura e rolagem
	if ($(window).width() > 600) {
		currentStep.find('.form-set:first').slideUp(700);
		nextStep.find('.form-set:first').slideDown(700);
	} else {
		currentStep.find('.form-set:first').hide();
		nextStep.find('.form-set:first').show();
	}
	
	//ativa e inativa o fieldset
	currentStep.addClass('fieldset-inactive');
	nextStep.removeClass('fieldset-inactive');
	
	nextStepPos = currentStep.offset().top + 1;
	$('html, body').scrollTop(nextStepPos);
}

$('.js-form-prev').click(function(e) {

	currentStep = $(this).parents('fieldset');
	prevStep = $(this).parents('fieldset').prev();

	if ($(window).width() > 600) {
		currentStep.find('.form-set:first').slideUp(700);
		prevStep.find('.form-set:first').slideDown(700);
	} else {
		currentStep.find('.form-set:first').hide();
		prevStep.find('.form-set:first').show();
	}

	currentStep.addClass('fieldset-inactive');
	prevStep.removeClass('fieldset-inactive');

	prevStepPos = prevStep.offset().top + 1;

	$('html, body').scrollTop(prevStepPos);

	e.preventDefault();

});
	
	$(document).ready(function(){
		var cpf = $("#cpf");
		
		if(cpf.val()!==''){
			$('.js-form-next').trigger('click');
		}
	})
	
	
function changeLabel(){
	var buscaProtocolo = document.getElementById("anonimoCHK").checked;
	
	var label = document.getElementById("labelCpfPrtcl")
	if(buscaProtocolo){
		label.innerHTML = "Digite o número do seu protocolo anônimo:"
	}else{
		label.innerHTML = "Digite seu CPF:"
	}
}

</script>

