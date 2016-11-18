package examples.chat;

import examples.chat.mvc.ChatController;
import examples.chat.mvc.ChatModel;
import examples.chat.mvc.ChatWindow;
import examples.chat.mvc.Chatter;

public class ChatRunner {
	public static void main(String[] args){
		ChatModel model = new ChatModel();
		ChatWindow window = new ChatWindow(model.getState());
		Chatter me = new Chatter("Nick");
		@SuppressWarnings("unused")
		ChatController controller = new ChatController(model, window, me);
		window.setVisible(true);
		
	}
}
