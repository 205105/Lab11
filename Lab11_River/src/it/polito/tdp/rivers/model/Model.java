package it.polito.tdp.rivers.model;

import java.util.List;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {
	
List<River> rivers = null ;
	
	public List<River> getRivers() {
		if (rivers!=null)
			return rivers ;
		else {
			RiversDAO dao = new RiversDAO() ;
			rivers = dao.getAllRivers() ;
			return this.rivers ;
		}
	}

}
