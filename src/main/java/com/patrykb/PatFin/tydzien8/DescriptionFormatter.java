package com.patrykb.PatFin.tydzien8;


//Klasa bazowa - usuwa białe znaki na początku i końcu

public class DescriptionFormatter {
    public String format(String description) {
        if (description == null) return "";
        return description.trim();
    }
}
