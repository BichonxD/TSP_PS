package tsp_ps;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * 
 * @author BOSOM-CHAUSSY
 */
public class Tsp_ps
{
	/*
	 * Liste des fichiers TSP :
	 * a280.tsp
	 * att48.tsp
	 * att532.tsp
	 * brazil58.tsp
	 * fl1577.tsp
	 * fl3795.tsp
	 * fnl4461.tsp
	 * kroB100.tsp
	 * kroB150.tsp
	 * kroB200.tsp
	 * kroC100.tsp
	 * kroD100.tsp
	 * mona-lisa100K.tsp
	 * pla7397.tsp
	 * pr2392.tsp
	 * rl5915.tsp
	 * rl5934.tsp
	 * u1060.tsp
	 * vm1084.tsp
	 * vm1748.tsp
	 * 
	 * Liste des fichiers XML :
	 * a280.xml
	 * att48.xml
	 * att532.xml
	 * br17.xml
	 * brazil58.xml
	 * fl1577.xml
	 * fl3795.xml
	 * fnl4461.xml
	 * kroB100.xml
	 * kroB150.xml
	 * kroB200.xml
	 * kroC100.xml
	 * kroD100.xml
	 * pla7397.xml
	 * pr2392.xml
	 * rl5915.xml
	 * rl5934.xml
	 * u1060.xml
	 * vm1084.xml
	 * vm1748.xml
	 */
	
	/**
	 * @param args
	 *                the command line arguments
	 * @throws InterruptedException
	 */
	
	static int nbIteration = 1000;
	static boolean TEST_COMPLET = true;
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException
	{
		long begin = 0, end = 0;
		float time;
		
		if (args.length < 1)
			throw new InterruptedException("Argument manquant. Format : BLABLABLA");
		
		// Le format des jeux de données de Mona Lisa et des autres fichiers sont différents.
		// Nous avons donc séparé le traitement de Mona Lisa du traitement des autres fichiers.
		if (args[0].equals("-testG"))
		{
			// Pour tester si les graphes sont complets ou non.
			ArrayList<CycleHamXML> cXML = new ArrayList<CycleHamXML>();
			ArrayList<CycleHamTSP> cTSP = new ArrayList<CycleHamTSP>();
			
			for (int i = 1; i < args.length; i++)
			{
				if(args[i].endsWith(".xml"))
					cXML.add(new CycleHamXML(args[i]));
				else if(args[i].endsWith(".tsp"))
					cTSP.add(new CycleHamTSP(args[i]));
				else
					System.err.println("Le format du fichier entrée n'est ni \"XML\" ni \"TSP\".");
			}
			
			//Pour les graphes XML on fait des tests supplémentaires.
			for (CycleHamXML c : cXML)
			{
				c.estComplet();
			}
			
		} else if (args[0].contains(".tsp"))
		{
			// Création du cycle et départ du chrono.
			CycleHamTSP c = new CycleHamTSP(args[0]);
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
			c.recuitSim(0.7, 5 * 60);
			
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
			
		} else if (args[0].contains(".xml"))
		{
			// Création du cycle et départ du chrono.
			CycleHamXML c = new CycleHamXML(args[0]);
			begin = System.currentTimeMillis();
			
			// Lancement de l'algo pour obtenir un cycle Hamiltonien de départ.
			c.plusProcheVoisinThreading();
			// c.plusProcheInsertion();
			// c.plusProcheVoisin();
			
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
			// new Dessin(c);
			
		} else
		{
			System.err.println("Le format du fichier entrée n'est ni \"XML\" ni \"TSP\".");
		}
	}
}
