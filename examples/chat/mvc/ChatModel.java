package examples.chat.mvc;

import mvc.Model;

public class ChatModel extends Model<ChatState>{
	
	public ChatModel(){
		this.state = new ChatState();
	}
	
	public void addMessage(ChatMessage message){
		this.state.messages.add(message);
		this.setChanged();
		this.notifyObservers();
	}
	
}
