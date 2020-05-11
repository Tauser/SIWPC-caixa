package br.gov.caixa.faleconosco.portal.dao;

import java.io.IOException;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import br.gov.caixa.faleconosco.portal.dto.OcorrenciaDTO;
import br.gov.caixa.faleconosco.portal.util.Util;

public class OcorrenciaDAO extends JdbcDAO {

	Connection conn;
	public Integer salvar(OcorrenciaDTO dto) throws SQLException{
		
		String sql = "{CALL OUV.OUVSP106_INCLR_OCRNA_INTERNET(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		conn = getConnection();
		CallableStatement cs = conn.prepareCall(sql);
		int index =1;
		if(!Util.isEmpty(dto.getCpf())){
			cs.setLong(index++, Long.valueOf(Util.onlyNumbers(dto.getCpf())));
		}else{
			cs.setNull(index++, Types.BIGINT);
		}
		
		cs.setString(index++, dto.getNome());
		cs.setString(index++, dto.getNoCidade());
		cs.setString(index++, dto.getEmail());
		cs.setString(index++, dto.getDdd());
		cs.setString(index++, dto.getTel());
		cs.setNull(index++, Types.CHAR); //ddd comercial
		cs.setNull(index++, Types.VARCHAR); //tel comercial
		cs.setString(index++, dto.getSgUf());
		cs.setNull(index++, Types.INTEGER); //nu residencia
		cs.setNull(index++, Types.LONGNVARCHAR); //complemento residencia
		cs.setNull(index++, Types.INTEGER); //logradouro
		cs.setNull(index++, Types.INTEGER); //localidade
		cs.setString(index++, dto.getOperacaoConta());
		if(!Util.isEmpty(dto.getAgencia())){
			cs.setInt(index++, Integer.valueOf(Util.onlyNumbers(dto.getAgencia())));
		}else{
			cs.setNull(index++, Types.INTEGER);
		}
		
		cs.setNull(index++, java.sql.Types.NULL); //nu natural agencia'
		cs.setTimestamp(index++, new java.sql.Timestamp(System.currentTimeMillis()));
		cs.setCharacterStream(index++, new StringReader(dto.getMensagem()),dto.getMensagem().length());
		cs.setNull(index++, Types.INTEGER);  //protocolo
		cs.setInt(index++, Integer.valueOf(dto.getCategoria()));
		cs.setInt(index++, dto.getNaturezaOcor());
		cs.setInt(index++, dto.getTipo());
		cs.registerOutParameter(index, java.sql.Types.INTEGER);
		cs.executeUpdate(); 
		Integer protolo = cs.getInt(index);
		return protolo;
	}
	public OcorrenciaDTO buscaReclamacao(String pedidoCPF) throws SQLException, IOException {
		
		OcorrenciaDTO dto = null;
		StringBuffer sb = new StringBuffer("select * from (select * from ouv.OUVVW042_OCORRENCIA_INTERNET where (cpf = ?) AND dhocorrencia <= (SYSDATE-7) and not dhocorrencia >= (SYSDATE+62) order by protocolo DESC) where rownum <= 1 union all select * from (select * from ouv.OUVVW042_OCORRENCIA_INTERNET where (protocolo = ?) AND dhocorrencia <= (SYSDATE-7) and not dhocorrencia >= (SYSDATE+62) order by protocolo DESC) where rownum <= 1 ");
		//StringBuffer sb = new StringBuffer("select * from ouv.OUVVW042_OCORRENCIA_INTERNET where rownum = 1 and PROTOCOLO = ?");
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sb.toString());
		
		int index = 1;
		ps.setString(index++, pedidoCPF);
		ps.setString(index++, pedidoCPF);
		
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			dto = new OcorrenciaDTO();
			dto.setNome(rs.getString("NOME"));
			dto.setProtocolo(rs.getLong("PROTOCOLO"));
			dto.setNaturezaOcor(rs.getInt("NUNATUREZA"));
			dto.setDataEnvio(rs.getDate("DHOCORRENCIA"));
			dto.setNoSituacaoOcorrencia(rs.getString("NOSITUACAOOCORRENCIA"));
			
			Long cpf = rs.getLong("CPF");
			if(!Util.isEmpty(cpf)){
				dto.setCpf(cpf.toString());
			}
			
			Long agencia = rs.getLong("NUUNIDADEAGENCIA");
			if(!Util.isEmpty(agencia)){
				dto.setAgencia(agencia.toString());
				dto.setIcRelacionamento("S");
			}else{
				dto.setIcRelacionamento("N");
			}
			
			String operacaoConta = rs.getString("OPERACAOCONTA");
			if(!Util.isEmpty(operacaoConta)){
				dto.setOperacao(operacaoConta.trim().substring(0,3));
				dto.setConta(operacaoConta.trim().substring(3));
			}
			
			dto.setEmail(rs.getString("EMAIL"));
			
			String telefoneComPrefixo = rs.getString("TELEFONECPREFIXO");
			if(!Util.isEmpty(telefoneComPrefixo)){
				dto.setDdd(telefoneComPrefixo.substring(0, 2));
				dto.setTel(telefoneComPrefixo.substring(2));
			}
			
			dto.setSgUf(rs.getString("UF"));
			
			String complemento = rs.getString("COMPLEMENTO");
			if(!Util.isEmpty(complemento)){
				dto.setNoCidade(complemento.replaceAll("Cidade:", ""));
			}
			dto.setCategoria(Integer.toString(rs.getInt("CATEGORIA")));
			dto.setMensagem(Util.readerToString(rs.getClob("MENSAGEM").getCharacterStream()));
		}
		
		
		return dto;
	}
	
	public OcorrenciaDTO recupera(String protocolo) throws SQLException, IOException {
		
		OcorrenciaDTO dto = null;
		
		StringBuffer sb = new StringBuffer("select * from ouv.OUVVW042_OCORRENCIA_INTERNET where rownum = 1 and PROTOCOLO = ?");
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sb.toString());
		
		int index = 1;
		ps.setString(index++, protocolo);
		
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			dto = new OcorrenciaDTO();
			dto.setNome(rs.getString("NOME"));
			dto.setProtocolo(rs.getLong("PROTOCOLO"));
			dto.setNaturezaOcor(rs.getInt("NUNATUREZA"));
			dto.setDataEnvio(rs.getDate("DHOCORRENCIA"));
			dto.setDhOcorrencia(rs.getTimestamp("DHOCORRENCIA"));
			
			dto.setNoSituacaoOcorrencia(rs.getString("NOSITUACAOOCORRENCIA"));
			
			Long cpf = rs.getLong("CPF");
			if(!Util.isEmpty(cpf)){
				dto.setCpf(cpf.toString());
			}
			
			Long agencia = rs.getLong("NUUNIDADEAGENCIA");
			if(!Util.isEmpty(agencia)){
				dto.setAgencia(agencia.toString());
				dto.setIcRelacionamento("S");
			}else{
				dto.setIcRelacionamento("N");
			}
			
			String operacaoConta = rs.getString("OPERACAOCONTA");
			if(!Util.isEmpty(operacaoConta)){
				dto.setOperacao(operacaoConta.trim().substring(0,3));
				dto.setConta(operacaoConta.trim().substring(3));
			}
			
			dto.setEmail(rs.getString("EMAIL"));
			
			String telefoneComPrefixo = rs.getString("TELEFONECPREFIXO");
			if(!Util.isEmpty(telefoneComPrefixo)){
				dto.setDdd(telefoneComPrefixo.substring(0, 2));
				dto.setTel(telefoneComPrefixo.substring(2));
			}
			
			dto.setSgUf(rs.getString("UF"));
			
			String complemento = rs.getString("COMPLEMENTO");
			if(!Util.isEmpty(complemento)){
				dto.setNoCidade(complemento.replaceAll("Cidade:", ""));
			}
			dto.setCategoria(Integer.toString(rs.getInt("CATEGORIA")));
			dto.setMensagem(Util.readerToString(rs.getClob("MENSAGEM").getCharacterStream()));
		}
		
		
		return dto;
	}

	
	
}
