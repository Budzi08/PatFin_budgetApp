package com.patrykb.PatFin.pattern.strategy.sort;

import java.util.Collections;
import java.util.List;

public class AscendingSortStrategy implements SortStrategy {
    @Override
    public void sort(List<String> items) { Collections.sort(items); }
}
