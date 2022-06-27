package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	public EventsDao dao;
	List<Integer> vertici;
	Graph<Integer, DefaultWeightedEdge> grafo;
	
	
	public Model() {
		dao= new EventsDao();
		
		
	}
	public void creaGrafo(int anno) {
		vertici = this.dao.getVertici();
		grafo = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, vertici);
		
		for(Integer v1 : this.grafo.vertexSet()) {
			for(Integer v2 : this.grafo.vertexSet()) {
				if(!v1.equals(v2)) {
					if(this.grafo.getEdge(v1, v2) == null) {
						Double latMediaV1 = dao.getLatMedia(anno, v1);
						Double latMediaV2 = dao.getLatMedia(anno, v2);
						
						Double lonMediaV1 = dao.getLonMedia(anno, v1);
						Double lonMediaV2 = dao.getLonMedia(anno, v2);
						
						Double distanzaMedia = LatLngTool.distance(new LatLng(latMediaV1,lonMediaV1), 
																	new LatLng(latMediaV2, lonMediaV2), 
																	LengthUnit.KILOMETER);
						
						Graphs.addEdgeWithVertices(this.grafo, v1, v2, distanzaMedia);
						
					}
				}
			}
		}
	}
	
	public List<Integer> getAnni() {
		return dao.getAnno();
	}
	
	
	
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
		
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	
	}
	
	//PER OTTENERE TUTTI I VICINI
	
	public List<Vicino> getAdiacenti (Integer distretto) {
		List<Vicino> vicini = new ArrayList<Vicino>();
		List<Integer> viciniID= Graphs.neighborListOf(this.grafo, distretto);
		for (Integer i: viciniID) {
			vicini.add(new Vicino(i, this.grafo.getEdgeWeight(this.grafo.getEdge(distretto, i))));
		}
		Collections.sort(vicini);
		return vicini;
	}
	public Set<Integer> getVertici() {
		return this.grafo.vertexSet();
	}
}
	
