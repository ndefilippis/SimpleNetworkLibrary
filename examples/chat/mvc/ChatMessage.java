package examples.chat.mvc;

public class ChatMessage {
	public Chatter user;
	public String message;
	
	public ChatMessage(Chatter user, String message){
		this.user = user;
		this.message = message;
	}
}
