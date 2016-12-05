package examples.simplemovement.mvc;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

public class MoverPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 20;
	private static final int HEIGHT = 20;
	
	private List<Mover> moversToDraw;
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(Mover mover : moversToDraw){
			g.fillRect(mover.getX(), -mover.getY(), WIDTH, HEIGHT);
		}
	}

	
	public void update(List<Mover> movers){
		moversToDraw = movers;
		repaint();
	}
}
