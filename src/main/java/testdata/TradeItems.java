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
public class TradeItems
{

	public static void processTradeItems (File tradeItemsFolder, String baseURI, String dataDir, String indexes) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));

		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();

			IRI tradeItemInd;
			IRI shipmentInd;
			IRI loadingUnitInd;


			IRI tradeItemClass = vf.createIRI(baseURI, "TradeItem");
			IRI shipmentClass = vf.createIRI(baseURI, "Shipment");
			IRI loadingUnitClass = vf.createIRI(baseURI, "LoadingUnit");

			File[] filesInDir = tradeItemsFolder.listFiles();

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
						tradeItemInd = vf.createIRI(baseURI, params[0] + "-" + params[1] + "_tradeitem" );
						connection.add(tradeItemInd, RDF.TYPE, tradeItemClass);

						//adding predicates
						shipmentInd = vf.createIRI(baseURI, params[0] + "_shipment");
						connection.add(shipmentInd, RDF.TYPE, shipmentClass);
						connection.add(tradeItemInd, vf.createIRI(baseURI + "belongsToShipment"), shipmentInd);

						loadingUnitInd = vf.createIRI(baseURI, params[1] + "_loadingUnit");
						connection.add(loadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(tradeItemInd, vf.createIRI(baseURI + "hasLoadingUnit"), loadingUnitInd);								

						connection.add(tradeItemInd, vf.createIRI(baseURI + "gtin"), vf.createLiteral(params[2]));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierQuantity"), vf.createLiteral(params[6], XMLSchema.DECIMAL));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "customerQuantity"), vf.createLiteral(params[7], XMLSchema.DECIMAL));								
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierProductDescription"), vf.createLiteral(params[8]));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierProductId"), vf.createLiteral(params[10]));
						
						if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
							connection.add(tradeItemInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[12]), XMLSchema.DATETIME));
						}
						
						connection.add(tradeItemInd, vf.createIRI(baseURI + "handlingInstruction"), vf.createLiteral(params[16]));
	

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
	
	public static void processTradeItemsHTTP (File tradeItemsFolder, String baseURI, String rdf4jServer, String repositoryId) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		Repository repo = new HTTPRepository(rdf4jServer, repositoryId);

		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();

			IRI tradeItemInd;
			IRI shipmentInd;
			IRI loadingUnitInd;


			IRI tradeItemClass = vf.createIRI(baseURI, "TradeItem");
			IRI shipmentClass = vf.createIRI(baseURI, "Shipment");
			IRI loadingUnitClass = vf.createIRI(baseURI, "LoadingUnit");

			File[] filesInDir = tradeItemsFolder.listFiles();

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
						tradeItemInd = vf.createIRI(baseURI, params[0] + "-" + params[1] + "_tradeitem" );
						connection.add(tradeItemInd, RDF.TYPE, tradeItemClass);

						//adding predicates
						shipmentInd = vf.createIRI(baseURI, params[0] + "_shipment");
						connection.add(shipmentInd, RDF.TYPE, shipmentClass);
						connection.add(tradeItemInd, vf.createIRI(baseURI + "belongsToShipment"), shipmentInd);

						loadingUnitInd = vf.createIRI(baseURI, params[1] + "_loadingUnit");
						connection.add(loadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(tradeItemInd, vf.createIRI(baseURI + "hasLoadingUnit"), loadingUnitInd);								

						connection.add(tradeItemInd, vf.createIRI(baseURI + "gtin"), vf.createLiteral(params[2]));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierQuantity"), vf.createLiteral(params[6], XMLSchema.DECIMAL));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "customerQuantity"), vf.createLiteral(params[7], XMLSchema.DECIMAL));								
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierProductDescription"), vf.createLiteral(params[8]));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierProductId"), vf.createLiteral(params[10]));
						
						if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
							connection.add(tradeItemInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[12]), XMLSchema.DATETIME));
						}
						
						connection.add(tradeItemInd, vf.createIRI(baseURI + "handlingInstruction"), vf.createLiteral(params[16]));
	

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
