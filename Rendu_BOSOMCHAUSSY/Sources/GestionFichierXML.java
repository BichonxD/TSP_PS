package tsp_ps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Lecture du fichier afin de créer toutes les villes
 */
public class GestionFichierXML
{
	static public double[][] lectureFichierV2(String nomFichier)
	{
		int i = -1, j = 0;
		int tailleMatrice = 0;
		double cout;
		double l[][] = null;
		boolean error = false;
		
		try
		{
			Scanner scan = new Scanner(new File(System.getProperty("user.dir") + "/" + nomFichier));
			
			// Scan du fichier complet
			while (!error && scan.hasNextLine())
			{
				// Récupère la ligne
				String line = scan.nextLine();
				// Découpe la ligne selon les '>'
				String[] tmp = line.split("<");
				
				// Si c'est le nom du fichier nous récupérons la taille de la matrice et nous la créons
				if (tmp[tmp.length - 1].equals("/name>"))
				{
					String[] name = tmp[1].split(">");
					name[1] = name[1].replaceAll("\\D+", "");
					tailleMatrice = Integer.parseInt(name[1]);
					l = new double[tailleMatrice][tailleMatrice];
				}
				// La balise vertex avertit du passage a un nouveau point.
				else if (tmp[tmp.length - 1].equals("vertex>"))
				{
					i++;
					j = 0;
				}
				// La balise edge avertit du passage a une nouvelle arrête.
				else if (tmp[tmp.length - 1].equals("/edge>"))
				{
					String[] tmp2 = tmp[1].split(">");
					// tmp2[0] contient le cout de l'arrete et du texte.
					String[] strCout = tmp2[0].split("\"");
					
					// tmp2[1] contient le nom du point à l'autre bout de l'arrete.
					// strCout[1] contient le cout de l'arrete au format exponentiel
					cout = Double.parseDouble(strCout[1]);
					
					j = Integer.parseInt(tmp2[1]);
					if (i == j)
						continue;
					else if (j >= tailleMatrice)
					{
						System.out.println("ERREUR : Mauvais formatage du fichier.");
						l = null;
						tailleMatrice = 0;
						error = true;
					} else
						l[i][j] = cout;
				}
			}
		} catch (FileNotFoundException e)
		{
			System.out.println("ERREUR : Fichier \"" + System.getProperty("user.dir") + "/" + nomFichier + "\" non trouvé.");
			l = null;
			tailleMatrice = 0;
			error = true;
		}
		
		// La diagonale de la matrice est mise à Double.MAX_VALUE pour signifier qu'il n'y a pas de
		// liaisons entre un point et lui-même.
		for (i = 0; i < tailleMatrice; i++)
		{
			l[i][i] = Double.MAX_VALUE;
		}
		
		if (!error)
			System.out.println("Lecture du fichier \"" + nomFichier + "\" terminée.");
		
		return l;
	}
}
