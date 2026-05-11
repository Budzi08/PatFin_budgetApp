package com.patrykb.PatFin.tydzien8;


//Klasa pochodna - rozszerza funkcję o zamianę liter na wielkie

public class UpperCaseDescriptionFormatter extends DescriptionFormatter {
    @Override
    public String format(String description) {
        return super.format(description).toUpperCase();
    }
}
