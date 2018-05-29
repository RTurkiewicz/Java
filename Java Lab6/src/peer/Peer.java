package peer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
import java.util.Map;
import java.util.Scanner;

import iremote.IPeer;
import iremote.IStorehouse;

public class Peer extends UnicastRemoteObject implements IPeer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean acceptFileChunk(String fileName, String partText, int part) throws RemoteException {
		
		fileChunks.put(fileName, part);
		
		File file = new File("C:\\Users\\Argon\\Desktop\\PwjJ-lab6\\Communication data\\" + id + "\\File chunks\\" + fileName);
		file.getParentFile().mkdirs();
		try {
			FileWriter fileWriter = new FileWriter(file, false);
			fileWriter.write(partText);
			fileWriter.close();
		} catch (IOException e) {
			System.out.print("Peer couldn't write accepted filechunk on disc.\n\n");
			return false;
		}
		return true;
	}

	@Override
	public String getFileChunk(String fileName) throws RemoteException {
		
		File file = new File("C:\\Users\\Argon\\Desktop\\PwjJ-lab6\\Communication data\\"
				+ id +"\\File chunks\\" + fileName);
		Scanner fileScanner;
		try {
			fileScanner = new Scanner(file);
			String partText = fileScanner.useDelimiter("\\A").next();
			fileScanner.close();
			return partText;
		} catch (FileNotFoundException e) {
			System.out.println("Node was asked to send a chunk of file it should have but doesn't. The chunk must have been manualy removed from the folder, check it.\n\n");
		}

		return chunkOfFileNotPresentTag;
	}
	
	public Peer(String id) throws RemoteException {
		
		super();
		this.id = id;
	}

	public static void main(String[] args) {
		
		System.out.print("Enter desired peer's name (id): ");
        Scanner consoleReader = new Scanner(System.in);
		String id = consoleReader.nextLine();
		System.out.println();
		
		Peer peer;
		Registry registry;
		try {
			peer = new Peer(id);
			registry = LocateRegistry.getRegistry(port);
		} catch (RemoteException e1) {
			System.out.print("A RemoteException occured during peer's creation or export.\n\n");
			consoleReader.close();
			return;
		}
		
        boolean continueExecution = true;
        do {
        	try {
            	peer.printMenu();            	
				int choice;
				try {
					choice = consoleReader.nextInt();
					consoleReader.nextLine();
				} catch (InputMismatchException e) {
					choice = 4;
				}
            	
            	switch (choice) {
            	case 1:
            		peer.manageRegistered(registry, consoleReader);
            		break;
            	case 2:
            		if (peer.isRegistered)
            			peer.printInfo();
            		break;
            	case 3:
            		if (peer.isRegistered)
            			peer.uploadFile(consoleReader);
            		break;
            	case 4:
            		if (peer.isRegistered)
            			peer.downloadFile(registry, consoleReader);
            		break;
            	case 0:
            		System.out.print("Thank you for your contribution. Farewell.\n\n");
            		continueExecution = false;
            		break;
            	default:
            		System.out.print("Not an option.\n\n");
            		break;         	
            	}
        	} catch (RemoteException e) {
        		System.out.print("A RemoteException occured when trying to connect.\n\n");
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

	private void manageRegistered(Registry registry, Scanner consoleReader) throws RemoteException {
		
		if (isRegistered) {
			storehouseStub.unregisterNode(id);
            try {
				Naming.unbind(ipURL + id);
			} catch (NotBoundException e) {
				e.printStackTrace();
				return;
			} catch (MalformedURLException e) {
				System.out.print("ipURL specified in application for peers is wrong.\n\n");
				return;
			}
			System.out.println("Peer: " + id + " unbound\n\n");
		}
		else {
			if (firstRun) {
				try {
					storehouseStub = (IStorehouse) registry.lookup("Storehouse");
				} catch (NotBoundException e) {
					System.out.print("Peer could not be registered because the storehouse is unbound.\n\n");
					return;
				}
				firstRun = false;
			}
			try {
				while (!storehouseStub.registerNode(id)) {
					System.out.print("Storehouse alread have a node registered for that id.\nEnter another id: ");
					id = consoleReader.nextLine();
				}
				Naming.rebind(ipURL + id, this);
				System.out.print("\n\n");
			} catch (MalformedURLException e) {
				System.out.print("ipURL specified in application for peers is wrong.\n\n");
				storehouseStub.unregisterNode(id);
				return;
			}	
            System.out.println("Peer: " + id + " bound\n\n");
		}
		isRegistered = !isRegistered;
	}
	
	private void uploadFile(Scanner consoleReader) {
		
		System.out.println(fileNameQuestion);
    	String fileName = consoleReader.nextLine();
    	
    	File file = new File(fileName);
    	try {
    		Scanner fileScanner = new Scanner(file);
    		String fileText = fileScanner.useDelimiter("\\A").next();
    		fileScanner.close();
    		
    		if (storehouseStub.uploadFile(fileName, fileText))
    			System.out.println("File has been uploaded to storehouse.\n\n");
    		else
    			System.out.println("File was not uploaded to storehouse because either the number of peers is to small,\n"
    					+ "a file of specified name has already been uploaded\nor one of the peers was unbound.\n\n");
		} catch (FileNotFoundException e) {
			System.out.print("Wrong name of file.\n\n");
		} catch (RemoteException e) {
			System.out.print("File could not be uploaded because the storehouse has been unbound.\n\n");
		}
	}
	
	private void downloadFile(Registry registry, Scanner consoleReader) throws RemoteException {
		
		System.out.println(fileNameQuestion);
    	String fileName = consoleReader.nextLine();

    	ArrayList<String> peersForFile = storehouseStub.getPeersForFile(fileName);
		if (peersForFile.isEmpty()) {
			System.out.println(noSuchFileAnswer);
			return;
		}
		
		IPeer firstPiecePeerStub;
		IPeer secondPiecePeerStub;
		IPeer thirdPiecePeerStub;
		try {
			firstPiecePeerStub = (IPeer) registry.lookup(peersForFile.get(0));
			secondPiecePeerStub = (IPeer) registry.lookup(peersForFile.get(1));
			thirdPiecePeerStub = (IPeer) registry.lookup(peersForFile.get(2));
		} catch (NotBoundException e1) {
			System.out.print("File could not be downloaded because one of peers containing filechunks has been unbounds.\n\n");
			return;
		} catch (RemoteException e) {
			System.out.print("File could not be downloaded because this peer couldn't connect to one of the peers containing filechunks.\n\n");
			return;
		}
		String fileText = firstPiecePeerStub.getFileChunk(fileName)
				+ secondPiecePeerStub.getFileChunk(fileName)
				+ thirdPiecePeerStub.getFileChunk(fileName);
		
		if (fileText.contains(chunkOfFileNotPresentTag)) {
			System.out.print("One of the peers does not have chunk of file it should have.\n"
					+ "The chunk must have been manually removed from the folder.\n\n");
			return;
		}
		
		File file = new File("C:\\Users\\Argon\\Desktop\\PwjJ-lab6\\Communication data\\"
				+ id +"\\Downloaded files\\" + fileName);
		file.getParentFile().mkdirs();
		try {
			FileWriter fileWriter = new FileWriter(file, false);
			fileWriter.write(fileText);
			fileWriter.close();
			System.out.print("File has been downloaded.\n\n");
		} catch (IOException e) {
			System.out.print("Writing downloaded file on disc ended with failure.\n\n");
		}		
	}
	
	private void printInfo() {
		
		System.out.println("Node's id: " + id);
		if (fileChunks.isEmpty())
			System.out.print("This node contains no filechunks.\n\n");
		else {
			System.out.println("Chunks of files this peers contains.\nfile's name, number of chunk's part");
			for (String fileName : fileChunks.keySet())
				System.out.println("\t" + fileName + " ," + fileChunks.get(fileName));
			System.out.print("\n\n");
		}
	}
		
	boolean firstRun = true;
	
	IStorehouse storehouseStub;
	
	private String id;
	
	private boolean isRegistered = false;
	
	private Map<String, Integer> fileChunks= new HashMap<String, Integer>();
	
	private static final String unregisteredMenu = "***Choose action***\n"
			+ "[1] Register node\n"
			+ "[0] End session\n"
			+ "*************\n\n";
	
	private static final String registeredMenu = "***Choose action***\n"
			+ "[1] Unregister node\n"
			+ "[2] Print node's info\n"
			+ "[3] Upload file\n"
			+ "[4] Download file\n"
			+ "[0] End session\n"
			+ "*************\n\n";
	
	private static final String fileNameQuestion = "Please enter file's name: ";
	
	private static final String noSuchFileAnswer = "Storehouse does not have such a file registered.\n\n";
	
	private static String chunkOfFileNotPresentTag = "<!ChunkOfFileWasNotPresentError!>";
	
	private static int port = 1099;
	private static String ipURL = "//localhost:" + port + "/";
}
