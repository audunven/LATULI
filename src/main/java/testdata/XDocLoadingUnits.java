package testdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
public class XDocLoadingUnits
{

	public static void processXDocLoadingUnits(File xdluFolder, String baseURI, String dataDir, String indexes) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");


		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));

		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();

			IRI xdluInd;
			IRI hubReconstructionLocationInd;
			IRI loadingUnitInd;
			IRI inboundParentLoadingUnitInd;
			IRI outboundParentLoadingUnitInd;
			IRI inboundConsignmentInd;
			IRI outboundConsignmentInd;
			IRI partyInd;
			IRI shipperInd;
			IRI receiverInd;
			IRI carrierInd;
			IRI consignorInd;
			IRI consigneeInd;
			IRI waveInd;

			IRI xdluClass = vf.createIRI(baseURI, "XDocLoadingUnit");
			IRI hubReconstructionLocationClass = vf.createIRI(baseURI, "HubReconstructionLocation");
			IRI loadingUnitClass = vf.createIRI(baseURI, "LoadingUnit");
			IRI consignmentClass = vf.createIRI(baseURI, "Consignment");
			IRI terminalOperatorClass = vf.createIRI(baseURI, "TerminalOperator");
			IRI shipperClass = vf.createIRI(baseURI, "Shipper");
			IRI receiverClass = vf.createIRI(baseURI, "Receiver");
			IRI consignorClass = vf.createIRI(baseURI, "Consignor");
			IRI consigneeClass = vf.createIRI(baseURI, "Consignee");
			IRI carrierClass = vf.createIRI(baseURI, "Carrier");
			IRI waveClass = vf.createIRI(baseURI, "Wave");

			File[] filesInDir = xdluFolder.listFiles();
			String[] params = null;

			BufferedReader br = null;

			for (int i = 0; i < filesInDir.length; i++) {

				try {

					String line;		

					br = new BufferedReader(new FileReader(filesInDir[i]));

					System.out.println("Reading file: " + filesInDir[i].getName());

					while ((line = br.readLine()) != null) {

						params = line.split(",");

						//adding types
						xdluInd = vf.createIRI(baseURI, params[0] + "_xDocLoadingUnit");
						connection.add(xdluInd, RDF.TYPE, xdluClass);

						//adding predicates
						hubReconstructionLocationInd = vf.createIRI(baseURI, params[17] + "_hubReconstructionLocation");
						connection.add(hubReconstructionLocationInd, RDF.TYPE, hubReconstructionLocationClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasHubReconstructionLocation"), hubReconstructionLocationInd);

						loadingUnitInd = vf.createIRI(baseURI, params[7] + "_loadingUnit");
						connection.add(hubReconstructionLocationInd, RDF.TYPE, loadingUnitClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasLoadingUnit"), loadingUnitInd);

						inboundParentLoadingUnitInd = vf.createIRI(baseURI, params[41] + "_loadingUnit");
						connection.add(inboundParentLoadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasInboundParentLoadingUnit"), inboundParentLoadingUnitInd);

						outboundParentLoadingUnitInd = vf.createIRI(baseURI, params[42] + "_loadingUnit");
						connection.add(outboundParentLoadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasOutboundParentLoadingUnit"), outboundParentLoadingUnitInd);

						inboundConsignmentInd = vf.createIRI(baseURI, params[11] + "_consignment");
						connection.add(inboundConsignmentInd, RDF.TYPE, consignmentClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasInboundConsignment"), inboundConsignmentInd);

						outboundConsignmentInd = vf.createIRI(baseURI, params[12] + "_consignment");
						connection.add(outboundConsignmentInd, RDF.TYPE, consignmentClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasOutboundConsignment"), outboundConsignmentInd);

						partyInd = vf.createIRI(baseURI, params[15] + "_party");
						connection.add(partyInd, RDF.TYPE, terminalOperatorClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasHubParty"), partyInd);

						shipperInd = vf.createIRI(baseURI, params[23] + "_party");
						connection.add(shipperInd, RDF.TYPE, shipperClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasShipper"), shipperInd);

						receiverInd = vf.createIRI(baseURI, params[26] + "_party");
						connection.add(receiverInd, RDF.TYPE, receiverClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasReceiver"), receiverInd);

						carrierInd = vf.createIRI(baseURI, params[29] + "_party");
						connection.add(carrierInd, RDF.TYPE, carrierClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasCarrier"), carrierInd);

						consignorInd = vf.createIRI(baseURI, params[32] + "_party");
						connection.add(consignorInd, RDF.TYPE, consignorClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasConsignor"), consignorInd);

						consigneeInd = vf.createIRI(baseURI, params[35] + "_party");
						connection.add(consigneeInd, RDF.TYPE, consigneeClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasConsignee"), consigneeInd);

						waveInd = vf.createIRI(baseURI, params[36] + "_wave");
						connection.add(waveInd, RDF.TYPE, waveClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "processedByWave"), waveInd);

						//adding literals
						connection.add(xdluInd, vf.createIRI(baseURI + "internalId"), vf.createLiteral(params[0]));
						
						if (!StringUtilities.convertToDateTime(params[1]).equals("0000-00-00T00:00:00")) {
							connection.add(xdluInd, vf.createIRI(baseURI + "preSortScanOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[1]), XMLSchema.DATETIME));
						}

						if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
							connection.add(xdluInd, vf.createIRI(baseURI + "reconstructedScanOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[2]), XMLSchema.DATETIME));

						}

						if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
							connection.add(xdluInd, vf.createIRI(baseURI + "finishedScanOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[3]), XMLSchema.DATETIME));

						}

						connection.add(xdluInd, vf.createIRI(baseURI + "volume"), vf.createLiteral(params[5], XMLSchema.DECIMAL));
						connection.add(xdluInd, vf.createIRI(baseURI + "weight"), vf.createLiteral(params[6], XMLSchema.DECIMAL));

						connection.add(xdluInd, vf.createIRI(baseURI + "reconstructionTypeId"), vf.createLiteral(params[37]));
						connection.add(xdluInd, vf.createIRI(baseURI + "splitShipment"), vf.createLiteral(params[40], XMLSchema.INT));


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
	
	public static void processXDocLoadingUnitsHTTP (File xdluFolder, String baseURI, String rdf4jServer, String repositoryId) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");


		Repository repo = new HTTPRepository(rdf4jServer, repositoryId);

		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();

			IRI xdluInd;
			IRI hubReconstructionLocationInd;
			IRI loadingUnitInd;
			IRI inboundParentLoadingUnitInd;
			IRI outboundParentLoadingUnitInd;
			IRI inboundConsignmentInd;
			IRI outboundConsignmentInd;
			IRI partyInd;
			IRI shipperInd;
			IRI receiverInd;
			IRI carrierInd;
			IRI consignorInd;
			IRI consigneeInd;
			IRI waveInd;

			IRI xdluClass = vf.createIRI(baseURI, "XDocLoadingUnit");
			IRI hubReconstructionLocationClass = vf.createIRI(baseURI, "HubReconstructionLocation");
			IRI loadingUnitClass = vf.createIRI(baseURI, "LoadingUnit");
			IRI consignmentClass = vf.createIRI(baseURI, "Consignment");
			IRI terminalOperatorClass = vf.createIRI(baseURI, "TerminalOperator");
			IRI shipperClass = vf.createIRI(baseURI, "Shipper");
			IRI receiverClass = vf.createIRI(baseURI, "Receiver");
			IRI consignorClass = vf.createIRI(baseURI, "Consignor");
			IRI consigneeClass = vf.createIRI(baseURI, "Consignee");
			IRI carrierClass = vf.createIRI(baseURI, "Carrier");
			IRI waveClass = vf.createIRI(baseURI, "Wave");

			File[] filesInDir = xdluFolder.listFiles();
			String[] params = null;

			BufferedReader br = null;

			for (int i = 0; i < filesInDir.length; i++) {

				try {

					String line;		

					br = new BufferedReader(new FileReader(filesInDir[i]));

					System.out.println("Reading file: " + filesInDir[i].getName());

					while ((line = br.readLine()) != null) {

						params = line.split(",");

						//adding types
						xdluInd = vf.createIRI(baseURI, params[0] + "_xDocLoadingUnit");
						connection.add(xdluInd, RDF.TYPE, xdluClass);

						//adding predicates
						hubReconstructionLocationInd = vf.createIRI(baseURI, params[17] + "_hubReconstructionLocation");
						connection.add(hubReconstructionLocationInd, RDF.TYPE, hubReconstructionLocationClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasHubReconstructionLocation"), hubReconstructionLocationInd);

						loadingUnitInd = vf.createIRI(baseURI, params[7] + "_loadingUnit");
						connection.add(hubReconstructionLocationInd, RDF.TYPE, loadingUnitClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasLoadingUnit"), loadingUnitInd);

						inboundParentLoadingUnitInd = vf.createIRI(baseURI, params[41] + "_loadingUnit");
						connection.add(inboundParentLoadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasInboundParentLoadingUnit"), inboundParentLoadingUnitInd);

						outboundParentLoadingUnitInd = vf.createIRI(baseURI, params[42] + "_loadingUnit");
						connection.add(outboundParentLoadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasOutboundParentLoadingUnit"), outboundParentLoadingUnitInd);

						inboundConsignmentInd = vf.createIRI(baseURI, params[11] + "_consignment");
						connection.add(inboundConsignmentInd, RDF.TYPE, consignmentClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasInboundConsignment"), inboundConsignmentInd);

						outboundConsignmentInd = vf.createIRI(baseURI, params[12] + "_consignment");
						connection.add(outboundConsignmentInd, RDF.TYPE, consignmentClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasOutboundConsignment"), outboundConsignmentInd);


						partyInd = vf.createIRI(baseURI, params[15] + "_party");
						connection.add(partyInd, RDF.TYPE, terminalOperatorClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasHubParty"), partyInd);


						shipperInd = vf.createIRI(baseURI, params[23] + "_party");
						connection.add(shipperInd, RDF.TYPE, shipperClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasShipper"), shipperInd);


						receiverInd = vf.createIRI(baseURI, params[26] + "_party");
						connection.add(receiverInd, RDF.TYPE, receiverClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasReceiver"), receiverInd);


						carrierInd = vf.createIRI(baseURI, params[29] + "_party");
						connection.add(carrierInd, RDF.TYPE, carrierClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasCarrier"), carrierInd);


						consignorInd = vf.createIRI(baseURI, params[32] + "_party");
						connection.add(consignorInd, RDF.TYPE, consignorClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasConsignor"), consignorInd);


						consigneeInd = vf.createIRI(baseURI, params[35] + "_party");
						connection.add(consigneeInd, RDF.TYPE, consigneeClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "hasConsignee"), consigneeInd);


						waveInd = vf.createIRI(baseURI, params[36] + "_wave");
						connection.add(waveInd, RDF.TYPE, waveClass);
						connection.add(xdluInd, vf.createIRI(baseURI + "processedByWave"), waveInd);

						//adding literals
						connection.add(xdluInd, vf.createIRI(baseURI + "internalId"), vf.createLiteral(params[0]));
						
						if (!StringUtilities.convertToDateTime(params[1]).equals("0000-00-00T00:00:00")) {
							connection.add(xdluInd, vf.createIRI(baseURI + "preSortScanOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[1]), XMLSchema.DATETIME));
						}

						if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
							connection.add(xdluInd, vf.createIRI(baseURI + "reconstructedScanOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[2]), XMLSchema.DATETIME));

						}

						if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
							connection.add(xdluInd, vf.createIRI(baseURI + "finishedScanOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[3]), XMLSchema.DATETIME));

						}

						connection.add(xdluInd, vf.createIRI(baseURI + "volume"), vf.createLiteral(params[5], XMLSchema.DECIMAL));
						connection.add(xdluInd, vf.createIRI(baseURI + "weight"), vf.createLiteral(params[6], XMLSchema.DECIMAL));

						connection.add(xdluInd, vf.createIRI(baseURI + "reconstructionTypeId"), vf.createLiteral(params[37]));
						connection.add(xdluInd, vf.createIRI(baseURI + "splitShipment"), vf.createLiteral(params[40], XMLSchema.INT));


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
