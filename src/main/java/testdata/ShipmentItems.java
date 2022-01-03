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
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.eclipse.rdf4j.repository.http.HTTPRepository;


/**
 * @author audunvennesland
 *
 */
public class ShipmentItems
{
	public static void processShipmentItems(File partiesFolder, String baseURI, String dataDir, String indexes) {

		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));
		//Repository repo = new HTTPRepository(rdf4jServer, repositoryId);

		try (RepositoryConnection connection = repo.getConnection()) {

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

					System.out.println("Reading file: " + filesInDir[i].getName());

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
	
	public static void processShipmentItemsHTTP (File partiesFolder, String baseURI, String rdf4jServer, String repositoryId) {

		//Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));
		Repository repo = new HTTPRepository(rdf4jServer, repositoryId);

		try (RepositoryConnection connection = repo.getConnection()) {

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

					System.out.println("Reading file: " + filesInDir[i].getName());

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
