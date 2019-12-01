package solution;

public class ImageSourceSingleton {

    private ImageSource source = null;

    private static ImageSourceSingleton singleton;

    private ImageSourceSingleton(){
    }

    public static ImageSourceSingleton getInstance(){
        if(singleton == null)
            singleton = new ImageSourceSingleton();
        return singleton;
    }

    public void setImageSource(ImageSource source){
        this.source = source;
    }

    public void run(){
        if(source != null){
            source.setFlagToFalse();
            source.run();
        }
    }
}
