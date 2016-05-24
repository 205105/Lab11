package it.polito.tdp.rivers.db;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.River;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class RiversDAO {

	public List<River> getAllRivers() {
		final String sql = "SELECT id, name FROM river";

		List<River> rivers = new LinkedList<River>();

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet res = st.executeQuery();

			while (res.next()) {
			
			rivers.add(new River(res.getInt("id"), res.getString("name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return rivers;

	}

	public List<Flow> getAllFlows(List<River> rivers) {
		final String sql = "SELECT id, day, flow, river FROM flow";

		List<Flow> flows = new LinkedList<Flow>();

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet res = st.executeQuery();

			while (res.next()) {
				flows.add(new Flow(res.getDate("day").toLocalDate(), res.getDouble("flow"),
						rivers.get(rivers.indexOf(new River(res.getInt("river"))))));
			}

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return flows;

	}
	
	public LocalDate getFirstDate(River river){
		final String sql = "SELECT day from flow where river=? order by day asc";
		
		Connection conn = DBConnect.getConnection() ;
		
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			
			st.setInt(1, river.getId());
			
			ResultSet rs = st.executeQuery() ;
			
			rs.first();
			
			return rs.getDate(1).toLocalDate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw(new RuntimeException(e)) ;
		}
	}
	
	public LocalDate getLastDate(River river){
		final String sql = "SELECT day from flow where river=? order by day desc";
		
		Connection conn = DBConnect.getConnection() ;
		
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			
			st.setInt(1, river.getId());
			
			ResultSet rs = st.executeQuery() ;
			
			rs.first();
			
			return rs.getDate(1).toLocalDate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw(new RuntimeException(e)) ;
		}
	}
	
	public int getNumberOfMisurations(River river){
		final String sql = "SELECT count(*)  from flow where river=?";
		
		Connection conn = DBConnect.getConnection() ;
		
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			
			st.setInt(1, river.getId());
			
			ResultSet rs = st.executeQuery() ;
			
			rs.first();
			
			return rs.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw(new RuntimeException(e)) ;
		}
	}
	
	public float getMediumValue(River river){
		final String sql = "SELECT flow  from flow where river=?";
		float totale=0;
		Connection conn = DBConnect.getConnection() ;
		
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			
			st.setInt(1, river.getId());
			
			ResultSet rs = st.executeQuery() ;
			
			while(rs.next()){
				totale+=rs.getFloat(1);
			}
			
			return totale/this.getNumberOfMisurations(river);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw(new RuntimeException(e)) ;
		}
	}

	public static void main(String[] args) {
		RiversDAO dao = new RiversDAO();

		List<River> rivers = dao.getAllRivers();
		System.out.println(rivers);

		List<Flow> flows = dao.getAllFlows(rivers);
		System.out.format("Loaded %d flows\n", flows.size());
		//System.out.println(flows) ;
	}

}
