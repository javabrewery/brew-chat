package com.example.brewchat.events;

import java.util.List;

/**
 * Created by josh on 4/27/15.
 */
public class CreateChatError {

    private List<String> errors;

    public CreateChatError(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors(){
        return errors;
    }
}
