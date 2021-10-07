package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
import testdata.ConsignmentsGenerator;
import testdata.HubReconstructionLocationGenerator;
import testdata.InboundTrucksGenerator;
import testdata.LoadingUnitGenerator;
import testdata.PartiesGenerator;
import testdata.ShipmentItemGenerator;
import testdata.ShipmentsGenerator;
import testdata.TransportsGenerator;
import testdata.WaveGenerator;
import testdata.XDocLoadingUnitGenerator;

public class M3OntoGeneratorUI {
	
	public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		
		String csvFolder = "./files/CSV/Last_100000";
		String m3Onto = "./files/ONTOLOGIES/M3Onto_100000.owl";
		//String numRows = "";
		
		//import manusquare ontology
		File ontoFile = new File(m3Onto);
		
		//access all files in folder
		File folder = new File(csvFolder);
		File[] files = folder.listFiles();
		
		//define the manager responsible for ontology manipulation
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File(m3Onto.substring(0, m3Onto.lastIndexOf("/"))), true);		
		manager.addIRIMapper(mapper);
		
		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		OWLDataFactory df = manager.getOWLDataFactory();
		
		for (File file : files) {
			if (file.getName().startsWith("Parties")) {
				
				System.out.println("Getting parties individuals from " + file.getName());
				
				PartiesGenerator data;
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				String line = br.readLine();

				String[] params = null;

				Set<PartiesGenerator> dataset = new HashSet<PartiesGenerator>();
				
				while (line != null) {
					params = line.split(";");

					data = new PartiesGenerator();							
					data.setPartiesHashCode(params[2]);
					data.setPartyName(params[3]);
					data.setCountry(params[6]);
					data.setCity(params[8]);
					data.setPostalCode(params[9]);
					data.setCoordinates(params[19]);
					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				//df = manager.getOWLDataFactory();
				
				OWLClass partyClass = OntologyOperations.getClass("Party", onto);
				OWLIndividual partyInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				//adding process chain
				for (PartiesGenerator td : dataset) {

					//adding process chain individual
					partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getPartiesHashCode() + "_Party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);

					//DP for expressing process chain name and id
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("partyHashCode", onto), partyInd, td.getPartiesHashCode());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("partyName", onto), partyInd, td.getPartyName());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("country", onto), partyInd, td.getCountry());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("city", onto), partyInd, td.getCity());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("postalCode", onto), partyInd, td.getPostalCode());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
								
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("asWKT", onto), partyInd, PartiesGenerator.formatCoordinates(td.getCoordinates()));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

				}
				//save the ontology in each iteration
				manager.saveOntology(onto);
							
				
			} else if (file.getName().startsWith("Shipments")) {
				
				System.out.println("Getting shipments individuals from " + file.getName());
				
				ShipmentsGenerator data;

				BufferedReader br = new BufferedReader(new FileReader(file));

				String line = br.readLine();

				String[] params = null;

				Set<ShipmentsGenerator> dataset = new HashSet<ShipmentsGenerator>();
				
				while (line != null) {
					params = line.split(";");

					data = new ShipmentsGenerator();
								
					data.setShipmentId(params[0]);
					data.setShippedOn(OntologyOperations.convertToDateTime(manager, params[3]));
					data.setExpectedDeliveryOn(OntologyOperations.convertToDateTime(manager, params[4]));
					data.setShipperAdditionalPartyIdentification(params[6]);
					data.setShipperGLN(params[7]);
					data.setShipperHashCode(params[8]);
					data.setReceiverAdditionalPartyIdentification(params[9]);
					data.setReceiverGLN(params[10]);
					data.setReceiverHashCode(params[11]);
					data.setQttBoxesInShipment(OntologyOperations.convertToInt(manager, params[18]));
					data.setQttPalletsInShipment(OntologyOperations.convertToInt(manager, params[19]));
					
					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass shipmentClass = OntologyOperations.getClass("Shipment", onto);

				//OWLDataFactory df = manager.getOWLDataFactory();

				OWLIndividual shipmentInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				//OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				//adding process chain
				for (ShipmentsGenerator td : dataset) {

					//adding shipment individual
					shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipmentId() + "_shipment"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);
								
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shippedOn", onto), shipmentInd, td.getShippedOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					if (!td.getExpectedDeliveryOn().getLiteral().startsWith("0000")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("expectedDeliveryOn", onto), shipmentInd, td.getExpectedDeliveryOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipperAdditionalPartyIdentification", onto), shipmentInd, td.getShipperAdditionalPartyIdentification());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipperGLN", onto), shipmentInd, td.getShipperGLN());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipperHashCode", onto), shipmentInd, td.getShipperHashCode());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("receiverAdditionalPartyIdentification", onto), shipmentInd, td.getReceiverAdditionalPartyIdentification());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("receiverGLN", onto), shipmentInd, td.getReceiverGLN());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("receiverHashCode", onto), shipmentInd, td.getReceiverHashCode());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxesInShipment", onto), shipmentInd, td.getQttBoxesInShipment());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPalletsInShipment", onto), shipmentInd, td.getQttPalletsInShipment());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					

				}
				//save the ontology in each iteration
				manager.saveOntology(onto);
				
			} else if (file.getName().startsWith("LoadingUnit")) {
				
				System.out.println("Getting loading unit individuals from " + file.getName());
				
				LoadingUnitGenerator data;

				BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Last_10000/LoadingUnits_last_10000.csv"));

				String line = br.readLine();

				String[] params = null;

				Set<LoadingUnitGenerator> dataset = new HashSet<LoadingUnitGenerator>();
				
				while (line != null) {
					params = line.split(";");

					data = new LoadingUnitGenerator();
								
					data.setLoadingUnitId(params[0]);
					data.setPackageTypeId(params[1]);
					data.setOrderNumber(params[2]);
					data.setLoadingUnitModifiedOn(OntologyOperations.convertToDateTime(manager, params[3]));

					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);

				//OWLDataFactory df = manager.getOWLDataFactory();

				OWLIndividual loadingUnitInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				//adding process chain
				for (LoadingUnitGenerator td : dataset) {

					//adding loading unit individual
					loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getLoadingUnitId() + "_loadingUnit"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);

					//DP for expressing process chain name and id
					if (!td.getLoadingUnitModifiedOn().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("loadingUnitModifiedOn", onto), loadingUnitInd, td.getLoadingUnitModifiedOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("packageTypeId", onto), loadingUnitInd, td.getPackageTypeId());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("orderNumber", onto), loadingUnitInd, td.getOrderNumber());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

				}
				//save the ontology in each iteration
				manager.saveOntology(onto);
				
			} else if (file.getName().startsWith("ShipmentItems")) {
				
				System.out.println("Getting shipment item individuals from " + file.getName());
				
				ShipmentItemGenerator data;

				BufferedReader br = new BufferedReader(new FileReader(file));

				String line = br.readLine();

				String[] params = null;

				Set<ShipmentItemGenerator> dataset = new HashSet<ShipmentItemGenerator>();
				
				while (line != null) {
					params = line.split(";");

					data = new ShipmentItemGenerator();								
					data.setShipmentItemId(params[0]);
					data.setLoadingUnitId(params[1]);
					data.setShipmentItemModifiedOn(OntologyOperations.convertToDateTime(manager, params[2]));
					data.setQuantity(OntologyOperations.convertToInt(manager, params[3]));

					dataset.add(data);
					line = br.readLine();
				}

				br.close();
				
				OWLClass shipmentItemClass = OntologyOperations.getClass("ShipmentItem", onto);
				OWLClass shipmentClass = OntologyOperations.getClass("Shipment", onto);
				OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);

				//OWLDataFactory df = manager.getOWLDataFactory();

				OWLIndividual shipmentItemInd = null;
				OWLIndividual shipmentInd = null;
				OWLIndividual loadingUnitInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				for (ShipmentItemGenerator td : dataset) {

					//adding shipmentItem individual
					shipmentItemInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipmentItemId() + "-" + td.getLoadingUnitId() + "_shipmentItem"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentItemClass, shipmentItemInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);					
					
					//object properties
					shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getShipmentItemId() + "_shipment");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("shipmentItemShipmentId", onto), shipmentItemInd, shipmentInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getLoadingUnitId() + "_loadingUnit");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("shipmentItemLoadingUnit", onto), shipmentItemInd, loadingUnitInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					//data properties
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipmentItemModifiedOn", onto), shipmentItemInd, td.getShipmentItemModifiedOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("quantity", onto), shipmentItemInd, td.getQuantity());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

				}
				//save the ontology in each iteration
				manager.saveOntology(onto);
				
			} else if (file.getName().startsWith("Transports")) {
				
				System.out.println("Getting transports individuals from " + file.getName());
				
				TransportsGenerator data;

				BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Last_10000/Transports_last_10000.csv"));

				String line = br.readLine();

				String[] params = null;

				Set<TransportsGenerator> dataset = new HashSet<TransportsGenerator>();
				
				while (line != null) {
					params = line.split(";");

					data = new TransportsGenerator();
					
					if (!params[0].equals("0") && !params[0].equalsIgnoreCase("TransportId")) {
					
					data.setTransportId(params[0]);
					data.setExpectedArrival(OntologyOperations.convertToDateTime(manager, params[2]));
					data.setTransportName(params[9]);
					data.setTransportType(params[11]);
					}

					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass transportClass = OntologyOperations.getClass("Transport", onto);

				//OWLDataFactory df = manager.getOWLDataFactory();

				OWLIndividual transportInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				//OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				//adding new transports
				for (TransportsGenerator td : dataset) {

					//adding transport individual
					transportInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getTransportId() + "_transport"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(transportClass, transportInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);


					//DP for expressing transport details
					if (!td.getExpectedArrival().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("expectedArrival", onto), transportInd, td.getExpectedArrival());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("transportName", onto), transportInd, df.getOWLLiteral(td.getTransportName().replaceAll(",", "_")));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("transportType", onto), transportInd, df.getOWLLiteral(td.getTransportType().replaceAll(",", "_")));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);


				}
				//save the ontology in each iteration
				manager.saveOntology(onto);
				
			} else if (file.getName().startsWith("Consignments")) {
				
				System.out.println("Getting consignment individuals from " + file.getName());
				
				ConsignmentsGenerator data;

				BufferedReader br = new BufferedReader(new FileReader(file));

				String line = br.readLine();

				String[] params = null;

				Set<ConsignmentsGenerator> dataset = new HashSet<ConsignmentsGenerator>();
				
				while (line != null) {
					params = line.split(";");

					data = new ConsignmentsGenerator();
								
					data.setConsignmentId(params[0]);
					data.setTransportId(params[3]);
					data.setConsignmentModifiedOn(OntologyOperations.convertToDateTime(manager, params[5]));
					data.setReconstructionLocation(params[8]);
					data.setCarrierHashCode(params[11]);
					data.setConsignorAdditionalPartyIdentification(params[12]);
					data.setConsignorGLN(params[13]);
					data.setConsignorHashCode(params[14]);
					data.setConsigneeAdditionalPartyIdentification(params[15]);
					data.setConsigneeGLN(params[16]);
					data.setConsigneeHashCode(params[17]);
					data.setWaveId(params[18]);
					data.setConsignmentTaskClosedOn(OntologyOperations.convertToDateTime(manager, params[23]));
					
					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass consignmentClass = OntologyOperations.getClass("Consignment", onto);
				OWLClass transportClass = OntologyOperations.getClass("Transport", onto);
				OWLClass waveClass = OntologyOperations.getClass("Wave", onto);

				//OWLDataFactory df = manager.getOWLDataFactory();

				OWLIndividual consignmentInd = null;
				OWLIndividual transportInd = null;
				OWLIndividual waveInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				//adding process chain
				for (ConsignmentsGenerator td : dataset) {

					//adding consignment individual
					consignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsignmentId() + "_consignment"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, consignmentInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);
					
					//adding transport individual and OP			
					if (!td.getTransportId().equals("NULL")) {
					transportInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getTransportId() + "_transport"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(transportClass, transportInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					//OP transportId from consignmentInd to transportInd
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("transportId", onto), consignmentInd, transportInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);				
					}
					
					//adding wave individual
					if (!td.getWaveId().equals("NULL")) {
					waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getWaveId() + "_wave"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					//OP waveId from consignmentInd to waveInd
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("consignmentWaveId", onto), consignmentInd, waveInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);			
					}
					
					if (!td.getConsignmentModifiedOn().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignmentModifiedOn", onto), consignmentInd, td.getConsignmentModifiedOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionLocation", onto), consignmentInd, td.getReconstructionLocation());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("carrierHashCode", onto), consignmentInd, td.getCarrierHashCode());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignorAdditionalPartyIdentification", onto), consignmentInd, td.getConsignorAdditionalPartyIdentification());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignorGLN", onto), consignmentInd, td.getConsignorGLN());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignorHashCode", onto), consignmentInd, td.getConsignorHashCode());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consigneeAdditionalPartyIdentification", onto), consignmentInd, td.getConsigneeAdditionalPartyIdentification());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consigneeGLN", onto), consignmentInd, td.getConsigneeGLN());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consigneeHashCode", onto), consignmentInd, td.getConsigneeHashCode());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					if (!td.getConsignmentTaskClosedOn().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignmentTaskClosedOn", onto), consignmentInd, td.getConsignmentTaskClosedOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					

				}
				//save the ontology in each iteration
				manager.saveOntology(onto);
				
			} else if (file.getName().startsWith("HubReconstructionLocations")) {
				
				System.out.println("Getting hub reconstriction location individuals from " + file.getName());
				
				HubReconstructionLocationGenerator data;

				BufferedReader br = new BufferedReader(new FileReader(file));

				String line = br.readLine();

				String[] params = null;

				Set<HubReconstructionLocationGenerator> dataset = new HashSet<HubReconstructionLocationGenerator>();
				
				while (line != null) {
					params = line.split(";");

					data = new HubReconstructionLocationGenerator();
								
					data.setHubReconstructionLocationId(params[0]);
					data.setHubReconstructionLocationAdditionalPartyIdentification(params[1]);
					data.setHubReconstructionLocationHashCode(params[3]);
					data.setHubReconstructionLocationLaneId(params[4]);


					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass hubReconstructionLocationClass = OntologyOperations.getClass("HubReconstructionLocation", onto);

				//OWLDataFactory df = manager.getOWLDataFactory();

				OWLIndividual hubReconstructionLocationInd = null;

				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				for (HubReconstructionLocationGenerator td : dataset) {

					//individuals and association to classes
					hubReconstructionLocationInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getHubReconstructionLocationHashCode() + "_hubReconstructionLocation"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(hubReconstructionLocationClass, hubReconstructionLocationInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);
					
					//data properties
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hubReconstructionLocationAdditionalPartyIdentification", onto), hubReconstructionLocationInd, td.getHubReconstructionLocationAdditionalPartyIdentification());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hubReconstructionLocationId", onto), hubReconstructionLocationInd, td.getHubReconstructionLocationId());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hubReconstructionLocationLaneId", onto), hubReconstructionLocationInd, td.getHubReconstructionLocationLaneId());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
				}
				//save the ontology in each iteration
				manager.saveOntology(onto);
				
			} else if (file.getName().startsWith("Waves")) {
				
				System.out.println("Getting wave individuals from " + file.getName());
				
				WaveGenerator data;

				BufferedReader br = new BufferedReader(new FileReader(file));

				String line = br.readLine();

				String[] params = null;

				Set<WaveGenerator> dataset = new HashSet<WaveGenerator>();
				
				while (line != null) {
					params = line.split(";");

					data = new WaveGenerator();
								
					data.setWaveId(params[0]);
					data.setWaveStartProcessingOn(OntologyOperations.convertToDateTime(manager, params[13]));
					data.setWaveEndProcessingOn(OntologyOperations.convertToDateTime(manager, params[14]));
					data.setQttTrailers(OntologyOperations.convertToInt(manager, params[15]));
					data.setQttBoxesInWave(OntologyOperations.convertToInt(manager, params[16]));
					data.setQttPalletsInWave(OntologyOperations.convertToInt(manager, params[17]));
					data.setQttBoxesProcessed(OntologyOperations.convertToInt(manager, params[18]));			
					data.setQttPalletsBuilt(OntologyOperations.convertToInt(manager, params[19]));
					data.setQttTasks(OntologyOperations.convertToInt(manager, params[20]));
					data.setQttShipments(OntologyOperations.convertToInt(manager, params[21]));

					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass waveClass = OntologyOperations.getClass("Wave", onto);

				//OWLDataFactory df = manager.getOWLDataFactory();

				OWLIndividual waveInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				//adding new waves
				for (WaveGenerator td : dataset) {

					//adding wave individual
					waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getWaveId() + "_wave"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);

					//DP for expressing wave details
					if (!td.getWaveStartProcessingOn().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveStartProcessingOn", onto), waveInd, td.getWaveStartProcessingOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					if (!td.getWaveEndProcessingOn().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveEndProcessingOn", onto), waveInd, td.getWaveEndProcessingOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttTrailers", onto), waveInd, td.getQttTrailers());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxesInWave", onto), waveInd, td.getQttBoxesInWave());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPalletsInWave", onto), waveInd, td.getQttPalletsInWave());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxesProcessed", onto), waveInd, td.getQttBoxesProcessed());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPalletsBuilt", onto), waveInd, td.getQttPalletsBuilt());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttTasks", onto), waveInd, td.getQttTasks());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttShipments", onto), waveInd, td.getQttShipments());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);


				}
				//save the ontology in each iteration
				manager.saveOntology(onto);
				
			} else if (file.getName().startsWith("XDocLoadingUnits")) {
				
				System.out.println("Getting xdoc loading units individuals from " + file.getName());
				
				XDocLoadingUnitGenerator data;

				BufferedReader br = new BufferedReader(new FileReader(file));

				String line = br.readLine();

				String[] params = null;

				Set<XDocLoadingUnitGenerator> dataset = new HashSet<XDocLoadingUnitGenerator>();
				
				while (line != null) {
					params = line.split(";");

					data = new XDocLoadingUnitGenerator();
								
					data.setInternalId(params[0]);
					data.setPreSortScanOn(OntologyOperations.convertToDateTime(manager, params[1]));
					data.setReconstructedScanOn(OntologyOperations.convertToDateTime(manager, params[2]));
					data.setFinishedScanOn(OntologyOperations.convertToDateTime(manager, params[3]));
					data.setxDocLoadingUnitVolume(OntologyOperations.convertToDecimal(manager,params[5]));
					data.setxDocLoadingUnitWeight(OntologyOperations.convertToDecimal(manager, params[6]));
					data.setLoadingUnitForXDocLoadingUnit(params[7]);
					data.setInboundConsignmentId(params[11]);
					data.setOutboundConsignmentId(params[12]);			
					data.setxDocLoadingUnitHubReconstructionLaneId(params[16]);
					data.setxDocLoadungUnitHubReconstructionLocationHashCode(params[20]);
					data.setxDocLoadingUnitWaveId(params[36]);
					data.setReconstructionTypeId(params[37]);
					data.setInboundParentLoadingUnitForXDocLoadingUnit(params[41]);
					data.setOutboundParentLoadingUnitForXDocLoadingUnit(params[42]);
					
					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass xDocLoadingUnitClass = OntologyOperations.getClass("XDocLoadingUnit", onto);
				OWLClass consignmentClass = OntologyOperations.getClass("Consignment", onto);
				OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);
				OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
				OWLClass hubReconstructionLocationClass = OntologyOperations.getClass("HubReconstructionLocation", onto);

				//OWLDataFactory df = manager.getOWLDataFactory();

				OWLIndividual xDocLoadingUnitInd = null;
				OWLIndividual inboundConsignmentInd = null;
				OWLIndividual outboundConsignmentInd = null;
				OWLIndividual loadingUnitInd = null;
				OWLIndividual waveInd = null;
				OWLIndividual hubReconstructionLocationInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				for (XDocLoadingUnitGenerator td : dataset) {

					//adding individual
					xDocLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" +td.getInternalId() + "_xDocLoadingUnit"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(xDocLoadingUnitClass, xDocLoadingUnitInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);				
					
					//object properties
					inboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getInboundConsignmentId() + "_consignment");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, inboundConsignmentInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("inboundConsignmentId", onto), xDocLoadingUnitInd, inboundConsignmentInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					outboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getOutboundConsignmentId() + "_consignment");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, outboundConsignmentInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("outboundConsignmentId", onto), xDocLoadingUnitInd, outboundConsignmentInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getLoadingUnitForXDocLoadingUnit() + "_loadingUnit");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("loadingUnitForXDocLoadingUnit", onto), xDocLoadingUnitInd, loadingUnitInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					
					waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getxDocLoadingUnitWaveId() + "_wave");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("xDocLoadingUnitWaveId", onto), xDocLoadingUnitInd, waveInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					
					hubReconstructionLocationInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getXDocLoadingUnitHubReconstructionLocationHashCode() + "_hubReconstructionLocation");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(hubReconstructionLocationClass, hubReconstructionLocationInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("xDocLoadingUnitHubReconstructionLocationHashCode", onto), xDocLoadingUnitInd, hubReconstructionLocationInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					


					//data properties
					if (!td.getPreSortScanOn().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("preSortScanOn", onto), xDocLoadingUnitInd, td.getPreSortScanOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					if (!td.getReconstructedScanOn().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionScanOn", onto), xDocLoadingUnitInd, td.getReconstructedScanOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					if (!td.getFinishedScanOn().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("finishedScanOn", onto), xDocLoadingUnitInd, td.getFinishedScanOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("xDocLoadingUnitHubReconstructionLaneId", onto), xDocLoadingUnitInd, td.getxDocLoadingUnitHubReconstructionLaneId());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionTypeId", onto), xDocLoadingUnitInd, td.getReconstructionTypeId());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

				}
				
				//save the ontology in each iteration
				manager.saveOntology(onto);
				
			} else if (file.getName().startsWith("InboundTruck")) {
				
				System.out.println("Getting inbound truck individuals from " + file.getName());
				
				InboundTrucksGenerator data;

				BufferedReader br = new BufferedReader(new FileReader(file));

				String line = br.readLine();

				String[] params = null;

				Set<InboundTrucksGenerator> dataset = new HashSet<InboundTrucksGenerator>();
				
				while (line != null) {
					params = line.split(";");

					data = new InboundTrucksGenerator();
								
					data.setTruckId(params[1]);
					data.setTrailerIdentification(params[2]);
					data.setFirstScan(OntologyOperations.convertToDateTime(manager, params[3]));
					data.setNumberOfShipments(OntologyOperations.convertToInt(manager, params[4]));
					data.setExpectedArrival(OntologyOperations.convertToDateTime(manager, params[5]));
					data.setNumberOfLooseCartons(OntologyOperations.convertToInt(manager, params[6]));
					data.setTotalVolume(OntologyOperations.convertToDecimal(manager, params[7]));
					data.setTotalWeight(OntologyOperations.convertToDecimal(manager, params[8]));
					data.setPalletNr(OntologyOperations.convertToInt(manager, params[9]));
					data.setM3HubId(params[10]);

					
					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass inboundTruckClass = OntologyOperations.getClass("InboundTruck", onto);
				OWLClass m3HubClass = OntologyOperations.getClass("HubReconstructionLocation", onto);

				//OWLDataFactory df = manager.getOWLDataFactory();

				OWLIndividual inboundTruckInd = null;
				OWLIndividual m3HubInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				//adding process chain
				for (InboundTrucksGenerator td : dataset) {

					//adding inbound truck individual
					inboundTruckInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getTruckId() + "_inboundTruck"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(inboundTruckClass, inboundTruckInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);
					
					if (!td.getM3HubId().equals("NULL")) {
					m3HubInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getM3HubId() + "m3HubId"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(m3HubClass, m3HubInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("m3HubId", onto), inboundTruckInd, m3HubInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);				
					}
				
					if (!td.getFirstScan().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("firstScan", onto), inboundTruckInd, td.getFirstScan());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					if (!td.getExpectedArrival().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("inboundTruckExpectedArrival", onto), inboundTruckInd, td.getExpectedArrival());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("palletNr", onto), inboundTruckInd, td.getPalletNr());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("totalWeight", onto), inboundTruckInd, td.getTotalWeight());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("totalVolume", onto), inboundTruckInd, td.getTotalVolume());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("numberOfLooseCartons", onto), inboundTruckInd, td.getNumberOfLooseCartons());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("trailerIdentification", onto), inboundTruckInd, td.getTrailerIdentification());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("numberOfShipments", onto), inboundTruckInd, td.getNumberOfShipments());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					

				}
				//save the ontology in each iteration
				manager.saveOntology(onto);
				
			}
		}
		
		
		
		
		
	}

}
