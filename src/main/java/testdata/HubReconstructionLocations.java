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
import org.eclipse.rdf4j.model.datatypes.XMLDatatypeUtil;

/**
 * @author audunvennesland
 *
 */
public class HubReconstructionLocations
{

	public static void processHubReconstructionLocations (File hubReconstructionLocationsFolder, String baseURI, String dataDir, String indexes) {


		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));


		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();


			IRI hubReconstructionInd;
			IRI hubReconstructionClass = vf.createIRI(baseURI, "HubReconstructionLocation");

			File[] filesInDir = hubReconstructionLocationsFolder.listFiles();
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
						hubReconstructionInd = vf.createIRI(baseURI, params[0] + "_hubReconstructionLocation");			
						connection.add(hubReconstructionInd, RDF.TYPE, hubReconstructionClass);

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
	
	public static void processHubReconstructionLocationsHTTP (File hubReconstructionLocationsFolder, String baseURI, String rdf4jServer, String repositoryId) {


		Repository repo = new HTTPRepository(rdf4jServer, repositoryId);


		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();


			IRI hubReconstructionInd;
			IRI hubReconstructionClass = vf.createIRI(baseURI, "HubReconstructionLocation");

			File[] filesInDir = hubReconstructionLocationsFolder.listFiles();
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
						hubReconstructionInd = vf.createIRI(baseURI, params[0] + "_hubReconstructionLocation");			
						connection.add(hubReconstructionInd, RDF.TYPE, hubReconstructionClass);

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
