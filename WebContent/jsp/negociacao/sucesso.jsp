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
	
					<h4 class="zeta">Proposta enviada com sucesso! </h4>
					<p class="description">
						Anote o número do protocolo:
						<strong>${protocoloStr}</strong>
						<br>
						Sua proposta será analisada, aguarde nosso contato.
					</p>
					
					<p class="action no-margin">
							<a href="${urlDownload}">Imprimir PDF</a>
					</p>
					
				
			
		</li>
	</ul>
</div>