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
	
	public static void main(String[] args) throws InterruptedException, ErreurFormatFichier, JDOMException, IOException
	{
		long begin = 0, end = 0;
		float time;
		
		// if(args.length < 2)
		// throw new InterruptedException("Argument manquant. Format : BLABLABLA");
		
		// Le format des jeux de données de Mona Lisa et des autres fichiers sont différents.
		// Nous avons donc séparé le traitement de Mona Lisa du traitement des autres fichiers.
		if (false/* args[2].equals("mona-lisa100K.tsp") */)
		{
			// Création du cycle et départ du chrono.
			CycleHamMonaLisa c = new CycleHamMonaLisa();
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
			new Dessin(c);
		} else
		{
			// Pour savoir si tout les graphes sont complets ou non.
			if (true)
			{
				ArrayList<CycleHam> c = new ArrayList<CycleHam>();
				//c.add(new CycleHam("a280.xml"));
				//c.add(new CycleHam("att48.xml"));
				//c.add(new CycleHam("att532.xml"));
				//BUGGE !! c.add(new CycleHam("br17.xml"));
				//c.add(new CycleHam("brazil58.xml"));
				c.add(new CycleHam("fl1577.xml"));
				//c.add(new CycleHam("fl3795.xml"));
				//c.add(new CycleHam("fnl4461.xml"));
				//c.add(new CycleHam("kroB100.xml"));
				//c.add(new CycleHam("kroB150.xml"));
				//c.add(new CycleHam("kroB200.xml"));
				//c.add(new CycleHam("kroD100.xml"));
				//c.add(new CycleHam("pla7397.xml"));
				//c.add(new CycleHam("pr2392.xml"));
				//c.add(new CycleHam("rl5915.xml"));
				//c.add(new CycleHam("rl5934.xml"));
				//c.add(new CycleHam("u1060.xml"));
				//c.add(new CycleHam("vm1084.xml"));
				//c.add(new CycleHam("vm1748.xml"));
				
				for(CycleHam t : c)
				{
					t.estComplet();
				}
			} else
			{
				// Création du cycle et départ du chrono.
				//CycleHam c = new CycleHam(args[2]);
				//CycleHam c = new CycleHam("a280.xml");
				//CycleHam c = new CycleHam("att48.xml");
				//CycleHam c = new CycleHam("att532.xml");
				CycleHam c = new CycleHam("br17.xml");
				//CycleHam c = new CycleHam("brazil58.xml");
				//CycleHam c = new CycleHam("fl1577.xml");
				//CycleHam c = new CycleHam("fl3795.xml");
				//CycleHam c = new CycleHam("fnl4461.xml");
				//CycleHam c = new CycleHam("kroB100.xml");
				//CycleHam c = new CycleHam("kroB150.xml");
				//CycleHam c = new CycleHam("kroB200.xml");
				//CycleHam c = new CycleHam("kroD100.xml");
				//CycleHam c = new CycleHam("pla7397.xml");
				//CycleHam c = new CycleHam("pr2392.xml");
				//CycleHam c = new CycleHam("rl5915.xml");
				//CycleHam c = new CycleHam("rl5934.xml");
				//CycleHam c = new CycleHam("u1060.xml");
				//CycleHam c = new CycleHam("vm1084.xml");
				//CycleHam c = new CycleHam("vm1748.xml");
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
