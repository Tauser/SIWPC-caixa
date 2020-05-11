package br.gov.caixa.faleconosco.portal.dao;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JdbcDAO {
	
	protected Connection getConnection() {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("jdbc/faleconosco-ds");
			Connection connection = ds.getConnection();

			return connection;

		} catch (Exception e) {
			System.out.println("ERRO AO EXTABELECER CONEXAO: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}