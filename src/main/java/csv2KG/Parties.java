package csv2KG;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.GEO;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import utilities.KGUtilities;
import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class Parties {

	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";

	public static void processPartiesToNTriple (File partiesFolder, String ntFile) {

		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "Party";
		String tripleClosure = "> .\n";
		
		String geoSparqlDataProperty = "<http://www.opengis.net/ont/geosparql#asWKT>";
		String geoSparqlTripleClosure = "^^<http://www.opengis.net/ont/geosparql#wktLiteral> .\n";

		String partyEntity;

		File[] filesInDir = partiesFolder.listFiles();

		BufferedReader br = null;
		BufferedWriter bw = null;


		List<String[]> line = new ArrayList<String[]>();

		for (int i = 0; i < filesInDir.length; i++) {


			try {

				br = new BufferedReader(new FileReader(filesInDir[i]));
				bw = new BufferedWriter(new FileWriter(ntFile, true));


				//System.out.println("Reading file: " + filesInDir[i].getName());

				try {
					line = StringUtilities.oneByOne(br);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (String[] params : line) {

					//adding types

					partyEntity = params[2] + "_party>";

					bw.write(KGUtilities.createType(partyEntity, baseURI, rdf_type, type, tripleClosure));

					//additionalPartyIdentification
					bw.write(KGUtilities.createDataProperty(partyEntity, baseURI, "additionalPartyIdentification", params[0], DATATYPE_STRING, tripleClosure));

					//gln
					bw.write(KGUtilities.createDataProperty(partyEntity, baseURI, "gln", params[1], DATATYPE_STRING, tripleClosure));

					//hashCode
					bw.write(KGUtilities.createDataProperty(partyEntity, baseURI, "hashCode", params[2], DATATYPE_STRING, tripleClosure));

					//code2
					bw.write(KGUtilities.createDataProperty(partyEntity, baseURI, "code2", params[7], DATATYPE_STRING, tripleClosure));

					//location
					bw.write(KGUtilities.createDataProperty(partyEntity, baseURI, "location", params[8], DATATYPE_STRING, tripleClosure));

					//postalCode
					bw.write(KGUtilities.createDataProperty(partyEntity, baseURI, "postalCode", params[9], DATATYPE_STRING, tripleClosure));

					//isHub
					bw.write(KGUtilities.createDataProperty(partyEntity, baseURI, "isHub", params[11], DATATYPE_INT, tripleClosure));

					//isShipper
					bw.write(KGUtilities.createDataProperty(partyEntity, baseURI, "isShipper", params[12], DATATYPE_INT, tripleClosure));

					//isCarrier
					bw.write(KGUtilities.createDataProperty(partyEntity, baseURI, "isCarrier", params[13], DATATYPE_INT, tripleClosure));

					//isConsignor
					bw.write(KGUtilities.createDataProperty(partyEntity, baseURI, "isConsignor", params[14], DATATYPE_INT, tripleClosure));

					//asWKT
					bw.write(KGUtilities.createGeoDataProperty(partyEntity, baseURI, geoSparqlDataProperty, StringUtilities.formatCoordinates(params[19]), geoSparqlTripleClosure));


				}//end for

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				try {
					if (bw != null)
						bw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}
	}

	public static void processPartiesToTSV(File partiesFolder, String tsvFile) {


		String partyEntity;

		File[] filesInDir = partiesFolder.listFiles();

		BufferedReader br = null;
		BufferedWriter bw = null;


		List<String[]> line = new ArrayList<String[]>();

		for (int i = 0; i < filesInDir.length; i++) {


			try {

				br = new BufferedReader(new FileReader(filesInDir[i]));
				bw = new BufferedWriter(new FileWriter(tsvFile, true));


				//System.out.println("Reading file: " + filesInDir[i].getName());

				try {
					line = StringUtilities.oneByOne(br);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (String[] params : line) {

					//adding types

					partyEntity = params[2] + "_party";

					bw.write(partyEntity + "\t" + "isType" + "\t" + "Party" + "\n");

					//hasAdditionalPartyId
					bw.write(partyEntity + "\t" + "hasAdditionalPartyId" + "\t" + params[0] + "\n");


					//hasGln
					bw.write(partyEntity + "\t" + "hasGln" + "\t" + params[1] + "\n");


					//hasHashCode
					bw.write(partyEntity + "\t" + "hasHashCode" + "\t" + params[2] + "\n");


					//hasCountryCode
					bw.write(partyEntity + "\t" + "hasCountryCode" + "\t" + params[7] + "\n");


					//hasCity
					bw.write(partyEntity + "\t" + "hasCity" + "\t" + params[8] + "\n");


					//hasPostalCode
					bw.write(partyEntity + "\t" + "hasPostalCode" + "\t" + params[9] + "\n");


					//isHub
					bw.write(partyEntity + "\t" + "isHub" + "\t" + params[11] + "\n");


					//isShipper
					bw.write(partyEntity + "\t" + "isShipper" + "\t" + params[12] + "\n");


					//isCarrier
					bw.write(partyEntity + "\t" + "isCarrier" + "\t" + params[13] + "\n");


					//isConsignor
					bw.write(partyEntity + "\t" + "isConsignor" + "\t" + params[14] + "\n");


					//hasCoordinates
					bw.write(partyEntity + "\t" + "hasCoordinates" + "\t" + params[19] + "\n");



				}//end for

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				try {
					if (bw != null)
						bw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}
	}

	public static void processPartiesToLocalRepo (File partiesFolder, String baseURI, String dataDir, String indexes, Repository repo) {

		String geosparqlURI = "http://www.opengis.net/ont/geosparql#";

		try (RepositoryConnection connection = repo.getConnection()) {

			connection.setNamespace("m3", baseURI);
			connection.setNamespace("geo", geosparqlURI);

			ValueFactory vf = connection.getValueFactory();

			IRI partyInd;
			IRI partyClass = vf.createIRI(baseURI, "Party");;

			File[] filesInDir = partiesFolder.listFiles();

			BufferedReader br = null;

			List<String[]> line = new ArrayList<String[]>();

			for (int i = 0; i < filesInDir.length; i++) {


				try {

					br = new BufferedReader(new FileReader(filesInDir[i]));

					//System.out.println("Reading file: " + filesInDir[i].getName());

					try {
						line = StringUtilities.oneByOne(br);
					} catch (Exception e) {
						e.printStackTrace();
					}

					for (String[] params : line) {

						//adding types
						partyInd = vf.createIRI(baseURI, params[2] + "_party");
						connection.add(partyInd, RDF.TYPE, partyClass);

						//adding literals
						connection.add(partyInd, vf.createIRI(baseURI + "additionalPartyIdentification"), vf.createLiteral(params[0]));
						connection.add(partyInd, vf.createIRI(baseURI + "gln"), vf.createLiteral(params[1]));
						connection.add(partyInd, vf.createIRI(baseURI + "hashCode"), vf.createLiteral(params[2]));
						connection.add(partyInd, vf.createIRI(baseURI + "code2"), vf.createLiteral(params[7]));
						connection.add(partyInd, vf.createIRI(baseURI + "location"), vf.createLiteral(params[8]));
						connection.add(partyInd, vf.createIRI(baseURI + "postalCode"), vf.createLiteral(params[9]));
						connection.add(partyInd, vf.createIRI(baseURI + "isHub"), vf.createLiteral(params[11], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isShipper"), vf.createLiteral(params[12], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isCarrier"), vf.createLiteral(params[13], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isConsignor"), vf.createLiteral(params[14], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(geosparqlURI + "asWKT"), vf.createLiteral(StringUtilities.formatCoordinates(params[19]), GEO.WKT_LITERAL));

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

	public static void processPartiesToRemoteRepo (File partiesFolder, String baseURI, String rdf4jServer, String repositoryId, Repository repo) {

		String geosparqlURI = "http://www.opengis.net/ont/geosparql#";

		try (RepositoryConnection connection = repo.getConnection()) {

			connection.setNamespace("m3", baseURI);
			connection.setNamespace("geo", geosparqlURI);

			ValueFactory vf = connection.getValueFactory();

			IRI partyInd;
			IRI partyClass = vf.createIRI(baseURI, "Party");;

			File[] filesInDir = partiesFolder.listFiles();

			BufferedReader br = null;

			List<String[]> line = new ArrayList<String[]>();

			for (int i = 0; i < filesInDir.length; i++) {


				try {

					br = new BufferedReader(new FileReader(filesInDir[i]));

					//System.out.println("Reading file: " + filesInDir[i].getName());

					try {
						line = StringUtilities.oneByOne(br);
					} catch (Exception e) {
						e.printStackTrace();
					}

					for (String[] params : line) {

						//adding types
						partyInd = vf.createIRI(baseURI, params[2] + "_party");
						connection.add(partyInd, RDF.TYPE, partyClass);

						//adding literals
						connection.add(partyInd, vf.createIRI(baseURI + "additionalPartyIdentification"), vf.createLiteral(params[0]));
						connection.add(partyInd, vf.createIRI(baseURI + "gln"), vf.createLiteral(params[1]));
						connection.add(partyInd, vf.createIRI(baseURI + "hashCode"), vf.createLiteral(params[2]));
						connection.add(partyInd, vf.createIRI(baseURI + "code2"), vf.createLiteral(params[7]));
						connection.add(partyInd, vf.createIRI(baseURI + "location"), vf.createLiteral(params[8]));
						connection.add(partyInd, vf.createIRI(baseURI + "postalCode"), vf.createLiteral(params[9]));
						connection.add(partyInd, vf.createIRI(baseURI + "isHub"), vf.createLiteral(params[11], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isShipper"), vf.createLiteral(params[12], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isCarrier"), vf.createLiteral(params[13], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(baseURI + "isConsignor"), vf.createLiteral(params[14], XMLSchema.INT));
						connection.add(partyInd, vf.createIRI(geosparqlURI + "asWKT"), vf.createLiteral(StringUtilities.formatCoordinates(params[19]), GEO.WKT_LITERAL));

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
