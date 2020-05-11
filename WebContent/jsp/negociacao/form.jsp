<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="p"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- versao 20160318 -->
<p:defineObjects />
<p:actionURL var="urlAcao" name="gravarNegociacao" />
<p:resourceURL var="urlAjaxPasso1" id="passo1" />
<p:resourceURL var="urlAjaxPasso2" id="passo2">
	<p:param name="tokenCaptcha" value="${tokenCaptcha}" />
</p:resourceURL>

<p:resourceURL var="urlImageCaptcha" id="imageCaptcha">
	<p:param name="tokenCaptcha" value="${tokenCaptcha}" />
</p:resourceURL>
<p:resourceURL var="urlAudioCaptcha" id="audioCaptcha">
	<p:param name="tokenCaptcha" value="${tokenCaptcha}" />
</p:resourceURL>

<p:resourceURL var="urlCaptchaTokenRefresh" id="refreshCaptcha" />


<input type="hidden" id="urlImageCaptcha" value="${urlImageCaptcha}" />
<input type="hidden" id="urlAudioCaptcha" value="${urlAudioCaptcha}" />

<script type="text/javascript"
	src="${pageContext.servletContext.contextPath}/js/jquery-1.6.4.js"></script>
<script type="text/javascript"
	src="${pageContext.servletContext.contextPath}/js/jquery.maskedinput-1.3.js"></script>
<script type="text/javascript"
	src="${pageContext.servletContext.contextPath}/js/faleconosco.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.servletContext.contextPath}/css/faleconosco.css">
<%-- 
<c:set var="admin" value="<%=com.ibm.portal.ac.data.RoleType.ADMIN%>"/>
<c:set var="editor" value="<%=com.ibm.portal.ac.data.RoleType.EDITOR %>"/>
<c:set var="curAC" value="${wp.ac[wp.selectionModel.selected]}"/>
 --%>

<div class="content-section section-services carregando">
	<form action="${urlAcao}" class="form-d" id="_form" method="POST">

		<fieldset class="fieldset-d clearfix form-step" id="passo1">
			<div class="legend clearfix">
				<span class="legend-step delta">1</span>
				<div class="legend-info">
					<h2 class="legend-title delta">Identifica&ccedil;&atilde;o</h2>
					<span class="legend-desc lighter milli">Sua
						identifica&ccedil;&atilde;o é importante e torna esse canal mais
						eficiente.</span> <input type="hidden" id="tokenCaptcha"
						name="tokenCaptcha" value="${tokenCaptcha}" />
				</div>
			</div>
			<ul class="form-set no-bullets">
				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-6_12">
						<label class="label-d">Nome completo/Raz&atilde;o Social:</label>
						<input type="text" name="nome" id="nome" value=""
							placeholder="Nome completo/Raz&atilde;o Social" class="field-d"
							maxlength="50">
					</div>
				</li>
				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-6_12">
						<label class="label-d">CPF/CNPJ:</label> <input id="cpfCnpj"
							type="text" name="cpfCnpj" class="field-d control-span-7_12"
							placeholder="111.111.111-11"  maxlength="18"  onkeyup="mascaraMutuario(this,maskCpfCnpj)" onblur="clearTimeout()"/>
					</div>
				</li>
				
				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-6_12">
						<label class="label-d">Nome da M&atilde;e:</label>
						<input type="text" name="noMae" id="noMae" value=""
							placeholder="Nome Completo da Mãe" class="field-d"
							maxlength="50">
					</div>
				</li>

				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-6_12">
						<label class="label-d">Data de Nascimento:</label> <input id="dtNascimento"
							type="text" name="dtNascimento" class="field-d control-span-7_12"
							placeholder="DD/MM/AAAA"  maxlength="18"  />
					</div>
				</li>


				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-6_12">
						<label class="label-d">Ag&ecirc;ncia de sua
							prefer&ecirc;ncia para atendimento:</label> <input name="agencia"
							placeholder="0000" id="agencia" class="field-d control-span-6_12"
							size="4" type="text">
					</div>
					<div class="control-item control-span-5_12">
						<label class="label-d" style="color: #1a789e;">Localize o
							ponto de atendimento</label> <a
							href="http://www.caixa.gov.br/atendimento/Paginas/encontre-a-caixa.aspx"
							target="_blank" class="submit-d submit-blue">Localizar
							agências</a>
					</div>
				</li>
				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-6_12">
						<label class="label-d">Email:</label> <input type="text"
							onblur="return validateEmail(this)" id="email" class="field-d"
							placeholder="email@email.com" maxlength="50" name="email" />
					</div>
				</li>
				<li class="form-row clearfix indentificacao">
					<div class="control-item">
						<label class="label-d">Telefone:</label> <input type="text"
							placeholder="00" name="ddd" id="ddd"
							class="field-d control-span-3_12" /> <input type="text"
							placeholder="0000-00000" name="tel" id="tel"
							class="field-d control-span-6_12" />
					</div>
				</li>
				<li class="form-row clearfix">
					<div class="control-item control-span-4_12">
						<label class="label-d">Cidade:</label> <input type="text"
							name="noCidade" id="noCidade" value="" placeholder="Cidade"
							class="field-d" maxlength="50">
					</div>
					<div class="control-item control-span-2_12">
						<label class="label-d">UF:</label> <select name="sgUf" id="sg_uf"
							class="select-d">
							<option value="">UF</option>
							<option value="AC">AC</option>
							<option value="AL">AL</option>
							<option value="AM">AM</option>
							<option value="AP">AP</option>
							<option value="BA">BA</option>
							<option value="CE">CE</option>
							<option value="DF">DF</option>
							<option value="ES">ES</option>
							<option value="GO">GO</option>
							<option value="MA">MA</option>
							<option value="MT">MT</option>
							<option value="MS">MS</option>
							<option value="MG">MG</option>
							<option value="PA">PA</option>
							<option value="PB">PB</option>
							<option value="PE">PE</option>
							<option value="PI">PI</option>
							<option value="PR">PR</option>
							<option value="RJ">RJ</option>
							<option value="RN">RN</option>
							<option value="RO">RO</option>
							<option value="RR">RR</option>
							<option value="RS">RS</option>
							<option value="SC">SC</option>
							<option value="SE">SE</option>
							<option value="SP">SP</option>
							<option value="TO">TO</option>
						</select>
					</div>
				</li>
				<li class=" clearfix control-span-6_12 msgErroPasso1"
					<c:if test="${msgErro == null or msgErro == ''}">style="display: none;"</c:if>>
					<div class="feedback feedback-error">
						<span class="feedback-text">Houve um erro, por favor
							corrija os seguintes campos:<span id="msgErroPasso1">Houve
								um erro, por favor corrija os seguintes campos: ${msgErro}</span>.
						</span> <span class="dialog-close feedback-close"><i
							class="font-icon i-cancel"></i> </span>
					</div>
				</li>


				<li class="form-row clearfix">
					<div class="control-item control-span-3_12">
						<button name="passo" value="1" type="submit"
							class="submit-d submit-blue js-form-next validate-user">
							<i class="font-icon i-down-dir"></i>Continuar
						</button>
					</div>
				</li>
			</ul>
		</fieldset>
		<fieldset class="fieldset-d clearfix form-step" id="passo2">
			<div class="legend clearfix">
				<span class="legend-step delta">2</span>
				<div class="legend-info">
					<h2 class="legend-title delta">Formul&aacute;rio de
						Negocia&ccedil;&atilde;o</h2>
					<span class="legend-desc lighter milli"> Leia as
						instru&ccedil;&otilde;es de preenchimento e nos envie todas as
						informa&ccedil;&otilde;es para uma an&aacute;lise mais eficiente.
					</span>
				</div>
			</div>
			<ul class="form-set no-bullets">


				<li class="form-row clearfix">
					<div class="control-item control-span-10_12" style="font-size: 14px">
						<p>INSTRU&Ccedil;&Otilde;ES PARA PREENCHIMENTO</p>

						<p>Para que sua proposta seja avaliada, &eacute; fundamental informar os seguintes dados:</p>

						<p>1 - Valor m&aacute;ximo para pagamento mensal e;</p>
							<p>2 - O motivo de negocia&ccedil;&atilde;o:</p>

							<p>- desemprego;</p>
							<p>- redu&ccedil;&atilde;o da renda familiar;</p>
							<p>- morte do devedor;</p>
							<p>- doen&ccedil;a grave;</p>
							<p>- outros (informar).</p>
						


						<p>Para negocia&ccedil;&atilde;o de Pessoa Jur&iacute;dica, &eacute; necess&aacute;rio informar, al&eacute;m do motivo e do valor mensal, a previs&atilde;o de recupera&ccedil;&atilde;o da situa&ccedil;&atilde;o.</p>
					</div>
				</li>
				<li class="form-row clearfix">
					<div class="control-item control-span-6_12">
						<label class="label-d">Mensagem:</label>
						<textarea rows="4" cols="20" class="field-d" name="mensagem"
							id="mensagem" maxlength="10000"></textarea>
					</div>

				</li>


				<div class="form-row clearfix">
				
					<div class="captcha">
				    	<div class="captcha-imagem">
				        	<img src="${urlImageCaptcha}" id="captchaImg" alt="Codigo" title="Codigo" />
				        </div>
				        <div class="captcha-controles">
				        	<div class="captcha-texto">
				            	<input type="text" name="captcha_response" placeholder="Digite o código acima" />
				            </div>
				            <div class="captcha-refresh">
				            
				            	<a href="javascript:void(0)" onclick="refreshCaptcha()">
				            		<img src="${pageContext.servletContext.contextPath}/images/captcha-refresh.png" title="Atualizar o código" alt="Atualizar o código" />
				            	</a>
				            
				            </div>
				            <div class="captcha-audio">
				            
				            	<a href="javascript:void(0)" onclick="playSound('audioCaptcha')">
				            		<img src="${pageContext.servletContext.contextPath}/images/captcha-audio.png" title="Ouça o código e digite no campo ao lado" alt="Ouça o código e digite no campo ao lado" />
				            	</a>
				            
				            	<audio preload="auto" autobuffer id="audioCaptcha"> 
  									<source src="${urlAudioCaptcha}" id="srcAudio" />
 								</audio>
				            
				            </div>
				            
				        </div>
				    </div>
				
                 </div>

				<li class=" clearfix msgErroPasso2"
					<c:if test="${msgErro == null or msgErro == ''}">style="display: none;"</c:if>>
					<div class="feedback feedback-error control-span-6_12"">
						<span class="feedback-text">Houve um erro, por favor
							corrija os seguintes campos: <span id="msgErroPasso2">${msgErro}</span>.
						</span> <span class="dialog-close feedback-close"><i
							class="font-icon i-cancel"></i> </span>
					</div>
				</li>
				<li class="form-row clearfix">
					<div class="control-item control-span-6_12">
						<button name="passo" value="2" type="submit"
							class="submit-d submit-blue js-form-next validate-user">
							<i class="font-icon i-down-dir"></i>Enviar Proposta
						</button>
						<span class="secondary-action form-row"> ou <a
							href="javascript:void(0);" class="js-form-prev"
							id="linkVoltaPasso2">volte à etapa anterior</a>
						</span>
					</div>
				</li>
			</ul>
		</fieldset>

	</form>
</div>
<script>
	//$(document).ready(function() {

	$('.fieldset-d').not(':first-child').addClass('fieldset-inactive');

	$('.js-form-next').click(function(e) {

		e.preventDefault();

		currentStep = $(this).parents('fieldset');
		nextStep = $(this).parents('fieldset').next();

		var url = "";
		switch (e.target.value) {
		case "1":
			url = '${urlAjaxPasso1}';
			break;
		case "2":
			url = '${urlAjaxPasso2}';
			break;
		default:
			url = '${urlAjaxPasso1}';
			break;
		}
		;

		$.ajax({
			type : "POST",
			url : url,
			data : $("#_form").serialize(),
			dataType : "xml",
			async : false,
			success : function(data) {
				var xml = data;
				var sucesso = $(xml).find('sucesso').text();
				var msgErro = $(xml).find('msgErro').text();
				var passo = $(xml).find('passo').text();
				var process = $(xml).find("process").text();
				if (sucesso == "true") {
					//esconde msg de erro
					$(".msgErroPasso" + passo).hide();

					//slide do painel
					if ($(window).width() > 600) {
						currentStep.find('.form-set:first').slideUp(700);
						nextStep.find('.form-set:first').slideDown(700);
					} else {
						currentStep.find('.form-set:first').hide();
						nextStep.find('.form-set:first').show();
					}
					currentStep.addClass('fieldset-inactive');
					nextStep.removeClass('fieldset-inactive');
					nextStepPos = currentStep.offset().top + 1;
					$('html, body').scrollTop(nextStepPos);

					if (process == "true") {
						$("#_form").submit();
					}

				} else {
					//exibe msg erro
					$("#msgErroPasso" + passo).text(msgErro);
					$(".msgErroPasso" + passo).show();

				}

				var data = new Date();

				//$("#captchaImg").attr("src","${pageContext.servletContext.contextPath}/jcaptcha?t="+data.getTime());

			}
		});
	});

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

	$('.js-form-restart').click(function(e) {
		currentStep = $(this).parents('fieldset');
		firstStep = $(this).parents('fieldset').siblings(':first-child');

		if ($(window).width() > 600) {
			currentStep.find('.form-set').slideUp(700);
			firstStep.find('.form-set').slideDown(700);
		} else {
			currentStep.find('.form-set').hide();
			firstStep.find('.form-set').show();
		}

		currentStep.addClass('fieldset-inactive');
		firstStep.removeClass('fieldset-inactive');

		firstStepPos = firstStep.offset().top + 1;

		$('html, body').scrollTop(firstStepPos);

		e.preventDefault();

	});

	//desabilita o passo 2 ao carregar a pagina
	$("#linkVoltaPasso2").trigger("click");
	//$(".form-row").hide();
	//$(".relacionamento").show(); talvez dar show no identuficação indentificacao
	$(".indentificacao").show()

	//mascaras
	//$("#cpf").mask("999.999.999-99");

	/* $("#cpf").keydown(function(){
	    try {
	    	 
	    	$("#cpf").unmask();
	    	
	    } catch (e)  {}
	    
	    
	    var tamanho = $("#cpf").val().length;
	    console.log(tamanho);
	    
	    
	    if(tamanho < 15){
	        $("#cpf").mask("999.999.999-99");
	    } else if(tamanho >= 15){
	        $("#cpf").mask("99.999.999/9999-99");
	    }
	   
	}); */
	
	//função unmask n esta retirando a mascar e toda vez que seta nova mascara(.mask) valor anterior é verdio
	//solução temporária
	
	//INÍCIO DA MÁSCARA CPF/CNPJ
	
	function mascaraMutuario(o,f){
	    v_obj=o
	    v_fun=f
	    setTimeout('execmascara()',1)
	}
	 
	function execmascara(){
	    v_obj.value=v_fun(v_obj.value)
	}
	 
	function maskCpfCnpj(v){
	 
	    v=v.replace(/\D/g,"")
	 
	    if (v.length <= 11) { //CPF	 
	       
	        v=v.replace(/(\d{3})(\d)/,"$1.$2")	 
	      
	        v=v.replace(/(\d{3})(\d)/,"$1.$2")

	        v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2")
	 
	    } else { //CNPJ

	        v=v.replace(/^(\d{2})(\d)/,"$1.$2")

	        v=v.replace(/^(\d{2})\.(\d{3})(\d)/,"$1.$2.$3")

	        v=v.replace(/\.(\d{3})(\d)/,".$1/$2")

	        v=v.replace(/(\d{4})(\d)/,"$1-$2")
	 
	    }
	 
	    return v
	 
	}
	
	//FIM DA MÁSCARA CPF/CNPJ
	
	
	$("#ddd").mask("99");
	$("#dtNascimento").mask("99/99/9999");
	$("#agencia").mask("9999");
	//funcao para os telefones de sao paulo
	$("#tel").mask("9999-9999?9", {
		reverse : true
	});

	$('textarea[maxlength]').keyup(function() {
		//get the limit from maxlength attribute  
		var limit = parseInt($(this).attr('maxlength'));
		//get the current text inside the textarea  
		var text = $(this).val();
		//count the number of characters in the text  
		var chars = text.length;

		//check if there are more characters then allowed  
		if (chars > limit) {
			//and if there are use substr to get the text before the limit  
			var new_text = text.substr(0, limit);

			//and change the current text with the new text  
			$(this).val(new_text);
		}
	});

	$(".dialog-close .font-icon.i-cancel").click(function() {
		$(this).parents("li").hide("slow");
	});

	$(".carregando").removeClass("carregando");

	function refreshCaptcha() {
		var url = "${urlCaptchaTokenRefresh}";
		$.ajax({
			type : "POST",
			url : url,
			async : false,
			success : function(data) {

				console.log(data);
				var resultado = JSON.parse(data);
				$("#tokenCaptcha").val(resultado.tokenCaptcha);
				$("#captchaImg").attr("src", resultado.imgCaptchaURL);
				$("#srcAudio").attr("src", resultado.soundCaptchaURL);
				$("#audioCaptcha")[0].load();
			}

		});
	}

	//});
</script>