package com.example.busreservation.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {
    public static String email(){
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return (a==null)? null : (String)a.getPrincipal(); // we set subject=email
    }
}
