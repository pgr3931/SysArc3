package solution;

import calcCentroidsFilter.CalcCentroidsFilter;

public class Test {
    public static void main(String[] args) {
        ImageSource source = new ImageSource();
        RectangleFilter rectangleFilter = new RectangleFilter();
        ThresholdFilter thresholdFilter = new ThresholdFilter();
        CalcCentroidsFilter calcCentroidsFilter = new CalcCentroidsFilter();
        DataSink dataSink = new DataSink();

        source.addPropertyChangeListener(rectangleFilter);
        rectangleFilter.addPropertyChangeListener(thresholdFilter);
        thresholdFilter.addPropertyChangeListener(calcCentroidsFilter);
        //calcCentroidsFilter.addPropertyChangeListener(dataSink);
        //dataSink.setResultPath("C:\\Users\\walte\\IdeaProjects\\ImgProcessing\\src\\main\\resources\\result.txt");
        source.setFilePath("C:\\Users\\walte\\IdeaProjects\\ImgProcessing\\src\\main\\resources\\loetstellen.jpg");
    }
}
