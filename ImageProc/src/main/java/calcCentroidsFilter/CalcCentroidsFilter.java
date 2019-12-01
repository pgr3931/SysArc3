package calcCentroidsFilter;

import dataContainers.Coordinate;
import pmp.filter.AbstractFilter;
import pmp.filter.DataTransformationFilter2;
import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;
import solution.DataSink;
import solution.ImageSource;
import solution.ImageSourceSingleton;
import solution.RectangleFilter;

import javax.media.jai.PlanarImage;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/* this filter expects the bonding discs to be completely white: pixel value of 255 on a scale of 0..255
 * all other pixels in the image are expected to have a pixel value < 255
 * use this filter adapting eventually the package name
 */
public class CalcCentroidsFilter extends DataTransformationFilter2<PlanarImage, ArrayList<Coordinate>> implements PropertyChangeListener {
	private HashMap<Coordinate, Boolean> _general = new HashMap<Coordinate, Boolean>();
	private LinkedList<ArrayList<Coordinate>> _figures = new LinkedList<ArrayList<Coordinate>>();
	private javax.media.jai.PlanarImage _image;
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	public CalcCentroidsFilter(Readable<PlanarImage> input) throws InvalidParameterException {
		super(input);
	}

	public CalcCentroidsFilter(Writeable<ArrayList<Coordinate>> output) throws InvalidParameterException {
		super(output);
	}

	public ArrayList<Coordinate> process(PlanarImage entity) {
		_image = entity;
		BufferedImage bi = entity.getAsBufferedImage();

		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				int p = bi.getRaster().getSample(x, y, 0);
				if (p == 255 && _general.containsKey(new Coordinate(x, y)) == false) {
					getNextFigure(bi, x, y);        //if there is a not visited white pixel, save all pixels belonging to the same figure
				}
			}
		}

		return calculateCentroids();    //calculate the centroids of all figures
	}

	private void getNextFigure(BufferedImage img, int x, int y) {
		ArrayList<Coordinate> figure = new ArrayList<Coordinate>();
		_general.put(new Coordinate(x, y), true);
		figure.add(new Coordinate(x, y));

		addConnectedComponents(img, figure, x, y);

		_figures.add(figure);
	}

	private void addConnectedComponents(BufferedImage img, ArrayList<Coordinate> figure, int x, int y) {
		if (x - 1 >= 0 && _general.containsKey((new Coordinate(x - 1, y))) == false && img.getRaster().getSample(x - 1, y, 0) == 255) {
			_general.put(new Coordinate(x - 1, y), true);
			figure.add(new Coordinate(x - 1, y));
			addConnectedComponents(img, figure, x - 1, y);
		}
		if (x + 1 < img.getWidth() && _general.containsKey((new Coordinate(x + 1, y))) == false && img.getRaster().getSample(x + 1, y, 0) == 255) {
			_general.put(new Coordinate(x + 1, y), true);
			figure.add(new Coordinate(x + 1, y));
			addConnectedComponents(img, figure, x + 1, y);
		}
		if (y - 1 >= 0 && _general.containsKey((new Coordinate(x, y - 1))) == false && img.getRaster().getSample(x, y - 1, 0) == 255) {
			_general.put(new Coordinate(x, y - 1), true);
			figure.add(new Coordinate(x, y - 1));
			addConnectedComponents(img, figure, x, y - 1);
		}
		if (y + 1 < img.getHeight() && _general.containsKey((new Coordinate(x, y + 1))) == false && img.getRaster().getSample(x, y + 1, 0) == 255) {
			_general.put(new Coordinate(x, y + 1), true);
			figure.add(new Coordinate(x, y + 1));
			addConnectedComponents(img, figure, x, y + 1);
		}
	}

	protected ArrayList<Coordinate> calculateCentroids() {
		ArrayList<Coordinate> centroids = new ArrayList<Coordinate>();
		int i = 0;
		for (ArrayList<Coordinate> figure : _figures) {
			LinkedList<Integer> xValues = new LinkedList<Integer>();
			LinkedList<Integer> yValues = new LinkedList<Integer>();

			for (Coordinate c : figure) {
				xValues.add(c._x);
				yValues.add(c._y);
			}

			Collections.sort(xValues);
			Collections.sort(yValues);

			int xMedian = xValues.get(xValues.size() / 2);
			int yMedian = yValues.get(yValues.size() / 2);

			centroids.add(new Coordinate(xMedian + (Integer) _image.getTileGridXOffset(), yMedian + (Integer) _image.getTileGridYOffset()));

			i++;
		}
		return centroids;
	}

	/*UE3 Beans Area*/

	public CalcCentroidsFilter(){}

	public void doProcess(){}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
		this.setOutput((DataSink)listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ImageSourceSingleton.getInstance().run();
	}
}
