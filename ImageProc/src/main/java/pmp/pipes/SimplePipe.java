package pmp.pipes;

import pmp.interfaces.IOable;
import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

public class SimplePipe<T> implements IOable<T, T> {
	
    private Readable<T> m_Input = null;
    private Writeable<T> m_Output = null;
	
	public SimplePipe(Readable<T> input)   {
        if (input == null){
            throw new InvalidParameterException("input filter can't be null!");
        }
        m_Input = input;
	}

	public SimplePipe(Writeable<T> output)   {
        if (output == null){
            throw new InvalidParameterException("output filter can't be null!");
        }
        m_Output = output;
	}

	public SimplePipe(Readable<T> input, Writeable<T> output)   {
        if (output == null){
            throw new InvalidParameterException("output filter can't be null!");
        }
        m_Output = output;

        if (input == null){
            throw new InvalidParameterException("input filter can't be null!");
        }
        m_Input = input;
	}

	public T read() throws StreamCorruptedException {
		if ( m_Input == null )
            throw new InvalidParameterException("input filter can't be null!");
		
		return m_Input.read();
	}
	
	public void write(T input) throws StreamCorruptedException {
		if ( m_Output == null )
            throw new InvalidParameterException("output filter can't be null!");
		
		m_Output.write(input);
	}
}
