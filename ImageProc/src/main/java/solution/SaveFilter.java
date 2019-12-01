package solution;

import calcCentroidsFilter.CalcCentroidsFilter;
import pmp.filter.DataTransformationFilter1;
import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.InvalidParameterException;
import java.util.Objects;

public class SaveFilter extends DataTransformationFilter1<PlanarImage> implements PropertyChangeListener {
    private String outputPath = "";

    SaveFilter(Readable<PlanarImage> input) throws InvalidParameterException {
        super(input);
    }

    SaveFilter(Writeable<PlanarImage> output) throws InvalidParameterException {
        super(output);
    }

    @Override
    protected void process(PlanarImage entity) {
        if(!outputPath.isEmpty()) {
            try {
                JAI.create("filestore", entity.getAsBufferedImage(), outputPath, "JPEG");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            System.out.println("Path for the save is invalid");
        }
    }

    /*UE3 Beans Area*/

    public SaveFilter(){}

    public void doProcess(){}

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        String tempPath = this.outputPath;
        this.outputPath = outputPath;
        listeners.firePropertyChange("outputPath", tempPath, outputPath);
    }

    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
        if(listener instanceof CalcCentroidsFilter)
            this.setOutput((CalcCentroidsFilter)listener);
        if(listener instanceof RectangleFilter)
            this.setOutput((RectangleFilter)listener);
        if(listener instanceof MedianFilter)
            this.setOutput((MedianFilter)listener);
        if(listener instanceof BallsFilter)
            this.setOutput((BallsFilter)listener);
        if(listener instanceof ThresholdFilter)
            this.setOutput((ThresholdFilter)listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ImageSourceSingleton.getInstance().run();
    }
}
