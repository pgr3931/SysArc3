package solution;

import calcCentroidsFilter.CalcCentroidsFilter;
import pmp.filter.DataTransformationFilter2;
import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import javax.media.jai.PlanarImage;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.InvalidParameterException;

public class ThresholdFilter extends DataTransformationFilter2<PlanarImage, PlanarImage> implements PropertyChangeListener {
    ThresholdFilter(Readable<PlanarImage> input) throws InvalidParameterException {
        super(input);
    }

    ThresholdFilter(Writeable<PlanarImage> output) throws InvalidParameterException {
        super(output);
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        float tempthreshold = this.threshold;
        this.threshold = threshold;
        listeners.firePropertyChange("threshold", tempthreshold, threshold);
    }

    private float threshold = 0.15f;

    @Override
    protected PlanarImage process(PlanarImage image) {
        Window.show(image);
        BufferedImage bufferedImage = image.getAsBufferedImage();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int color = bufferedImage.getRGB(x, y);

                int red = (color >>> 16) & 0xFF;
                int green = (color >>> 8) & 0xFF;
                int blue = (color) & 0xFF;

                float brightness = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;

                if (brightness <= threshold) {
                    bufferedImage.setRGB(x, y, 0xFFFFFFFF);
                }
            }
        }
        return PlanarImage.wrapRenderedImage(bufferedImage);

    }

    /*UE3 Beans Area*/

    public ThresholdFilter(){}

    public void doProcess(){
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
        if(listener instanceof SaveFilter)
            this.setOutput((SaveFilter)listener);
        if(listener instanceof BallsFilter)
            this.setOutput((BallsFilter)listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ImageSourceSingleton.getInstance().run();
    }
}
