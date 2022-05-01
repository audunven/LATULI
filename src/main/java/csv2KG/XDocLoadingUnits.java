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
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import utilities.KGUtilities;
import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class XDocLoadingUnits {
	
	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";
	
	public static void processXDocLoadingUnitsToNTriple (File xdluFolder, String ntFile) {
		
		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "XDocLoadingUnit";
		String tripleClosure = "> .\n";

		String xDocLoadingUnitEntity;

		File[] filesInDir = xdluFolder.listFiles();

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


					//isType				
					xDocLoadingUnitEntity = params[0] + "_xDocLoadingUnit>";

					bw.write(KGUtilities.createType(xDocLoadingUnitEntity, baseURI, rdf_type, type, tripleClosure));


					//hasHubReconstructionLocation
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasHubReconstructionLocation", params[17], "_hubReconstructionLocation", tripleClosure));


					//hasLoadingUnit
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasLoadingUnit", params[7], "_loadingUnit", tripleClosure));


					//hasInboundParentLoadingUnit
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasInboundParentLoadingUnit", params[41], "_loadingUnit", tripleClosure));


					//hasOutboundParentLoadingUnit
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasOutboundParentLoadingUnit", params[42], "_loadingUnit", tripleClosure));


					//hasInboundConsignment
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasInboundConsignment", params[11], "_consignment", tripleClosure));


					//hasOutboundConsignment
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasOutboundConsignment", params[12], "_consignment", tripleClosure));


					//hasHubParty
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasHubParty", params[15], "_party", tripleClosure));


					//hasShipperParty
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasShipperParty", params[23], "_party", tripleClosure));


					//hasReceiverParty
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasReceiverParty", params[26], "_party", tripleClosure));


					//hasCarrierParty
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasCarrierParty", params[29], "_party", tripleClosure));


					//hasConsignorParty
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasConsignorParty", params[32], "_party", tripleClosure));


					//hasConsigneeParty
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasConsigneeParty", params[35], "_party", tripleClosure));


					//processedByWave
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "processedByWave", params[36], "_wave", tripleClosure));


					//internalId
					bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "internalId", params[0], DATATYPE_STRING, tripleClosure));


					//presortScanOn
					if (!StringUtilities.convertToDateTime(params[1]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "presortScanOn", StringUtilities.convertToDateTime(params[1]), DATATYPE_DATETIME, tripleClosure));

					}

					//reconstructedScanOn
					if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "reconstructedScanOn", StringUtilities.convertToDateTime(params[2]), DATATYPE_DATETIME, tripleClosure));


					}

					//finishedScanOn
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "finishedScanOn", StringUtilities.convertToDateTime(params[3]), DATATYPE_DATETIME, tripleClosure));


					}

					//volume
					bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "volume", params[5], DATATYPE_DECIMAL, tripleClosure));


					//weight
					bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "weight", params[6], DATATYPE_DECIMAL, tripleClosure));


					//reconstructionType
					bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "reconstructionType", params[37], DATATYPE_STRING, tripleClosure));


					//splitShipment
					bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "splitShipment", params[40], DATATYPE_INT, tripleClosure));



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

	
	public static void processXDocLoadingUnitsToTSV (File xdluFolder, String tsvFile) {

		String xDocLoadingUnitEntity;

		File[] filesInDir = xdluFolder.listFiles();

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


					//isType				
					xDocLoadingUnitEntity = params[0] + "_xDocLoadingUnit";

					bw.write(xDocLoadingUnitEntity + "\t" + "isType" + "\t" + "XDocLoadingUnit" + "\n");

					//hasHubReconstructionLocation
					bw.write(xDocLoadingUnitEntity + "\t" + "hasHubReconstructionLocation" + "\t" + params[17] + "_hubReconstructionLocation" + "\n");

					//hasLoadingUnit
					bw.write(xDocLoadingUnitEntity + "\t" + "hasLoadingUnit" + "\t" + params[7] + "_loadingUnit" + "\n");

					//hasInboundParentLoadingUnit
					bw.write(xDocLoadingUnitEntity + "\t" + "hasInboundParentLoadingUnit" + "\t" + params[41] + "_loadingUnit" + "\n");

					//hasOutboundParentLoadingUnit
					bw.write(xDocLoadingUnitEntity + "\t" + "hasOutboundParentLoadingUnit" + "\t" + params[42] + "_loadingUnit" + "\n");

					//hasInboundConsignment
					bw.write(xDocLoadingUnitEntity + "\t" + "hasInboundConsignment" + "\t" + params[11] + "_consignment" + "\n");

					//hasOutboundConsignment
					bw.write(xDocLoadingUnitEntity + "\t" + "hasOutboundConsignment" + "\t" + params[12] + "_consignment" + "\n");

					//hasHubParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasHubParty" + "\t" + params[15] + "_party" + "\n");

					//hasShipperParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasShipperParty" + "\t" + params[23] + "_party" + "\n");

					//hasReceiverParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasReceiverParty" + "\t" + params[26] + "_party" + "\n");

					//hasCarrierParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasCarrierParty" + "\t" + params[29] + "_party" + "\n");

					//hasConsignorParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasConsignorParty" + "\t" + params[32] + "_party" + "\n");

					//hasConsigneeParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasConsigneeParty" + "\t" + params[35] + "_party" + "\n");

					//isProcessedByWave
					bw.write(xDocLoadingUnitEntity + "\t" + "isProcessedByWave" + "\t" + params[36] + "_wave" + "\n");

					//hasInternalId
					bw.write(xDocLoadingUnitEntity + "\t" + "hasInternalId" + "\t" + params[0] + "\n");


					//hasPresortScanOn
					if (!StringUtilities.convertToDateTime(params[1]).equals("0000-00-00T00:00:00")) {
						bw.write(xDocLoadingUnitEntity + "\t" + "hasPresortScanOn" + "\t" + StringUtilities.convertToDateTime(params[1]) + "\n");

					}

					//hasReconstructedScanOn
					if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
						bw.write(xDocLoadingUnitEntity + "\t" + "hasReconstructedScanOn" + "\t" + StringUtilities.convertToDateTime(params[2]) + "\n");

					}

					//hasFinishedScanOn
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
						bw.write(xDocLoadingUnitEntity + "\t" + "hasFinishedScanOn" + "\t" + StringUtilities.convertToDateTime(params[3]) + "\n");

					}

					//hasVolume
					bw.write(xDocLoadingUnitEntity + "\t" + "hasVolume" + "\t" + params[5] + "\n");

					//hasWeight
					bw.write(xDocLoadingUnitEntity + "\t" + "hasWeight" + "\t" + params[6] + "\n");

					//hasReconstructionType
					bw.write(xDocLoadingUnitEntity + "\t" + "hasReconstructionType" + "\t" + params[37] + "\n");

					//isSplitShipment
					bw.write(xDocLoadingUnitEntity + "\t" + "isSplitShipment" + "\t" + params[40] + "\n");


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

	public static void processXDocLoadingUnitsToLocalRepo (File xdluFolder, String baseURI, String dataDir, String indexes, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("m3", baseURI);

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

					//System.out.println("Reading file: " + filesInDir[i].getName());

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
	
	public static void processXDocLoadingUnitsToRemoteRepo (File xdluFolder, String baseURI, String rdf4jServer, String repositoryId, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("m3", baseURI);

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

					//System.out.println("Reading file: " + filesInDir[i].getName());

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
