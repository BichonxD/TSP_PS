package tsp_ps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import java.awt.Color;
import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import java.util.List;
import java.util.Iterator;

/**
 * Lecture du fichier afin de créer toutes les villes
 */
public class GestionFichier
{
	private Document document;
	private Element racine;
	
	
	/**Le constructeur par défaut choisi l'instance de Mona Lisa*/
	static public ArrayList<Ville> lectureFichier()
	{
		ArrayList<Ville> l = new ArrayList<Ville>();
		try
		{
			Scanner scan = new Scanner(new File("mona-lisa100K.tsp"));
			// Scan du fichier complet
			while(scan.hasNextLine())
			{
				// Récupère la ligne
				String line = scan.nextLine();
				// Découpe la ligne selon les espaces
				String[] coord = line.split("[ ]+");
				// Ajoute la ville à la liste
				l.add(new Ville(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]), Integer.parseInt(coord[2])));
			}
		}catch(FileNotFoundException e)
		{
			System.err.println("Fichier non trouvé");
		}
		
		System.out.println("Lecture du fichier terminée");
		return l;
	}
	
	static public ArrayList<Ville> lectureFichier(String nomFichier)
	{	
				//On crée une instance de SAXBuilder
				SAXBuilder sxb = new SAXBuilder();
				
				try
				{
					//On crée un nouveau document JDOM avec en argument le fichier XML
					//Le parsing est terminé ;)
					document = sxb.build(new File(System.getProperty("user.dir") + "/" + nomFichier));
				} catch (Exception e)
				{
				}
				
				if (document != null)
				{
					//On initialise un nouvel élément racine avec l'élément racine du document.
					racine = document.getRootElement();
				} else
					racine = null;
	}
			
			public String getTitre()
			{
				//On crée une List contenant tous les noeuds "etudiant" de l'Element racine
				List<Element> titre = racine.getChildren("Title");
				
				//On crée un Iterator sur notre liste
				Iterator<Element> i = titre.iterator();
				
				//On crée l'Element courant afin de
				//pouvoir utiliser les méthodes propres aux Element
				Element courant = (Element) i.next();
				
				return courant.getText();
			}
			
			public Color getCouleurFond()
			{
				Color couleur;
				String hex;
				
				try
				{
					//Notre couleur
					hex = racine.getAttribute("style").getValue();
					couleur = Color.decode(hex.substring(5, 12));
					
					return couleur;
				} catch (NullPointerException e)
				{
					return Color.white;
				}
			}
			
			public ArrayList<MySegment> getSegments() throws DataConversionException
			{
				ArrayList<MySegment> segments = new ArrayList<MySegment>();
				MyPoint pt1, pt2;
				Color couleur;
				String hex;
				
				//On crée une List contenant tous les noeuds "etudiant" de l'Element racine
				List<Element> lignes = racine.getChildren("line");
				
				//On crée un Iterator sur notre liste
				Iterator<Element> i = lignes.iterator();
				try
				{
					while (i.hasNext())
					{
						//On recrée l'Element courant à chaque tour de boucle afin de
						//pouvoir utiliser les méthodes propres aux Element comme :
						//sélectionner un nœud fils, modifier du texte, etc...
						Element courant = (Element) i.next();
						
						//On créé nos nouveaux points
						pt1 = new MyPoint(courant.getAttribute("x1").getIntValue(), courant.getAttribute("y1").getIntValue(), null, Color.black);
						pt2 = new MyPoint(courant.getAttribute("x2").getIntValue(), courant.getAttribute("y2").getIntValue(), null, Color.black);
						if (pt1.equals(pt2))
							throw new DataConversionException("pt1", "pt2");
						//Notre couleur
						hex = courant.getAttribute("style").getValue();
						couleur = Color.decode(hex.substring(7, 14));
						//Notre segment
						segments.add(new MySegment(pt1, pt2, couleur));
					}
				} catch (NullPointerException e)
				{
					throw new DataConversionException("pt1", "pt2");
				}
				
				return segments;
			}
			
			public Document getDocument()
			{
				return document;
			}
			
		}
}
