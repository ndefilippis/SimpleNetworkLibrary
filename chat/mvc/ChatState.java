package chat.mvc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mvc.State;

public class ChatState implements State{
	public Set<Chatter> users;
	public List<ChatMessage> messages;
	
	public ChatState(){
		messages = new LinkedList<ChatMessage>();
		users = new HashSet<Chatter>();
	}
}
