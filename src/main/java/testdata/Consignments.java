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
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class Consignments {

	public static void processConsignments (File consignmentFolder, String baseURI, String dataDir, String indexes) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));

		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();

			IRI consignmentInd;
			IRI transportInd;
			IRI waveInd;
			IRI carrierInd;
			IRI consignorInd;
			IRI consigneeInd;

			IRI consignmentClass = vf.createIRI(baseURI, "Consignment");
			IRI transportClass = vf.createIRI(baseURI, "Transport");
			IRI waveClass = vf.createIRI(baseURI, "Wave");
			IRI consignorClass = vf.createIRI(baseURI, "Consignor");
			IRI consigneeClass = vf.createIRI(baseURI, "Consignee");
			IRI carrierClass = vf.createIRI(baseURI, "Carrier");

			File[] filesInDir = consignmentFolder.listFiles();

			BufferedReader br = null;

			List<String[]> line = new ArrayList<String[]>();

			for (int i = 0; i < filesInDir.length; i++) {

				try {

					br = new BufferedReader(new FileReader(filesInDir[i]));

					System.out.println("Reading file: " + filesInDir[i].getName());

					try {
						line = StringUtilities.oneByOne(br);
					} catch (Exception e) {
						e.printStackTrace();
					}

					for (String[] params : line) {

						//adding types
						consignmentInd = vf.createIRI(baseURI, params[0] + "_consignment" );
						connection.add(consignmentInd, RDF.TYPE, consignmentClass);

						//adding predicates
						if (!params[3].equals("NULL")) {
							transportInd = vf.createIRI(baseURI, params[3] + "_transport");
							connection.add(transportInd, RDF.TYPE, transportClass);
							connection.add(consignmentInd, vf.createIRI(baseURI + "includesTransport"), transportInd);
						}

						if (!params[18].equals("NULL")) {								
							waveInd = vf.createIRI(baseURI, params[18] + "_wave");
							connection.add(waveInd, RDF.TYPE, waveClass);
							connection.add(consignmentInd, vf.createIRI(baseURI + "processedByWave"), waveInd);								
						}

						carrierInd = vf.createIRI(baseURI, params[11] + "_party");
						connection.add(carrierInd, RDF.TYPE, carrierClass);
						connection.add(consignmentInd, vf.createIRI(baseURI + "hasCarrierParty"), carrierInd);

						consignorInd = vf.createIRI(baseURI, params[14] + "_party");
						connection.add(consignorInd, RDF.TYPE, consignorClass);
						connection.add(consignmentInd, vf.createIRI(baseURI + "hasConsignorParty"), consignorInd);

						consigneeInd = vf.createIRI(baseURI, params[17] + "_party");
						connection.add(consigneeInd, RDF.TYPE, consigneeClass);
						connection.add(consignmentInd, vf.createIRI(baseURI + "hasConsigneeParty"), consigneeInd);

						if (!StringUtilities.convertToDateTime(params[23]).equals("0000-00-00T00:00:00")) {
							connection.add(consignmentInd, vf.createIRI(baseURI + "taskClosedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[23]), XMLSchema.DATETIME));
						}
						
						connection.add(consignmentInd, vf.createIRI(baseURI + "consignmentId"), vf.createLiteral(params[0]));
						connection.add(consignmentInd, vf.createIRI(baseURI + "consignmentType"), vf.createLiteral(params[6]));
						connection.add(consignmentInd, vf.createIRI(baseURI + "fullPalletConsignment"), vf.createLiteral(params[32]));
						connection.add(consignmentInd, vf.createIRI(baseURI + "qttBoxes"), vf.createLiteral(params[33], XMLSchema.INT));								
						connection.add(consignmentInd, vf.createIRI(baseURI + "qttPallets"), vf.createLiteral(params[34], XMLSchema.INT));
						connection.add(consignmentInd, vf.createIRI(baseURI + "qttReconstructedPallets"), vf.createLiteral(params[35], XMLSchema.INT));
						connection.add(consignmentInd, vf.createIRI(baseURI + "qttReconstructedParcels"), vf.createLiteral(params[36], XMLSchema.INT));
						connection.add(consignmentInd, vf.createIRI(baseURI + "totalConsignmentVolume"), vf.createLiteral(params[40], XMLSchema.DECIMAL));
						connection.add(consignmentInd, vf.createIRI(baseURI + "totalConsignmentWeight"), vf.createLiteral(params[41], XMLSchema.DECIMAL));

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
				}


			}

		}
		repo.shutDown();

		long endTime = System.nanoTime();		
		long timeElapsed = endTime - startTime;		
		System.err.println("The ontology generation process took: " + timeElapsed/60000000000.00 + " minutes");

		long usedMemoryAfterOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Memory increased after ontology creation: " + (usedMemoryAfterOntologyCreation-usedMemoryBeforeOntologyCreation)/1000000 + " MB");

		System.out.println("\nUsed Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB"); 


}
	
	public static void processConsignmentsHTTP (File consignmentFolder, String baseURI, String rdf4jServer, String repositoryId) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		Repository repo = new HTTPRepository(rdf4jServer, repositoryId);
		
		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();

			IRI consignmentInd;
			IRI transportInd;
			IRI waveInd;
			IRI carrierInd;
			IRI consignorInd;
			IRI consigneeInd;

			IRI consignmentClass = vf.createIRI(baseURI, "Consignment");
			IRI transportClass = vf.createIRI(baseURI, "Transport");
			IRI waveClass = vf.createIRI(baseURI, "Wave");
			IRI consignorClass = vf.createIRI(baseURI, "Consignor");
			IRI consigneeClass = vf.createIRI(baseURI, "Consignee");
			IRI carrierClass = vf.createIRI(baseURI, "Carrier");

			File[] filesInDir = consignmentFolder.listFiles();

			BufferedReader br = null;

			List<String[]> line = new ArrayList<String[]>();

			for (int i = 0; i < filesInDir.length; i++) {

				try {

					br = new BufferedReader(new FileReader(filesInDir[i]));

					System.out.println("Reading file: " + filesInDir[i].getName());

					try {
						line = StringUtilities.oneByOne(br);
					} catch (Exception e) {
						e.printStackTrace();
					}

					for (String[] params : line) {

						//adding types
						consignmentInd = vf.createIRI(baseURI, params[0] + "_consignment" );
						connection.add(consignmentInd, RDF.TYPE, consignmentClass);

						//adding predicates
						if (!params[3].equals("NULL")) {
							transportInd = vf.createIRI(baseURI, params[3] + "_transport");
							connection.add(transportInd, RDF.TYPE, transportClass);
							connection.add(consignmentInd, vf.createIRI(baseURI + "includesTransport"), transportInd);
						}

						if (!params[18].equals("NULL")) {								
							waveInd = vf.createIRI(baseURI, params[18] + "_wave");
							connection.add(waveInd, RDF.TYPE, waveClass);
							connection.add(consignmentInd, vf.createIRI(baseURI + "processedByWave"), waveInd);								
						}

						carrierInd = vf.createIRI(baseURI, params[11] + "_party");
						connection.add(carrierInd, RDF.TYPE, carrierClass);
						connection.add(consignmentInd, vf.createIRI(baseURI + "hasCarrierParty"), carrierInd);

						consignorInd = vf.createIRI(baseURI, params[14] + "_party");
						connection.add(consignorInd, RDF.TYPE, consignorClass);
						connection.add(consignmentInd, vf.createIRI(baseURI + "hasConsignorParty"), consignorInd);

						consigneeInd = vf.createIRI(baseURI, params[17] + "_party");
						connection.add(consigneeInd, RDF.TYPE, consigneeClass);
						connection.add(consignmentInd, vf.createIRI(baseURI + "hasConsigneeParty"), consigneeInd);

						//adding literals
						if (!StringUtilities.convertToDateTime(params[23]).equals("0000-00-00T00:00:00")) {
							connection.add(consignmentInd, vf.createIRI(baseURI + "taskClosedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[23]), XMLSchema.DATETIME));
						}
						
						connection.add(consignmentInd, vf.createIRI(baseURI + "consignmentId"), vf.createLiteral(params[0]));
						connection.add(consignmentInd, vf.createIRI(baseURI + "consignmentType"), vf.createLiteral(params[6]));
						connection.add(consignmentInd, vf.createIRI(baseURI + "fullPalletConsignment"), vf.createLiteral(params[32]));
						connection.add(consignmentInd, vf.createIRI(baseURI + "qttBoxes"), vf.createLiteral(params[33], XMLSchema.INT));								
						connection.add(consignmentInd, vf.createIRI(baseURI + "qttPallets"), vf.createLiteral(params[34], XMLSchema.INT));
						connection.add(consignmentInd, vf.createIRI(baseURI + "qttReconstructedPallets"), vf.createLiteral(params[35], XMLSchema.INT));
						connection.add(consignmentInd, vf.createIRI(baseURI + "qttReconstructedParcels"), vf.createLiteral(params[36], XMLSchema.INT));
						connection.add(consignmentInd, vf.createIRI(baseURI + "totalConsignmentVolume"), vf.createLiteral(params[40], XMLSchema.DECIMAL));
						connection.add(consignmentInd, vf.createIRI(baseURI + "totalConsignmentWeight"), vf.createLiteral(params[41], XMLSchema.DECIMAL));

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
				}


			}

		}
		repo.shutDown();

		long endTime = System.nanoTime();		
		long timeElapsed = endTime - startTime;		
		System.err.println("The ontology generation process took: " + timeElapsed/60000000000.00 + " minutes");

		long usedMemoryAfterOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Memory increased after ontology creation: " + (usedMemoryAfterOntologyCreation-usedMemoryBeforeOntologyCreation)/1000000 + " MB");

		System.out.println("\nUsed Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB"); 


}


}
