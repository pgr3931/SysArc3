package pmp.filter;

import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import javax.media.jai.PlanarImage;
import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

/*
 * an abstract source: 
 * derive your source from this class and implement the read method which returns the next value of the stream 
 * 
 * hook method: 	read(): T 	which reads a new T object (from file, from network, ...) 
 * 
 * contract: a null entity signals end of stream to downstream pipeline
 */
public abstract class Source<T> implements Readable<T>, Runnable{
	
    protected Writeable<T> m_Output = null;
    
    public static Object ENDING_SIGNAL = null;

    public void setOutput(Writeable<T> output){
        if (output == null) {
            throw new InvalidParameterException("output filter can't be null!");
        }
        m_Output = output;
    }
    
    public Source(Writeable<T> output) throws InvalidParameterException{
        if (output == null){
            throw new InvalidParameterException("output filter can't be null!");
        }
        m_Output = output;
    }
    
    public Source() {
    	
    }

/*
 * pull next value from source. 
 * contract: returning a null signals the end of the stream to the caller
 */
	public abstract T read() throws StreamCorruptedException;
	
/*
 * epilogue is a cleanup method at the end of the stream
 */
	public void epilogue()  {
		//noop
	}

/*
 * for active sources
 * @see java.lang.Runnable#run()
 */
    public void run() {
        T output = null;
        try {
            do {
                if (m_Output == null)
                    throw new StreamCorruptedException("output filter is null");
                
                output = read();
                m_Output.write(output);
                
            } while(output != null);
            epilogue();

        } catch (StreamCorruptedException e) {
            // TODO Automatisch erstellter Catch-Block
            e.printStackTrace();
        }
    }
	
    
}
