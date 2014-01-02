package tsp_ps;

import java.util.ArrayList;

public class Thread_ParcourArray extends Thread
{
	private Ville v;
	private ArrayList<Ville> list;
	int indice_depart;
	int indice_fin;
	int indice_resultat;
	double distance;
	
	public Thread_ParcourArray(Ville v, ArrayList<Ville> list, int indice_depart, int indice_fin)
	{
		this.v = v;
		this.list = list;
		this.indice_depart = indice_depart;
		this.indice_fin = indice_fin;
		distance = Double.MAX_VALUE;
	}
	
	public void run()
	{
		int i;
		double d_tmp;
		
		for(i = indice_depart; (i < indice_fin) && (i < list.size()); i++)
		{
			Ville v_visitee = list.get(i);
			d_tmp = v.distanceCarre(v_visitee);
			if(d_tmp < distance)
			{
				indice_resultat = i;
				distance = d_tmp;
			}
		}
	}
	
	public Double getDistance()
	{
		return distance;
	}
	
	public int getIndice_resultat()
	{
		return indice_resultat;
	}
}
