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
public class DangerousGoods {
	
	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";
	
	public static void processDangerousGoodsToNTriple (File dangerousGoodsFolder, String ntFile) {
		
		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "DangerousGoods";
		String tripleClosure = "> .\n";

		String dangerousGoodsEntity;

		File[] filesInDir = dangerousGoodsFolder.listFiles();

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

					dangerousGoodsEntity = params[1] + "-" + params[0] + "_dgr>";

					//rdf:type					
					bw.write(KGUtilities.createType(dangerousGoodsEntity, baseURI, rdf_type, type, tripleClosure));

					//relatesToTradeItem
					bw.write(KGUtilities.createObjectProperty(dangerousGoodsEntity, baseURI, "relatesToTradeItem", params[1], "_tradeItem", tripleClosure));

					
					//belongsToShipment
					bw.write(KGUtilities.createObjectProperty(dangerousGoodsEntity, baseURI, "belongsToShipment", params[2], "_shipment", tripleClosure));
					
					//hasLoadingUnit
					bw.write(KGUtilities.createObjectProperty(dangerousGoodsEntity, baseURI, "hasLoadingUnit", params[0], "_loadingUnit", tripleClosure));

					//modifiedOn
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {						
					bw.write(KGUtilities.createDataProperty(dangerousGoodsEntity, baseURI, "modifiedOn", StringUtilities.convertToDateTime(params[3]), DATATYPE_DATETIME, tripleClosure));
					}
					
					//hasUNIdentifier
					if (!params[5].equals("NULL")) {
					bw.write(KGUtilities.createDataProperty(dangerousGoodsEntity, baseURI, "hasUNIdentifier", params[5], DATATYPE_STRING, tripleClosure));
					}
					
					//hasRegulationClass
					if (!params[6].equals("NULL")) {
					bw.write(KGUtilities.createDataProperty(dangerousGoodsEntity, baseURI, "hasRegulationClass", params[6], DATATYPE_STRING, tripleClosure));
					}					

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
	
	public static void processDangerousGoodsToTSV (File dangerousGoodsFolder, String tsvFile) {

		String dangerousGoodsEntity;

		File[] filesInDir = dangerousGoodsFolder.listFiles();

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

					dangerousGoodsEntity = params[1] + "-" + params[0] + "_dgr";

					//isType					
					bw.write(dangerousGoodsEntity + "\t" + "isType" + "\t" + "DangerousGoods" + "\n");

					//relatesToTradeItem
					bw.write(dangerousGoodsEntity + "\t" + "relatesToTradeItem" + "\t" + params[1] + "_tradeItem" + "\n");

					
					//belongsToShipment
					bw.write(dangerousGoodsEntity + "\t" + "belongsToShipment" + "\t" + params[2] + "_shipment" + "\n");

					
					//hasLoadingUnit
					bw.write(dangerousGoodsEntity + "\t" + "hasLoadingUnit" + "\t" + params[0] + "_loadingUnit" + "\n");


					//isModifiedOn
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {						
					bw.write(dangerousGoodsEntity + "\t" + "isModifiedOn" + "\t" + StringUtilities.convertToDateTime(params[3]) + "\n");
					}
					
					//hasUNIdentifier
					if (!params[5].equals("NULL")) {
					bw.write(dangerousGoodsEntity + "\t" + "hasUNIdentifier" + "\t" + params[5] + "\n");
					}
					
					//hasRegulationClass
					if (!params[6].equals("NULL")) {
					bw.write(dangerousGoodsEntity + "\t" + "hasRegulationClass" + "\t" + params[6] + "\n");
					}
					

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

	
	public static void processDangerousGoodsToLocalRepo (File partiesFolder, String baseURI, String dataDir, String indexes, Repository repo) {

		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("m3", baseURI);

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

					//System.out.println("Reading file: " + filesInDir[i].getName());

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
						
						//TODO: add UNIdentifier (params 5) and RegulationClass (params 6) as long as they are not NULL


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
	
	public static void processDangerousGoodsToRemoteRepo (File partiesFolder, String baseURI, String rdf4jServer, String repositoryId, Repository repo) {


		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("m3", baseURI);

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

					//System.out.println("Reading file: " + filesInDir[i].getName());

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
