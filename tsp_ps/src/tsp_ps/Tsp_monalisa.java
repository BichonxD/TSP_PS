package tsp_ps;

/**
 * 
 * @author BOSOM-CHAUSSY
 */
public class Tsp_monalisa
{
	/**
	 * @param args
	 *                the command line arguments
	 * @throws InterruptedException
	 */
	
	static int nbIteration = 1000;
	
	public static void main(String[] args) throws InterruptedException
	{
		long begin = 0, end = 0;
		float time;
		
		if(args.length < 2)
			throw new InterruptedException("Argument manquant. Format : BLABLABLA");
		
		//Le format des jeux de données de Mona Lisa et des autres fichiers sont différents.
		//Nous avons donc séparé le traitement de Mona Lisa du traitement des autres fichiers.
		if(args[2].equals("mona-lisa100K.tsp"))
		{
			//Création du cycle et départ du chrono.
			CycleHamMonaLisa c = new CycleHamMonaLisa();
			begin = System.currentTimeMillis();
			
			
			//Lancement de l'algo pour obtenir un cycle Hamiltonien de départ.
			c.plusProcheVoisinThreading();
			
			
			//CHECKPOINT
			//Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
			end = System.currentTimeMillis();
			time = ((float) (end - begin)) / 1000f;
			if (c.estCycle())
				System.out.println("La solution est un cycle");
			else
				System.out.println("La solution n'est pas un cycle");
			
			System.out.println("Distance total avant optimisation : " + Math.round(c.calculDistanceTotal()));
			System.out.println("Temps d'execution de Plus Proche Voisin Thread : " + time + "\n\n");
			
			
			//Lancement de l'optimisation via le recuit simulé et du chrono
			begin = System.currentTimeMillis();
			c.recuitSim(0.7, 15*60);
			
			
			//FIN
			//Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
			end = System.currentTimeMillis();
			time = ((float) (end - begin)) / 1000f;
			if (c.estCycle())
				System.out.println("La solution est un cycle");
			else
				System.out.println("La solution n'est pas un cycle");
			System.out.println("Distance total finale : " + Math.round(c.calculDistanceTotal()));
			System.out.println("Temps d'execution de l'optimisation : " + time + "\n\n");
		}
		else
		{
			
		}
		
		
		
		
		new Dessin(c);
	}
}
