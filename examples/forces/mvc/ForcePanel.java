package examples.forces.mvc;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

public class ForcePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private List<Particle> particlesToDraw;
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(Particle p : particlesToDraw){
			int c = (int)(255*getChargeColor(p.getCharge()));
			double d = getMassConstant(p.getMass());
			g.setColor(new Color((int)(d*(255 - c)), (int)(d*(255 - c)), (int)(d*c)));
			g.fillOval((int)p.getX(), -(int)p.getY(), 7, 7);
		}
	}
	
	private double getChargeColor(double charge){
		double d = charge*10;
		return 1/(Math.pow(Math.E, -d) + 1);
	}
	
	private double getMassConstant(double mass){
		double d = (mass)*1;
		d = 1/(Math.pow(Math.E, d) + 1);
		return  1/(Math.pow(Math.E, -d) + 1);
	}

	
	public void update(List<Particle> movers){
		particlesToDraw = movers;
		repaint();
	}
}
