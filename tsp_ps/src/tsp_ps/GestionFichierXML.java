package tsp_ps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.jdom2.*;
import org.jdom2.input.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Lecture du fichier afin de créer toutes les villes
 */
public class GestionFichierXML
{
	static public double[][] lectureFichier(String nomFichier) throws ErreurFormatXML, JDOMException, IOException
	{
		Document document = null;
		Element racine = null;
		
		// On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		
		// On crée un nouveau document JDOM avec en argument le fichier XML
		document = sxb.build(new File(System.getProperty("user.dir") + "/" + nomFichier));
		
		if (document != null)
		{
			// On initialise un nouvel élément racine avec l'élément racine du document.
			racine = document.getRootElement();
		}
		
		// On trouve le graph, remonte une erreur si il n'y a pas exactement un graphe
		List<Element> graph = racine.getChildren("graph");
		if (graph.size() != 1)
			throw new ErreurFormatXML("Erreur dans le format du fichier (Au niveau Graph).");
		
		// On trouve les vertex (sommets du graphe), remonte une erreur s'il n'y a aucun vertex
		List<Element> vertex = graph.get(0).getChildren("vertex");
		if (vertex.size() <= 0)
			throw new ErreurFormatXML("Erreur dans le format du fichier (Au niveau Vertex).");
		
		// Nous créons notre matrice qui contiendra nos segments
		double map[][] = new double[vertex.size()][vertex.size()];
		int i = 0, j = 0;
		
		// Nous parcourons pour chaque vertex tous ses edge (liaisons entre deux vertex)
		for (Element v : vertex)
		{
			// On recupere la liste des edge et vérifie qu'il y en a le bon nombre
			List<Element> edge = v.getChildren("edge");
			if (edge.size() != vertex.size() - 1)
				throw new ErreurFormatXML("Erreur dans le format du fichier. (Au niveau Edge) edge.size = " + edge.size() + " vertex.size = " + vertex.size());
			
			// Nous récupérons la valeur de tous les edges.
			// Si la valeure est 0 nous mettons MAX_DOUBLE pour indiquer qu'il y a l'infini entre nos deux
			// vertex.
			for (Element e : edge)
			{
				if (j == i)
				{
					map[i][j] = Double.MAX_VALUE;
					j++;
				}
				
				try
				{
					map[i][j] = e.getAttribute("cost").getDoubleValue();
					if (map[i][j] == 0)
						map[i][j] = Double.MAX_VALUE;
				} catch (DataConversionException e1)
				{
					e1.printStackTrace();
				}
				// System.out.println("map[" + i + "][" + j + "] = " + map[i][j]);
				j++;
			}
			
			i++;
			j = 0;
		}
		
		// Le dernier segement n'a pas été mis à MAX_VALUE
		map[i - 1][i - 1] = Double.MAX_VALUE;
		
		return map;
	}
	
	static public double[][] lectureFichierV2(String nomFichier)
	{
		int i = -1, j = 0;
		int tailleMatrice;
		double cout;
		double l[][] = null;
		
		try
		{
			Scanner scan = new Scanner(new File(System.getProperty("user.dir") + "/" + nomFichier));
			
			// Scan du fichier complet
			while (scan.hasNextLine())
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
					if (j != Integer.parseInt(tmp2[1]))
						System.out.println("Warning : Un point est ecrit à un endroit où il ne faut pas. (" + j + " != " + Integer.parseInt(tmp2[1]) + ").");
					
					// strCout[1] contient le cout de l'arrete au format exponentiel
					cout = Double.parseDouble(strCout[1]);
					
					// ICI GESTION DU CAS OU LE COUT EST ZERO
					
					l[i][j] = cout;
					j++;
				}
				
				// La diagonale de la matrice est mise à Double.MAX_VALUE pour signifier qu'il n'y a pas de
				// liaisons entre un point et lui-même.
				if (l != null && i == j)
				{
					l[i][j] = Double.MAX_VALUE;
					j++;
				}
			}
		} catch (FileNotFoundException e)
		{
			System.err.println("Fichier \"" + System.getProperty("user.dir") + "/" + nomFichier + "\" non trouvé.");
		}
		
		System.out.println("Lecture du fichier \"" + nomFichier + "\" terminée");
		return l;
	}
}
