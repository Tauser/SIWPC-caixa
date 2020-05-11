package br.gov.caixa.faleconosco.portal.service;

import java.util.ArrayList;
import java.util.List;

import br.gov.caixa.faleconosco.portal.dao.CategoriaDAO;
import br.gov.caixa.faleconosco.portal.dao.OcorrenciaDAO;
import br.gov.caixa.faleconosco.portal.dto.CategoriaDTO;
import br.gov.caixa.faleconosco.portal.dto.OcorrenciaDTO;
import br.gov.caixa.faleconosco.portal.util.Util;

public class FaleConoscoService {
	
	public Integer gravaFormulario(OcorrenciaDTO dto)throws Exception{
		OcorrenciaDAO dao = new OcorrenciaDAO();
		Integer protocolo = dao.salvar(dto);
		return protocolo;
	}
	
	public List<CategoriaDTO> listarCategorias() throws Exception{
		CategoriaDAO catDAO = new CategoriaDAO();
		return catDAO.list();
		
	}
	
	public OcorrenciaDTO buscaReclamacao(String pedidoCPF) throws Exception{
		OcorrenciaDAO ocorDAO = new OcorrenciaDAO();
		OcorrenciaDTO ocorrencia = ocorDAO.buscaReclamacao(pedidoCPF);
		if(!Util.isEmpty(ocorrencia)){
			CategoriaDAO catDAO = new CategoriaDAO();
			CategoriaDTO categoria = catDAO.getCategoria(Integer.valueOf(ocorrencia.getCategoria()));
			if(!Util.isEmpty(categoria)){
				ocorrencia.setNoCategoria(categoria.getNome());
			}
		}
		return ocorrencia;
	}
	
	public OcorrenciaDTO recuperar(String protocolo) throws Exception{
		OcorrenciaDAO ocorDAO = new OcorrenciaDAO();
		OcorrenciaDTO ocorrencia = ocorDAO.recupera(protocolo);
		if(!Util.isEmpty(ocorrencia)){
			CategoriaDAO catDAO = new CategoriaDAO();
			CategoriaDTO categoria = catDAO.getCategoria(Integer.valueOf(ocorrencia.getCategoria()));
			if(!Util.isEmpty(categoria)){
				ocorrencia.setNoCategoria(categoria.getNome());
			}
		}
		return ocorrencia;
	}

	public List<CategoriaDTO> listarCategoriasParaDenuncias() {
		List<CategoriaDTO> retorno = new ArrayList<CategoriaDTO>();

		retorno.add(new CategoriaDTO(1, "Atendimento - Ag�ncia"));
		retorno.add(new CategoriaDTO(2, "Atendimento - Lot�ricas / Correspondentes banc�rios"));
		retorno.add(new CategoriaDTO(3, "Atendimento - Telesservi�os"));
		retorno.add(new CategoriaDTO(4, "Bolsa fam�lia"));
		retorno.add(new CategoriaDTO(5, "Cart�o Cidad�o"));
		retorno.add(new CategoriaDTO(6, "Cart�es"));
		retorno.add(new CategoriaDTO(7, "Conectividade Social"));
		retorno.add(new CategoriaDTO(8, "Conta Corrente"));
		retorno.add(new CategoriaDTO(9, "Conta Poupan�a"));
		retorno.add(new CategoriaDTO(10, "Contas -  Abertura Fraudulenta"));
		retorno.add(new CategoriaDTO(11, "Contas Utilizadas para Golpe"));
		retorno.add(new CategoriaDTO(12, "Corrup��o"));
		retorno.add(new CategoriaDTO(13, "Demais Programas e Benef�cios Sociais"));
		retorno.add(new CategoriaDTO(14, "Demais Servi�os Banc�rios"));
		retorno.add(new CategoriaDTO(15, "Empr�stimos"));
		retorno.add(new CategoriaDTO(16, "FGTS - Contas e Saque"));
		retorno.add(new CategoriaDTO(17, "FIES"));
		retorno.add(new CategoriaDTO(18, "Financiamento Habitacional"));
		retorno.add(new CategoriaDTO(19, "Internet - Internet Banking CAIXA"));
		retorno.add(new CategoriaDTO(20, "Internet - P�gina da CAIXA na Internet"));
		retorno.add(new CategoriaDTO(21, "Minha Casa Melhor"));
		retorno.add(new CategoriaDTO(22, "PIS - Abono e Rendimentos"));
		retorno.add(new CategoriaDTO(23, "Programa Minha Casa Minha Vida"));
		retorno.add(new CategoriaDTO(24, "Seguros/Cons�rcios/Capitaliza��o"));
		retorno.add(new CategoriaDTO(25, "Outras Den�ncias"));

		return retorno;
	}

	public String getNomeCategoriaDenuncia(String categoria) {
		
		int nuCategoria =	Integer.parseInt(categoria);
		List<CategoriaDTO> categoriasDeDenuncia = listarCategoriasParaDenuncias();
		for (CategoriaDTO categoriaDTO : categoriasDeDenuncia) {
			if(nuCategoria== categoriaDTO.getCodigo()){
				return categoriaDTO.getNome();
			}
		}
		System.out.println("Categoria de Denuncia nao encontrada : "+nuCategoria);
		return "";
	}
}