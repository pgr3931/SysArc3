package solution;

import calcCentroidsFilter.CalcCentroidsFilter;
import pmp.filter.DataTransformationFilter2;
import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.MedianFilterDescriptor;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.InvalidParameterException;

public class MedianFilter extends DataTransformationFilter2<PlanarImage, PlanarImage> implements PropertyChangeListener {
    MedianFilter(Readable<PlanarImage> input) throws InvalidParameterException {
        super(input);
    }

    MedianFilter(Writeable<PlanarImage> output) throws InvalidParameterException {
        super(output);
    }

    public int getMaskSize() {
        return maskSize;
    }

    public void setMaskSize(int maskSize) {
        int tempmaskSize = this.maskSize;
        this.maskSize = maskSize;
        listeners.firePropertyChange("maskSize", tempmaskSize, maskSize);
    }

    private int maskSize = 5;

    @Override
    protected PlanarImage process(PlanarImage image) {
        Window.show(image);
        BufferedImage src = image.getAsBufferedImage();

        ParameterBlock pb = new ParameterBlock();
        pb.addSource(src);
        pb.add(MedianFilterDescriptor.MEDIAN_MASK_SQUARE);
        pb.add(maskSize);
        RenderedOp result = JAI.create("MedianFilter", pb);

        return PlanarImage.wrapRenderedImage(result);
    }

    /*UE3 Beans Area*/

    public MedianFilter(){}

    public void doProcess(){}

    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
        if(listener instanceof CalcCentroidsFilter)
            this.setOutput((CalcCentroidsFilter)listener);
        if(listener instanceof RectangleFilter)
            this.setOutput((RectangleFilter)listener);
        if(listener instanceof BallsFilter)
            this.setOutput((BallsFilter)listener);
        if(listener instanceof SaveFilter)
            this.setOutput((SaveFilter)listener);
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
