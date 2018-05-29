package storehouse;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import iremote.IPeer;
import iremote.IStorehouse;

public class Storehouse extends UnicastRemoteObject implements IStorehouse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Storehouse() throws RemoteException {
		super();
	}

	@Override
	public boolean registerNode(String id) throws RemoteException {
		
		if (nodes.contains(id))
			return false;
		
		nodes.add(id);
		return true;
	}
	
	@Override
	public void unregisterNode(String id) throws RemoteException{
		
		nodes.remove(id);
	}

	@Override
	public boolean uploadFile(String fileName, String fileText) throws RemoteException {

		if (nodes.size() < 3) {
			System.out.println("File of name: " + fileName + " has not been uploaded because there are only " + nodes.size() +  " peers, when"
					+ "there should be atleast 3 of them.\n\n");
			return false;
		}
		
		if (savedFiles.containsKey(fileName)) {
			System.out.println("File of name: " + fileName + " has not been uploaded because a file of specified name"
					+ "has already been uploaded.\n\n");
			return false;
		}
		
		int fileSize = fileText.length();
		int firstLimit = fileSize / 3;
		int secondLimit = firstLimit * 2;
		String firstPiece = fileText.substring(0, firstLimit);
		String secondPiece = fileText.substring(firstLimit, secondLimit);
		String thirdPiece = fileText.substring(secondLimit);	
		
		//
		List<CustomPair> appearances = new ArrayList<>();
		nodes.stream().forEach(x -> appearances.add(new CustomPair(x, 0)));
		for (List<String> peers : savedFiles.values()) {
			for (String peer : nodes)
				if (peers.contains(peer)) {
					for (CustomPair pair : appearances)
						if (pair.peer.equals(peer))
							pair.numberOfFiles++;
				}
		}
		
		String peer1Name = nodes.get(0);
		int minValue = Integer.MAX_VALUE;
		for (CustomPair pair : appearances) {
			if (pair.numberOfFiles < minValue) {
				minValue = pair.numberOfFiles;
				peer1Name = pair.peer;
			}
		}
		appearances.remove(new CustomPair(peer1Name, minValue));
		
		String peer2Name = nodes.get(1);
		minValue = Integer.MAX_VALUE;
		for (CustomPair pair : appearances) {
			if (pair.numberOfFiles < minValue) {
				minValue = pair.numberOfFiles;
				peer1Name = pair.peer;
			}
		}
		appearances.remove(new CustomPair(peer2Name, minValue));
		
		String peer3Name = nodes.get(2);
		minValue = Integer.MAX_VALUE;
		for (CustomPair pair : appearances) {
			if (pair.numberOfFiles < minValue) {
				minValue = pair.numberOfFiles;
				peer1Name = pair.peer;
			}
		}
		appearances.remove(new CustomPair(peer3Name, minValue));
		//
		
		Registry registry = LocateRegistry.getRegistry();
		try {
			IPeer peer1 = (IPeer) registry.lookup(peer1Name);
			IPeer peer2 = (IPeer) registry.lookup(peer2Name);
			IPeer peer3 = (IPeer) registry.lookup(peer3Name);		
			
			boolean peer1success = peer1.acceptFileChunk(fileName, firstPiece, 1);
			boolean peer2success = peer2.acceptFileChunk(fileName, secondPiece, 2);
			boolean peer3success = peer3.acceptFileChunk(fileName, thirdPiece, 3);
			
			if (!peer1success || !peer2success || !peer3success)
				return false;
			
			ArrayList<String> peersForFile = new ArrayList<>();
			peersForFile.add(peer1Name);
			peersForFile.add(peer2Name);
			peersForFile.add(peer3Name);
			savedFiles.put(fileName, peersForFile);
		} catch (NotBoundException e) {
			System.out.print("Pending file was not uploaded because one of the peers on the list has been unbound.\n\n");
			return false;
		}		
		return true;
	}

	@Override
	public ArrayList<String> getPeersForFile(String fileName) throws RemoteException {

		if (!savedFiles.containsKey(fileName))
			return new ArrayList<String>();
		
		return savedFiles.get(fileName);
	}
	
	public static void main(String[] args) {
		
		Storehouse storehouse;
		try {
			storehouse = new Storehouse();
			LocateRegistry.createRegistry(port);
		} catch (RemoteException e1) {
			System.out.print("A RemoteException occured during storehouse's creation or export.\n\n");
			return;
		}
		
		boolean continueExecution = true;
    	Scanner consoleReader = new Scanner(System.in);
		do {
			storehouse.printMenu();
			int choice;
			try {
				choice = consoleReader.nextInt();
				consoleReader.nextLine();
			} catch (InputMismatchException e) {
				choice = 4;
			}				
        	
        	switch (choice) {
        	case 1:
        		try {
					storehouse.manageRegistered();
				} catch (RemoteException e) {
	        		System.out.print("A RemoteException occured when trying to connect.\n\n");
				}
        		break;
        	case 2:
        		if (storehouse.isRegistered)
        			storehouse.printRegisteredNodesInfo();
        		break;
        	case 3:
        		if (storehouse.isRegistered) {
        			storehouse.printDistributedFilesInfo();
        		}
        		break;
        	case 0:
        		System.out.print("Thank you for your contribution. Farewell.\n\n");
        		continueExecution = false;
        		break;
        	default:
        		System.out.print("Not an option.\n\n");
        		break;         	
        	}
		} while (continueExecution);
    	consoleReader.close();
	}
	
	private void printMenu() {
		
    	if (isRegistered)
    		System.out.print(registeredMenu);
    	else
    		System.out.print(unregisteredMenu);
	}
	
	private void printDistributedFilesInfo() {
		
		if (savedFiles.isEmpty())
			System.out.print("There are no distributed files.\n\n");
		else {
			System.out.println("Distributed files along with nodes that contain their data in file part's order.");
			for (String fileName : savedFiles.keySet()) {
				System.out.println(fileName);
				for (String peerName : savedFiles.get(fileName))
					System.out.println("\t" + peerName);
			}
			System.out.print("\n\n");
		}
	}
	
	private void printRegisteredNodesInfo() {
		
		if (nodes.isEmpty())
			System.out.print("There are no registered nodes.\n\n");
		else {
			System.out.println("ID's of registered nodes:");
			for (String id : nodes)
				System.out.println("\t" + id);
			System.out.print("\n\n");
		}
	}
	
	private void manageRegistered() throws RemoteException {
		
		if (isRegistered) {
			try {
				Naming.unbind(ipURL + name);
			} catch (MalformedURLException e) {
				System.out.print("ipURL specified in application for storehouse is wrong.\n\n");
				return;
			} catch (NotBoundException e) {
				e.printStackTrace();
				return;
			}
            System.out.println(name + " unbound\n\n");
		}
		else {
			try {
				Naming.rebind(ipURL + name, this);
			} catch (MalformedURLException e) {
				System.out.print("ipURL specified in application for storehouse is wrong.\n\n");
				return;
			}
            System.out.println(name + " bound\n\n");
		}			
		
		isRegistered = !isRegistered;
	}
	
	private List<String> nodes = new ArrayList<String>();
	
	private Map<String, ArrayList<String>> savedFiles = new HashMap<String, ArrayList<String>>();
	
	private boolean isRegistered = false;
	
	private static final String unregisteredMenu = "***Choose action***\n"
			+ "[1] Register storehouse\n"
			+ "[0] End session\n"
			+ "*************\n\n";
	
	private static final String registeredMenu = "***Choose action***\n"
			+ "[1] Unregister storehouse\n"
			+ "[2] Print registered nodes info\n"
			+ "[3] Print distributed files info\n"
			+ "[0] End session\n"
			+ "*************\n\n";
	
	private static String name = "Storehouse";
	
	private static int port = 1099;
	private static String ipURL = "//localhost:" + port + "/";
}

class CustomPair {
	
	String peer;
	int numberOfFiles;
	
	CustomPair(String peer, int numberOfFiles) {
		
		this.peer = peer;
		this.numberOfFiles = numberOfFiles;
	}
}
