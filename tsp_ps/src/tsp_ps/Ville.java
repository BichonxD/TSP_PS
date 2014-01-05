package tsp_ps;

/**
 * Representation d'une ville. Contient les coordonnées de la ville ainsi qu'un
 * nombre identifiant la ville
 */
public class Ville
{
	int _n;
	double _x, _y;
	
	Ville(int n, double x, double y)
	{
		_n = n;
		_x = x;
		_y = y;
	}
	
	/**
	 * Calcule la distance euclidienne entre les 2 villes Soit les points
	 * (x1,y1) et (x2,y2), la distance entre ces points : d = sqrt( (x1-x2)²
	 * + (y1-y2)² )
	 * 
	 * @param v
	 *                Ville avec laquelle on veut calculer la distance
	 * @return Distance avec la ville v
	 */
	public double distance(Ville v)
	{
		return Math.sqrt((this._x - v._x) * (this._x - v._x) + (this._y - v._y) * (this._y - v._y));
	}
	
	public double distanceCarre(Ville v)
	{
		return (this._x - v._x) * (this._x - v._x) + (this._y - v._y) * (this._y - v._y);
	}
	
	@Override
	public String toString()
	{
		return _n + ""; /* + " : " + _x + ", " + _y; */
		
	}
	
	public int getNumber()
	{
		return _n;
	}
	
	double getX()
	{
		return _x;
	}
	
	double getY()
	{
		return _y;
	}
}
