package tsp_ps;

import java.util.ArrayList;

/**
 * 
 * @author BOSOM-CHAUSSY
 */
public class Tsp_ps
{
	/*
	 * Liste des fichiers TSP : a280.tsp att48.tsp att532.tsp brazil58.tsp fl1577.tsp fl3795.tsp fnl4461.tsp kroB100.tsp
	 * kroB150.tsp kroB200.tsp kroC100.tsp kroD100.tsp mona-lisa100K.tsp pla7397.tsp pr2392.tsp rl5915.tsp rl5934.tsp
	 * u1060.tsp vm1084.tsp vm1748.tsp
	 * 
	 * Liste des fichiers XML : a280.xml att48.xml att532.xml br17.xml brazil58.xml fl1577.xml fl3795.xml fnl4461.xml
	 * kroB100.xml kroB150.xml kroB200.xml kroC100.xml kroD100.xml pla7397.xml pr2392.xml rl5915.xml rl5934.xml u1060.xml
	 * vm1084.xml vm1748.xml
	 */

	private static double TEMPS_EXEC = 15;
	private static boolean DEBUG = false;
	private static boolean COMPARE = false;
	private static boolean COMPARE_PREMIERTOUR = false;
	private static boolean PPVT = true;
	private static boolean PPV = false;
	private static boolean PPI = false;
	private static boolean DRAW = true;
	private static int sommetAleatoire = -1;
	
	public static boolean getDEBUG()
	{
		return DEBUG;
	}
	
	public static int getSommetAleatoire()
	{
		return sommetAleatoire;
	}
	
	public static void setSommetAleatoire(int sommetAleatoire)
	{
		Tsp_ps.sommetAleatoire = sommetAleatoire;
	}
	
	/**
	 * @param args
	 *                the command line arguments
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException
	{
		int i;
		long begin = 0, end = 0;
		float time;
		ArrayList<String> listeName = null;
		ArrayList<Double> listeDistances = null;
		ArrayList<Float> listeTemps = null;
		
		if (args.length < 1)
			throw new InterruptedException("ERREUR : Argument manquant. Pour l'aide utilisez l'option --help.");
		
		// Le format des jeux de données peut être XML ou TSP.
		// Nous avons donc séparé le traitement des XML et TSP.
		
		for (i = 0; i < args.length; i++)
		{
			if (args[i].startsWith("-"))
			{
				// Permet de tester si une liste de graph est reconnues par le programme.
				if (args[i].equals("-lectG"))
				{
					if (args[i + 1].equals("-DEBUG"))
					{
						DEBUG = true;
						i++;
					}
					
					ArrayList<CycleHamXML> cXML = new ArrayList<CycleHamXML>();
					ArrayList<CycleHamTSP> cTSP = new ArrayList<CycleHamTSP>();
					boolean BREAK = false;
					
					for (i++; i < args.length; i++)
					{
						if (args[i].endsWith(".xml"))
							cXML.add(new CycleHamXML(args[i], false));
						else if (args[i].endsWith(".tsp"))
							cTSP.add(new CycleHamTSP(args[i], false));
						else
						{
							System.err.println("ERREUR : Le format du fichier en entrée n'est ni \"XML\" ni \"TSP\".");
							BREAK = true;
							break;
						}
					}
					
					if (BREAK)
						break;
					
					// Si nous sommes en mode DEBUG, nous affichons des informations supplémentaires
					// propres aux graphiques xml.
					if (DEBUG)
					{
						for (CycleHamXML c : cXML)
						{
							c.estComplet();
						}
					}
				}
				// Permet de spécifier le temps maximum d'execution du programme
				else if (args[i].equals("-t"))
				{
					try
					{
						TEMPS_EXEC = Double.parseDouble(args[i + 1]);
					} catch (NumberFormatException e)
					{
						System.err.println("ERREUR : Argument non valide ou manquant après l'option \"-t\"");
						break;
					}
				}
				// Active le mode DEBUG.
				else if (args[i].equals("-debug") || args[i].equals("-DEBUG") || args[i].equals("-verbose"))
					DEBUG = true;
				// Permet de comparer les résultats entre deux fichiers.
				// Par défaut la comparaison se fait avec comme algo le PPV.
				else if (args[i].equals("-compare"))
				{
					COMPARE = true;
					COMPARE_PREMIERTOUR = false;
					PPVT = false;
					PPV = true;
					PPI = false;

					listeName = new ArrayList<String>();
					listeDistances = new ArrayList<Double>();
					listeTemps = new ArrayList<Float>();
				}
				// Permet de préciser si l'utilisateur souhaite utiliser le PPVT, PPV ou le PPI.
				// Le PPVT n'est pas possible pour les graphs XML.
				// C'est donc le PPI qui est utilisé à la place du PPVT par défaut
				else if (args[i].equals("-ppvT") || args[i].equals("-PPVT"))
				{
					PPVT = true;
					PPV = false;
					PPI = false;
				} else if (args[i].equals("-ppv") || args[i].equals("-PPV"))
				{
					PPVT = false;
					PPV = true;
					PPI = false;
				} else if (args[i].equals("-ppi") || args[i].equals("-PPI"))
				{
					PPVT = false;
					PPV = false;
					PPI = true;
				}
				// Permet de spécifier pour les graphes TSP si l'on souhaite avoir un dessin par défaut on
				// dessine.
				else if (args[i].equals("-NDraw"))
				{
					DRAW = true;
				} else if (args[i].equals("-Draw"))
				{
					DRAW = false;
				}
				// Affiche l'aide
				else if (args[i].equals("--help"))
				{
					System.out.println("Aide pas encore faîtes :P");
				} else
				{
					System.err.println("ERREUR : Option non reconnue.");
					break;
				}
			}
			// Si le graph est un TSP et qu'il est bien le dernier argument en paramètre (sauf si nous sommes en
			// mode compare)
			else if (args[i].endsWith(".tsp") && ((COMPARE ^ COMPARE_PREMIERTOUR) || i + 1 >= args.length))
			{
				//Sauvegarde du nom pour plus de commodit si on est en mode compare
				if(COMPARE)
					listeName.add(args[i]);
				
				// Si on est en mode DEBUG on lance le chronomètre.
				if (DEBUG || COMPARE)
					begin = System.currentTimeMillis();
				
				// Création du cycle.
				CycleHamTSP c = new CycleHamTSP(args[i], COMPARE);
				if (!c.estValide())
					break;
				
				// Lancement de l'algo pour obtenir un cycle Hamiltonien de départ.
				if (PPVT)
					c.plusProcheVoisinThreading();
				else if (PPV)
					c.plusProcheVoisin();
				else if (PPI)
					c.plusProcheInsertion();
				// Au cas où si tous les booleen sont à faux.
				else
				{
					System.out.println("ERREUR : Utilisation de l'algorithme Aléatoire.");
					c.algoAleatoire();
				}
				
				// CHECKPOINT
				// Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
				if (DEBUG)
				{
					end = System.currentTimeMillis();
					time = ((float) (end - begin)) / 1000f;
					
					if (c.estCycle())
						System.out.println("La solution est un cycle;");
					else
						System.out.println("WARNING : La solution obtenue n'est pas un cycle !");
					
					if (PPVT)
						System.out.println("Temps d'execution de l'algorithme du Plus Proche Voisin Threadé (PPVT) : " + time);
					else if (PPV)
						System.out.println("Temps d'execution de l'algorithme du Plus Proche Voisin (PPV) : " + time);
					else if (PPI)
						System.out.println("Temps d'execution de l'algorithme de la Plus Proche Insertion (PPI) : " + time);

					if(COMPARE)
						listeTemps.add(time);
					
				} else
				{
					if (!c.estCycle())
						System.out.println("WARNING : La solution obtenue n'est pas un cycle !");
				}
				System.out.println("Distance totale avant optimisation : " + Math.round(c.calculDistanceTotal()));
				if(DEBUG && COMPARE)
					listeDistances.add(c.calculDistanceTotal());
				
				// Si on est en mode DEBUG on lance le chronomètre.
				if (DEBUG)
					begin = System.currentTimeMillis();
				
				c.recuitSim(0.7, (int) Math.floor(TEMPS_EXEC * 60));
				
				// FIN
				// Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
				if (DEBUG)
				{
					end = System.currentTimeMillis();
					time = ((float) (end - begin)) / 1000f;
					if (c.estCycle())
						System.out.println("La solution est un cycle;");
					else
						System.out.println("WARNING : La solution obtenue n'est pas un cycle !");
					
					System.out.println("Temps d'execution de l'optimisation du Recuit Simulé: " + time);
					
					if(COMPARE)
						listeTemps.add(time);
					
				} else if(COMPARE)
				{
					end = System.currentTimeMillis();
					time = ((float) (end - begin)) / 1000f;
					listeTemps.add(time);
				} else
				{
					if (!c.estCycle())
						System.out.println("WARNING : La solution obtenue n'est pas un cycle !");
				}
				
				System.out.println("Distance total finale : " + Math.round(c.calculDistanceTotal()));
				if(COMPARE)
					listeDistances.add(c.calculDistanceTotal());
				
				if(COMPARE && !COMPARE_PREMIERTOUR)
					COMPARE_PREMIERTOUR = true;
				else if(COMPARE && COMPARE_PREMIERTOUR)
				{
					COMPARE = false;
					COMPARE_PREMIERTOUR = false;
					
					//Affichage du résultat de la comparaison
					for(int m = 0; m < listeName.size(); m++)
					{
						System.out.println("-" + listeName.get(m));
						if(DEBUG)
						{
							System.out.println("\t- Cycle de base : t = " + listeTemps.get(2 * m) + " min, d = " + listeTemps.get(2 * m));
							System.out.println("\t- Cycle optimisé : t = " + listeTemps.get(2 * m + 1) + " min, d = " + listeTemps.get(2 * m + 1));
						}
						else
							System.out.println("\t- Cycle optimisé : t = " + listeTemps.get(m) + " min, d = " + listeTemps.get(m));
					}
				}
				
				// Affichage du dessin
				if(DRAW)
					new DessinTSP(c);
				
			}
			// Si le graph est un XML et qu'il est bien le dernier argument en paramètre (sauf si nous sommes en
			// mode compare)
			else if (args[i].endsWith(".xml") && ((COMPARE ^ COMPARE_PREMIERTOUR) || i + 1 >= args.length))
			{
				//Sauvegarde du nom pour plus de commodit si on est en mode compare
				if(COMPARE)
					listeName.add(args[i]);
				
				// Si on est en mode DEBUG on lance le chronomètre.
				if (DEBUG || COMPARE)
					begin = System.currentTimeMillis();
				
				// Création du cycle.
				CycleHamXML c = new CycleHamXML(args[i], COMPARE);
				if (!c.estValide())
					break;
				
				// Lancement de l'algo pour obtenir un cycle Hamiltonien de départ.
				if (PPV)
					c.plusProcheVoisin();
				else if (PPI || PPVT)
					c.plusProcheInsertion();
				// Au cas où si tous les booleen sont à faux.
				else
				{
					System.out.println("ERREUR : Utilisation de l'algorithme Aléatoire.");
					c.algoAleatoire();
				}
				
				// CHECKPOINT
				// Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
				if (DEBUG)
				{
					end = System.currentTimeMillis();
					time = ((float) (end - begin)) / 1000f;
					
					if (c.estCycle())
						System.out.println("La solution est un cycle;");
					else
						System.out.println("WARNING : La solution obtenue n'est pas un cycle !");
					
					if (PPV)
						System.out.println("Temps d'execution de l'algorithme du Plus Proche Voisin (PPV) : " + time);
					else if (PPI || PPVT)
						System.out.println("Temps d'execution de l'algorithme de la Plus Proche Insertion (PPI) : " + time);
					
					if(COMPARE)
						listeTemps.add(time);
					
				} else
				{
					if (!c.estCycle())
						System.out.println("WARNING : La solution obtenue n'est pas un cycle !");
				}
				System.out.println("Distance totale avant optimisation : " + Math.round(c.calculDistanceTotal()));
				if(DEBUG && COMPARE)
					listeDistances.add(c.calculDistanceTotal());
				
				// Si on est en mode DEBUG on lance le chronomètre.
				if (DEBUG)
					begin = System.currentTimeMillis();
				
				c.recuitSim(0.7, (int) Math.floor(TEMPS_EXEC * 60));
				
				// FIN
				// Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
				if (DEBUG)
				{
					end = System.currentTimeMillis();
					time = ((float) (end - begin)) / 1000f;
					if (c.estCycle())
						System.out.println("La solution est un cycle;");
					else
						System.out.println("WARNING : La solution obtenue n'est pas un cycle !");
					
					System.out.println("Temps d'execution de l'optimisation du Recuit Simulé: " + time);
					
					if(COMPARE)
						listeTemps.add(time);
					
				} else if(COMPARE)
				{
					end = System.currentTimeMillis();
					time = ((float) (end - begin)) / 1000f;
					listeTemps.add(time);
				} else
				{
					if (!c.estCycle())
						System.out.println("WARNING : La solution obtenue n'est pas un cycle !");
				}
				
				System.out.println("Distance total finale : " + Math.round(c.calculDistanceTotal()));
				if(COMPARE)
					listeDistances.add(c.calculDistanceTotal());
				
				if(COMPARE && !COMPARE_PREMIERTOUR)
					COMPARE_PREMIERTOUR = true;
				else if(COMPARE && COMPARE_PREMIERTOUR)
				{
					COMPARE = false;
					COMPARE_PREMIERTOUR = false;
					
					//Affichage du résultat de la comparaison
					for(int m = 0; m < listeName.size(); m++)
					{
						System.out.println("-" + listeName.get(m));
						if(DEBUG)
						{
							System.out.println("\t- Cycle de base : t = " + listeTemps.get(2 * m) + " min, d = " + listeTemps.get(2 * m));
							System.out.println("\t- Cycle optimisé : t = " + listeTemps.get(2 * m + 1) + " min, d = " + listeTemps.get(2 * m + 1));
						}
						else
							System.out.println("\t- Cycle optimisé : t = " + listeTemps.get(m) + " min, d = " + listeTemps.get(m));
					}
				}
				
			} else
			{
				System.err.println("ERREUR : Le format du fichier en entrée n'est ni \"XML\" ni \"TSP\".");
			}
		}
	}
}
