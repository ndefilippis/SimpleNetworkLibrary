package mvc;
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

public class CounterViewer implements Observer{
	private JFrame frame = new JFrame();
	private JLabel label = new JLabel();
	private JButton incrementButton = new JButton("+");
	private JButton decrementButton = new JButton("-");
	
	public CounterViewer(int value){
		frame.setTitle("Simple Counter");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(300, 400);
		frame.getContentPane().setLayout(new BorderLayout());
		
		label.setText(""+value);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font(null, Font.BOLD, 72));
		
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.getContentPane().add(incrementButton, BorderLayout.NORTH);
		frame.getContentPane().add(decrementButton, BorderLayout.SOUTH);
		
		
	}
	
	public void setVisible(boolean value){
		frame.setVisible(value);
	}
	
	public void removeIncrementListener(ActionListener listener){
		incrementButton.removeActionListener(listener);
	}
	
	public void removeDecrementListener(ActionListener listener){
		decrementButton.removeActionListener(listener);
	}
	
	public void addIncrementListener(ActionListener listener){
		incrementButton.addActionListener(listener);
	}
	
	public void addDecrementListener(ActionListener listener){
		decrementButton.addActionListener(listener);
	}
	
	public void addWindowListener(WindowListener listener){
		frame.addWindowListener(listener);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Counter){
			label.setText(""+((Counter)o).getValue());
		}
	}
}
