package solution;

import dataContainers.Coordinate;
import pmp.filter.Sink;
import pmp.interfaces.Readable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class DataSink extends Sink<ArrayList<Coordinate>> implements PropertyChangeListener {

    private String resultPath = "";
    private int begin;
    private int end;

    DataSink(Readable<ArrayList<Coordinate>> input, int begin, int end) throws InvalidParameterException {
        if (input == null) {
            throw new InvalidParameterException("input filter can't be null!");
        }
        m_Input = input;
        this.begin = begin;
        this.end = end;
    }

    DataSink(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public void write(ArrayList<Coordinate> data) {
        if(!resultPath.isEmpty()) {
            if (data != null) {
                try {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultPath)));
                    for (Coordinate coordinate : data) {
                        writer.append("Coordinates: ");
                        writer.append("X: ");
                        writer.append(String.valueOf(coordinate._x));
                        writer.append(" Y: ");
                        writer.append(String.valueOf(coordinate._y));
                        writer.append(" Diameter: ");
                        writer.append(String.valueOf(coordinate._diameter));
                        writer.append(" Result: ");
                        if (coordinate._diameter < begin)
                            writer.append("Failed; The diameter is too small.");
                        else if (coordinate._diameter > end)
                            writer.append("Failed; The diameter is too large.");
                        else
                            writer.append("Success");
                        writer.append("\n");
                    }
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Path for the result is invalid");
        }
    }

    /*UE3 Beans Area*/

    public DataSink(){}

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
        ImageSourceSingleton.getInstance().run();
    }

    public void doProcess(){}

    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ImageSourceSingleton.getInstance().run();
    }
}
