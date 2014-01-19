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
	static public ArrayList<Ville> lectureFichier(String nomFichier)
	{
		int i, j, nbLine = 0;
		boolean NODE_COORD_SECTION = false, error = false;
		ArrayList<Ville> l = new ArrayList<Ville>();
		
		try
		{
			Scanner scan = new Scanner(new File(System.getProperty("user.dir") + "/" + nomFichier));
			// Scan du fichier complet
			while (scan.hasNextLine())
			{
				nbLine++;
				// Récupère la ligne
				String line = scan.nextLine();
				// Découpe la ligne selon les espaces
				String[] coord = line.split("[ ]+");
				
				if (NODE_COORD_SECTION && !(coord[0].equals("EOF")))
				{
					for(i = 0; coord[i].equals(""); i++);
					for(j = i+1; coord[j].equals(""); j++);
					try
					{
						// Ajoute la ville à la liste
						l.add(new Ville(Integer.parseInt(coord[i]), Double.parseDouble(coord[j]), Double.parseDouble(coord[coord.length-1])));
					} catch (NumberFormatException e)
					{
						System.out.println("ERREUR : Problème dans l'écriture des nombres. Mauvais formatage du ficher à la ligne " + nbLine + " : " + line);
					}
				} else if (coord[0].equals("EDGE_WEIGHT_TYPE"))
				{
					if (!(coord[2].equals("EUC_2D") || coord[2].equals("CEIL_2D")))
					{
						System.out.println("ERREUR : Les données sont formatées selon le format " + coord[2] + ". Nous ne gérons pas ce format de donnée. Veuillez essayer dans un autre format ou avec le fichier \".xml\".");
						error = true;
						break;
					}
				} else if (coord[0].equals("NODE_COORD_SECTION"))
					NODE_COORD_SECTION = true;
			}
			
		} catch (FileNotFoundException e)
		{
			System.out.println("ERREUR : Fichier \"" + System.getProperty("user.dir") + "/" + nomFichier + "\" non trouvé.");
			error = true;
		}
		
		if(!error)
			System.out.println("Lecture du fichier \"" + nomFichier + "\" terminée.");
		
		return l;
	}
}
