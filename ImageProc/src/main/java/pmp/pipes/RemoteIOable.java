package pmp.pipes;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteIOable<T> extends Remote {
    public T read() throws RemoteException;
    public void write(T value) throws RemoteException;
}
