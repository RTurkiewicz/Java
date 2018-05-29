package iremote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IStorehouse extends Remote {
	
	public boolean registerNode(String id) throws RemoteException;
	public void unregisterNode(String id) throws RemoteException;
	public boolean uploadFile(String fileName, String fileText) throws RemoteException;
	public ArrayList<String> getPeersForFile(String fileName) throws RemoteException;	
}
