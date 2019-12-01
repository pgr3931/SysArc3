package pmp.filter;

import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

/* this filter filters incoming values by blocking them if a condition (abstract method "forward") on that entity fails
 * 
 * hook method:		forward(T): boolean     	which tests whether the incoming T object should pass this filter (true) or should be blocked (false)
 * 
 * contract: a null entity signals end of stream
 */

public abstract class ForwardingFilter<T> extends AbstractFilter<T,T> {

    public ForwardingFilter(Readable<T> input, Writeable<T> output) throws InvalidParameterException {
        super(input, output);

    }

    public ForwardingFilter(Readable<T> input) throws InvalidParameterException {
        super(input);

    }

    public ForwardingFilter(Writeable<T> output) throws InvalidParameterException {
        super(output);
    }

    public T read() throws StreamCorruptedException {
    	T input;
    	
        while (true) {
            input = readInput();
        	if (input == ENDING_SIGNAL)  {
        		beforeSendingEndingSignal();
        		return input;
        	}
        	else if ( forward(input) )  
        		return input;
        }       		
           	
    }

    public void write(T value) throws StreamCorruptedException {
    	if (value == ENDING_SIGNAL)  {
    		beforeSendingEndingSignal();
    		writeOutput(value);
    	}
    	else if ( forward(value) )
            writeOutput(value);
    }
    

    protected abstract boolean forward(T entity);

}
