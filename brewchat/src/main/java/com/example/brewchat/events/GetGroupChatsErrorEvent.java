package com.example.brewchat.events;

import java.util.List;

/**
 * Event for errors in pulling group chat list from QB
 *
 * Created by josh on 4/28/15.
 */
public class GetGroupChatsErrorEvent {

    List<String> errors;

    public GetGroupChatsErrorEvent(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}
