package tsp_ps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map.Entry;
import javax.swing.JPanel;

public class CanvasTSP extends JPanel
{
	private static final long serialVersionUID = 1L;
	private final CycleHamTSP c;
	private static final double ratio = 0.05;
	
	public CanvasTSP(CycleHamTSP c)
	{
		super();
		this.c = c;
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		
		// On definit la couleur de fond et on remplit le canvas.
		g2d.setColor(new Color(255, 255, 255));
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		// for(Pair<Ville, Ville> entry : c.get_arretes())
		for(Entry<Ville, Ville> entry : c.get_arretes().entrySet())
		{
			Ville v1 = entry.getKey();
			Ville v2 = entry.getValue();
			// Ville v1 = entry.fst();
			// Ville v2 = entry.snd();
			g2d.setColor(new Color(0, 0, 0));
			// g.fillOval((int)(v1.getX()*ratio),
			// (int)(v1.getY()*ratio), 1, 1);
			g.drawLine((int) (v1.getX() * ratio), (int) (this.getHeight() - v1.getY() * ratio), (int) (v2.getX() * ratio), (int) (this.getHeight() - v2.getY() * ratio));
		}
	}
}
