package chat.mvc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

import mvc.Controller;

public class ChatController extends Controller<ChatModel, ChatWindow, ChatInput>{
	
	private Chatter currentChatter;
	
	public ChatController(ChatModel model, ChatWindow view, Chatter currentChatter) {
		super(model, view);
		this.currentChatter = currentChatter;
		view.addSubmitListener(new SubmitListener());
	}

	@Override
	public void handleInput(ChatInput input) {
		this.model.addMessage(new ChatMessage(currentChatter, input.getMessage()));
	}
	
	private class SubmitListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			model.addMessage(new ChatMessage(currentChatter, view.getCurrentMessage()));
			view.clearMessage();
		}
	}

}
