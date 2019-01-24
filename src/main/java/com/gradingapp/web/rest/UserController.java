package com.gradingapp.web.rest;

import com.gradingapp.web.rest.util.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

/**
 * Created by Preet on 22/01/2019.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    ActiveUserStore activeUserStore;

    @RequestMapping(value = "/users/loggedUsers", method = RequestMethod.GET)
    public List<String> getLoggedUsers(Locale locale, Model model) {
        return activeUserStore.getUsers();

    }
}
