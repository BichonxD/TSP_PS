package tsp_ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public class CycleHamXML
{
	private final double _map[][];
	private ArrayList<Integer> _villesRestantes;
	private HashMap<Integer, Integer> _arretes;
	private static final Random _rand = new Random();
	private Integer _depart;
	private final int _nbVilles;
	private double _distance;
	
	public CycleHamXML(String nomFichier, boolean nonRandom)
	{
		// Initialisation des listes
		_map = GestionFichierXML.lectureFichierV2(nomFichier);
		_villesRestantes = new ArrayList<Integer>();
		_arretes = new HashMap<Integer, Integer>();
		
		if (_map != null)
			_nbVilles = _map.length;
		else
			_nbVilles = 0;
		
		// Lancement de la fonction d'init
		if (estValide())
			this.init(nonRandom);
	}
	
	/**
	 * Fonction d'initialisation du cycle.
	 */
	public final void init(boolean nonRandom)
	{
		// On ajoute toutes nos villes aux villes restantes.
		for (int i = 0; i < _nbVilles; i++)
		{
			_villesRestantes.add(i);
		}
		
		// On vide le cycle possiblement existant.
		_arretes.clear();
		_distance = 0;
		
		// Choix de la ville de depart aléatoirement ou non en fonction du boolean en paramètre
		if (nonRandom)
		{
			// Si le sommet n'a pas été setté.
			if (Tsp_ps.getSommetAleatoire() == -1 || Tsp_ps.getSommetAleatoire() >= _nbVilles)
			{
				_depart = _rand.nextInt(_nbVilles);
				Tsp_ps.setSommetAleatoire(_depart);
			} else
			{
				_depart = Tsp_ps.getSommetAleatoire();
			}
		} else
		{
			_depart = _rand.nextInt(_nbVilles);
		}
		
		// On signale la ville choisie.
		System.out.println("Ville de départ : " + (_depart + 1) + " / " + _nbVilles);
		
		// Retrait de la ville de départ des villes restantes
		_villesRestantes.remove(_depart);
	}
	
	/**
	 * Fonction servant pour le DEBUG afin d'avoir quelques informations sur le graph en question.
	 * */
	public void estComplet()
	{
		int nb = 0, i = 0, j = 0, nb_dif = 0;
		for (double d1[] : _map)
		{
			for (double d : d1)
			{
				if (d == Double.MAX_VALUE)
					nb++;
				
				if (j == i && d != Double.MAX_VALUE)
					System.out.println("Warning : map[" + i + "][" + j + "] = " + _map[i][j]);
				if (_map[i][j] != _map[j][i])
					nb_dif++;
				j++;
			}
			i++;
			j = 0;
		}
		
		System.out.println("Il y a " + (nb - i) + " cases vides sur " + i * i + " dans la map (" + i + " points). Soit environ " + ((nb - i) * 100) / (i * i) + " %.");
		System.out.println("Il y a " + nb_dif + " points qui n'ont pas les mêmes arrêtes.");
	}
	
	/**
	 * Permet de savoir si le graph est valide ou non.
	 * */
	public boolean estValide()
	{
		return _nbVilles > 0;
	}
	
	/**
	 * Algorithme qui ajoute aléatoirement les villes dans le cycle
	 */
	public void algoAleatoire()
	{
		Integer v_enCours = _depart;
		Integer v_suivante;
		int pos;
		
		while (!_villesRestantes.isEmpty())
		{
			// On récupère une ville
			pos = _rand.nextInt(_villesRestantes.size());
			v_suivante = _villesRestantes.get(pos);
			
			// Ajout de la ville au cycle
			_arretes.put(v_enCours, v_suivante);
			
			// On ajoute la distance
			_distance += _map[v_enCours][v_suivante];
			
			// On supprime la ville
			_villesRestantes.remove(v_suivante);
			v_enCours = v_suivante;
			v_suivante = null;
		}
		
		_arretes.put(v_enCours, _depart);
		_distance += _map[v_enCours][_depart];
	}
	
	/**
	 * Algorithme du plus proche voisin
	 */
	public void plusProcheVoisin()
	{
		Integer v_enCours = _depart;
		Integer v_best = null;
		double d, d2;
		
		while (!_villesRestantes.isEmpty())
		{
			d = Double.MAX_VALUE;
			
			for (Integer v_test : _villesRestantes)
			{
				d2 = _map[v_enCours][v_test];
				if (d2 < d)
				{
					d = d2;
					v_best = v_test;
				}
			}
			
			_distance += d;
			_arretes.put(v_enCours, v_best);
			_villesRestantes.remove(v_best);
			v_enCours = v_best;
		}
		
		_arretes.put(v_enCours, _depart);
		_distance += _map[(int) v_enCours][(int) _depart];
	}
	
	/**
	 * Indique si l'on a un cycle ou non. Retourne false si la hashmap des arrête et vide, si toutes les clés ne sont pas
	 * aussi des valeurs et si la ville de départ n'est pas celle d'arrivée.
	 * 
	 * @return true si l'on a un cycle, false sinon
	 */
	public boolean estCycle()
	{
		boolean ret = true;
		
		Integer val = _depart;
		int count = 0;
		
		if (!_arretes.isEmpty())
		{
			if (_arretes.keySet().containsAll(_arretes.values()))
			{
				// Parcours des valeurs;
				while (count < _arretes.size())
				{
					val = _arretes.get(val);
					count++;
					// Si l'on retrouve la ville de départ
					// sans qu'on ai attend le nombre de
					// ville totale il y a un problème
					if ((val == _depart) && (count < _arretes.size()))
					{
						System.out.println("Ville de départ avant la fin du parcours du cycle");
						ret = false;
						count = _arretes.size() + 1;
					}
				}
				
				// Fin du cycle et pas la ville de départ =>
				// problème
				if (val != _depart)
				{
					System.out.println("Pas la ville de départ à la fin du cycle");
					ret = false;
				}
			} else
			{
				System.out.println("Toutes les clés ne sont pas parmis les valeurs");
				ret = false;
			}
		} else
		{
			System.out.println("Arrêtes vides");
			ret = false;
		}
		
		return ret;
	}
	
	@Override
	public String toString()
	{
		return _arretes.toString();
	}
	
	/**
	 * Renvoie la distance totale calcule lors de la construction du graphe.
	 * 
	 * @return distance totale du chemin
	 */
	public double getDistanceTotal()
	{
		return _distance;
	}
	
	/**
	 * Calcul la distance totale a partir des arretes.
	 * 
	 * @return distance totale du chemin
	 */
	public double calculDistanceTotal()
	{
		double d = 0;
		
		for (Entry<Integer, Integer> entry : _arretes.entrySet())
		{
			Integer key = entry.getKey();
			Integer value = entry.getValue();
			d += _map[key][value];
		}
		
		return d;
	}
	
	public double calculDistanceTotal(ArrayList<Integer> l)
	{
		double d = 0;
		int i;
		for (i = 0; i < l.size() - 1; i++)
		{
			d += _map[l.get(i)][l.get(i + 1)];
		}
		d += _map[l.get(i)][l.get(0)];
		
		return d;
	}
	
	/**
	 * Renvoie la ville de depart
	 * 
	 * @return Ville _depart
	 */
	public int get_depart()
	{
		return _depart;
	}
	
	/**
	 * Renvoie l'ensemble des arretes formant le chemin trouve
	 * 
	 * @return HashMap<Ville, Ville> _arretes
	 */
	public HashMap<Integer, Integer> get_arretes()
	{
		return _arretes;
	}
	
	/**
	 * Algortihme de la Plus Proche Insertion. A ete abandonne car beaucoup plus long et moins performant que le plus
	 * proche voisin.
	 */
	public void plusProcheInsertion()
	{
		Integer v_prime, v, w1, w2;
		ArrayList<Integer> _villesVisitees = new ArrayList<Integer>();
		double d, d_tmp;
		
		// On ajoute la ville la plus proche pour avoir un cycle.
		v = null;
		d = Double.MAX_VALUE;
		for (Integer vtest : _villesRestantes)
		{
			d_tmp = _map[vtest][_depart];
			if (d_tmp < d)
			{
				v = vtest;
				d = d_tmp;
			}
		}
		_arretes.put(_depart, v);
		_arretes.put(v, _depart);
		// On enleve la ville des restantes et on l'ajoute aux visitees
		_villesVisitees.add(_depart);
		_villesVisitees.add(v);
		_villesRestantes.remove(v);
		
		while (!_villesRestantes.isEmpty())
		{
			v_prime = _villesRestantes.get(0);
			v = null;
			w1 = null;
			w2 = null;
			d = Double.MAX_VALUE;
			
			// On trouve la ville la plus proche
			for (Integer v_visitee : _villesVisitees)
			{
				d_tmp = _map[v_prime][v_visitee];
				if (d_tmp < d)
				{
					v = v_visitee;
					d = d_tmp;
				}
			}
			
			// Maintenant on trouve qu'elle est la ville voisine qui
			// se liera à v_prime
			// Il ne peut y avoir que 2 possibilités.
			for (Entry<Integer, Integer> entry : _arretes.entrySet())
			{
				Integer key = entry.getKey();
				Integer value = entry.getValue();
				if (value.equals(v))
				{
					w1 = key;
					break;
				}
			}
			w2 = _arretes.get(v);
			
			// Les anciennes arretes sont automatiquement
			// remplacees.
			// Si c'est w1 la plus proche
			if (_map[v][v_prime] + _map[v_prime][w1] - _map[v][w1] < _map[v][v_prime] + _map[v_prime][w2] - _map[v][w2])
			{
				_arretes.put(w1, v_prime);
				_arretes.put(v_prime, v);
			} // Si cest w2
			else
			{
				_arretes.put(v, v_prime);
				_arretes.put(v_prime, w2);
			}
			
			// On enleve la ville des restantes et on l'ajoute aux
			// visitees
			_villesVisitees.add(v_prime);
			_villesRestantes.remove(v_prime);
		}
	}
	
	public void twoOpt(int n)
	{
		System.out.println("Optimisation : 2-Opt");
		int i = 0, iplus1, j, jplus1, taille;
		long begin = 0, end = 0;
		float time = 0;
		Integer v = _depart;
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		do
		{
			list.add(v);
			v = _arretes.get(v);
		} while (v != _depart);
		
		taille = list.size();
		begin = System.currentTimeMillis();
		boolean amelioration = true;
		
		while (amelioration)
		{
			amelioration = false;
			for (i = 0; i < taille - 4; i++)
			{
				Integer xi = list.get(i);
				
				iplus1 = i + 1;
				Integer xiplus1 = list.get(iplus1);
				for (j = i + 2; j < taille - 1; j++)
				{
					Integer xj = list.get(j);
					jplus1 = j + 1;
					Integer xjplus1 = list.get(jplus1);
					if (xjplus1 != xi)
					{
						if (_map[xi][xiplus1] + _map[xj][xjplus1] > _map[xi][xj] + _map[xiplus1][xjplus1])
						{
							// Remplacer les arêtes (xi, xi+1) et (xj, xj+1) par
							// (xi, xj) et
							// (xi+1, xj+1) dans H
							ArrayList<Integer> res = reverse(list, iplus1, j);
							// Calcule la nouvelle distance
							if (calculDistanceTotal(res) < calculDistanceTotal(list))
							{
								list = res;
								amelioration = true;
							}
						}
					}
				}
				end = System.currentTimeMillis();
				time = ((float) (end - begin)) / 1000f;
				if (time > n)
				{
					amelioration = false;
					break;
				}
			}
			if (time > n)
			{
				amelioration = false;
				break;
			}
		}
		end = System.currentTimeMillis();
		time = ((float) (end - begin)) / 1000f;
		System.out.println("Temps d'execution de 2-opt : " + time + " ( i = " + i + ")");
		
		for (i = 0; i < list.size() - 1; i++)
		{
			_arretes.put(list.get(i), list.get(i + 1));
		}
		_arretes.put(list.get(i), list.get(0));
	}
	
	/**
	 * Inverse la liste entre les bornes i et j, i doit être inférieur à j
	 * 
	 * @param list
	 *                La liste à inverser
	 * @param i
	 *                Premier point de la liste à inverser
	 * @param j
	 *                Dernier point de la liste à inverser
	 * @return Nouvelle liste inversée
	 */
	public ArrayList<Integer> reverse(ArrayList<Integer> list, int i, int j)
	{
		ArrayList<Integer> l = new ArrayList<Integer>();
		Integer xi, xj;
		
		l.addAll(list);
		for (; i < j; i++, j--)
		{
			xi = l.get(i);
			xj = l.get(j);
			
			l.set(i, xj);
			l.set(j, xi);
		}
		return l;
	}

	/** Fourni une solution voisine de celle donnée en paramètre en appelant l'un des algorithmes. */
	public ArrayList<Integer> solutionVoisine(ArrayList<Integer> list, double tailleDeplacement)
	{
		/*int tmp = (int) tailleDeplacement;
		
		if(tmp > 2)
			return solutionVoisineVInversionPays(list, (int) tailleDeplacement);
		else
			return solutionVoisineVInversionPays(list, 2);*/
		
		return solutionVoisine2Opt(list);
	}
	
	/** Inverse 2 villes qui sont voisines en choisissant aléatoirement la ville à inverser */
	public ArrayList<Integer> solutionVoisineInversionVillesVoisines(ArrayList<Integer> list)
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
		int i, j, k;
		
		i = _rand.nextInt(_nbVilles);
		if(i + 1 < _nbVilles)
			j = i +1;
		else
			j = 0;
		
		for (k = 0; k < _nbVilles; k++)
		{
			if (k == i)
			{
				res.add(list.get(j));
			} else if (k == j)
			{
				res.add(list.get(i));
			} else
			{
				res.add(list.get(k));
			}
		}
		
		return res;
	}

	/**Met un groupe de villes choisit aléatoirement en fin de liste. La taille du groupe a déplacer est fournie en argument.*/
	public ArrayList<Integer> solutionVoisineInversionPays(ArrayList<Integer> list, int tailleDeplacement)
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
		int i, k;
		
		i = _rand.nextInt(_nbVilles - tailleDeplacement * 2);
		
		for (k = 0; k < _nbVilles; k++)
		{
			if (k - i < 0)
				res.add(list.get(k));
			else if ((k - i >= 0) && (k < _nbVilles - tailleDeplacement))
				res.add(list.get(tailleDeplacement + k));
			else if(k >= _nbVilles - tailleDeplacement)
			{
				res.add(list.get(i));
				i++;
			}
		}
		
		return res;
	}

	/** Inverse deux villes toutes deux choisient aléatoirement. */
	public ArrayList<Integer> solutionVoisineAleatoire(ArrayList<Integer> list)
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
		int i, j, k;
		
		i = _rand.nextInt(_nbVilles);
		do
		{
			j = _rand.nextInt(_nbVilles);
		} while (i == j);
		
		for (k = 0; k < _nbVilles; k++)
		{
			if (k == i)
			{
				res.add(list.get(j));
			} else if (k == j)
			{
				res.add(list.get(i));
			} else
			{
				res.add(list.get(k));
			}
		}
		
		return res;
	}
	
	/** Solution voisine obtenue via un algo proche du 2-opt (1ère version) */
	public ArrayList<Integer> solutionVoisine2Opt(ArrayList<Integer> list)
	{
		ArrayList<Integer> res;
		int i, iplus1, j, jplus1, taille = list.size();
		
		do
		{
			i = _rand.nextInt(_nbVilles);
		} while (i > taille - 4);
		
		iplus1 = i + 1;
		Integer xi = list.get(i), xiplus1 = list.get(iplus1);
		
		for (j = i + 2; j < (taille - 1); ++j)
		{
			jplus1 = j + 1;
			Integer xj = list.get(j), xjplus1 = list.get(jplus1);
			
			if (xjplus1 != xi)
			{
				if (_map[xi][xiplus1] + _map[xj][xjplus1] > _map[xi][xj] + _map[xiplus1][xjplus1])
				{
					// Remplacer les arêtes (xi, xi+1) et (xj, xj+1) par (xi,
					// xj) et (xi+1, xj+1) dans H
					res = reverse(list, iplus1, j);
					// Calcule la nouvelle distance
					if (calculDistanceTotal(res) < calculDistanceTotal(list))
						return res;
				}
			}
		}
		return reverse(list, iplus1, j - 1);
	}
	
	/** Solution voisine obtenue via un algo proche du 2-opt (2de version) */
	public ArrayList<Integer> solutionVoisine2OptV2(ArrayList<Integer> list)
	{
		int i, j, taille = list.size();
		
		i = _rand.nextInt(taille - 4);
		
		if(i > 3)
		{
			j = _rand.nextInt(i - 2);
			j += taille - i + 2;
		}
		else
			j = taille - i - 1;	
		
		return reverse(list, i + 1, j);
		
	}
	
	public void recuitSimule(double tauxLimiteAcceptation, int tempsAlloue, int nbIteration, double tauxDecrementT, boolean DEBUG)
	{
		if (DEBUG)
			System.out.println("Optimisation : Recuit Simulé(" + tauxLimiteAcceptation + ", " + tempsAlloue / 60 + ", " + nbIteration + ", " + tauxDecrementT + ")");
		
		double temp = Math.round(initRecuit(10, nbIteration, 1000, 0.8));
		
		if (DEBUG)
			System.out.println("Température Initiale : " + temp);
		
		// Solution courrant
		ArrayList<Integer> solCourante = new ArrayList<Integer>();
		double valCourante;
		
		// Initialisation de la solution courante
		Integer v = _depart;
		do
		{
			solCourante.add(v);
			v = _arretes.get(v);
		} while (v != _depart);
		valCourante = calculDistanceTotal(solCourante);
		
		// Solution voisine
		ArrayList<Integer> solVoisine;
		double valVoisine;
		
		// Stockage de la meilleure solution trouvée
		ArrayList<Integer> bestSol = new ArrayList<Integer>();
		double bestVal;
		
		bestSol.addAll(solCourante);
		bestVal = valCourante;
		
		// Utile pour le critère d'arrêt
		double tauxAcceptation;
		int compteur = 0;
		
		// Autre
		int i;
		long begin = System.currentTimeMillis(), end;
		float time = 0;
		
		// Exécution de l'algo du recuit
		do
		{
			int nbMov = 0;
			
			for (i = 0; i < nbIteration; ++i)
			{
				// Récupération d'une solution voisine
				solVoisine = solutionVoisine(solCourante, temp * 10 / _nbVilles);
				valVoisine = calculDistanceTotal(solVoisine);
				
				// Acceptation ou non de cette solution
				if (critereMetropolis((valVoisine - valCourante), temp))
				{
					solCourante = solVoisine;
					valCourante = valVoisine;
					nbMov++;
					
					// Stockage de la meilleure solution trouvée
					if (valCourante < bestVal)
					{
						bestSol = solCourante;
						bestVal = valCourante;
						// On a améliorer la solution donc on on réinitialise le compteur de non
						// amélioration
						compteur = 0;
						// System.out.println("solution amélioree ! distance : " + bestVal + "(" +
						// valCourante + ")");
					}
				}
			}
			
			// Calcul du taux d'acceptation
			tauxAcceptation = nbMov / (double) nbIteration;
			if (tauxAcceptation < tauxLimiteAcceptation)
				compteur++; // La solution ne change pas suffisamment, pour continuer
				
			// Actualise la température
			temp *= tauxDecrementT;
			
			// Calcul du temps écoulé
			end = System.currentTimeMillis();
			time = ((float) (end - begin)) / 1000f;
		} while ((compteur < 100) && (temp > 10) && (time < tempsAlloue));
		
		if (DEBUG)
		{
			end = System.currentTimeMillis();
			time = ((float) (end - begin)) / 1000f;
			System.out.println("Temps d'execution du recuit : " + time);
			System.out.println("taux : " + tauxAcceptation + ", température : " + temp + ", compteur : " + compteur);
		}
		
		// Stockage de la meilleure solution
		for (i = 0; i < (bestSol.size() - 1); i++)
		{
			_arretes.put(bestSol.get(i), bestSol.get(i + 1));
		}
		_arretes.put(bestSol.get(i), bestSol.get(0));
		
	}
	
	/**
	 * Calcule la réponse au critère de Métropolis si val < 0, on améliore la solution donc on accepte sinon, on calcule
	 * la probabilité d'acceptation par rapport à exp(-val/temp)
	 * 
	 * @param val
	 *                Valeur à considérer
	 * @param temp
	 *                Température à prendre en compte
	 * @return Booléen si l'on répond au critère de Metropolis ou non.
	 */
	public boolean critereMetropolis(double val, double temp)
	{
		if (val <= 0)
			return true;
		
		return (_rand.nextDouble() <= Math.exp(-val / temp));
	}
	
	/**
	 * Initialise la température pour le recuit
	 * 
	 * @param temp
	 *                Température initiale à tester
	 * @param iteration
	 *                Nombre d'itération pour chaque palier
	 * @param nbAcceptations
	 *                Nombre d'acceptation pour chaque palier
	 * @param taux
	 *                Taux d'acceptation que l'on veut considérer
	 * @return La température de base du recuit.
	 */
	public double initRecuit(double temp, int iteration, int nbAcceptations, double taux)
	{
		// Solution courrant
		ArrayList<Integer> solCourante = new ArrayList<Integer>();
		double valCourante;
		
		// Initialissation de la solution courante
		Integer v = _depart;
		do
		{
			solCourante.add(v);
			v = _arretes.get(v);
		} while (v != _depart);
		
		valCourante = calculDistanceTotal(solCourante);
		
		// Solution voisine
		ArrayList<Integer> solVoisine;
		double valVoisine;
		
		// Autre
		boolean continuer = true;
		int nbMov, i;
		
		// Exécution de l'algo du recuit
		while (continuer)
		{
			nbMov = 0;
			i = 0;
			
			while ((i < iteration) && (nbMov < nbAcceptations))
			{
				// Récupération d'une solution voisine
				solVoisine = solutionVoisine(solCourante, temp * 10 / _nbVilles);
				valVoisine = calculDistanceTotal(solVoisine);
				
				// Acceptation ou non de cette solution
				if (critereMetropolis((valVoisine - valCourante), temp))
				{
					solCourante = solVoisine;
					valCourante = valVoisine;
					nbMov++;
				}
				i++;
			}
			
			// Calcul du taux d'acceptation pour augmenter ou non la température
			if ((nbMov / (double) i) < taux)
			{
				temp *= 2;
			} else
				continuer = false;
		}
		return temp;
	}
	
}
