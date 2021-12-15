package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import owlprocessing.OntologyOperations;
import testdata.Consignments;
import testdata.DangerousGoods;
import testdata.HubReconstructionLocations;
import testdata.Parties;
import testdata.ShipmentItems;
import testdata.Shipments;
import testdata.Shipments_simple;
import testdata.TradeItems;
import testdata.Transports;
import testdata.Waves;
import testdata.Waves_simple;
import testdata.Parties_simple;
import utilities.StringUtilities;

public class M3OntoGeneratorUI_FullDataset {
	
	public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		
		long startTime = System.nanoTime();
		
		String csvFolder = "./files/CSV/Truls/split";
		String ontoFilePath = "./files/ONTOLOGIES/M3Onto_TBox.owl";
		
		//access all files in folder
		File parentFolder = new File(csvFolder);
		File[] files = parentFolder.listFiles();
		
		for (File folder : files) {
			if (folder.getName().startsWith("Parties")) {
				
				Parties_simple.processParties(folder, ontoFilePath);

			} else if (folder.getName().startsWith("Shipments")) {
				
				Shipments_simple.processShipments(folder, ontoFilePath);
				
			} else if (folder.getName().startsWith("Waves")) {
				
				Waves_simple.processWaves(folder, ontoFilePath);
	
			} 
			
//			else if (file.getName().startsWith("TradeItems")) {
//				
//				TradeItems_simple.processTradeItems(folder, onto);
//				
//				
//			} else if (file.getName().startsWith("DRGs")) {
//				
//				DangerousGoods_simple.processDangerousGoods(folder, onto);
//				
//				
//			} else if (file.getName().startsWith("ShipmentItems")) {
//				
//				ShipmentItems_simple.processShipmentItems(folder, onto);
//				
//			} else if (file.getName().startsWith("Transports")) {
//				
//				Transports_simple.processTransports(folder, onto);
//				
//			} else if (file.getName().startsWith("Consignments")) {
//				
//				Consignments_simple.processConsignments(folder, onto);
//				
//			} else if (file.getName().startsWith("HubReconstructionLocations")) {
//				
//				HubReconstructionLocations_simple.processHubReconstructionLocations(folder, onto);
//				
//			} else if (file.getName().startsWith("Waves")) {
//				
//				Waves_simple.processWaves(folder, onto);
//				
//			} else if (file.getName().startsWith("XDocLoadingUnits")) {
//				
//				XDocLoadingUnits_simple.processXDocLoadingUnits(folder, onto);
//				
//			} 
			

			
		}
		
		long endTime = System.nanoTime();		
		long timeElapsed = endTime - startTime;		
		System.err.println("The ontology generation process took: " + timeElapsed/60000000000.00 + " minutes");
		
		System.out.println("\nMEMORY USAGE for the entire transformation process: ");
		System.out.println("Used Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB");  
		
	}
	
	public static String formatCoordinates (String coordinates) {
		return "POINT(" + coordinates.substring(0, coordinates.indexOf(",")) + " " + coordinates.substring(coordinates.indexOf(",")+1, coordinates.length()) + ")";
	}

}
