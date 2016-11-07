package chat;

import chat.mvc.ChatController;
import chat.mvc.ChatModel;
import chat.mvc.ChatWindow;
import chat.mvc.Chatter;

public class ChatRunner {
	public static void main(String[] args){
		ChatModel model = new ChatModel();
		ChatWindow window = new ChatWindow(model.getState());
		Chatter me = new Chatter("Nick");
		ChatController controller = new ChatController(model, window, me);
		window.setVisible(true);
		
	}
}
