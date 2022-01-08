
package sparqlquery;

import java.io.IOException;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


public class SparqlQuery {

	public static void main(String[] args) throws JsonSyntaxException, JsonIOException, OWLOntologyCreationException, IOException {


		String test = createSparqlQuery();

		System.out.println(test);
		
		String rdf4jServer = "http://78.91.106.145:8080/rdf4j-server";
		String repositoryId = "LATKG_TEST";
		
		int numTriples = numStatements(rdf4jServer, repositoryId);
		
		System.out.println("Repository " + repositoryId + " contains " + numTriples + " triples.");


	}


	public static int numStatements(String rdf4jServer, String repositoryId) {
		
		int numStatements = 0;
		
		Repository repo = new HTTPRepository(rdf4jServer, repositoryId);
		
		try (RepositoryConnection conn = repo.getConnection()) {

			   String queryString = "SELECT (COUNT(*) as ?Triples) WHERE { ?s ?p ?o} ";
			   TupleQuery tupleQuery = conn.prepareTupleQuery(queryString);
			   try (TupleQueryResult result = tupleQuery.evaluate()) {
			      while (result.hasNext()) {  
			         BindingSet bindingSet = result.next();
			         numStatements = Integer.parseInt(bindingSet.getValue("Triples").stringValue());

			      }
			   }
			}
		repo.shutDown();
		
		return numStatements;
		
	}
	
	

	public static String createSparqlQuery() {


		String strQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n";
		strQuery += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n";
		strQuery += "PREFIX lat: <http://latuli.no/onto#>  \n";
		strQuery += "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";
		strQuery += "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n";

		strQuery += "PREFIX geo: <http://www.opengis.net/ont/geosparql#> \n";
		strQuery += "PREFIX geof: <http://www.opengis.net/def/function/geosparql/> \n";
		strQuery += "PREFIX uom: <http://www.opengis.net/def/uom/OGC/1.0/> \n";


		strQuery += "\nSELECT DISTINCT ?xDocLoadingUnitInd ?loadingUnitForXDoc ?inboundConsignmentId ?outboundConsignmentId \n";


		strQuery += "\nWHERE { \n";

		
		strQuery += "\n?xDocLoadingUnitInd rdf:type ?xDocLoadingUnit .\n";
		strQuery += "?xDocLoadingUnitInd lat:loadingUnitForXDocLoadingUnit ?loadingUnitForXDoc .\n";
		strQuery += "?xDocLoadingUnitInd lat:inboundConsignmentId ?inboundConsignmentId .\n";
		strQuery += "?xDocLoadingUnitInd lat:outboundConsignmentId ?outboundConsignmentId .\n";

		strQuery += "}";

		//System.out.println(strQuery);

		return strQuery;
	}





}