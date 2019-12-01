package solution;

import calcCentroidsFilter.CalcCentroidsFilter;
import org.jaitools.media.jai.kernel.KernelFactory;
import pmp.filter.DataTransformationFilter2;
import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.InvalidParameterException;

public class BallsFilter extends DataTransformationFilter2<PlanarImage, PlanarImage> implements PropertyChangeListener {
    BallsFilter(Readable<PlanarImage> input) throws InvalidParameterException {
        super(input);
    }

    BallsFilter(Writeable<PlanarImage> output) throws InvalidParameterException {
        super(output);
    }

    private int radius = 5;
    private KernelJAI kernel = KernelFactory.createCircle(radius);

    @Override
    protected PlanarImage process(PlanarImage image) {
        Window.show(image);
        BufferedImage src = image.getAsBufferedImage();

        PlanarImage ballsImage = PlanarImage.wrapRenderedImage(dilate(erode(src)));
        Window.show(ballsImage);
        return ballsImage;
    }

    private RenderedOp erode(Object src) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(src);
        pb.add(kernel);

        return JAI.create("erode", pb);
    }

    private RenderedOp dilate(Object src) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(src);
        pb.add(kernel);

        return JAI.create("dilate", pb);
    }

    /*UE3 Beans Area*/

    public BallsFilter(){}


    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        int tempradius = this.radius;
        this.radius = radius;
        listeners.firePropertyChange("radius", tempradius, radius);
    }

    public void doProcess(){}

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
