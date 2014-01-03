package tsp_ps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Lecture du fichier mona-lisa100k.tsp afin de créer toutes les villes
 */
public class GestionFichierTSP
{
	static public ArrayList<Ville> lectureFichier()
	{
		ArrayList<Ville> l = new ArrayList<Ville>();
		try
		{
			Scanner scan = new Scanner(new File("mona-lisa100K.tsp"));
			// Scan du fichier complet
			while (scan.hasNextLine())
			{
				// Récupère la ligne
				String line = scan.nextLine();
				// Découpe la ligne selon les espaces
				String[] coord = line.split("[ ]+");
				// Ajoute la ville à la liste
				l.add(new Ville(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]), Integer.parseInt(coord[2])));
			}
		} catch (FileNotFoundException e)
		{
			System.err.println("Fichier non trouvé");
		}
		
		System.out.println("Lecture du fichier \"mona-lisa100K.tsp\" terminée");
		return l;
	}
}