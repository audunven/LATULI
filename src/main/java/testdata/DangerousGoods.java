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
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class DangerousGoods
{
	public static void processDangerousGoods (File partiesFolder, String baseURI, String dataDir, String indexes) {


		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));


		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();


			IRI dangerousGoodsInd;
			IRI tradeItemInd;
			IRI shipmentInd;
			IRI loadingUnitInd;
			
			
			IRI dangerousGoodsClass = vf.createIRI(baseURI, "DangerousGoods");
			IRI tradeItemClass = vf.createIRI(baseURI, "TradeItem");
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
						dangerousGoodsInd = vf.createIRI(baseURI, params[1] + "-" + params[0] + "_dangerousGoods");			
						connection.add(dangerousGoodsInd, RDF.TYPE, dangerousGoodsClass);

						//adding predicate
						tradeItemInd = vf.createIRI(baseURI, params[1] + "_tradeItem");
						connection.add(tradeItemInd, RDF.TYPE, tradeItemClass);
						connection.add(dangerousGoodsInd, vf.createIRI(baseURI + "relatesToTradeItem"), tradeItemInd);
						
						//adding predicate
						shipmentInd = vf.createIRI(baseURI, params[2] + "_shipment");
						connection.add(shipmentInd, RDF.TYPE, shipmentClass);
						connection.add(dangerousGoodsInd, vf.createIRI(baseURI + "belongsToShipment"), shipmentInd);

						//adding predicate
						loadingUnitInd = vf.createIRI(baseURI, params[0] + "_loadingUnit");
						connection.add(loadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(dangerousGoodsInd, vf.createIRI(baseURI + "hasLoadingUnit"), loadingUnitInd);

						//adding literals
						if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
						connection.add(dangerousGoodsInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[3]), XMLSchema.DATETIME));					
						}


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
	
	public static void processDangerousGoodsHTTP (File partiesFolder, String baseURI, String rdf4jServer, String repositoryId) {


		Repository repo = new HTTPRepository(rdf4jServer, repositoryId);


		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();


			IRI dangerousGoodsInd;
			IRI tradeItemInd;
			IRI shipmentInd;
			IRI loadingUnitInd;
			
			
			IRI dangerousGoodsClass = vf.createIRI(baseURI, "DangerousGoods");
			IRI tradeItemClass = vf.createIRI(baseURI, "TradeItem");
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
						dangerousGoodsInd = vf.createIRI(baseURI, params[1] + "-" + params[0] + "_dangerousGoods");			
						connection.add(dangerousGoodsInd, RDF.TYPE, dangerousGoodsClass);

						//adding predicate
						tradeItemInd = vf.createIRI(baseURI, params[1] + "_tradeItem");
						connection.add(tradeItemInd, RDF.TYPE, tradeItemClass);
						connection.add(dangerousGoodsInd, vf.createIRI(baseURI + "relatesToTradeItem"), tradeItemInd);
						
						//adding predicate
						shipmentInd = vf.createIRI(baseURI, params[2] + "_shipment");
						connection.add(shipmentInd, RDF.TYPE, shipmentClass);
						connection.add(dangerousGoodsInd, vf.createIRI(baseURI + "belongsToShipment"), shipmentInd);

						//adding predicate
						loadingUnitInd = vf.createIRI(baseURI, params[0] + "_loadingUnit");
						connection.add(loadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(dangerousGoodsInd, vf.createIRI(baseURI + "hasLoadingUnit"), loadingUnitInd);

						//adding literals
						if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
						connection.add(dangerousGoodsInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[3]), XMLSchema.DATETIME));					
						}


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
