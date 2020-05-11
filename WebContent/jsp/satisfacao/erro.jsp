<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="p"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<p:renderURL portletMode="view" var="url"/>
<div class="content-section section-services">
	<div class="legend clearfix confirma">
		<span class="legend-step delta">3</span>
		<div class="legend-info confirma">
			<h2 class="legend-title delta">Atenção</h2>
		</div>
	</div>
	<ul class="form-set no-bullets">
		<li class="form-row clearfix">
			<div class="icone-alerta">
				Erro
			</div>
		</li>
		<li class="form-row clearfix text-center">
			<h4 class="zeta"> Houve um problema durante sua avaliação </h4>
			<p class="description">
				<a href="${url}"> voltar</a>
			</p>
			<p class="description">
				Tente novamente mais tarde.
			</p>
		</li>
	</ul>
</div>