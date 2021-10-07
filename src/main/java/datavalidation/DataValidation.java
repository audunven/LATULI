package datavalidation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DataValidation {


	public static void main(String[] args) throws IOException {

		Set<String> identifiedCommonLoadingUnits = new HashSet<String>();
		identifiedCommonLoadingUnits.add("040018955734209036");	
		identifiedCommonLoadingUnits.add("040018955734153896");
		identifiedCommonLoadingUnits.add("040018955733925739");
		identifiedCommonLoadingUnits.add("040018955734151236");
		identifiedCommonLoadingUnits.add("040018955734177847");
		identifiedCommonLoadingUnits.add("040018955734537566");
		identifiedCommonLoadingUnits.add("040018955733901788");
		identifiedCommonLoadingUnits.add("040018955733382273");
		identifiedCommonLoadingUnits.add("040018958003631286");
		identifiedCommonLoadingUnits.add("059025961315651974");
		identifiedCommonLoadingUnits.add("040018955734248608");		
		identifiedCommonLoadingUnits.add("040018955734092454");
		identifiedCommonLoadingUnits.add("040018955733854978");
		identifiedCommonLoadingUnits.add("040018955733480627");
		identifiedCommonLoadingUnits.add("040018955733852318");
		identifiedCommonLoadingUnits.add("040018955734190808");
		identifiedCommonLoadingUnits.add("040018955734224657");

		Set<Consignment> consignments = readConsignments("./files/CSV/Consignments_last_100000.csv");
		Set<Shipment> shipments = readShipments("./files/CSV/Shipments_last_100000.csv");
		Set<ShipmentItem> shipmentItems = readShipmentItems("./files/CSV/ShipmentItems_last_100000.csv");
		Set<XDocLoadingUnit> xDocLoadingUnits = readXDocLoadingUnits("./files/CSV/XDocLoadingUnits_last_100000.csv");
		Set<Wave> waves = readWaves("./files/CSV/Waves_last_100000.csv");
		Set<Transport> transports = readTransports("./files/CSV/Transports_last_100000.csv");

		//getShipmentDataFromLoadingUnitId("040018955734209036", shipmentItems, shipments, xDocLoadingUnits, consignments, waves, transports);

		getShipmentDataFromLoadingUnitId(identifiedCommonLoadingUnits, shipmentItems, shipments, xDocLoadingUnits, consignments, waves, transports);


	}

	public static void getShipmentDataFromLoadingUnitId (Set<String> loadingUnitIds, Set<ShipmentItem> shipmentItemSet, Set<Shipment> shipmentSet, 
			Set<XDocLoadingUnit> xDocLoadingUnitSet, Set<Consignment> consignmentSet, Set<Wave> waveSet, Set<Transport> transportSet) {

		String shipmentIdFromShipmentItem = null;
		String shippedOn = null;
		String expectedDeliveryOn = null;
		String internalId = null;
		String preSortScanOn = null;
		String reconstructedScanOn = null;
		String inboundConsignmentId = null;
		String outboundConsignmentId = null;
		String waveId = null;
		String waveStartProcessingOn = null;
		String waveEndProcessingOn = null;
		String taskClosedOn = null;
		String transportId = null;
		String transportName = null;
		String expectedArrivalOn = null;
		String loadingOn = null;
		String finishedScanOn = null;

		for (String loadingUnitId : loadingUnitIds) {


			shipmentIdFromShipmentItem = getShipmentIdFromLoadingUnitId(loadingUnitId, shipmentItemSet);
			shippedOn = getShippedOn(shipmentIdFromShipmentItem, shipmentSet);
			expectedDeliveryOn = getShipmentExpectedDeliveryOn(shipmentIdFromShipmentItem, shipmentSet);

			for (XDocLoadingUnit xd : xDocLoadingUnitSet) {
				if (xd.getLoadingUnitForXDocLoadingUnit().equals(loadingUnitId)) {
					internalId = xd.getInternalId();
					preSortScanOn = xd.getPreSortScanOn();
					reconstructedScanOn = xd.getReconstructedScanOn();
					inboundConsignmentId = xd.getInboundConsignmentId();
					outboundConsignmentId = xd.getOutboundConsignmentId();
					loadingOn = xd.getLoadingOn();
					finishedScanOn = xd.getFinishedScanOn();
				}
			}

			for (Consignment c : consignmentSet) {
				if (c.getConsignmentId().equals(outboundConsignmentId)) {
					waveId = c.getConsignmentWaveId();
					taskClosedOn = c.getConsignmentTaskClosedOn();
					transportId = c.getTransportId();
				}
			}

			for (Wave w : waveSet) {
				if (w.getWaveId().equals(waveId)) {
					waveStartProcessingOn = w.getWaveStartProcessingOn();
					waveEndProcessingOn = w.getWaveEndProcessingOn();
				}
			}

			for (Transport t : transportSet) {
				if (t.getTransportId().equals(transportId)) {
					expectedArrivalOn = t.getExpectedArrival();
					transportName = t.getTransportName();
				}
			}


			System.out.println("All data about loading unit: " + loadingUnitId);
			System.out.println("Associated shipment Id: " + shipmentIdFromShipmentItem);
			System.out.println("Shipment timestamp: " + shippedOn);
			System.out.println("Shipment expected delivery: " + expectedDeliveryOn);
			System.out.println("XDocLoadingUnit InternaId: " + internalId);
			System.out.println("PreSortScanOn: " + preSortScanOn);
			System.out.println("ReconstructedScanOn: " + reconstructedScanOn);
			System.out.println("Inbound Consignment: " + inboundConsignmentId);
			System.out.println("Outbound Consignment: " + outboundConsignmentId);
			System.out.println("Wave Id: " + waveId);
			System.out.println("Wave start processing on: " + waveStartProcessingOn);
			System.out.println("Wave end processing on: " + waveEndProcessingOn);
			System.out.println("Consignment Task closed on: " + taskClosedOn);
			System.out.println("Loading on: " + loadingOn);
			System.out.println("Finished scan on: " + finishedScanOn);
			System.out.println("Transport Id: " + transportId);
			System.out.println("Transport name: " + transportName);
			System.out.println("Transport expected arrival on: " + expectedArrivalOn);
			System.out.println("\n");

		}	

	}

	public static void getShipmentDataFromLoadingUnitId (String loadingUnitId, Set<ShipmentItem> shipmentItemSet, Set<Shipment> shipmentSet, 
			Set<XDocLoadingUnit> xDocLoadingUnitSet, Set<Consignment> consignmentSet, Set<Wave> waveSet, Set<Transport> transportSet) {

		String shipmentIdFromShipmentItem = getShipmentIdFromLoadingUnitId(loadingUnitId, shipmentItemSet);
		String shippedOn = getShippedOn(shipmentIdFromShipmentItem, shipmentSet);
		String internalId = null;
		String preSortScanOn = null;
		String reconstructedScanOn = null;
		String inboundConsignmentId = null;
		String outboundConsignmentId = null;
		String waveId = null;
		String waveStartProcessingOn = null;
		String waveEndProcessingOn = null;
		String taskClosedOn = null;
		String transportId = null;
		String transportName = null;
		String expectedArrivalOn = null;
		String loadingOn = null;

		for (XDocLoadingUnit xd : xDocLoadingUnitSet) {
			if (xd.getLoadingUnitForXDocLoadingUnit().equals(loadingUnitId)) {
				internalId = xd.getInternalId();
				preSortScanOn = xd.getPreSortScanOn();
				reconstructedScanOn = xd.getReconstructedScanOn();
				inboundConsignmentId = xd.getInboundConsignmentId();
				outboundConsignmentId = xd.getOutboundConsignmentId();
				loadingOn = xd.getLoadingOn();
			}
		}

		for (Consignment c : consignmentSet) {
			if (c.getConsignmentId().equals(outboundConsignmentId)) {
				waveId = c.getConsignmentWaveId();
				taskClosedOn = c.getConsignmentTaskClosedOn();
				transportId = c.getTransportId();
			}
		}

		for (Wave w : waveSet) {
			if (w.getWaveId().equals(waveId)) {
				waveStartProcessingOn = w.getWaveStartProcessingOn();
				waveEndProcessingOn = w.getWaveEndProcessingOn();
			}
		}

		for (Transport t : transportSet) {
			if (t.getTransportId().equals(transportId)) {
				expectedArrivalOn = t.getExpectedArrival();
				transportName = t.getTransportName();
			}
		}


		System.out.println("All data about loading unit: " + loadingUnitId);
		System.out.println("Associated shipment Id: " + shipmentIdFromShipmentItem);
		System.out.println("Shipment timestamp: " + shippedOn);
		System.out.println("XDocLoadingUnit InternaId: " + internalId);
		System.out.println("PreSortScanOn: " + preSortScanOn);
		System.out.println("ReconstructedScanOn: " + reconstructedScanOn);
		System.out.println("Inbound Consignment: " + inboundConsignmentId);
		System.out.println("Outbound Consignment: " + outboundConsignmentId);
		System.out.println("Wave Id: " + waveId);
		System.out.println("Wave start processing on: " + waveStartProcessingOn);
		System.out.println("Wave end processing on: " + waveEndProcessingOn);
		System.out.println("Consignment Task closed on: " + taskClosedOn);
		System.out.println("Loading on: " + loadingOn);
		System.out.println("Transport Id: " + transportId);
		System.out.println("Transport name: " + transportName);
		System.out.println("Transport expected arrival on: " + expectedArrivalOn);

	}


	public static Set<String> getLoadingUnitsFromShipmentId (String shipmentId, Set<ShipmentItem> shipmentItems) {
		Set<String> identifiedLoadingUnits = new HashSet<String>();

		for (ShipmentItem si : shipmentItems) {
			if (si.getShipmentItemId().equals(shipmentId)) {
				identifiedLoadingUnits.add(si.getLoadingUnitId());
			}
		}

		return identifiedLoadingUnits;
	}

	public static String getShippedOn (String shipmentId, Set<Shipment> shipmentSet) {
		String shippedOn = null;
		for (Shipment s : shipmentSet) {
			if (s.getShipmentId().equals(shipmentId)) {
				shippedOn = s.getShippedOn();
			}
		}

		return shippedOn;
	}
	

	public static String getShipmentExpectedDeliveryOn (String shipmentId, Set<Shipment> shipmentSet) {
		String expectedDeliveryOn = null;
		for (Shipment s : shipmentSet) {
			if (s.getShipmentId().equals(shipmentId)) {
				expectedDeliveryOn = s.getExpectedDeliveryOn();
			}
		}

		return expectedDeliveryOn;
	}

	public static String getShipmentIdFromLoadingUnitId (String loadingUnitId, Set<ShipmentItem> shipmentItemSet) {

		String loadingUnitFromShipmentItem = null;

		for (ShipmentItem si : shipmentItemSet) {
			if (si.getLoadingUnitId().equals(loadingUnitId)) {
				loadingUnitFromShipmentItem = si.getShipmentItemId();
			}
		}

		return loadingUnitFromShipmentItem;
	}

	public static Set<String> findCommonShipmentId (Set<Shipment> shipmentSet, Set<ShipmentItem> shipmentItemSet) {

		Set<String> commonShipmentIds = new HashSet<String>();

		for (Shipment s : shipmentSet) {
			for (ShipmentItem si : shipmentItemSet)
				if (s.getShipmentId().equals(si.getShipmentItemId())) {
					commonShipmentIds.add(s.getShipmentId());
				}
		}

		return commonShipmentIds;
	}

	public static Set<String> findMappedLoadingUnits (Set<String> shipmentIds, Set<ShipmentItem> shipmentItemSet) {

		Set<String> shipmentItemLoadingUnitIds = new HashSet<String>();

		//get the loading units for the common shipment ids
		for (String s : shipmentIds) {
			for (ShipmentItem shipmentItem : shipmentItemSet) {
				if (s.equals(shipmentItem.getShipmentItemId())) {
					shipmentItemLoadingUnitIds.add(shipmentItem.getLoadingUnitId());
				}
			}
		}

		return shipmentItemLoadingUnitIds;
	}

	public static Set<String> findCommonLoadingUnits (Set<String> mappedLoadingUnits, Set<XDocLoadingUnit> xDocLoadingUnits) {

		Set<String> commonLoadingUnitsShipmentItemsAndXDocLoadingUnits = new HashSet<String>();

		//get the loading unit ids that are common in shipment item and xdocloading unit
		for (String sl : mappedLoadingUnits) {
			for (XDocLoadingUnit xdl : xDocLoadingUnits) {
				if (sl.equals(xdl.getLoadingUnitForXDocLoadingUnit())) {
					commonLoadingUnitsShipmentItemsAndXDocLoadingUnits.add(sl);
				}
			}
		}

		return commonLoadingUnitsShipmentItemsAndXDocLoadingUnits;
	}


	public static Set<Consignment> readConsignments(String consignmentsFile) throws IOException {

		Set<Consignment> consignmentSet = new HashSet<Consignment>();

		Consignment consignmentData = null;

		BufferedReader consignmentReader = new BufferedReader(new FileReader(consignmentsFile));

		String consignmentLine = consignmentReader.readLine();
		String[] consignmentParams = null;

		while (consignmentLine != null) {
			consignmentParams = consignmentLine.split(";");

			consignmentData = new Consignment.ConsignmentBuilder()
					.setConsignmentId(consignmentParams[0])
					.setTransportId(consignmentParams[3])
					.setConsignmentModifiedOn(consignmentParams[5])
					.setConsignmentWaveId(consignmentParams[18])
					.setConsignmentTaskClosedOn(consignmentParams[23])
					.build();

			consignmentSet.add(consignmentData);
			consignmentLine = consignmentReader.readLine();

		}

		consignmentReader.close();

		return consignmentSet;	

	}

	public static Set<Shipment> readShipments(String shipmentsFile) throws IOException {

		Set<Shipment> shipmentSet = new HashSet<Shipment>();

		Shipment shipmentData = null;

		BufferedReader shipmentReader = new BufferedReader(new FileReader(shipmentsFile));
		String shipmentLine = shipmentReader.readLine();
		String[] shipmentParams = null;

		while (shipmentLine != null) {
			shipmentParams = shipmentLine.split(",");

			shipmentData = new Shipment.ShipmentBuilder()
					.setShipmentId(shipmentParams[0])
					.setShippedOn(shipmentParams[3])
					.setExpectedDeliveryOn(shipmentParams[4])
					.setShipperAdditionalPartyIdentification(shipmentParams[6])
					.setShipperGLN(shipmentParams[7])
					.setShipperHashCode(shipmentParams[8])
					.setReceiverAdditionalPartyIdentification(shipmentParams[9])
					.setReceiverGLN(shipmentParams[10])
					.setReceiverHashCode(shipmentParams[11])
					.setQttBoxesInShipment(shipmentParams[18])
					.setQttPalletsInShipment(shipmentParams[19])
					.build();

			shipmentSet.add(shipmentData);
			shipmentLine = shipmentReader.readLine();

		}

		shipmentReader.close();

		return shipmentSet;
	}


	public static Set<ShipmentItem> readShipmentItems(String shipmentItemsFile) throws IOException {

		ShipmentItem shipmentItemData = null;

		BufferedReader shipmentItemReader = new BufferedReader(new FileReader(shipmentItemsFile));


		String shipmentItemLine = shipmentItemReader.readLine();
		String[] shipmentItemParams = null;
		Set<ShipmentItem> shipmentItemSet = new HashSet<ShipmentItem>();

		while (shipmentItemLine != null) {
			shipmentItemParams = shipmentItemLine.split(",");

			shipmentItemData = new ShipmentItem.ShipmentItemBuilder()
					.setShipmentItemId(shipmentItemParams[0])
					.setLoadingUnitId(shipmentItemParams[1])
					.setShipmentItemModifiedOn(shipmentItemParams[2])
					.setQuantity(shipmentItemParams[3])
					.build();

			shipmentItemSet.add(shipmentItemData);
			shipmentItemLine = shipmentItemReader.readLine();

		}

		shipmentItemReader.close();

		return shipmentItemSet;


	}

	public static Set<LoadingUnit> readLoadingUnits(String loadingUnitsFile) throws IOException {


		LoadingUnit loadingUnitData = null;
		BufferedReader loadingUnitReader = new BufferedReader(new FileReader(loadingUnitsFile));

		String loadingUnitLine = loadingUnitReader.readLine();
		String[] loadingUnitParams = null;
		Set<LoadingUnit> loadingUnitSet = new HashSet<LoadingUnit>();

		while (loadingUnitLine != null) {
			loadingUnitParams = loadingUnitLine.split(",");

			loadingUnitData = new LoadingUnit.LoadingUnitBuilder()
					.setLoadingUnitId(loadingUnitParams[0])
					.setPackageTypeId(loadingUnitParams[1])
					.setOrderNumber(loadingUnitParams[2])
					.setLoadingUnitModifiedOn(loadingUnitParams[3])
					.build();

			loadingUnitSet.add(loadingUnitData);
			loadingUnitLine = loadingUnitReader.readLine();

		}

		loadingUnitReader.close();

		return loadingUnitSet;

	}

	public static Set<Wave> readWaves(String wavesFile) throws IOException {

		Wave waveData = null;

		BufferedReader waveReader = new BufferedReader(new FileReader(wavesFile));

		String waveLine = waveReader.readLine();
		String[] waveParams = null;
		Set<Wave> waveSet = new HashSet<Wave>();

		while (waveLine != null) {
			waveParams = waveLine.split(",");

			waveData = new Wave.WaveBuilder()
					.setWaveId(waveParams[0])
					.setWaveStartProcessingOn(waveParams[13])
					.setWaveEndProcessingOn(waveParams[14])
					.setQttTrailers(waveParams[15])
					.setQttBoxesInWave(waveParams[16])
					.setQttPalletsInWave(waveParams[17])
					.setQttBoxesProcessed(waveParams[18])
					.setQttPalletsBuilt(waveParams[19])
					.setQttTasks(waveParams[20])
					.setQttShipments(waveParams[21])
					.build();

			waveSet.add(waveData);
			waveLine = waveReader.readLine();

		}

		waveReader.close();

		return waveSet;
	}

	public static Set<Transport> readTransports(String transportsFile) throws IOException {

		Transport transportData = null;

		BufferedReader transportReader = new BufferedReader(new FileReader(transportsFile));

		String transportLine = transportReader.readLine();
		String[] transportParams = null;
		Set<Transport> transportSet = new HashSet<Transport>();

		while (transportLine != null) {
			transportParams = transportLine.split(",");

			transportData = new Transport.TransportBuilder()
					.setTransportId(transportParams[0])
					.setExpectedArrival(transportParams[2])
					.setTransportName(transportParams[9])
					.setTransportType(transportParams[11])
					.build();

			transportSet.add(transportData);
			transportLine = transportReader.readLine();

		}

		transportReader.close();

		return transportSet;
	}


	public static Set<XDocLoadingUnit> readXDocLoadingUnits(String xDocLoadingUnitsFile) throws IOException {

		XDocLoadingUnit xDocLoadingUnitData = null;		
		BufferedReader xDocLoadingUnitReader = new BufferedReader(new FileReader(xDocLoadingUnitsFile));

		String xDocLoadingUnitLine = xDocLoadingUnitReader.readLine();
		String[] xDocLoadingUnitParams = null;
		Set<XDocLoadingUnit> xDocLoadingUnitSet = new HashSet<XDocLoadingUnit>();

		while (xDocLoadingUnitLine != null) {
			xDocLoadingUnitParams = xDocLoadingUnitLine.split(",");

			xDocLoadingUnitData = new XDocLoadingUnit.XDocLoadingUnitBuilder()
					.setInternalId(xDocLoadingUnitParams[0])
					.setPreSortScanOn(xDocLoadingUnitParams[1])
					.setReconstructedScanOn(xDocLoadingUnitParams[2])
					.setFinishedScanOn(xDocLoadingUnitParams[3])
					.setLoadingOn(xDocLoadingUnitParams[4])
					.setxDocLoadingUnitVolume(xDocLoadingUnitParams[5])
					.setxDocLoadingUnitWeight(xDocLoadingUnitParams[6])
					.setLoadingUnitForXDocLoadingUnit(xDocLoadingUnitParams[7])
					.setInboundConsignmentId(xDocLoadingUnitParams[11])
					.setOutboundConsignmentId(xDocLoadingUnitParams[12])
					.setxDocLoadingUnitHubReconstructionLaneId(xDocLoadingUnitParams[16])
					.setxDocLoadingUnitHubReconstructionLocationHashCode(xDocLoadingUnitParams[20])
					.setxDocLoadingUnitWaveId(xDocLoadingUnitParams[36])
					.setReconstructionTypeId(xDocLoadingUnitParams[37])
					.setInboundParentLoadingUnitForXDocLoadingUnit(xDocLoadingUnitParams[41])
					.setOutboundParentLoadingUnitForXDocLoadingUnit(xDocLoadingUnitParams[42])
					.build();

			xDocLoadingUnitSet.add(xDocLoadingUnitData);
			xDocLoadingUnitLine = xDocLoadingUnitReader.readLine();

		}

		xDocLoadingUnitReader.close();

		return xDocLoadingUnitSet;
	}

}
