package examples.counter.mvc;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ServerCounterViewer implements Observer{
	private JFrame frame = new JFrame();
	private JLabel label = new JLabel();
	
	public ServerCounterViewer(int value){
		frame.setTitle("Simple Counter");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 200);
		frame.getContentPane().setLayout(new BorderLayout());
		
		label.setText(""+value);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font(null, Font.BOLD, 72));
		
		frame.getContentPane().add(label, BorderLayout.CENTER);		
	}
	
	public void setVisible(boolean value){
		frame.setVisible(value);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Counter){
			label.setText(""+((Counter)o).getValue());
		}
	}
}
