package pmp.filter;

import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

/*
 * a simple sink: takes passively objects or pulls actively objects without doing anything with the stream objects
 * derive your sink from this simple sink
 * 
 * hook method:   	write(T): void 		which writes a T object somewhere (file, network, ...); 
 *                                      a default version is supplied which does nothing (a dark sink, for testing purposes) 
 * contract: a null input signals end-of-stream
 */
public class Sink<T> implements Writeable<T>, Runnable{
	
    protected Readable<T> m_Input = null;
    
    public static Object ENDING_SIGNAL = null;
    
    
    public Sink(Readable<T> input) throws InvalidParameterException{
        if (input == null){
            throw new InvalidParameterException("input filter can't be null!");
        }
        m_Input = input;
    }
    
    public Sink()  {
    	
    }

/*
 * push next value into sink
 * @see interfaces.Writeable#write(java.lang.Object)
 */
	public void write(T value) throws StreamCorruptedException {
		//noop
	}
	
/*
 * epilogue is a cleanup method at the end of the stream in case of an active sink	
 */
	public void epilogue()  {
		//noop
	}

/*
 * for active sinks
 * @see java.lang.Runnable#run()
 */
    public void run() {
        T input = null;
        try {
            do {
                if (m_Input == null)
                    throw new StreamCorruptedException("input filter is null");
                
                input = m_Input.read();

                if (input != null) 
                    write(input);
                
            } while(input != null);
            epilogue();

        } catch (StreamCorruptedException e) {
            // TODO Automatisch erstellter Catch-Block
            e.printStackTrace();
        }
    }
	
    
}
