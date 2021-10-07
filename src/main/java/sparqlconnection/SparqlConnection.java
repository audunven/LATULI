package sparqlconnection;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

import sparqlquery.SparqlQuery;


public class SparqlConnection {


	static String SPARQL_ENDPOINT = "http://localhost:8080/rdf4j-server/repositories/LATULI_1?infer=false&limit=0&offset=0"; 

	public static void main(String[] args) {

		String strQuery = SparqlQuery.createSparqlQuery();

		Repository repository;

		Map<String, String> headers = new HashMap<String, String>();
		//headers.put("Authorization", AUTHORISATION_TOKEN);
		headers.put("accept", "application/JSON");

		repository = new SPARQLRepository(SPARQL_ENDPOINT);
		repository.initialize();
		((SPARQLRepository) repository).setAdditionalHttpHeaders(headers);

		TupleQuery tupleQuery = connect(repository, strQuery);
		
		try (TupleQueryResult result = tupleQuery.evaluate()) {
			
			int counter = 0;
			
			while (result.hasNext()) {
				counter++;
				BindingSet solution = result.next();


			}
			
			System.out.println("Number of results: " + counter);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	public static TupleQuery connect (Repository repository, String strQuery) {

		TupleQuery tupleQuery;

		try (RepositoryConnection conn = repository.getConnection()) {
			tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, strQuery);


			return tupleQuery;
		}


	}

}
