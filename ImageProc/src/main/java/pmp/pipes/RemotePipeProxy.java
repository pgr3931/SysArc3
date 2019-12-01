package pmp.pipes;

import pmp.interfaces.IOable;

import java.io.StreamCorruptedException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class RemotePipeProxy<T> implements IOable<T, T>{

    RemoteIOable<T> m_RemotePipe = null;
    public RemotePipeProxy(String remoteAdr, String remotePipeName) throws StreamCorruptedException {
        if (!remoteAdr.endsWith("/")) remoteAdr += "/";
        try {
            m_RemotePipe = (RemoteIOable)Naming.lookup("rmi://" + remoteAdr  + remotePipeName);
        } catch (Exception e) {
            // TODO Automatisch erstellter Catch-Block
            e.printStackTrace();           
        }
    }
    
    public T read() throws StreamCorruptedException {
        try {
            return m_RemotePipe.read();
        } catch (RemoteException e) {
            throw new StreamCorruptedException(e.getMessage());
        }
    }

    public void write(T value) throws StreamCorruptedException {
        try {
            m_RemotePipe.write(value);
        } catch (RemoteException e) {
            throw new StreamCorruptedException(e.getMessage());
        }
    }
    

}
