package solution;

import calcCentroidsFilter.CalcCentroidsFilter;
import pmp.filter.AbstractFilter;
import pmp.filter.DataTransformationFilter2;
import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;

import javax.media.jai.PlanarImage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.InvalidParameterException;

public class RectangleFilter extends DataTransformationFilter2<PlanarImage, PlanarImage> implements PropertyChangeListener {
    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    private boolean showRectangle;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        int tempx = this.x;
        this.x = x;
        listeners.firePropertyChange("x", tempx, x);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        int tempy = this.y;
        this.y = y;
        listeners.firePropertyChange("y", tempy, y);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        int tempwidth = this.width;
        this.width = width;
        listeners.firePropertyChange("width", tempwidth, width);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        int tempheight = this.height;
        this.height = height;
        listeners.firePropertyChange("height", tempheight, height);
    }

    private int x = 0;
    private int y = 40;
    private int width = -1;
    private int height = -1;

    RectangleFilter(Readable<PlanarImage> input, boolean showRectangle) throws InvalidParameterException {
        super(input);
        this.showRectangle = showRectangle;
    }

    RectangleFilter(Writeable<PlanarImage> output, boolean showRectangle) throws InvalidParameterException {
        super(output);
        this.showRectangle = showRectangle;
    }


    @Override
    public PlanarImage process(PlanarImage image) {
        Window.show(image);
        x = Math.max(x, 0);
        y = Math.max(y, 0);
        if(width < 0)
            width = image.getWidth();
        if(height < 0)
            height = image.getHeight() - 200;
        Rectangle rectangle = new Rectangle(x, y, width, height);

        if(showRectangle) {
            BufferedImage img = image.getAsBufferedImage();
            Graphics2D g2d = img.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            g2d.dispose();
            Window.show(PlanarImage.wrapRenderedImage(img));
        }

        image = PlanarImage.wrapRenderedImage(image.getAsBufferedImage(rectangle, image.getColorModel()));
        return image;
    }

    /*UE3 Bean Area*/

    public RectangleFilter(){

    }

    public boolean isShowRectangle() {
        return showRectangle;
    }

    public void setShowRectangle(boolean showRectangle) {
        boolean tempRect = this.showRectangle;
        this.showRectangle = showRectangle;
        listeners.firePropertyChange("filePath", tempRect, showRectangle);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
        if(listener instanceof CalcCentroidsFilter)
            this.setOutput((CalcCentroidsFilter)listener);
        if(listener instanceof BallsFilter)
            this.setOutput((BallsFilter)listener);
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

    public void doProcess(){
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ImageSourceSingleton.getInstance().run();
    }
}
