package com.example.brewchat.events;

import java.util.List;

/**
 * Created by josh on 4/26/15.
 */
public class AuthenticationErrorEvent {

    private List errorList;

    public AuthenticationErrorEvent(List errorList){
        this.errorList = errorList;
    }

    public List getErrorList(){
        return errorList;
    }
}
