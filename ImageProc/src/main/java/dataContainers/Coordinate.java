
package dataContainers;

public class Coordinate {

	public final int _x;
	public final int _y;
	public final int _diameter;
	
	public Coordinate(int x, int y, int diameter){
		_x = x;
		_y = y;
		_diameter = diameter;
	}

    public Coordinate(int x, int y){
        _x = x;
        _y = y;
        _diameter = 0;
    }
	
    @Override
    public boolean equals(Object o) {
        if (this == o){
        	return true;
        }
        if (!(o instanceof Coordinate)){
        	return false;
        }
        Coordinate c = (Coordinate) o;
        if (_x == c._x && _y == c._y && _diameter == c._diameter){
        	return true;
        }
        else{
        	return false;
        }
    }

    @Override
    public int hashCode() {
        return 31 * _x + _y + _diameter;
    }
    
    @Override
    public String toString(){
    	return "[" + _x + "," + _y + "] " + _diameter;
    }

}
