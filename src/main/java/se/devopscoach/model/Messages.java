package se.devopscoach.model;

import com.google.gson.Gson;
import se.devopscoach.exception.ResourceNotFoundException;

import java.util.Collection;
import java.util.HashMap;

public class Messages {

    private HashMap<Long, Message> messages = new HashMap(); // Choosing hashmap - cheap and easy to find the messages

    public Message setMessage(Long id, Message message) {
        this.messages.put(id, message);
        return this.messages.get(id);
    }

    public Message getMessage(Long id) throws ResourceNotFoundException {

        Message msg = this.messages.get(id);
        String text = "Message with ID=" + id + " does not exist";

        if (msg == null) {
            throw new ResourceNotFoundException(text);
        } else {
            return msg;
        }
    }

    public String removeMessage(Long id) throws ResourceNotFoundException {
        Message msg = this.messages.get(id);
        String text = "Message with ID=" + id + " does not exist, so it cannot be removed.";

        if (msg == null) {
            throw new ResourceNotFoundException(text);
        } else {
            this.messages.remove(id);
            return listMessages();
        }
    }

    public String listMessages() {
        Collection coll = this.messages.values();
        Gson gson = new Gson();
        return gson.toJson(coll);
    }
}
