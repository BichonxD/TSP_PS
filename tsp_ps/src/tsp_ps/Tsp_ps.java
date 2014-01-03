package tsp_ps;

import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.JDOMException;

/**
 * 
 * @author BOSOM-CHAUSSY
 */
public class Tsp_ps
{
	/**
	 * @param args
	 *                the command line arguments
	 * @throws InterruptedException
	 */
	
	static int nbIteration = 1000;
	static boolean TEST_COMPLET = false;
	static boolean MONA_LISA = false;
	
	public static void main(String[] args) throws InterruptedException, ErreurFormatXML, JDOMException, IOException
	{
		long begin = 0, end = 0;
		float time;
		
		// if(args.length < 2)
		// throw new InterruptedException("Argument manquant. Format : BLABLABLA");
		
		// Le format des jeux de données de Mona Lisa et des autres fichiers sont différents.
		// Nous avons donc séparé le traitement de Mona Lisa du traitement des autres fichiers.
		if (MONA_LISA && args[2].equals("mona-lisa100K.tsp"))
		{
			// Création du cycle et départ du chrono.
			CycleHamTSP c = new CycleHamTSP();
			begin = System.currentTimeMillis();
			
			// Lancement de l'algo pour obtenir un cycle Hamiltonien de départ.
			c.plusProcheVoisinThreading();
			
			// CHECKPOINT
			// Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
			end = System.currentTimeMillis();
			time = ((float) (end - begin)) / 1000f;
			if (c.estCycle())
				System.out.println("La solution est un cycle");
			else
				System.out.println("La solution n'est pas un cycle");
			
			System.out.println("Distance total avant optimisation : " + Math.round(c.calculDistanceTotal()));
			System.out.println("Temps d'execution de Plus Proche Voisin Thread : " + time + "\n\n");
			
			// Lancement de l'optimisation via le recuit simulé et du chrono
			begin = System.currentTimeMillis();
			c.recuitSim(0.7, 15 * 60);
			
			// FIN
			// Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
			end = System.currentTimeMillis();
			time = ((float) (end - begin)) / 1000f;
			if (c.estCycle())
				System.out.println("La solution est un cycle");
			else
				System.out.println("La solution n'est pas un cycle");
			System.out.println("Distance total finale : " + Math.round(c.calculDistanceTotal()));
			System.out.println("Temps d'execution de l'optimisation : " + time + "\n\n");
			
			// Affichage du dessin
			new DessinTSP(c);
		} else
		{
			// Pour savoir si tout les graphes sont complets ou non.
			if (TEST_COMPLET)
			{
				ArrayList<CycleHamXML> c = new ArrayList<CycleHamXML>();
				//c.add(new CycleHamXML("a280.xml"));
				c.add(new CycleHamXML("att48.xml"));
				//c.add(new CycleHamXML("att532.xml"));
				//BUGGE !! c.add(new CycleHamXML("br17.xml"));
				//c.add(new CycleHamXML("brazil58.xml"));
				//c.add(new CycleHamXML("fl1577.xml"));
				//c.add(new CycleHamXML("fl3795.xml"));
				//c.add(new CycleHamXML("fnl4461.xml"));
				//c.add(new CycleHamXML("kroB100.xml"));
				//c.add(new CycleHamXML("kroB150.xml"));
				//c.add(new CycleHamXML("kroB200.xml"));
				//c.add(new CycleHamXML("kroD100.xml"));
				//c.add(new CycleHamXML("pla7397.xml"));
				//c.add(new CycleHamXML("pr2392.xml"));
				//c.add(new CycleHamXML("rl5915.xml"));
				//c.add(new CycleHamXML("rl5934.xml"));
				//c.add(new CycleHamXML("u1060.xml"));
				//c.add(new CycleHamXML("vm1084.xml"));
				//c.add(new CycleHamXML("vm1748.xml"));
				
				for(CycleHamXML t : c)
				{
					t.estComplet();
				}
			} else
			{
				// Création du cycle et départ du chrono.
				//CycleHamXML c = new CycleHamXML(args[2]);
				//CycleHamXML c = new CycleHamXML("a280.xml");
				CycleHamXML c = new CycleHamXML("att48.xml");
				//CycleHamXML c = new CycleHamXML("att532.xml");
				//CycleHamXML c = new CycleHamXML("br17.xml");
				//CycleHamXML c = new CycleHamXML("brazil58.xml");
				//CycleHamXML c = new CycleHamXML("fl1577.xml");
				//CycleHamXML c = new CycleHamXML("fl3795.xml");
				//CycleHamXML c = new CycleHamXML("fnl4461.xml");
				//CycleHamXML c = new CycleHamXML("kroB100.xml");
				//CycleHamXML c = new CycleHamXML("kroB150.xml");
				//CycleHamXML c = new CycleHamXML("kroB200.xml");
				//CycleHamXML c = new CycleHamXML("kroD100.xml");
				//CycleHamXML c = new CycleHamXML("pla7397.xml");
				//CycleHamXML c = new CycleHamXML("pr2392.xml");
				//CycleHamXML c = new CycleHamXML("rl5915.xml");
				//CycleHamXML c = new CycleHamXML("rl5934.xml");
				//CycleHamXML c = new CycleHamXML("u1060.xml");
				//CycleHamXML c = new CycleHamXML("vm1084.xml");
				//CycleHamXML c = new CycleHamXML("vm1748.xml");
				begin = System.currentTimeMillis();
				
				// Lancement de l'algo pour obtenir un cycle Hamiltonien de départ.
				c.plusProcheVoisinThreading();
				//c.plusProcheInsertion();
				//c.plusProcheVoisin();
				
				// CHECKPOINT
				// Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
				end = System.currentTimeMillis();
				time = ((float) (end - begin)) / 1000f;
				if (c.estCycle())
					System.out.println("La solution est un cycle");
				else
					System.out.println("La solution n'est pas un cycle");
				
				System.out.println("Distance total avant optimisation : " + Math.round(c.calculDistanceTotal()));
				System.out.println("Temps d'execution de Plus Proche Voisin Thread : " + time + "\n\n");
				
				// Lancement de l'optimisation via le recuit simulé et du chrono
				begin = System.currentTimeMillis();
				c.recuitSim(0.7, 15 * 60);
				
				// FIN
				// Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
				end = System.currentTimeMillis();
				time = ((float) (end - begin)) / 1000f;
				if (c.estCycle())
					System.out.println("La solution est un cycle");
				else
					System.out.println("La solution n'est pas un cycle");
				System.out.println("Distance total finale : " + Math.round(c.calculDistanceTotal()));
				System.out.println("Temps d'execution de l'optimisation : " + time + "\n\n");
				
				// Affichage du dessin
				//new Dessin(c);
			}
		}
	}
}
