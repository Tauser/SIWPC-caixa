<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="p"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<p:defineObjects />
<p:actionURL var="urlAcao" name="gravar" />
<p:actionURL var="reabrirReclamacao" name="reabrirReclamacao"/>
<p:resourceURL var="urlAjaxPasso1" id="passo1" />
<p:resourceURL var="buscaReclamacao" id="buscaReclamacao"  />
<p:resourceURL var="validaManifesto" id="validaManifesto"  />

<p:resourceURL var="urlAjaxPasso2" id="passo2" >
			<p:param name="tokenCaptcha" value="${tokenCaptcha}"/>
</p:resourceURL>

<p:resourceURL var="urlImageCaptcha" id="imageCaptcha" >
	<p:param name="tokenCaptcha" value="${tokenCaptcha}"/>
</p:resourceURL>
<p:resourceURL var="urlAudioCaptcha" id="audioCaptcha">
	<p:param name="tokenCaptcha" value="${tokenCaptcha}"/>
</p:resourceURL>

<p:resourceURL var="urlCaptchaTokenRefresh" id="refreshCaptcha"/>

<jsp:useBean id="timestampImg" class="java.util.Date" />


<input type="hidden" id="urlImageCaptcha" value="${urlImageCaptcha}" />
<input type="hidden" id="urlAudioCaptcha" value="${urlAudioCaptcha}" />

<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/jquery-1.6.4.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/jquery.maskedinput-1.3.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/faleconosco.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/css/faleconosco.css">

<div class="content-section section-services carregando">

	<form action="${urlAcao}" class="form-d" id="_form" method="POST" >
		
		<fieldset class="fieldset-d clearfix fomr-step" id="passo1">
			<div class="legend clearfix">
				<span class="legend-step delta">1</span>
				<div class="legend-info">
					<h2 class="legend-title delta">Pedido</h2>
					<span class="legend-desc lighter milli">
						Informe se voc&ecirc; j&aacute; possui um pedido ou protocolo de reclama&ccedil;&atilde;o em andamento.
						<input type="hidden" id="tokenCaptcha" name="tokenCaptcha" value="${tokenCaptcha}"/>
						
					</span>
				</div>
			</div>
			<!-- INICIO DO FORMULARIO DE RECLAMAÇÃO -->
			<ul class="form-set no-bullets">
				<li class="clearfix">
					<div class="control-item control-span-6_12">
						<label class="label-d">Você já possui um número de pedido?</label>
						<div>
							<label><input type="radio" id="tenhoPedido" name="icPedido" value="S"  />Sim</label>
							<label><input type="radio" id="naoTenhoPedido" name="icPedido" value="N" />N&atilde;o</label>
						</div>
					</div>
				</li>
				
				<li class="clearfix formrow formPedido" style="display:none;" >
					<div class="control-item">
						<label class="label-d">N&uacute;mero do Pedido ou CPF:</label>
						<input type="text" " size="15" class="field-d control-span-9_12" name="protocoloCPF" id="protocoloCPF"/>
					</div>
				</li>
				<li class=" clearfix msgErroPassobuscaReclamacao"
					<c:if test="${msgErro == null or msgErro == ''}">style="display: none;"</c:if>>
					<div class="feedback feedback-error control-span-6_12">
						<span class="feedback-text"><span id="msgErroPassobuscaReclamacao">${msgErro}</span>.</span>
						<span class="dialog-close feedback-close"><i
							class="font-icon i-cancel"></i>
						</span>
					</div>
				</li>
				<li class="form-row clearfix formPedido" style="display: none;" >
					<div class="control-item control-span-3_12">
						<button name="passo" value="buscaReclamacao" type="submit"
							class="submit-d submit-blue js-form-next validate-user">
							<i class="font-icon i-down-dir"></i>Continuar
						</button>
					</div>
				</li>
			</ul>
			</fieldset>
			<fieldset class="fieldset-d clearfix fomr-step dadoReclamacao" id="passo2" style="display:none;">
				<div class="legend clearfix">
					<span class="legend-step delta">2</span>
					<div class="legend-info">
						<h2 class="legend-title delta">Reclama&ccedil;&atilde;o</h2>
					</div>
				</div>
				<ul class="form-set no-bullets" style="display:none">
				<li class="clearfix form-row">
					<div class="control-item control-span-6_12">
						<label>Pedido número: <span id="spanProtocolo"></span></label>
					</div>
				</li>
				<li class="form-row clearfix">
					<div class="control-item control-span-3_12">
						<span id="spanNomeCategoria"></span>
					</div>
					<div class="control-item control-span-3_12">
						<label>Enviada dia </label> 
						<span id="spanDataEnvio"></span>
					</div>
				</li>
				<li class="form-row clearfix">
					<div class="control-item control-span-6_12"  style="background: #EAEAEA; padding:5px;">
						<label  style="font-weight: bold;">
							Situação: 
						</label> 
						<span id="spanNoSituacao"></span>
					</div>
				</li>
				<li class="form-row clearfix">
					<li class="form-row clearfix">
						<div class="control-item control-span-6_12" >
								<label class="label-d">
									Sua identifica&ccedil;&atilde;o é importante e torna esse canal mais eficiente
								</label>
							<div>
								<input type="radio" id="tenhoContaDisable" name="icRelacionamentoDisable" value="S" disabled="disabled"/>Tenho conta
								<input type="radio" id="naoTenhoContaDisable" name="icRelacionamentoDisable" value="N" disabled="disabled" />N&atilde;o tenho conta
							</div>
						</div>
				</li>
				<li class="form-row clearfix">
					<label class="label-d">
						Nome:
					</label>
					<div class="control-item control-span-3_12">
						<input type="text" disabled="disabled"  id="nomeDisabled" class="field-d" />
					</div>
				</li>
				<li class="form-row clearfix ">
					<div class="control-item control-span-6_12">
						<label class="label-d">Categoria:</label> 
						<select name="categoria" id="categoriaReclamacao" class="select-d" onchange="showNISLabel(this)" >
							<option value="">Escolha</option>
							<c:forEach items="${categorias}" var="categoria">
								<option value="${categoria.codigo}">${categoria.nome}</option>
							</c:forEach>
						</select>

					</div>
				</li>
				<li class="form-row clearfix">
					<div class="control-item control-span-6_12">
						<label class="label-d">Mensagem:</label>
						<label class="label-d" id="NISLabelReclamacao" style="display:none;">Registre sua ocorrência e informe o nº do seu NIS</label>
						<textarea rows="4" cols="20" class="field-d" name="mensagem" id="mensagem" maxlength="10000"></textarea>
					</div>
				</li>
				<li class=" clearfix msgErroPassovalidaManifesto"
					<c:if test="${msgErro == null or msgErro == ''}">style="display: none;"</c:if>>
					<div class="feedback feedback-error control-span-6_12"">
						<span class="feedback-text">Houve um erro, por favor corrija os seguintes campos: <span id="msgErroPassovalidaManifesto">${msgErro}</span>.</span>
						<span class="dialog-close feedback-close"><i
							class="font-icon i-cancel"></i>
						</span>
					</div>
				</li>
				<div class="form-row clearfix">
					
					<div class="captcha">
				    	<div class="captcha-imagem">
				        	<img src="${urlImageCaptcha}?timestamp=${timestampImg.getTime()}" id="captchaImg_V" alt="Codigo" title="Codigo" />
				        </div>
				        <div class="captcha-controles">
				        	<div class="captcha-texto">
				            	<input type="text" name="captcha_response" onblur="update(this)" id="captchaTXT_V" placeholder="Digite o texto acima" />
				            </div>
				            <div class="captcha-refresh">
				            
				            	<a href="javascript:void(0)" onclick="refreshCaptcha()">
				            		<img src="${pageContext.servletContext.contextPath}/images/captcha-refresh.png" title="Atualizar o código" alt="Atualizar o código" />
				            	</a>
				            
				            </div>
				            <div class="captcha-audio">
				            
				            	<a href="javascript:void(0)" onclick="playSound('audioCaptcha_V')">
				            		<img src="${pageContext.servletContext.contextPath}/images/captcha-audio.png" title="Ouça o código e digite no campo ao lado" alt="Ouça o código e digite no campo ao lado"  />
				            	</a>
				            
				            	<audio preload="auto" autobuffer id="audioCaptcha_V"> 
  									<source src="${urlAudioCaptcha}" id="srcAudio_V" />
 								</audio>
				            
				            </div>
				            
				        </div>
				    </div>
				</div>
				<li class="form-row clearfix">
					<div class="control-item control-span-6_12">
						<button name="passo" value="validaManifesto" type="submit"
							class="submit-d submit-blue js-form-next validate-user">
							<i class="font-icon i-down-dir"></i>Continuar
						</button>
						<span class="secondary-action form-row"> ou <a
							href="javascript:void(0);" class="js-form-prev" id="linkVoltaPasso2">volte à etapa
								anterior</a> </span>
					</div>
				</li>
				
			</ul>
			</fieldset>
		<!-- FINAL DO FORMULARIO DE RECLAMAÇÃO -->
		<!-- FORMULARIO DE PRIMEIRA RECLAMAÇÃO -->
		<fieldset class="fieldset-d clearfix form-step primeiroRegistro" id="passo1" style="display:none;">
			<div class="legend clearfix">
				<span class="legend-step delta">2</span>
				<div class="legend-info">
					<h2 class="legend-title delta">Identifica&ccedil;&atilde;o</h2>
					<span class="legend-desc lighter milli">Sua identifi&ccedil;&atilde;o é importante e torna esse canal mais eficiente.</span>
				</div>
			</div>
			<br />
			<ul class="form-set no-bullets">
				<li class="form-row clearfix">
					<div class="control-item control-span-6_12">
						<label class="label-d">Qual seu relacionamento com a
							CAIXA?</label>
						<div>
							<input type="radio" id="tenhoConta" name="icRelacionamento" value="S" />Tenho
							conta &nbsp;&nbsp;&nbsp;
							<input type="radio" id="naoTenhoConta" name="icRelacionamento" value="N" />N&atilde;o
							tenho conta 
							<c:if test="${natureza==4}">
								&nbsp;&nbsp;&nbsp;
								<input type="radio" id="anonimo" name="icRelacionamento" value="A" />An&ocirc;nimo
							</c:if>
						</div>
					</div></li>
				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-6_12">
						<label class="label-d">Nome completo:</label> <input type="text"
							name="nome" id="nome" value="" placeholder="Nome completo"
							class="field-d" maxlength="50">
					</div>
				</li>
				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-6_12">
						<label class="label-d">CPF:</label> <input id="cpf" type="text" name="cpf"
							class="field-d control-span-6_12" placeholder="111.111.111-11" maxlength="14" onkeyup="MascaraCPF(this)"
							size="15"  />
					</div>
				</li>

				<li class="form-row clearfix" id="liconta" style="display: none;">
					<div class="control-item control-span-3_12">
						<label class="label-d">Ag&ecirc;ncia:</label> <input type="text"
							name="agencia" placeholder="3333" id="agencia"
							class="field-d control-span-6_12" size="15"  />
					</div>
					<div class="control-item control-span-3_12 ">
						<label class="label-d">Conta:</label> <input type="text"
							name="conta" placeholder="3333333-3" id="conta"
							 onkeydown="javascript:hifen(this,event);"
							 onblur="hifenNotLast(this)"
							class="field-d control-span-6_12" size="21"  />
					</div>
				</li>
				<li class="form-row clearfix " id="lioperacao" style="display:none">
					<div class="control-item control-span-6_12">
						<label class="label-d control-item">Opera&ccedil;&atilde;o:</label> 
							<div class="control-span-8_12">
								<select name="operacao" class="select-d" >
									<option value="001">001 - Pessoa F&iacute;sica</option>
									<option value="013">013 - Conta Poupan&ccedil;a</option>
									<option value="003">003 - Pessoa Jur&iacute;dica</option>
								</select> 
							</div>
							<span class="tooltipBTN nome" style="background: url('<%=request.getContextPath()%>/images/bkg_botao.jpg')"
								  onmouseover="show(tooltipOP)" onmouseout="hide(tooltipOP)"
							>?
								
								<span class="tooltip" id="tooltipOP">
										<img src="<%=request.getContextPath() %>/images/bkg_Operacao.jpg"/>
								</span>		
							</span>
					</div>
				</li>

				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-6_12">
						<label class="label-d">Email:</label> <input type="text" onblur="return validateEmail(this)" id="email"
							class="field-d" placeholder="email@email.com" maxlength="50" name="email" />
					</div>
				</li>
				<li class="form-row clearfix indentificacao">
					<div class="control-item">
						<label class="label-d">Telefone:</label>
						 <input type="text"
							placeholder="61" name="ddd" id="ddd" 
							class="field-d control-span-3_12" /> <input type="text"
						
							placeholder="3333-33333" name="tel" id="tel" onkeyup="MascaraTelefone(this)"
							class="field-d control-span-6_12" />
					</div>
				</li>
				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-4_12">
						<label class="label-d">Cidade:</label> <input type="text"
							name="noCidade" id="noCidade" value="" placeholder="Cidade"
							class="field-d" maxlength="50">
					</div>
					<div class="control-item control-span-2_12">
						<label class="label-d">UF:</label> 
						<select name="sgUf" id="sg_uf" class="select-d">
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
						<span class="feedback-text">Houve um erro, por favor corrija os seguintes campos:<span id="msgErroPasso1">Houve um erro, por favor corrija os seguintes campos: ${msgErro}</span>.</span>
						<span class="dialog-close feedback-close"><i
							class="font-icon i-cancel"></i>
						</span>
					</div></li>
					
					
				<li class="form-row clearfix indentificacao">
					<div class="control-item control-span-3_12">
						<button name="passo" value="1" type="submit"
							class="submit-d submit-blue js-form-next validate-user">
							<i class="font-icon i-down-dir"></i>Continuar
						</button>
					</div>
				</li>
			</ul>
		</fieldset>
		<fieldset class="fieldset-d clearfix form-step primeiroRegistro" id="passo2" style="display:none;">
			<div class="legend clearfix">
				<span class="legend-step delta">3</span>
				<div class="legend-info">
					<h2 class="legend-title delta">Formul&aacute;rio de ${tituloPasso2}</h2>
					<span class="legend-desc lighter milli">
						Registre aqui sua reclama&ccedil;&atilde;o
					</span>
				</div>
			</div>
			<ul class="form-set no-bullets">
				<li class="form-row clearfix primeiroRegistro">
					<div class="control-item control-span-6_12">
						<label class="label-d">Categoria:</label> <select class="select-d"  
							name="categoria" onchange="showNISLabel(this)" >
							<option value="">Escolha</option>
							<c:forEach items="${categorias}" var="categoria">
								<option value="${categoria.codigo}">${categoria.nome}</option>
							</c:forEach>
						</select>

					</div>
				</li>
				<li class="form-row clearfix primeiroRegistro">
					<div class="control-item control-span-6_12">
						<label class="label-d">Mensagem:</label>
						<label class="label-d" id="NISLabel" style="display:none;">Registre sua ocorrência e informe o nº do seu NIS</label>
						<textarea rows="4" cols="20" class="field-d" name="mensagem" id="mensagem" maxlength="10000"></textarea>
					</div>
				</li>

				<div class="form-row clearfix primeiroRegistro">
					<div class="captcha">
				    	<div class="captcha-imagem">
				        	<img src="${urlImageCaptcha}?timestamp=${timestampImg.getTime()}" id="captchaImg_N" alt="Codigo" title="Codigo" />
				        </div>
				        <div class="captcha-controles">
				        	<div class="captcha-texto">
				            	<input type="text" name="captcha_response" onblur="update(this)" id="captchaTXT_N" placeholder="Digite o texto acima" />
				            </div>
				            <div class="captcha-refresh">
				            
				            	<a href="javascript:void(0)" onclick="refreshCaptcha()">
				            		<img src="${pageContext.servletContext.contextPath}/images/captcha-refresh.png" title="Atualizar o código" alt="Atualizar o código"  />
				            	</a>
				            
				            </div>
				            <div class="captcha-audio">
				            	<a href="javascript:void(0)" onclick="playSound('audioCaptcha_N')">
				            		<img src="${pageContext.servletContext.contextPath}/images/captcha-audio.png" title="Ouça o código e digite no campo ao lado" alt="Ouça o código e digite no campo ao lado" />
				            	</a>
				            	<audio preload="auto" autobuffer id="audioCaptcha_N"> 
  									<source src="${urlAudioCaptcha}" id="srcAudio_N" />
 								</audio>
				            </div>
				        </div>
				    </div>
				</div>

				<li class=" clearfix msgErroPasso2"
					<c:if test="${msgErro == null or msgErro == ''}">style="display: none;"</c:if>>
					<div class="feedback feedback-error control-span-6_12"">                                        
						<span class="feedback-text">Houve um erro, por favor corrija os seguintes campos: <span id="msgErroPasso2">${msgErro}</span>.</span>
						<span class="dialog-close feedback-close"><i
							class="font-icon i-cancel"></i>
						</span>
					</div>
				</li>
				
				<li class="form-row clearfix primeiroRegistro">
					<div class="control-item control-span-3_12">
						<button name="passo" value="2" type="submit"
							class="submit-d submit-blue js-form-next validate-user">
							<i class="font-icon i-down-dir"></i>Continuar
						</button>
						<span class="secondary-action"> ou <a
							href="javascript:void(0);" class="js-form-prev" id="linkVoltaPasso2">volte à etapa
								anterior</a> </span>
					</div></li>
			</ul>
		</fieldset>
		<!-- FINAL DO FORMULARIO DE PRIMEIRA RECLAMAÇÃO -->
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
			case "buscaReclamacao":
				url = '${buscaReclamacao}';
				break;
			case "validaManifesto":
				url = '${validaManifesto}';
				break;
			case "1":
				url = '${urlAjaxPasso1}';
				break;
			case "2":
				url = '${urlAjaxPasso2}';
				break;
			default:
				url = '${urlAjaxPasso1}';
				break;
			};

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
						
						if(process=="true"){
							$("#_form").submit();
						}
						if(passo=="buscaReclamacao"){
							populaDetalheReclamacao(xml);
						}
					} else {
						//exibe msg erro
						$("#msgErroPasso" + passo).text(msgErro);
						$(".msgErroPasso" + passo).show();
					}
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

		$("#tenhoPedido").click(function(){
			$(".formPedido").show();
			$(".dadoReclamacao").show();
			$(".primeiroRegistro").hide();
			$("#_form").prop("action","${reabrirReclamacao}");
			var data = new Date();
			$("#captchaTXT_V").prop('disabled',false);
			$("#captchaTXT_N").prop('disabled',true);
			

		});
		$("#naoTenhoPedido").click(function(){
			$(".formPedido").hide();
			$(".dadoReclamacao").hide();
			$(".primeiroRegistro .form-row").hide();
			$(".primeiroRegistro .form-row:eq(0)").show();
			$(".primeiroRegistro").show();
			$(".primeiroRegistro").removeClass("fieldset-inactive");
			$(".msgErroPassobuscaReclamacao").hide();
			$("#_form").prop("action","${urlAcao}");
			var data = new Date();
			$("#captchaTXT_N").prop('disabled',false);
			$("#captchaTXT_V").prop('disabled',true);
			$(".js-form-prev").trigger("click");
			
		});
		

		$("#tenhoConta").click(function() {
			//$(".form-row").show();
			$("#liconta").show();
			$("#lioperacao").show();
			$(".indentificacao").show();
		});

		$("#naoTenhoConta").click(function() {
			//$(".form-row").show();
			$("#liconta").hide();
			$("#lioperacao").hide();
			$(".indentificacao").show();
		});
		

		
		
		
		
	
		//mascaras
		//$("#cpf").mask("999.999.999-99");
		$("#ddd").mask("99?99");
		$("#agencia").mask("9?9999999");
		$("#protocoloCPF").mask("9?9999999999");
		//funcao para os telefones de sao paulo
		//$("#tel").mask("9999-9999?9",{reverse:true});
		
		
		 $('textarea[maxlength]').keyup(function(){  
        //get the limit from maxlength attribute  
        var limit = parseInt($(this).attr('maxlength'));  
        //get the current text inside the textarea  
        var text = $(this).val();  
        //count the number of characters in the text  
        var chars = text.length;  
  
        //check if there are more characters then allowed  
        if(chars > limit){  
            //and if there are use substr to get the text before the limit  
            var new_text = text.substr(0, limit);  
  
            //and change the current text with the new text  
            $(this).val(new_text);  
        }  
    }); 
    
    $(".dialog-close .font-icon.i-cancel").click(function(){
	    $(this).parents("li").hide("slow");
	});
    
    $(".carregando").removeClass("carregando");
	
	
	function refreshCaptcha(){
		var url = "${urlCaptchaTokenRefresh}"; 
		$.ajax({
				type : "POST",
				url : url,
				async : false,
				success: function(data){
					
					console.log(data);
					var resultado = JSON.parse(data);
					$("#tokenCaptcha").val(resultado.tokenCaptcha);

					$("#captchaImg_V").attr("src",resultado.imgCaptchaURL);
					$("#srcAudio_V").attr("src",resultado.soundCaptchaURL);				
					$("#audioCaptcha_V")[0].load();
					
					$("#captchaImg_N").attr("src",resultado.imgCaptchaURL);
					$("#srcAudio_N").attr("src",resultado.soundCaptchaURL);				
					$("#audioCaptcha_N")[0].load();
					
				}
		});
	}
	
	//});
	
	
	function update(field){
			var valorAtual = field.value;
		var name = field.name;
		var campos = document.getElementsByName(name);
		for(var i=0; i<campos.length;i++){
				campos[i].value=valorAtual;
		}
	}
	
	
</script>