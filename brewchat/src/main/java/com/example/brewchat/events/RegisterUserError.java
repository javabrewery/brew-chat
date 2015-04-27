package com.example.brewchat.events;

import java.util.List;

/**
 * Created by josh on 4/27/15.
 */
public class RegisterUserError {

    private List<String> errors;

    public RegisterUserError(List<String> errors){
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
