package chat.mvc;

import mvc.Input;

public class ChatInput extends Input{
	private String message;
	
	public ChatInput(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
