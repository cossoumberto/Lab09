package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer,Country> idMap) {
 
		String sql = "SELECT CCode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				idMap.put(rs.getInt("CCode"), new Country(rs.getInt("CCode"), rs.getString("StateAbb"), rs.getString("StateNme")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Country> getCountryConnessiAnno(Map<Integer, Country> idMap, int anno) {

		String sql = "SELECT DISTINCT state1no FROM contiguity WHERE YEAR<=?";
		List<Country> list = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				list.add(idMap.get(rs.getInt("state1no")));
			}
			conn.close();
			return list;
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Border> getBorder(Map<Integer, Country> idMap, List<Country> countryConnessiAnno) {
		
		String sql = "SELECT state1no, state2no FROM contiguity WHERE conttype=1";
		List<Border> list = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Country c1 = idMap.get(rs.getInt("state1no"));
				Country c2 = idMap.get(rs.getInt("state2no"));
				if(countryConnessiAnno.contains(c1) && countryConnessiAnno.contains(c2))
					list.add(new Border(c1, c2));
			}
			conn.close();
			return list;
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
