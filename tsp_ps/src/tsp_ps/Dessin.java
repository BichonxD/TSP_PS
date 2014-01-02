package tsp_ps;

import java.awt.Dimension;
import javax.swing.JFrame;

public class Dessin extends JFrame
{
	
	private static final long serialVersionUID = 1L;
	
	public Dessin(CycleHam c)
	{
		super("TSP Mona Lisa");
		this.setSize(new Dimension(1000, 1000));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Dessin pan = new Dessin(c);
		Canvas pan = new Canvas(c);
		// On prévient notre JFrame que notre JPanel sera son content
		this.setContentPane(pan);
		this.setVisible(true);
		pan.paint(getGraphics());
	}
}
