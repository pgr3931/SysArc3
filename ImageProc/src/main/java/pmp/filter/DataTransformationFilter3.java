package pmp.filter;

import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
* same as DataTransformationFilter2, but with the possibility to output several new objects of the same or another type
* as that of the incoming object. Example: a line to several words
* 
* hook method: process(T): ArrayList<S>     which produces several S out of one T. Note that each S will be pushed or pulled separately
* 
* contract: a null entity signals end of stream
*/

public abstract class DataTransformationFilter3<T,S> extends AbstractFilter<T,S> {
	
	protected ArrayList<S> resultBuffer = new ArrayList<S>();

    public DataTransformationFilter3(Readable<T> input, Writeable<S> output) throws InvalidParameterException {
        super(input, output);

    }

    public DataTransformationFilter3(Readable<T> input) throws InvalidParameterException {
        super(input);

    }

    public DataTransformationFilter3(Writeable<S> output) throws InvalidParameterException {
        super(output);

    }

    public S read() throws StreamCorruptedException {
		S result = null;

		if (resultBuffer.size() == 0 )  {
//buffer is empty: pull next input   
			T entity = null;
			
			while (resultBuffer.size() == 0)  {
    		  entity = readInput();
    		  if (entity != null) 
    			  resultBuffer = process(entity);
    		  else
    			  break;
			}
    		
			if (entity != null)  {
    			result = resultBuffer.get(0);
    			resultBuffer.remove(0);
    		}
		}
		else  {
			result = resultBuffer.get(0);
			resultBuffer.remove(0);			
		}

		if (result == ENDING_SIGNAL)
        	beforeSendingEndingSignal();
		
        return result;
    }
    

    public void write(T value) throws StreamCorruptedException {

        if (value != null)  { 
        	resultBuffer = process(value);
        	if (resultBuffer.size()>0)  {
            //now push the result buffer and thereafter empty it
        		for (S result:resultBuffer)        		
        			writeOutput(result);
        		resultBuffer = new ArrayList<S>();
        	}
        	else //value results in an empty result list: do nothing
        		return;
        }
        else  {
        	beforeSendingEndingSignal();
    		writeOutput(null);
        }     	
    }
    
    /**
     * does the transformation on entity
     * @param entity
     */
    protected abstract ArrayList<S> process(T entity);

    @Override
    public void run() {
        T input = null;
        S output = null;
        try {
            do {
            	input = readInput();
                write(input);                
            }while(input != null);
        } catch (StreamCorruptedException e) {
            // TODO Automatisch erstellter Catch-Block
            e.printStackTrace();
        }
    }
    
    

	
	
}
