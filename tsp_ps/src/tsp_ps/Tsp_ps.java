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

	private static int TEMPS_EXEC = 15;
	private static double TAUX_LIM_ACCEPTATION = 0.7;
	private static int NB_ITERATION = 3000;
	private static double TAUX_DECREMENT_TEMP = 0.85;
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
		ArrayList<Integer> listeDistances = null;
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
						{
							CycleHamXML cx = new CycleHamXML(args[i], false);
							if(cx.estValide())
								cXML.add(cx);
						}
						else if (args[i].endsWith(".tsp"))
						{
							CycleHamTSP ct = new CycleHamTSP(args[i], false);
							if(ct.estValide())
								cTSP.add(ct);
						}
						else
						{
							System.out.println("ERREUR : Le format du fichier en entrée n'est ni \"XML\" ni \"TSP\".");
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
						TEMPS_EXEC = Integer.parseInt(args[i + 1]);
						i++;
					} catch (NumberFormatException e)
					{
						System.out.println("ERREUR : Argument non valide ou manquant après l'option \"-t\"");
						break;
					}
				}
				// Permet de spécifier le taux d'acceptation du recuit simulé
				else if (args[i].equals("-tauxAccept"))
				{
					try
					{
						TAUX_LIM_ACCEPTATION = Double.parseDouble(args[i + 1]);
						i++;
					} catch (NumberFormatException e)
					{
						System.out.println("ERREUR : Argument non valide ou manquant après l'option \"-t\"");
						break;
					}
				}
				// Permet de spécifier le nombre d'itération du recuit simulé
				else if (args[i].equals("-nbIt"))
				{
					try
					{
						NB_ITERATION = Integer.parseInt(args[i + 1]);
						i++;
					} catch (NumberFormatException e)
					{
						System.out.println("ERREUR : Argument non valide ou manquant après l'option \"-t\"");
						break;
					}
				}
				// Permet de spécifier le taux de décrémentation de la température du recuit simulé
				else if (args[i].equals("-tauxDecT"))
				{
					try
					{
						TAUX_DECREMENT_TEMP = Double.parseDouble(args[i + 1]);
						i++;
					} catch (NumberFormatException e)
					{
						System.out.println("ERREUR : Argument non valide ou manquant après l'option \"-t\"");
						break;
					}
				}
				// Active le mode DEBUG.
				else if ((args[i].equals("-debug") || args[i].equals("-DEBUG") || args[i].equals("-verbose")) && !COMPARE)
					DEBUG = true;
				// Permet de comparer les résultats entre deux fichiers.
				// Par défaut la comparaison se fait avec comme algo le PPV.
				else if (args[i].equals("-compare"))
				{
					if(i+2 >= args.length)
						System.out.println("ERREUR : Argument manquant après l'option \"-t\"");
						
					COMPARE = true;
					COMPARE_PREMIERTOUR = false;
					PPVT = false;
					PPV = true;
					PPI = false;
					if(listeName == null)
						listeName = new ArrayList<String>();
					if(listeDistances == null)
						listeDistances = new ArrayList<Integer>();
					if(listeTemps == null)
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
					DRAW = false;
				} else if (args[i].equals("-Draw"))
				{
					DRAW = true;
				}
				// Affiche l'aide
				else if (args[i].equals("--help") || args[i].equals("-?"))
				{
					System.out.println("Utilisation : [OPTION]... [FILE]");
					System.out.println("Par défaut le programme dessine si possible le graphe et utilise le PPVT pour les .tsp ou le PPV pour les .xml.");
					System.out.println("   -lectG [FICHIER]...\t\t\t\t\tEssaie de lire la liste de fichiers");
					System.out.println("   -t [ENTIER]\t\t\t\t\t\tPermet de spécifier le temps d'execution de l'optimisation d'un graphe en minute. Par défaut ce temps est fixé à " + TEMPS_EXEC + " minutes.");
					System.out.println("   -tauxAccept [DECIMAL]\t\t\t\tPermet de spécifier le taux d'acceptation du recuit. Par défaut ce taux est fixé à " + TAUX_LIM_ACCEPTATION + ".");
					System.out.println("   -nbIt [ENTIER]\t\t\t\t\tPermet de spécifier le nombre d'itération du recuit. Par défaut ce temps est fixé à " + NB_ITERATION + ".");
					System.out.println("   -tauxDecT [DECIMAL]\t\t\t\t\tPermet de spécifier le taux de décrémentation de la température du recuit. Par défaut ce temps est fixé à " + TAUX_DECREMENT_TEMP + ".");
					System.out.println("   -debug, -DEBUG, -verbose\t\t\t\tAffiche les messages de débug.");
					System.out.println("   -compare [FICHIER] [OPTION]... [FICHIER]\t\tPermet de comparer deux ou plus fichiers entre eux. Les options disponibles entre les deux fichiers ne peuvent inclure -DEBUG.");
					System.out.println("   -ppvt\t\t\t\t\t\tSpécifie l'utilisation de l'algorithme du Plus Proche Voisin Thréadé si possible (impossible pour les fichiers xml execute donc le PPV).");
					System.out.println("   -ppv\t\t\t\t\t\t\tSpécifie l'utilisation de l'algorithme du Plus Proche Voisin.");
					System.out.println("   -ppi\t\t\t\t\t\t\tSpécifie l'utilisation de l'algorithme de la Plus Proche Insertion.");
					System.out.println("   -Draw\t\t\t\t\t\tSpécifie l'affichage du dessin.");
					System.out.println("   --help, -?\t\t\t\t\t\tAffiche cette aide.");
				} else
				{
					System.out.println("ERREUR : Option non reconnue.");
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
						System.out.println("La solution est un cycle.");
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
					listeDistances.add((int) Math.round(c.calculDistanceTotal()));
				
				// Si on est en mode DEBUG on lance le chronomètre.
				if (DEBUG)
					begin = System.currentTimeMillis();
				
				c.recuitSimule(TAUX_LIM_ACCEPTATION, TEMPS_EXEC * 60, NB_ITERATION, TAUX_DECREMENT_TEMP, DEBUG);
				
				// FIN
				// Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
				if (DEBUG)
				{
					end = System.currentTimeMillis();
					time = ((float) (end - begin)) / 1000f;
					if (c.estCycle())
						System.out.println("La solution est un cycle.");
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
				
				System.out.println("Distance totale finale : " + Math.round(c.calculDistanceTotal()) + "\n");
				if(COMPARE)
					listeDistances.add((int) Math.round(c.calculDistanceTotal()));
				
				if(COMPARE && !COMPARE_PREMIERTOUR)
					COMPARE_PREMIERTOUR = true;
				else if(COMPARE && COMPARE_PREMIERTOUR)
				{
					COMPARE = false;
					COMPARE_PREMIERTOUR = false;
					
					//Affichage du résultat de la comparaison
					System.out.println("\nRésumé de la comparaison :");
					for(int m = 0; m < listeName.size(); m++)
					{
						System.out.println("-" + listeName.get(m));
						if(DEBUG)
						{
							System.out.println("\t- Cycle de base : t = " + listeTemps.get(2 * m) + " s, d = " + listeDistances.get(2 * m));
							System.out.println("\t- Cycle optimisé : t = " + listeTemps.get(2 * m + 1) + " s, d = " + listeDistances.get(2 * m + 1));
						}
						else
							System.out.println("\t- Cycle optimisé : t = " + listeTemps.get(m) + " s, d = " + listeDistances.get(m));
					}
					
					setSommetAleatoire(-1);
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
				if (PPV || PPVT)
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
						System.out.println("La solution est un cycle.");
					else
						System.out.println("WARNING : La solution obtenue n'est pas un cycle !");
					
					if (PPV || PPVT)
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
					listeDistances.add((int) Math.round(c.calculDistanceTotal()));
				
				// Si on est en mode DEBUG on lance le chronomètre.
				if (DEBUG)
					begin = System.currentTimeMillis();
				
				c.recuitSimule(TAUX_LIM_ACCEPTATION, TEMPS_EXEC * 60, NB_ITERATION, TAUX_DECREMENT_TEMP, DEBUG);
				
				// FIN
				// Affichage du chrono, du score et vérification que c'est un cycle hamiltonien correct
				if (DEBUG)
				{
					end = System.currentTimeMillis();
					time = ((float) (end - begin)) / 1000f;
					if (c.estCycle())
						System.out.println("La solution est un cycle.");
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
				
				System.out.println("Distance totale finale : " + Math.round(c.calculDistanceTotal()) + "\n");
				if(COMPARE)
					listeDistances.add((int) Math.round(c.calculDistanceTotal()));
				
				if(COMPARE && !COMPARE_PREMIERTOUR)
					COMPARE_PREMIERTOUR = true;
				else if(COMPARE && COMPARE_PREMIERTOUR)
				{
					COMPARE = false;
					COMPARE_PREMIERTOUR = false;
					
					//Affichage du résultat de la comparaison
					System.out.println("\nRésumé de la comparaison :");
					for(int m = 0; m < listeName.size(); m++)
					{
						System.out.println("-" + listeName.get(m));
						if(DEBUG)
						{
							System.out.println("\t- Cycle de base : t = " + listeTemps.get(2 * m) + " s, d = " + listeDistances.get(2 * m));
							System.out.println("\t- Cycle optimisé : t = " + listeTemps.get(2 * m + 1) + " s, d = " + listeDistances.get(2 * m + 1));
						}
						else
							System.out.println("\t- Cycle optimisé : t = " + listeTemps.get(m) + " s, d = " + listeDistances.get(m));
					}
					
					setSommetAleatoire(-1);
				}
				
			} else
			{
				System.err.println("ERREUR : Le format du fichier en entrée n'est ni \"XML\" ni \"TSP\".");
			}
		}
	}
}
