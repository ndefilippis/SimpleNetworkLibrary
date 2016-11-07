package chat.mvc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import mvc.View;

public class ChatWindow extends View<ChatState>{
	private JFrame frame;
	private JTextArea textPanel = new JTextArea();
	private JTextField input = new JTextField();
	protected JLabel label;
	private JButton submit = new JButton("Submit");
	
	public ChatWindow(ChatState initialState) {
		super(initialState);
		frame = new JFrame();
		frame.setTitle("Simple Chat");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(300, 400);
		frame.getContentPane().setLayout(new BorderLayout());
		JPanel inputComponent = new JPanel();
		inputComponent.setLayout(new BorderLayout());
		inputComponent.add(input, BorderLayout.CENTER);
		inputComponent.add(submit, BorderLayout.EAST);
		frame.getContentPane().add(inputComponent, BorderLayout.SOUTH);
		frame.getContentPane().add(textPanel, BorderLayout.CENTER);
		textPanel.setBackground(Color.CYAN);
		textPanel.setLayout(new FlowLayout());
	}
	
	public String getCurrentMessage(){
		return input.getText();
	}
	
	public void clearMessage(){
		input.setText("");
	}
	
	public void addTypingListener(ActionListener actionListener){
		input.addActionListener(actionListener);
	}
	
	public void addSubmitListener(ActionListener actionListener){
		submit.addActionListener(actionListener);
	}

	@Override
	protected void update(ChatState state) {
		textPanel.setText("");
		for(ChatMessage message : state.messages){
			label = new JLabel(message.user.name + ": " + message.message);
			textPanel.setText(textPanel.getText()+"\n"+label.getText());
		}
	}
	
	public void setVisible(boolean value){
		frame.setVisible(value);
	}
	
}
