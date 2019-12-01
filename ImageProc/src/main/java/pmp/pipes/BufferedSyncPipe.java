package pmp.pipes;

import pmp.interfaces.IOable;

import java.io.StreamCorruptedException;
import java.util.LinkedList;

public class BufferedSyncPipe<T> implements IOable<T, T> {

	private static final int minBufferSize = 1;
	private int m_MaxBufSize = 0;
	
	private LinkedList<T> m_Buf = new LinkedList<T>();
	
	public BufferedSyncPipe(int maxBufferSize) {
		super();
		m_MaxBufSize = Math.max(maxBufferSize, minBufferSize);
	}
	
	public synchronized boolean isFull(){
		return m_Buf.size() == m_MaxBufSize;
	}
	
	public synchronized boolean isEmpty(){
		return m_Buf.size() == 0;
	}

	public synchronized T read() throws StreamCorruptedException {
		while(isEmpty()){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		T obj = m_Buf.removeFirst();
		notifyAll(); 	// waiting in write
		return obj;
	}

	public synchronized void write(T value) throws StreamCorruptedException {
		while(isFull()){
			try{
				wait();
			} catch (InterruptedException e){}
		}
		 m_Buf.addLast(value);
		notifyAll();	// waiting in read
	}
}
