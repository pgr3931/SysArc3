package solution;

import calcCentroidsFilter.CalcCentroidsFilter;
import pmp.filter.Source;
import pmp.interfaces.Writeable;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.InvalidParameterException;


public class ImageSource extends Source<PlanarImage> implements PropertyChangeListener {

    private boolean flag;
    public String filePath = "";

    ImageSource(Writeable<PlanarImage> output) throws InvalidParameterException {
        if (output == null) {
            throw new InvalidParameterException("output filter can't be null!");
        }
        m_Output = output;
    }

    @Override
    public PlanarImage read() {
        if (!filePath.isEmpty()) {
            try {
                if (!flag) {
                    flag = true;
                    return JAI.create("fileload", filePath);
                } else return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("Path for the source is invalid");
            return null;
        }
    }


    /*UE3 Bean Area*/

    public ImageSource() {
        ImageSourceSingleton.getInstance().setImageSource(this);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        String tempPath = this.filePath;
        this.filePath = filePath;
        listeners.firePropertyChange("filePath", tempPath, filePath);
    }

    public void setFlagToFalse(){
        flag = false;
    }

    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
        if (listener instanceof CalcCentroidsFilter)
            this.setOutput((CalcCentroidsFilter) listener);
        if (listener instanceof BallsFilter)
            this.setOutput((BallsFilter) listener);
        if (listener instanceof MedianFilter)
            this.setOutput((MedianFilter) listener);
        if (listener instanceof SaveFilter)
            this.setOutput((SaveFilter) listener);
        if (listener instanceof ThresholdFilter)
            this.setOutput((ThresholdFilter) listener);
        if (listener instanceof RectangleFilter)
            this.setOutput((RectangleFilter) listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
