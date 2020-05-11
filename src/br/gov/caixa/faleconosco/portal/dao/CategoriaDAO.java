package br.gov.caixa.faleconosco.portal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.caixa.faleconosco.portal.dto.CategoriaDTO;

public class CategoriaDAO extends JdbcDAO {

	Connection conn;
	public List<CategoriaDTO> list() throws SQLException{
		List<CategoriaDTO> retorno = new ArrayList<CategoriaDTO>();
		String sql = "select CODIGO, NOME from ouv.OUVVW041_CATEGORIA_INTERNET order by NOME ASC";
		conn = getConnection();
		PreparedStatement ps = conn.prepareCall(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			CategoriaDTO dto = new CategoriaDTO();
			dto.setCodigo(Integer.valueOf(rs.getInt("CODIGO")));
			dto.setNome(rs.getString("NOME"));
			retorno.add(dto);
		}
		rs.close();
		ps.close();
		conn.close();
		return retorno;
	}
	public CategoriaDTO getCategoria(Integer codigo) throws SQLException{
		CategoriaDTO retorno = null;
		String sql = "select CODIGO, NOME from ouv.OUVVW041_CATEGORIA_INTERNET WHERE CODIGO = ?";
		conn = getConnection();
		PreparedStatement ps = conn.prepareCall(sql);
		ps.setInt(1, codigo);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			retorno = new CategoriaDTO();
			retorno.setCodigo(Integer.valueOf(rs.getInt("CODIGO")));
			retorno.setNome(rs.getString("NOME"));
			
		}
		rs.close();
		ps.close();
		conn.close();
		return retorno;
	}
	
}