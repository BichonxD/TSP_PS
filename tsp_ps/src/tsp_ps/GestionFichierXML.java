package tsp_ps;

import java.io.File;
import java.io.IOException;
import org.jdom2.*;
import org.jdom2.input.*;
import java.util.List;

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
				if(j == i)
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
		
		//Le dernier segement n'a pas été mis à MAX_VALUE
		map[i-1][i-1] = Double.MAX_VALUE;
		
		return map;
	}
	
}
