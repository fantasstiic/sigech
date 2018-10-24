package com.rrsol.geogov.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

@Component
public class DataAccess {

	public Map<String, Object> executeSP(String nomProcedimiento, Object[][] parametro, JdbcTemplate jdbcTemplate,
			String schema_properties) {
		try {
			Map<String, Object> parametrosIngresoMap = new HashMap<String, Object>();
			SimpleJdbcCall info = new SimpleJdbcCall(jdbcTemplate).withProcedureName(nomProcedimiento)
					.withSchemaName(schema_properties).withoutProcedureColumnMetaDataAccess();
			for (int x = 0; x < parametro.length; x++) {
				for (int i = 0; i < parametro[x].length - 1; i++) {
					if (parametro[x][i + 1] instanceof Integer) {
						info.declareParameters(
								new SqlParameter(String.valueOf(parametro[x][i]), java.sql.Types.INTEGER));
					} else if (parametro[x][i + 1] instanceof Double) {
						info.declareParameters(
								new SqlParameter(String.valueOf(parametro[x][i]), java.sql.Types.DOUBLE));
					} else {
						info.declareParameters(
								new SqlParameter(String.valueOf(parametro[x][i]), java.sql.Types.VARCHAR));
					}
					parametrosIngresoMap.put(String.valueOf(parametro[x][i]), parametro[x][i + 1]);
					break;
				}
			}

			SqlParameterSource parametrosCountDataFile = new MapSqlParameterSource().addValues(parametrosIngresoMap);

			Map<String, Object> parametrosSalida = info.execute(parametrosCountDataFile);
			return parametrosSalida;
		} catch (Exception e) {
			System.out.println(
					"Error en el Store1 " + e.getMessage() + " Procedure " + nomProcedimiento + " Exception " + e);
			return null;
		}

	}

}
