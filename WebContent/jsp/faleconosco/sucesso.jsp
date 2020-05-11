<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="p"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<p:defineObjects />

<p:renderURL portletMode="view" var="url"/>
<p:resourceURL var="urlDownload" id="download" >
	<p:param name="protocolo" value="${protocolo}"/>
</p:resourceURL>
<div class="content-section section-services">
	<div class="legend clearfix confirma">
		<span class="legend-step delta">3</span>
		<div class="legend-info confirma">
			<h2 class="legend-title delta">Confirmação</h2>
		</div>
	</div>
	<ul class="form-set no-bullets">
		<li class="form-row clearfix">
			<c:choose>
				<c:when test="${nome eq 'elogio' }">
					<h4 class="zeta">Seu ${nome} foi enviado com sucesso! </h4>
					<p class="description">
					Anote seu número de ${nome}:
					<strong>${protocoloStr}</strong>
					<br/>
					Obrigado pelo seu contato.
					</p>
					<p class="description">
						Sua opini&atilde;o &eacute; muito importante para que possamos sempre melhorar nossos serviços.
					</p>
					<p class="action submit-d submit-blue">
						<a href="${url}" class="btn blue btn-small non-fluid">Enviar outro ${nome}</a>
					</p>
					<p class="action no-margin">
							<a href="${urlDownload}">Imprimir PDF</a>
					</p>
				</c:when>
				<c:otherwise>
					<h4 class="zeta">Sua ${nome} foi enviada com sucesso! </h4>
					<p class="description">
						Anote seu número de ${nome}:
						<strong>${protocoloStr}</strong>
						<br/>
						Obrigado pelo seu contato.
					</p>
					<p class="description">
						Sua opini&atilde;o &eacute; muito importante para que possamos sempre melhorar nossos serviços.
					</p>
					<p class="action submit-d submit-blue">
						<a href="${url}" class="btn blue btn-small non-fluid">Enviar outra ${nome}</a>
					</p>
					<p class="action no-margin">
							<a href="${urlDownload}">Imprimir PDF</a>
					</p>
					
				</c:otherwise>
			</c:choose>
			
		</li>
	</ul>
</div>