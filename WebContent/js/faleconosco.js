/**
 * 
 */

function showNISLabel(field,event){
	if(field.value==4){
		$("#NISLabel").show();
		$("#NISLabelReclamacao").show();
	}else{
		$("#NISLabel").hide();
		$("#NISLabelReclamacao").hide();

	}
} 

function playSound(id) {
              document.getElementById(id).play();
          }


function hifen(field, event) {
	// var input = document.getElementById('campoconta');
	var keyCode = event.keyCode ? event.keyCode : event.which ? event.which
			: event.charCode;
	if (keyCode != 46 && keyCode != 8) {

		var campo;
		var size;
		var temp1;
		var temp2;

		campo = field.value;// document.getElementById('campoconta').value;
		campo = campo.replace("-", "");
		size = campo.length;
		if (size >= 2) {
			temp1 = campo.slice(0, size);
			temp2 = campo.slice(size, size);
			campo = temp1 + "-" + temp2;

		}
		// document.getElementById('campoconta').value = campo;
		field.value = campo;
	}
}

function hifenNotLast(field){
	var valor = field.value;
	var index = valor.indexOf("-");
	if(index!=-1){
		if(index==(valor.length-1)){
			var inicio = valor.substring(0,index-1);
			var fim = valor.substring(index-1,index);
			field.value=inicio+"-"+fim;
		}
	}
}


function validateEmail(field) {
	if (field.value == '') {
		return true;
	}
	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	if (re.test(field.value)) {
		return true;
	} else {
		alert("Voc\u00ea digitou um endere\u00e7o inv\u00e1lido, por favor corriga o email.");
		field.focus();
	}
}

function show(elem) {
	elem.style.display = "block";
}

function hide(elem) {
	elem.style.display = "";
}

function populaDetalheReclamacao(xml){
	$("#spanProtocolo").text($(xml).find("protocolo").text());
	$("#spanNoSituacao").text($(xml).find("situacao").text());
	$("#spanNomeCategoria").text($(xml).find("nomeCategoria").text());
	$("#spanDataEnvio").text($(xml).find("dataEnvio").text());
	var icRelacionamento = $(xml).find("icRelacionamento").text();
	if(icRelacionamento=="S"){
		$("#tenhoContaDisable").prop("checked",true);
	}else{
		$("#naoTenhoContaDisable").prop("checked",true);
	}

	$("#nomeDisabled").val($(xml).find("nome").text());
	$("#categoriaReclamacao").val($(xml).find("categoria").text());
}


if(!$(".wpthemeHeader").length){
	$(document).ready(function() {


    // Visually enhance selects 
    var label;
    var newLabel;
    var $selectButton;
    
    $('select').each(function() {
        label = $(this).children('option:selected').text();
        
        $(this).css('opacity','0');
        $(this).wrap('<div class="select-button" />');
        $(this).before('<span class="select-label">' + label + '</span><i class="i-justified i-justified-r font-icon i-down-dir"></i>');
    });
    $('select').change(function() {
        newLabel = $(this).children('option:selected').text();
        $selectButton = $(this).parent('.select-button');
        
        $selectButton.children('.select-label').text(newLabel);
    });
});}


function MascaraTelefone(telefone){
	
    if(mascaraInteiro(telefone)==false){
            event.returnValue = false;
    }       
    return formataCampo(telefone, '0000-00000', event);
}

function MascaraCPF(cpf){
	
    if(mascaraInteiro(cpf)==false){
            event.returnValue = false;
    }       
    return formataCampo(cpf, '000.000.000-00', event);
}

//formata de forma generica os campos
function formataCampo(campo, Mascara, evento) { 
        var boleanoMascara; 

        var Digitato = evento.keyCode;
        exp = /\-|\.|\/|\(|\)| /g
        campoSoNumeros = campo.value.toString().replace( exp, "" ); 

        var posicaoCampo = 0;    
        var NovoValorCampo="";
        var TamanhoMascara = campoSoNumeros.length;; 

        if (Digitato != 8) { // backspace 
                for(i=0; i<= TamanhoMascara; i++) { 
                        boleanoMascara  = ((Mascara.charAt(i) == "-") || (Mascara.charAt(i) == ".")
                                                                || (Mascara.charAt(i) == "/")) 
                        boleanoMascara  = boleanoMascara || ((Mascara.charAt(i) == "(") 
                                                                || (Mascara.charAt(i) == ")") || (Mascara.charAt(i) == " ")) 
                        if (boleanoMascara) { 
                                NovoValorCampo += Mascara.charAt(i); 
                                  TamanhoMascara++;
                        }else { 
                                NovoValorCampo += campoSoNumeros.charAt(posicaoCampo); 
                                posicaoCampo++; 
                          }              
                  }      
                campo.value = NovoValorCampo;
                  return true; 
        }else { 
                return true; 
        }
}

function mascaraInteiro(){
	var key = 'which' in event ? event.which : event.keyCode;
    if (key < 48 || key > 57){
            event.returnValue = false;
            return false;
    }
    return true;
}