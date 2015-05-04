package com.example.brewchat.events;

import java.util.List;

/**
 * Created by jon on 04/05/15.
 */
public class UsersLoadingErrorEvent {
    private List<String> errors;

    public UsersLoadingErrorEvent(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}
