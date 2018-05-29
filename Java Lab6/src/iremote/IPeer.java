package iremote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPeer extends Remote {
	
	public boolean acceptFileChunk(String fileName, String partText, int part) throws RemoteException;
	public String getFileChunk(String fileName) throws RemoteException;
}
