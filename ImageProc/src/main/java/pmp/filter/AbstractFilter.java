package pmp.filter;

import pmp.interfaces.IOable;
import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

/* This is the most abstract filter class in PmP. Any filter in the pipeline should derive somehow from this
 * class. It is responsible for:
 * - connection with the pipeline during construction
 * - run method for active filter
 * contract: a null entity signals end of stream
 */

public abstract class AbstractFilter<in, out> implements IOable<in, out>, Runnable {
    protected Readable<in> m_Input = null;
    protected Writeable<out> m_Output = null;

    public static Object ENDING_SIGNAL = null;   //actually, there is no other choice since null is the only object
                                                 //satisfying any type request for "in" and "out" template

    public AbstractFilter(){}

    public AbstractFilter(Readable<in> input) throws InvalidParameterException{
        if (input == null){
            throw new InvalidParameterException("input can't be null!");
        }
        m_Input = input;
    }

    public AbstractFilter(Writeable<out> output)throws InvalidParameterException{
        if (output == null){
            throw new InvalidParameterException("output can't be null!");
        }
        m_Output = output;
    }

    public AbstractFilter(Readable<in> input, Writeable<out> output)throws InvalidParameterException{
        if (input == null){
            throw new InvalidParameterException("input can't be null!");
        }else if (output == null){
            throw new InvalidParameterException("output can't be null!");
        }
        m_Input = input;
        m_Output = output;
    }

    protected void writeOutput(out value) throws StreamCorruptedException{
        if (m_Output != null){
           m_Output.write(value);
        }else{
            throw new StreamCorruptedException("output is null");
        }
    }

    protected in readInput() throws StreamCorruptedException{
        if (m_Input != null){
            return m_Input.read();
        }else{
            throw new StreamCorruptedException("input is null");
        }
    }

    /**
     * runs the filter in active-mode
     */
    public void run() {
        out output = null;

        try {
            do {

                output = read();

                writeOutput(output);

            }while(output != null);
        } catch (StreamCorruptedException e) {
            System.out.print("Thread reports error: ");
            System.out.println(Thread.currentThread().getId() + " (" + Thread.currentThread().getName() + ")");
            e.printStackTrace();
        }
    }


    /**
     * derived class may override this, if they are interested in
     * getting informed before the ending-signal is sent
     */
    protected void beforeSendingEndingSignal() throws StreamCorruptedException {}

    

}
