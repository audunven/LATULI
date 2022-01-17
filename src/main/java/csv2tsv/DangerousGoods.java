package csv2tsv;

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

import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class DangerousGoods {


	//test
	public static void main(String[] args) {
		
		String folder = "./files/CSV/Audun/_FILTER_HUB/dgr_split_filtered";
		String outputFile = "./files/CSV/Audun/dangerousGoodsTest.tsv";
		processDangerousGoodsToTSV(new File(folder), outputFile);
		
		
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
					bw = new BufferedWriter(new FileWriter(tsvFile));

					System.out.println("Reading file: " + filesInDir[i].getName());
					
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
						bw.write(dangerousGoodsEntity + "\t" + "relatesToTradeItem" + "\t" + params[1] + "\n");

						
						//belongsToShipment
						bw.write(dangerousGoodsEntity + "\t" + "belongsToShipment" + "\t" + params[2] + "\n");

						
						//hasLoadingUnit
						bw.write(dangerousGoodsEntity + "\t" + "hasLoadingUnit" + "\t" + params[0] + "\n");


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
	
}
