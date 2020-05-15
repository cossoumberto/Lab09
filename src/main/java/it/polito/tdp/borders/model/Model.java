package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private Map<Integer, Country> idMap;
	private BordersDAO dao;
	private Graph <Country, DefaultEdge> grafo;
	private List<Country> countryConnessiAnno;
	
	public Model() {
		idMap = new HashMap<>();
		dao = new BordersDAO();
	}

	public List<Country> loadAllCountries() {
		dao.loadAllCountries(idMap);
		List<Country> list = new LinkedList<>(idMap.values());
		Collections.sort(list);
		return list;
	}
	public Map<Integer, Country> getIdMap() {
		return idMap;
	}
	
	public void creaGrafo(int anno) {
		dao.loadAllCountries(idMap);
		grafo = new SimpleGraph<>(DefaultEdge.class);
		
		//crea vertici
		countryConnessiAnno = new ArrayList<>(dao.getCountryConnessiAnno(idMap, anno));
		Graphs.addAllVertices(grafo, countryConnessiAnno);
		
		//crea archi
		for(Border b : dao.getBorder(idMap, countryConnessiAnno))
			grafo.addEdge(b.getC1(), b.getC2());
	}
	
	public Graph<Country, DefaultEdge> getGrafo() {
		return this.grafo;
	}
	
	public int getComponentiConnesse()  {
		ConnectivityInspector<Country, DefaultEdge> ci = new ConnectivityInspector<>(grafo);
		return ci.connectedSets().size();
	}
	
	public int getComponentiConnesseNonSole()  {
		ConnectivityInspector<Country, DefaultEdge> ci = new ConnectivityInspector<>(grafo);
		List<Set<Country>> piuConnesse = new ArrayList<>();
		for(Set<Country> s : ci.connectedSets())
			if(s.size()>1)
				piuConnesse.add(s);
		return piuConnesse.size();
	}
	
	public List<Country> visitaAmpiezza(Country source) {
		List<Country> visita = new ArrayList<>();
		
		GraphIterator<Country, DefaultEdge> bfv = new BreadthFirstIterator<>(grafo, source);
		while(bfv.hasNext()) {
			visita.add( bfv.next() ) ;
		}
		return visita ;
	}
	
	public List<Country> visitaConRicorsiva(Country source) {
		List<Country> visita = new ArrayList<>();
		List<Country> parziale = new ArrayList<>();
		parziale.add(source);
		visita.add(source);
		cerca(parziale, 0, visita);
		return visita;
	}

	private void cerca(List<Country> parziale, int l, List<Country> visita) {
		if(Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1)).size()==0)
			return;
		if(visita.containsAll(Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))))
			return;
		for(Country c : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			if(!visita.contains(c)) {
				parziale.add(c);
				visita.add(c);
				cerca(parziale, l+1, visita);
			}
		}
		parziale.remove(parziale.size()-1);
	}
}
