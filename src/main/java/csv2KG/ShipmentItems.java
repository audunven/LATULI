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
public class ShipmentItems {

	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";

	public static void processShipmentItemsToNTriple (File partiesFolder, String ntFile) {

		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "ShipmentItem";
		String tripleClosure = "> .\n";

		String shipmentItemEntity;

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

					//adding type
					shipmentItemEntity = params[0] + "_" + params[1] + "_shipmentItem>";
					
					bw.write(KGUtilities.createType(shipmentItemEntity, baseURI, rdf_type, type, tripleClosure));

					//belongsToShipment						
					bw.write(KGUtilities.createObjectProperty(shipmentItemEntity, baseURI, "belongsToShipment", params[0], "_shipment", tripleClosure));

					//hasLoadingUnit						
					bw.write(KGUtilities.createObjectProperty(shipmentItemEntity, baseURI, "hasLoadingUnit", params[1], "_loadingUnit", tripleClosure));

					//quantity
					bw.write(KGUtilities.createDataProperty(shipmentItemEntity, baseURI, "quantity", params[3], DATATYPE_INT, tripleClosure));


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

	public static void processShipmentItemsToTSV(File partiesFolder, String tsvFile) {


		String shipmentItemEntity;

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


					//adding type

					shipmentItemEntity = params[0] + "_" + params[1] + "_shipmentItem";

					bw.write(shipmentItemEntity + "\t" + "isType" + "\t" + "ShipmentItem" + "\n");

					//belongsToShipment						
					bw.write(shipmentItemEntity + "\t" + "belongsToShipment" + "\t" + params[0] + "_shipment" + "\n");


					//hasLoadingUnit						
					bw.write(shipmentItemEntity + "\t" + "hasLoadingUnit" + "\t" + params[1] + "_loadingUnit" + "\n");


					//hasQuantity
					bw.write(shipmentItemEntity + "\t" + "hasQuantity" + "\t" + params[3] + "\n");


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

	public static void processShipmentItemsToLocalRepo (File partiesFolder, String baseURI, String dataDir, String indexes, Repository repo) {

		try (RepositoryConnection connection = repo.getConnection()) {

			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI shipmentItemInd;
			IRI shipmentInd;
			IRI loadingUnitInd;
			IRI shipmentItemClass = vf.createIRI(baseURI, "ShipmentItem");
			IRI shipmentClass = vf.createIRI(baseURI, "Shipment");
			IRI loadingUnitClass = vf.createIRI(baseURI, "LoadingUnit");

			File[] filesInDir = partiesFolder.listFiles();
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
						shipmentItemInd = vf.createIRI(baseURI, params[0] + "_" + params[1] + "_shipmentItem");			
						connection.add(shipmentItemInd, RDF.TYPE, shipmentItemClass);

						//adding predicate
						shipmentInd = vf.createIRI(baseURI, params[0] + "_shipment");
						connection.add(shipmentInd, RDF.TYPE, shipmentClass);
						connection.add(shipmentItemInd, vf.createIRI(baseURI + "belongsToShipment"), shipmentInd);

						//adding predicate
						loadingUnitInd = vf.createIRI(baseURI, params[1] + "_loadingUnit");
						connection.add(loadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(shipmentItemInd, vf.createIRI(baseURI + "hasLoadingUnit"), loadingUnitInd);

						//adding literals
						connection.add(shipmentItemInd, vf.createIRI(baseURI + "quantity"), vf.createLiteral(params[3], XMLSchema.INT));

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

	public static void processShipmentItemsToRemoteRepo (File partiesFolder, String baseURI, String rdf4jServer, String repositoryId, Repository repo) {

		try (RepositoryConnection connection = repo.getConnection()) {

			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI shipmentItemInd;
			IRI shipmentInd;
			IRI loadingUnitInd;
			IRI shipmentItemClass = vf.createIRI(baseURI, "ShipmentItem");
			IRI shipmentClass = vf.createIRI(baseURI, "Shipment");
			IRI loadingUnitClass = vf.createIRI(baseURI, "LoadingUnit");

			File[] filesInDir = partiesFolder.listFiles();
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
						shipmentItemInd = vf.createIRI(baseURI, params[0] + "_" + params[1] + "_shipmentItem");			
						connection.add(shipmentItemInd, RDF.TYPE, shipmentItemClass);

						//adding predicate
						shipmentInd = vf.createIRI(baseURI, params[0] + "_shipment");
						connection.add(shipmentInd, RDF.TYPE, shipmentClass);
						connection.add(shipmentItemInd, vf.createIRI(baseURI + "belongsToShipment"), shipmentInd);

						//adding predicate
						loadingUnitInd = vf.createIRI(baseURI, params[1] + "_loadingUnit");
						connection.add(loadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(shipmentItemInd, vf.createIRI(baseURI + "hasLoadingUnit"), loadingUnitInd);

						//adding literals
						connection.add(shipmentItemInd, vf.createIRI(baseURI + "quantity"), vf.createLiteral(params[3], XMLSchema.INT));

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
}
