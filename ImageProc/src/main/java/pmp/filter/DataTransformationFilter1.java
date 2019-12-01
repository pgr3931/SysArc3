package pmp.filter;

import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

/* this filter changes "in place" the input (without changing neither the type nor the reference) and 
 * sends the modified input as an output
 * 
 * hook method:  process(T): void     which transforms a T in place
 * 
 * contract: a null entity signals end of stream
 */

public abstract class DataTransformationFilter1<T> extends AbstractFilter<T,T> {

    public DataTransformationFilter1(Readable<T> input, Writeable<T> output) throws InvalidParameterException {
        super(input, output);

    }

    public DataTransformationFilter1(Readable<T> input) throws InvalidParameterException {
        super(input);

    }

    public DataTransformationFilter1(Writeable<T> output) throws InvalidParameterException {
        super(output);

    }

    public DataTransformationFilter1(){}

    public void setOutput(Writeable<T> output){
        if (output == null) {
            throw new InvalidParameterException("output filter can't be null!");
        }
        m_Output = output;
    }

    public T read() throws StreamCorruptedException {
        T entity = readInput();
        if (entity != null) process(entity);
        if (entity == ENDING_SIGNAL)
        	beforeSendingEndingSignal();
        return entity;
    }

    public void write(T value) throws StreamCorruptedException {
        if (value != null) process(value);
        if (value == ENDING_SIGNAL)
        	beforeSendingEndingSignal();
        writeOutput(value);
    }
    
    /**
     * does the transformation on entity (mutable entity). Alternatively, for transforming an
     * immutable entity, use DataTransformationFilter2.
     * @param entity
     */
    protected abstract void process(T entity);

    

}
