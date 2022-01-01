package testdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.model.vocabulary.GEO;


import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class Parties {

	public static void processParties(File partiesFolder, String baseURI, String dataDir, String indexes) {

		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));
		String geosparqlURI = "http://www.opengis.net/ont/geosparql#";

		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();

			IRI partyInd;
			IRI partyClass = vf.createIRI(baseURI, "Party");;

			File[] filesInDir = partiesFolder.listFiles();

			BufferedReader br = null;
			
			List<String[]> line = new ArrayList<String[]>();

			for (int i = 0; i < filesInDir.length; i++) {


				try {

					br = new BufferedReader(new FileReader(filesInDir[i]));

					System.out.println("Reading file: " + filesInDir[i].getName());
					
					for (String[] params : line) {

						//adding types
						partyInd = vf.createIRI(baseURI, params[1] + "-" + params[3] + "_party");
						connection.add(partyInd, RDF.TYPE, partyClass);

						//adding literals
						connection.add(partyInd, vf.createIRI(baseURI + "additionalPartyIdentification"), vf.createLiteral(params[1]));
						connection.add(partyInd, vf.createIRI(baseURI + "gln"), vf.createLiteral(params[2]));
						connection.add(partyInd, vf.createIRI(baseURI + "hashCode"), vf.createLiteral(params[3]));
						connection.add(partyInd, vf.createIRI(baseURI + "code2"), vf.createLiteral(params[4]));
						connection.add(partyInd, vf.createIRI(baseURI + "location"), vf.createLiteral(params[51]));
						connection.add(partyInd, vf.createIRI(baseURI + "postalCode"), vf.createLiteral(params[6]));
						connection.add(partyInd, vf.createIRI(baseURI + "isHub"), vf.createLiteral(params[7], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isShipper"), vf.createLiteral(params[8], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isCarrier"), vf.createLiteral(params[9], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isConsignor"), vf.createLiteral(params[10], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(geosparqlURI + "asWKT"), vf.createLiteral(StringUtilities.formatCoordinates(params[11] + "," + params[12]), GEO.WKT_LITERAL));

					}

				} catch (IOException e) {

					e.printStackTrace();

				} finally {

					try {
						if (br != null)
							br.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}


			}

		}
		repo.shutDown();

	}
	
	public static void processPartiesHTTP(File partiesFolder, String baseURI, String rdf4jServer, String repositoryId) {

		Repository repo = new HTTPRepository(rdf4jServer, repositoryId);
		String geosparqlURI = "http://www.opengis.net/ont/geosparql#";

		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();

			IRI partyInd;
			IRI partyClass = vf.createIRI(baseURI, "Party");;

			File[] filesInDir = partiesFolder.listFiles();

			BufferedReader br = null;
			
			List<String[]> line = new ArrayList<String[]>();

			for (int i = 0; i < filesInDir.length; i++) {


				try {

					br = new BufferedReader(new FileReader(filesInDir[i]));

					System.out.println("Reading file: " + filesInDir[i].getName());
					
					for (String[] params : line) {

						//adding types
						partyInd = vf.createIRI(baseURI, params[1] + "-" + params[3] + "_party");
						connection.add(partyInd, RDF.TYPE, partyClass);

						//adding literals
						connection.add(partyInd, vf.createIRI(baseURI + "additionalPartyIdentification"), vf.createLiteral(params[1]));
						connection.add(partyInd, vf.createIRI(baseURI + "gln"), vf.createLiteral(params[2]));
						connection.add(partyInd, vf.createIRI(baseURI + "hashCode"), vf.createLiteral(params[3]));
						connection.add(partyInd, vf.createIRI(baseURI + "code2"), vf.createLiteral(params[4]));
						connection.add(partyInd, vf.createIRI(baseURI + "location"), vf.createLiteral(params[51]));
						connection.add(partyInd, vf.createIRI(baseURI + "postalCode"), vf.createLiteral(params[6]));
						connection.add(partyInd, vf.createIRI(baseURI + "isHub"), vf.createLiteral(params[7], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isShipper"), vf.createLiteral(params[8], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isCarrier"), vf.createLiteral(params[9], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isConsignor"), vf.createLiteral(params[10], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(geosparqlURI + "asWKT"), vf.createLiteral(StringUtilities.formatCoordinates(params[11] + "," + params[12]), GEO.WKT_LITERAL));

					}

				} catch (IOException e) {

					e.printStackTrace();

				} finally {

					try {
						if (br != null)
							br.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}


			}

		}
		repo.shutDown();

	}

}
