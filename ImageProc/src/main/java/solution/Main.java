package solution;

import calcCentroidsFilter.CalcCentroidsFilter;
import dataContainers.Coordinate;
import pmp.interfaces.Readable;
import pmp.interfaces.Writeable;
import pmp.pipes.SimplePipe;

import javax.media.jai.PlanarImage;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static void pull(boolean showRect, int toleranceBegin, int toleranceEnd) {
        ImageSource source = new ImageSource();
        SimplePipe<PlanarImage> pipe1 = new SimplePipe<>(source);

        RectangleFilter rectangleFilter = new RectangleFilter((Readable<PlanarImage>) pipe1, showRect);
        SimplePipe<PlanarImage> pipe2 = new SimplePipe<>((Readable<PlanarImage>) rectangleFilter);

        ThresholdFilter thresholdFilter = new ThresholdFilter((Readable<PlanarImage>) pipe2);
        SimplePipe<PlanarImage> pipe3 = new SimplePipe<>((Readable<PlanarImage>) thresholdFilter);

        MedianFilter medianFilter = new MedianFilter((Readable<PlanarImage>) pipe3);
        SimplePipe<PlanarImage> pipe4 = new SimplePipe<>((Readable<PlanarImage>) medianFilter);

        BallsFilter ballsFilter = new BallsFilter((Readable<PlanarImage>) pipe4);
        SimplePipe<PlanarImage> pipe5 = new SimplePipe<>((Readable<PlanarImage>) ballsFilter);

        SaveFilter saveFilter = new SaveFilter((Readable<PlanarImage>) pipe5);
        SimplePipe<PlanarImage> pipe6 = new SimplePipe<>((Readable<PlanarImage>) saveFilter);

        CalcCentroidsFilter calcCentroidsFilter = new CalcCentroidsFilter(pipe6);
        SimplePipe<ArrayList<Coordinate>> pipe7 = new SimplePipe<>((Readable<ArrayList<Coordinate>>) calcCentroidsFilter);

        DataSink sink = new DataSink(pipe7, toleranceBegin, toleranceEnd);
        sink.run();
    }

    private static void push(boolean showRect, int toleranceBegin, int toleranceEnd) {
        DataSink sink = new DataSink(toleranceBegin, toleranceEnd);
        SimplePipe<ArrayList<Coordinate>> pipe1 = new SimplePipe<>(sink);

        CalcCentroidsFilter calcCentroidsFilter = new CalcCentroidsFilter(pipe1);
        SimplePipe<PlanarImage> pipe2 = new SimplePipe<PlanarImage>(calcCentroidsFilter);

        SaveFilter saveFilter = new SaveFilter((Writeable<PlanarImage>) pipe2);
        SimplePipe<PlanarImage> pipe3 = new SimplePipe<>((Writeable<PlanarImage>) saveFilter);

        BallsFilter ballsFilter = new BallsFilter((Writeable<PlanarImage>) pipe3);
        SimplePipe<PlanarImage> pipe4 = new SimplePipe<>((Writeable<PlanarImage>) ballsFilter);

        MedianFilter medianFilter = new MedianFilter((Writeable<PlanarImage>) pipe4);
        SimplePipe<PlanarImage> pipe5 = new SimplePipe<>((Writeable<PlanarImage>) medianFilter);

        ThresholdFilter thresholdFilter = new ThresholdFilter((Writeable<PlanarImage>) pipe5);
        SimplePipe<PlanarImage> pipe6 = new SimplePipe<>((Writeable<PlanarImage>) thresholdFilter);

        RectangleFilter rectangleFilter = new RectangleFilter((Writeable<PlanarImage>) pipe6, showRect);
        SimplePipe<PlanarImage> pipe7 = new SimplePipe<>((Writeable<PlanarImage>) rectangleFilter);

        ImageSource source = new ImageSource(pipe7);
        source.run();
    }

    private static boolean showRect = false;
    private static int begin = 0;
    private static int end = 0;

    private static void processInput(Scanner scanner){
        System.out.println("Should a rectangle be drawn over the original image? Y N");
        String input = scanner.nextLine();
        if (input.equals("Y") || input.equals("y"))
            showRect = true;

        boolean valid = false;
        System.out.println("Enter the begin of the tolerance range.");
        while (!valid) {
            input = scanner.nextLine();
            try {
                begin = Integer.parseInt(input);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        valid = false;
        System.out.println("Enter the end of the tolerance range.");
        while (!valid) {
            input = scanner.nextLine();
            try {
                end = Integer.parseInt(input);
                if (begin >= end)
                    System.out.println("The begin of the tolerance range must me smaller than the end.");
                else valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter either 'push' or 'pull' to choose a method.");
        String input = scanner.nextLine();
        switch (input) {
            case "pull":
                processInput(scanner);
                pull(showRect, begin, end);
                break;
            case "push":
               processInput(scanner);
               push(showRect, begin, end);
                break;
            default:
                System.out.println("Please enter either 'push', 'pull' or 'exit'.");
        }

    }
}
