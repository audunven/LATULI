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

public class M3OntoGeneratorUI_FullDataset {
	
	public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		
		String csvFolder = "./files/CSV/Truls/Tail_100000/";
		String m3Onto = "./files/ONTOLOGIES/M3Onto_TBox.owl";
		//String numRows = "";
		
		//import ontology
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
					
					data.setAdditionalPartyIdentification(params[0]);
					data.setGln(params[1]);
					data.setPartiesHashCode(params[2]);
					data.setPartyName(params[3]);
					data.setAddressDetail(params[4]);
					data.setCode3(params[5]);
					data.setCode2(params[6]);
					data.setLocation(params[7]);
					data.setPostalCode(params[8]);
					data.setModifiedOn(OntologyOperations.convertToDateTime(manager, params[9]));
					data.setCoordinates(params[10]);


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

				//adding new parties
				for (PartiesGenerator td : dataset) {
					//iterator+=1;	

					//adding party individual
					if (!td.getAdditionalPartyIdentification().equals("nan")) {
					partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getAdditionalPartyIdentification() + "_Party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);
					}


					//DP for expressing party details
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("additionalPartyIdentification", onto), partyInd, td.getAdditionalPartyIdentification());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hashCode", onto), partyInd, td.getPartiesHashCode());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("partyName", onto), partyInd, td.getPartyName());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("addressDetail", onto), partyInd, td.getAddressDetail());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("code3", onto), partyInd, td.getCode3());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("code2", onto), partyInd, td.getCode2());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("location", onto), partyInd, td.getLocation());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("postalCode", onto), partyInd, td.getPostalCode());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					if (!td.getModifiedOn().getLiteral().startsWith("0000")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("modifiedOn", onto), partyInd, td.getModifiedOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange); 
					}
								
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("asWKT", onto), partyInd, formatCoordinates(td.getCoordinates()));
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
					params = line.split(",");

					data = new ShipmentsGenerator();
								
					data.setShipmentId(params[1]);
					data.setShippedOn(OntologyOperations.convertToDateTime(manager, params[2]));
					data.setExpectedDeliveryOn(OntologyOperations.convertToDateTime(manager, params[3]));
					data.setShipperAdditionalPartyIdentification(params[4]);
					data.setReceiverAdditionalPartyIdentification(params[5]);
					data.setPlannedDeliveryDate(OntologyOperations.convertToDateTime(manager,params[6]));
					data.setQttBoxesInShipment(OntologyOperations.convertToInt(manager, params[7]));
					data.setQttPalletsInShipment(OntologyOperations.convertToInt(manager, params[8]));
					data.setOriginalDataSource(params[9]);
					data.setYear(OntologyOperations.convertToDecimal(manager, params[10]));
					data.setSeason(OntologyOperations.convertToDecimal(manager, params[11]));
					data.setWeekDay(OntologyOperations.convertToDecimal(manager, params[12]));
							
					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass shipmentClass = OntologyOperations.getClass("Shipment", onto);
				OWLClass shipperClass = OntologyOperations.getClass("Shipper", onto);
				OWLClass receiverClass = OntologyOperations.getClass("Receiver", onto);

				OWLIndividual shipmentInd = null;
				OWLIndividual shipperInd = null;
				OWLIndividual receiverInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				for (ShipmentsGenerator td : dataset) { 
					  					  
					  //adding shipment individual 
					  shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipmentId() + "_Shipment")); 
					  classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd); 
					  addAxiomChange = new AddAxiom(onto, classAssertionAxiom); 
					  manager.applyChange(addAxiomChange);
					  
					  	//adding shipper individual
						if (!td.getShipperAdditionalPartyIdentification().equals("NULL")) {
						shipperInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipperAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(shipperClass, shipperInd);			
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP waveId from shipmentInd to shipperInd
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasShipperParty", onto), shipmentInd, shipperInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);			
						}
						
					  	//adding receiver individual
						if (!td.getReceiverAdditionalPartyIdentification().equals("NULL")) {
						receiverInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getReceiverAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(receiverClass, receiverInd);			
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
								
						//OP waveId from shipmentInd to receiverInd
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasReceiverParty", onto), shipmentInd, receiverInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);			
						}
					  
					  
					  
					  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipmentId", onto), shipmentInd, td.getShipmentId()); 
					  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					  manager.applyChange(addAxiomChange);
					  
					  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shippedOn", onto), shipmentInd, td.getShippedOn()); 
					  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					  manager.applyChange(addAxiomChange);
					  
					  if (!td.getExpectedDeliveryOn().getLiteral().startsWith("0000")) {
					  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("expectedDeliveryOn", onto), shipmentInd, td.getExpectedDeliveryOn());
					  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					  manager.applyChange(addAxiomChange); 
					  }
					  
					  if (!td.getPlannedDeliveryDate().getLiteral().startsWith("0000")) {
					  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("plannedDeliveryDate", onto), shipmentInd, td.getPlannedDeliveryDate());
					  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					  manager.applyChange(addAxiomChange); 
					  }
					  
					  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxes", onto), shipmentInd, td.getQttBoxesInShipment());
					  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					  manager.applyChange(addAxiomChange);
					  
					  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPallets", onto), shipmentInd, td.getQttPalletsInShipment());
					  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					  manager.applyChange(addAxiomChange);
					  
					  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), shipmentInd, td.getYear()); 
					  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					  manager.applyChange(addAxiomChange);
					  
					  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), shipmentInd, td.getSeason()); 
					  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					  manager.applyChange(addAxiomChange);
					  
					  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), shipmentInd, td.getWeekDay()); 
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
					params = line.split(",");

					data = new LoadingUnitGenerator();
								
					data.setLoadingUnitId(params[1]);
					data.setPackageTypeId(params[2]);
					data.setOriginalDataSource(params[3]);
					
					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);

				OWLIndividual loadingUnitInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				for (LoadingUnitGenerator td : dataset) {

					//adding loading unit individual
					loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getLoadingUnitId() + "_LoadingUnit"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);


					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("packageTypeId", onto), loadingUnitInd, td.getPackageTypeId());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), loadingUnitInd, td.getOriginalDataSource());
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
					params = line.split(",");

					data = new ShipmentItemGenerator();
								
					data.setShipmentId(params[1]);
					data.setLoadingUnitId(params[2]);
					data.setQuantity(OntologyOperations.convertToDecimal(manager, params[3]));
					data.setOriginalDataSource(params[4]);

					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass shipmentItemClass = OntologyOperations.getClass("ShipmentItem", onto);
				OWLClass shipmentClass = OntologyOperations.getClass("Shipment", onto);
				OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);

				OWLIndividual shipmentItemInd = null;
				OWLIndividual shipmentInd = null;
				OWLIndividual loadingUnitInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null;  

				AddAxiom addAxiomChange = null;

				for (ShipmentItemGenerator td : dataset) {

					//adding shipmentItem individual
					shipmentItemInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipmentId() + "-" + td.getLoadingUnitId() + "_ShipmentItem"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentItemClass, shipmentItemInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);
								
					//object properties
					shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getShipmentId() + "_Shipment");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("belongsToShipment", onto), shipmentItemInd, shipmentInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getLoadingUnitId() + "_LoadingUnit");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasLoadingUnit", onto), shipmentItemInd, loadingUnitInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					//data properties

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), shipmentItemInd, td.getOriginalDataSource());
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
					params = line.split(",");

					data = new TransportsGenerator();
					
					if (!params[1].equals("0") && !params[1].equalsIgnoreCase("TransportId")) {
					
					data.setTransportId(params[1]);
					data.setTransportType(params[2]);
					data.setOriginalDataSource(params[3]);
					
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

					transportInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getTransportId() + "_Transport"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(transportClass, transportInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("transportId", onto), transportInd, td.getTransportId());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("transportType", onto), transportInd, td.getTransportType());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), transportInd, td.getOriginalDataSource());
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
					params = line.split(",");

					data = new ConsignmentsGenerator();
								
					data.setConsignmentId(params[1]);
					data.setTransportId(params[2]);
					data.setConsignmentType(params[3]);			
					data.setReconstructionLane(params[4]);
					data.setReconstructionLocation(params[5]);
					data.setCarrierAdditionalPartyIdentification(params[6]);
					data.setConsignorAdditionalPartyIdentification(params[7]);
					data.setConsigneeAdditionalPartyIdentification(params[8]);
					data.setConsignmentWaveId(params[9]);
					data.setHubAdditionalPartyidentification(params[10]);
					data.setReconstructionType(params[11]);
					data.setTaskClosedOn(OntologyOperations.convertToDateTime(manager, params[12]));
					data.setFullPalletConsignment(params[13]);
					data.setQttBoxes(OntologyOperations.convertToInt(manager, params[14]));
					data.setQttPallets(OntologyOperations.convertToInt(manager, params[15]));
					data.setQttReconstructedPallets(OntologyOperations.convertToInt(manager, params[16]));
					data.setQttReconstructedParcels(OntologyOperations.convertToInt(manager, params[17]));
					data.setOriginalDataSource(params[18]);
					data.setTaskId(params[19]);
					data.setTaskDescription(params[20]);
					data.setTotalConsignmentVolume(OntologyOperations.convertToDecimal(manager, params[21]));
					data.setTotalConsignmentWeight(OntologyOperations.convertToDecimal(manager, params[22]));
					
					if (params.length == 26) {
				
					data.setYear(OntologyOperations.convertToDecimal(manager, params[23]));
					data.setSeason(OntologyOperations.convertToDecimal(manager, params[24]));
					data.setWeekDay(OntologyOperations.convertToDecimal(manager, params[25]));
					 
					
					}
					
					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass consignmentClass = OntologyOperations.getClass("Consignment", onto);
				OWLClass transportClass = OntologyOperations.getClass("Transport", onto);
				OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
				OWLClass partyClass = OntologyOperations.getClass("Party", onto);

				OWLIndividual consignmentInd = null;
				OWLIndividual transportInd = null;
				OWLIndividual waveInd = null;
				OWLIndividual carrierInd = null;
				OWLIndividual consignorInd = null;
				OWLIndividual consigneeInd = null;
				OWLIndividual hubInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				for (ConsignmentsGenerator td : dataset) {

					//adding consignment individual
					consignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsignmentId() + "_Consignment"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, consignmentInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);

					//adding transport individual and OP			
					if (!td.getTransportId().equals("NULL")) {
						transportInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getTransportId() + "_Transport"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(transportClass, transportInd);			
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);

						//OP transportId from consignmentInd to transportInd
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("includesTransport", onto), consignmentInd, transportInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);			
					}

					//adding wave individual
					if (!td.getConsignmentWaveId().equals("NULL")) {
						waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsignmentWaveId() + "_Wave"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);			
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);

						//OP waveId from consignmentInd to waveInd
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("processedByWave", onto), consignmentInd, waveInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);			
					}
					
					//adding carrier party individual
					if (!td.getCarrierAdditionalPartyIdentification().equals("0") || !td.getCarrierAdditionalPartyIdentification().equals("Hub internal movements")) {
						
						carrierInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getCarrierAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, carrierInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP 
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasCarrierParty", onto), consignmentInd, carrierInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

					}
					
					//adding consignor party individual
					if (!td.getConsignorAdditionalPartyIdentification().equals("0") || !td.getConsignorAdditionalPartyIdentification().equals("Hub internal movements")) {
						
						consignorInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsignorAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consignorInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP 
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsignorParty", onto), consignmentInd, consignorInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

					}
					
					//adding consignee party individual
					if (!td.getConsigneeAdditionalPartyIdentification().equals("0") || !td.getConsigneeAdditionalPartyIdentification().equals("Hub internal movements")) {
						
						consigneeInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsigneeAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consigneeInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP 
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsigneeParty", onto), consignmentInd, consigneeInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

					}
					
					//adding hub party individual
					if (!td.getHubAdditionalPartyidentification().equals("0") || !td.getHubAdditionalPartyidentification().equals("Hub internal movements")) {
						
						hubInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getHubAdditionalPartyidentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, hubInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP 
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubParty", onto), consignmentInd, hubInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

					}
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignmentId", onto), consignmentInd, td.getConsignmentId());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignmentType", onto), consignmentInd, td.getConsignmentType());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionLane", onto), consignmentInd, td.getReconstructionLane());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionLocation", onto), consignmentInd, td.getReconstructionLocation());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					
					if (!td.getTaskClosedOn().getLiteral().equals("0000-00-00T00:00:00")) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("taskClosedOn", onto), consignmentInd, td.getTaskClosedOn());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					}
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("fullPalletConsignment", onto), consignmentInd, td.getFullPalletConsignment());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxes", onto), consignmentInd, td.getQttBoxes());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPallets", onto), consignmentInd, td.getQttPallets());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttReconstructedPallets", onto), consignmentInd, td.getQttReconstructedPallets());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttReconstructedParcels", onto), consignmentInd, td.getQttReconstructedParcels());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), consignmentInd, td.getOriginalDataSource());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("taskId", onto), consignmentInd, td.getTaskId());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("taskDescription", onto), consignmentInd, td.getTaskDescription());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("totalConsignmentVolume", onto), consignmentInd, td.getTotalConsignmentVolume());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("totalConsignmentWeight", onto), consignmentInd, td.getTotalConsignmentWeight());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
				
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), consignmentInd, td.getYear());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), consignmentInd, td.getSeason());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), consignmentInd, td.getWeekDay());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					 
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
					params = line.split(",");

					data = new HubReconstructionLocationGenerator();
								
					data.setAdditionalPartyIdentification(params[1]);
					data.setLaneId(params[2]);

					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass hubReconstructionLocationClass = OntologyOperations.getClass("HubReconstructionLocation", onto);
				OWLClass partyClass = OntologyOperations.getClass("Party", onto);

				OWLIndividual hubReconstructionLocationInd = null;
				OWLIndividual partyInd = null;

				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				for (HubReconstructionLocationGenerator td : dataset) {

					//individuals and association to classes
					hubReconstructionLocationInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getAdditionalPartyIdentification() + "_HubReconstructionLocation"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(hubReconstructionLocationClass, hubReconstructionLocationInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);
					
					//object properties
					partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getAdditionalPartyIdentification() + "_Party");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasParty", onto), hubReconstructionLocationInd, partyInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					

					//data properties
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("additionalPartyIdentification", onto), hubReconstructionLocationInd, td.getAdditionalPartyIdentification());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionLane", onto), hubReconstructionLocationInd, td.getLaneId());
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
					params = line.split(",");

					data = new WaveGenerator();
								
					data.setWaveId(params[1]);
					data.setReleasedOn(OntologyOperations.convertToDateTime(manager, params[2]));
					data.setHubAdditionalPartyIdentification(params[3]);
					data.setClosedOn(OntologyOperations.convertToDateTime(manager, params[4]));
					data.setStatus(params[5]);
					data.setWaveStartProcessingOn(OntologyOperations.convertToDateTime(manager, params[6]));
					data.setWaveEndProcessingOn(OntologyOperations.convertToDateTime(manager, params[7]));
					data.setQttTrailers(OntologyOperations.convertToInt(manager, params[8]));
					data.setQttBoxes(OntologyOperations.convertToInt(manager, params[9]));
					data.setQttBoxesProcessed(OntologyOperations.convertToInt(manager, params[10]));			
					data.setQttPalletsBuilt(OntologyOperations.convertToInt(manager, params[11]));
					data.setQttTasks(OntologyOperations.convertToInt(manager, params[12]));
					data.setQttShipments(OntologyOperations.convertToInt(manager, params[13]));
					data.setOriginalDataSource(params[14]);
					
					if (params.length == 18) {
					
					data.setYear(OntologyOperations.convertToDecimal(manager, params[15]));
					data.setSeason(OntologyOperations.convertToDecimal(manager, params[16]));
					data.setWeekDay(OntologyOperations.convertToDecimal(manager, params[17]));

					}
					
					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
				OWLClass partyClass = OntologyOperations.getClass("Party", onto);

				OWLIndividual waveInd = null;
				OWLIndividual partyInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				//adding new waves
				for (WaveGenerator td : dataset) {

					waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getWaveId() + "_Wave"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);
					

					//adding hubParty	
					partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getHubAdditionalPartyIdentification() + "_Party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
						
					//OP 
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubParty", onto), waveInd, partyInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	



					//DPs
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveId", onto), waveInd, td.getWaveId());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					if (!td.getReleasedOn().getLiteral().equals("0000-00-00T00:00:00")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("releasedOn", onto), waveInd, td.getReleasedOn());
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);
						}
					
					if (!td.getClosedOn().getLiteral().equals("0000-00-00T00:00:00")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("closedOn", onto), waveInd, td.getClosedOn());
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);
						}
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("status", onto), waveInd, td.getStatus());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
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
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxes", onto), waveInd, td.getQttBoxes());
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
					
					if (td.getYear() != null) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), waveInd, td.getYear()); 
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					manager.applyChange(addAxiomChange);
					}
					  
					if (td.getSeason() != null) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), waveInd, td.getSeason()); 
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					manager.applyChange(addAxiomChange);
					}
					  
					if (td.getWeekDay() != null) {
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), waveInd, td.getWeekDay()); 
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					manager.applyChange(addAxiomChange);
					} 


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
					params = line.split(",");

					data = new XDocLoadingUnitGenerator();
								
					//TODO: add waveId
					data.setInternalId(params[1]);
					data.setPreSortScanOn(OntologyOperations.convertToDateTime(manager, params[2]));
					data.setReconstructedScanOn(OntologyOperations.convertToDateTime(manager, params[3]));
					data.setVolume(OntologyOperations.convertToDecimal(manager, params[4]));
					data.setWeight(OntologyOperations.convertToDecimal(manager, params[5]));
					data.setLoadingUnit(params[6]);
					data.setInboundConsignmentId(params[7]);
					data.setOutboundConsignmentId(params[8]);	
					data.setAdditionalPartyIdentification(params[9]);
					data.setHubReconstructionAdditionalPartyIdentification(params[10]);
					data.setShipperAdditionalPartyIdentification(params[11]);
					data.setReceiverAdditionalPartyIdentification(params[12]);
					data.setCarrierAdditionalPartyIdentification(params[13]);
					data.setConsignorAdditionalPartyIdentification(params[14]);
					data.setConsigneeAdditionalPartyIdentification(params[15]);			
					data.setReconstructionTypeId(params[16]);
					data.setSplitShipment(OntologyOperations.convertToInt(manager, params[17]));
					data.setInboundParentLoadingUnitId(params[18]);
					data.setOutboundParentLoadingUnitId(params[19]);
					data.setOriginalDataSource(params[20]);
					
					if (params.length == 25) {
						
					data.setYear(OntologyOperations.convertToDecimal(manager, params[23]));
					data.setSeason(OntologyOperations.convertToDecimal(manager, params[24]));
					data.setWeekDay(OntologyOperations.convertToDecimal(manager, params[25]));
					 
					
					}
					
					dataset.add(data);
					line = br.readLine();

				}

				br.close();
				
				OWLClass xDocLoadingUnitClass = OntologyOperations.getClass("XDocLoadingUnit", onto);
				OWLClass consignmentClass = OntologyOperations.getClass("Consignment", onto);
				OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);
				OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
				OWLClass partyClass = OntologyOperations.getClass("Party", onto);
				OWLClass hubReconstructionLocationClass = OntologyOperations.getClass("HubReconstructionLocation", onto);

				OWLIndividual xDocLoadingUnitInd = null;
				OWLIndividual inboundConsignmentInd = null;
				OWLIndividual outboundConsignmentInd = null;
				OWLIndividual loadingUnitInd = null;
				OWLIndividual inboundParentLoadingUnitInd = null;
				OWLIndividual outboundParentLoadingUnitInd = null;
				OWLIndividual waveInd = null;
				OWLIndividual hubReconstructionLocationInd = null;
				OWLIndividual partyInd = null;
				OWLIndividual hubReconstructionInd = null;
				OWLIndividual shipperInd = null;
				OWLIndividual receiverInd = null;
				OWLIndividual carrierInd = null;
				OWLIndividual consignorInd = null;
				OWLIndividual consigneeInd = null;
				
				OWLAxiom classAssertionAxiom = null; 
				OWLAxiom OPAssertionAxiom = null; 
				OWLAxiom DPAssertionAxiom = null; 

				AddAxiom addAxiomChange = null;

				for (XDocLoadingUnitGenerator td : dataset) {

					//adding individual
					xDocLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" +td.getInternalId() + "_XDocLoadingUnit"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(xDocLoadingUnitClass, xDocLoadingUnitInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);
					
					
					//object properties
					hubReconstructionLocationInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getHubReconstructionAdditionalPartyIdentification() + "_HubReconstructionLocation");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(hubReconstructionLocationClass, hubReconstructionLocationInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasReconstructionLocation", onto), xDocLoadingUnitInd, hubReconstructionLocationInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					
					loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getLoadingUnitForXDocLoadingUnit() + "_LoadingUnit");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasLoadingUnit", onto), xDocLoadingUnitInd, loadingUnitInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					inboundParentLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getInboundParentLoadingUnitId() + "_LoadingUnit");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, inboundParentLoadingUnitInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasInboundParentLoadingUnit", onto), xDocLoadingUnitInd, inboundParentLoadingUnitInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					outboundParentLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getOutboundParentLoadingUnitId() + "_LoadingUnit");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, outboundParentLoadingUnitInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasOutboundParentLoadingUnit", onto), xDocLoadingUnitInd, outboundParentLoadingUnitInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					inboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getInboundConsignmentId() + "_Consignment");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, inboundConsignmentInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasInboundConsignment", onto), xDocLoadingUnitInd, inboundConsignmentInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					outboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getOutboundConsignmentId() + "_Consignment");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, outboundConsignmentInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasOutboundConsignment", onto), xDocLoadingUnitInd, outboundConsignmentInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					
					if (!td.getAdditionalPartyIdentification().equals("0") || !td.getAdditionalPartyIdentification().equals("Hub internal movements")) {
						
						partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP 
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasParty", onto), xDocLoadingUnitInd, partyInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

					}
					
					//adding hub reconstruction party
					if (!td.getHubReconstructionAdditionalPartyIdentification().equals("0") || !td.getHubReconstructionAdditionalPartyIdentification().equals("Hub internal movements")) {
						
						hubReconstructionInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getHubReconstructionAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, hubReconstructionInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP 
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubReconstructionParty", onto), xDocLoadingUnitInd, hubReconstructionInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

					}
					
					//adding shipper party individual
					if (!td.getShipperAdditionalPartyIdentification().equals("0") || !td.getShipperAdditionalPartyIdentification().equals("Hub internal movements")) {
						
						shipperInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipperAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, shipperInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP 
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasShipperParty", onto), xDocLoadingUnitInd, shipperInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

					}
					
					//adding receiver party individual
					if (!td.getReceiverAdditionalPartyIdentification().equals("0") || !td.getReceiverAdditionalPartyIdentification().equals("Hub internal movements")) {
						
						receiverInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getReceiverAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, receiverInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP 
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasReceiverParty", onto), xDocLoadingUnitInd, receiverInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

					}
					
					//adding carrier party individual
					if (!td.getCarrierAdditionalPartyIdentification().equals("0") || !td.getCarrierAdditionalPartyIdentification().equals("Hub internal movements")) {
						
						carrierInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getCarrierAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, carrierInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP 
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasCarrierParty", onto), xDocLoadingUnitInd, carrierInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

					}
					
					//adding consignor party individual
					if (!td.getConsignorAdditionalPartyIdentification().equals("0") || !td.getConsignorAdditionalPartyIdentification().equals("Hub internal movements")) {
						
						consignorInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsignorAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consignorInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP 
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsignorParty", onto), xDocLoadingUnitInd, consignorInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

					}
					
					//adding consignee party individual
					if (!td.getConsigneeAdditionalPartyIdentification().equals("0") || !td.getConsigneeAdditionalPartyIdentification().equals("Hub internal movements")) {
						
						consigneeInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsigneeAdditionalPartyIdentification() + "_Party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consigneeInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);
						
						//OP 
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsigneeParty", onto), xDocLoadingUnitInd, consigneeInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

					}
					

					waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getWaveId() + "_Wave"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("processedByWave", onto), xDocLoadingUnitInd, waveInd);
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
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("volume", onto), xDocLoadingUnitInd, td.getVolume());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weight", onto), xDocLoadingUnitInd, td.getWeight());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionTypeId", onto), xDocLoadingUnitInd, td.getReconstructionTypeId());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("splitShipment", onto), xDocLoadingUnitInd, td.getSplitShipment());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), xDocLoadingUnitInd, td.getOriginalDataSource());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);
					
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), xDocLoadingUnitInd, td.getYear());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), xDocLoadingUnitInd, td.getSeason());
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), xDocLoadingUnitInd, td.getWeekDay());
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
	
	public static String formatCoordinates (String coordinates) {
		return "POINT(" + coordinates.substring(0, coordinates.indexOf(",")) + " " + coordinates.substring(coordinates.indexOf(",")+1, coordinates.length()) + ")";
	}

}
