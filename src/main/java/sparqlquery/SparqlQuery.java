
package sparqlquery;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


public class SparqlQuery {

	public static void main(String[] args) throws JsonSyntaxException, JsonIOException, OWLOntologyCreationException, IOException {


		String test = createSparqlQuery();

		System.out.println(test);


	}



	public static String createSparqlQuery() {


		String strQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n";
		strQuery += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n";
		strQuery += "PREFIX lat: <http://latuli.no/onto#>  \n";
		strQuery += "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";
		strQuery += "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n";

		//include necessary prefixes if supplier distance is to be included
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





	/**
	 * Get the elements that are common from a variable number of sets
	 *
	 * @param collections input sets
	 * @return set of common elements in the input sets
	 * Feb 27, 2020
	 */
	private static <T> Set<T> getCommonElements(Collection<? extends Collection<T>> collections) {

		Set<T> common = new LinkedHashSet<T>();
		if (!collections.isEmpty()) {
			Iterator<? extends Collection<T>> iterator = collections.iterator();
			common.addAll(iterator.next());
			while (iterator.hasNext()) {
				common.retainAll(iterator.next());
			}
		}
		return common;
	}


	/**
	 * Sorts a map based on similarity scores (values in the map)
	 *
	 * @param map the input map to be sorted
	 * @return map with sorted values
	 * May 16, 2019
	 */
	private static <K, V extends Comparable<V>> Map<K, V> sortDescending(final Map<K, V> map) {
		Comparator<K> valueComparator = new Comparator<K>() {
			public int compare(K k1, K k2) {
				int compare = map.get(k2).compareTo(map.get(k1));
				if (compare == 0) return 1;
				else return compare;
			}
		};
		Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);

		sortedByValues.putAll(map);

		return sortedByValues;
	}

	/**
	 * Checks a collection for null or empty values 
	 * @param c
	 * @return
       May 4, 2020
	 */
	public static boolean isNullOrEmpty( final Collection< ? > c ) {
		return c == null || c.isEmpty();
	}



}