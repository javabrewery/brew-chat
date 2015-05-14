package com.example.brewchat.interfaces;

/**
 * Interface for sending login information from fragments
 *
 * Created by josh on 4/25/15.
 */
public interface LoginListener {
    public void buttonAuth(String username, String password, boolean saveCredentials);
}
