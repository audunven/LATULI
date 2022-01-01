package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;


public class RDF4J {

	public static void main(String[] args) {

		String rdf4jServer = "http://78.91.106.145:8080/rdf4j-server";
		String repositoryID = "LATKG1";
		Repository repo = new HTTPRepository(rdf4jServer, repositoryID);

		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();
			String baseURI = "http://latuli.no/onto#";

			IRI shipmentItemInd;
			IRI shipmentInd;
			IRI loadingUnitInd;
			IRI shipmentItemClass = vf.createIRI(baseURI, "ShipmentItem");
			IRI shipmentClass = vf.createIRI(baseURI, "Shipment");
			IRI loadingUnitClass = vf.createIRI(baseURI, "LoadingUnit");

			String CSV_folder = "./files/CSV/test_split_small";
			File folder = new File(CSV_folder);
			File[] filesInDir = folder.listFiles();
			System.out.println("There are " + filesInDir.length + " files in the folder.");
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
						shipmentItemInd = vf.createIRI(baseURI, params[1] + "-" + params[2] + "_shipmentitem");			
						connection.add(shipmentItemInd, RDF.TYPE, shipmentItemClass);

						//adding predicate
						shipmentInd = vf.createIRI(baseURI, params[1] + "_shipment");
						connection.add(shipmentInd, RDF.TYPE, shipmentClass);
						connection.add(shipmentItemInd, vf.createIRI(baseURI + "belongsToShipment"), shipmentInd);
						
						//adding predicate
						loadingUnitInd = vf.createIRI(baseURI, params[2] + "_loadingunit");
						connection.add(loadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(shipmentItemInd, vf.createIRI(baseURI + "hasLoadingUnit"), loadingUnitInd);

						//adding literals - do I need to represent as an xsd datatype?
						connection.add(shipmentItemInd, vf.createIRI(baseURI + "quantity"), vf.createLiteral(params[3]));
						connection.add(shipmentItemInd, vf.createIRI(baseURI + "originalDataSource"), vf.createLiteral(params[4]));

						

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

			}//end for


		}//end try

		repo.shutDown();


	}//end main

}//end class
