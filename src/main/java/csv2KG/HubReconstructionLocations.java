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
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import utilities.KGUtilities;
import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class HubReconstructionLocations {
	
	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";
	
	public static void processHubReconstructionLocationsToNTriple (File hubReconstructionLocationsFolder, String ntFile) {
		
		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "HubReconstructionLocation";
		String tripleClosure = "> .\n";

		String hubReconstructionLocationEntity;

		File[] filesInDir = hubReconstructionLocationsFolder.listFiles();

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

					hubReconstructionLocationEntity = params[0] + "_hubReconstructionLocation>";
					
					//rdf:type
					bw.write(KGUtilities.createType(hubReconstructionLocationEntity, baseURI, rdf_type, type, tripleClosure));
					
					//hasHubParty						
					bw.write(KGUtilities.createObjectProperty(hubReconstructionLocationEntity, baseURI, "hasHubParty", params[3], "_party", tripleClosure));

					//hubReconstructionLocationId
					bw.write(KGUtilities.createDataProperty(hubReconstructionLocationEntity, baseURI, "hubReconstructionLocationId", params[0], DATATYPE_STRING, tripleClosure));
			
					//additionalPartyIdentification
					bw.write(KGUtilities.createDataProperty(hubReconstructionLocationEntity, baseURI, "additionalPartyIdentification", params[1], DATATYPE_STRING, tripleClosure));
					
					//reconstructionLane
					bw.write(KGUtilities.createDataProperty(hubReconstructionLocationEntity, baseURI, "reconstructionLane", params[4], DATATYPE_STRING, tripleClosure));

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
	
	public static void processHubReconstructionLocationsToTSV (File hubReconstructionLocationsFolder, String tsvFile) {

		String hubReconstructionLocationEntity;

		File[] filesInDir = hubReconstructionLocationsFolder.listFiles();

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

					hubReconstructionLocationEntity = params[0] + "_hubReconstructionLocation";
					
					bw.write(hubReconstructionLocationEntity + "\t" + "isType" + "\t" + "HubReconstructionLocation" + "\n");
					
					//hasHubParty						
					bw.write(hubReconstructionLocationEntity + "\t" + "hasHubParty" + "\t" + params[3] + "_party" + "\n");


					//hasHubReconstructionLocation
					bw.write(hubReconstructionLocationEntity + "\t" + "hasHubReconstructionLocation" + "\t" + params[0] + "\n");

					
					//hasAdditionalPartyId
					bw.write(hubReconstructionLocationEntity + "\t" + "hasAdditionalPartyId" + "\t" + params[1] + "\n");

					
					//hasLaneId
					bw.write(hubReconstructionLocationEntity + "\t" + "hasLaneId" + "\t" + params[4] + "\n");


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

	public static void processHubReconstructionLocationsToLocalRepo (File hubReconstructionLocationsFolder, String baseURI, String dataDir, String indexes, Repository repo) {


		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI hubReconstructionInd;
			IRI partyInd;
			IRI hubReconstructionClass = vf.createIRI(baseURI, "HubReconstructionLocation");
			IRI terminalOperatorClass = vf.createIRI(baseURI, "TerminalOperator");

			File[] filesInDir = hubReconstructionLocationsFolder.listFiles();
			String[] params = null;

			BufferedReader br = null;

			for (int i = 0; i < filesInDir.length; i++) {


				try {

					String line;		

					br = new BufferedReader(new FileReader(filesInDir[i]));

					//System.out.println("Reading file: " + filesInDir[i].getName());

					while ((line = br.readLine()) != null) {

						params = line.split(",");

						//adding type
						hubReconstructionInd = vf.createIRI(baseURI, params[0] + "_hubReconstructionLocation");			
						connection.add(hubReconstructionInd, RDF.TYPE, hubReconstructionClass);
						
						//adding predicates
						partyInd = vf.createIRI(baseURI, params[3] + "_party");
						connection.add(partyInd, RDF.TYPE, terminalOperatorClass);
						connection.add(hubReconstructionInd, vf.createIRI(baseURI + "hasHubParty"), partyInd);

						//adding literals
						connection.add(hubReconstructionInd, vf.createIRI(baseURI + "hubReconstructionLocationId"), vf.createLiteral(params[0]));
						connection.add(hubReconstructionInd, vf.createIRI(baseURI + "additionalPartyIdentification"), vf.createLiteral(params[1]));
						connection.add(hubReconstructionInd, vf.createIRI(baseURI + "reconstructionLane"), vf.createLiteral(params[4]));

					}//end while

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
	
	public static void processHubReconstructionLocationsToRemoteRepo (File hubReconstructionLocationsFolder, String baseURI, String rdf4jServer, String repositoryId, Repository repo) {

		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();


			IRI hubReconstructionInd;
			IRI partyInd;
			IRI hubReconstructionClass = vf.createIRI(baseURI, "HubReconstructionLocation");
			IRI terminalOperatorClass = vf.createIRI(baseURI, "TerminalOperator");

			File[] filesInDir = hubReconstructionLocationsFolder.listFiles();
			String[] params = null;

			BufferedReader br = null;

			for (int i = 0; i < filesInDir.length; i++) {


				try {

					String line;		

					br = new BufferedReader(new FileReader(filesInDir[i]));

					//System.out.println("Reading file: " + filesInDir[i].getName());

					while ((line = br.readLine()) != null) {

						params = line.split(",");

						//adding type
						hubReconstructionInd = vf.createIRI(baseURI, params[0] + "_hubReconstructionLocation");			
						connection.add(hubReconstructionInd, RDF.TYPE, hubReconstructionClass);
						
						//adding predicates
						partyInd = vf.createIRI(baseURI, params[3] + "_party");
						connection.add(partyInd, RDF.TYPE, terminalOperatorClass);
						connection.add(hubReconstructionInd, vf.createIRI(baseURI + "hasHubParty"), partyInd);

						//adding literals
						connection.add(hubReconstructionInd, vf.createIRI(baseURI + "hubReconstructionLocationId"), vf.createLiteral(params[0]));
						connection.add(hubReconstructionInd, vf.createIRI(baseURI + "additionalPartyIdentification"), vf.createLiteral(params[1]));
						connection.add(hubReconstructionInd, vf.createIRI(baseURI + "reconstructionLane"), vf.createLiteral(params[4]));

					}//end while

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

}//end class
