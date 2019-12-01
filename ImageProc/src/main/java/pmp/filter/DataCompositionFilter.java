package pmp.filter;

import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

/* this filter collects one or more inputs to create one output
 * example: words to a line
 *
 * hook methods:    fillEntity(in, out): boolean    which fills an "in" into the "out" object which is guaranteed to not being null;
 *                                                  returns true if the "out" object is complete and ready for transfer
 *                                                  if it returns false and the mode is "push", then the "out" object gets
 *                                                  automatically buffered by this implementation
 *               	getNewEntityObject(): out		which creates a new, empty "out" object
 *
 * contract: a null entity signals end of stream
 */

public abstract class DataCompositionFilter<in, out> extends AbstractFilter<in, out> implements Runnable {

    private out m_TempWriteEntity = null;
    protected boolean m_EndOfStream = false;


    public DataCompositionFilter(Readable<in> input, Writeable<out> output) throws InvalidParameterException {
        super(input, output);
    }

    public DataCompositionFilter(Readable<in> input) throws InvalidParameterException {
        super(input);
    }

    public DataCompositionFilter(Writeable<out> output) throws InvalidParameterException {
        super(output);
    }

    /**
     * read an entity from the filter. the filter will act like an passive-filter
     */
    public out read() throws StreamCorruptedException {
        // just read the next entity and return it 
        out value;

        if (m_EndOfStream)
            value = null;
        else
            value = getNextEntity();

        if (value == ENDING_SIGNAL)
            beforeSendingEndingSignal();

        return value;
    }

    /**
     * write an entity into the filter. the filter will act like an passive-filter
     * and passes the entity to the next filter, after it processed it
     *
     * @param value
     * @throws StreamCorruptedException
     */
    public void write(in value) throws StreamCorruptedException {
        if (value != ENDING_SIGNAL) {
            if (m_TempWriteEntity == null) m_TempWriteEntity = getNewEntityObject();
            if (fillEntity(value, m_TempWriteEntity)) {
                writeOutput(m_TempWriteEntity);
                m_TempWriteEntity = null;
            }
        } else {
            if (m_TempWriteEntity != null)
                fillEntity(value, m_TempWriteEntity);    //signal with value==null that entity might have to be flushed
            if (m_TempWriteEntity != null)
                writeOutput(m_TempWriteEntity);

            beforeSendingEndingSignal();
            writeOutput(null);
            m_EndOfStream = true;
        }

    }

    /**
     * reads the whole next entity from input
     *
     * @return the next entity
     * @throws StreamCorruptedException
     */
    protected out getNextEntity() throws StreamCorruptedException {
        if (!m_EndOfStream) {
            out entity = getNewEntityObject();
            boolean finished = false;
            in input;
            do {
                input = readInput();
                if (input != ENDING_SIGNAL) {
                    finished = fillEntity(input, entity);
                } else {
                    fillEntity(input, entity);    //signal with input==null that entity might have to be flushed
                    m_EndOfStream = true;
                    finished = true;
                }
            } while (!finished);
            return entity;
        } else {
            return null;
        }
    }


    /**
     * fill the entity with the next given value
     *
     * @param nextVal
     * @param entity
     * @return true if the entity is finished
     */
    protected abstract boolean fillEntity(in nextVal, out entity);

    /**
     * Abstract Filter class asks implementation for new Entity-Object
     * (because it doesn't know the constructor-arguments)
     *
     * @return
     */
    protected abstract out getNewEntityObject();
}
