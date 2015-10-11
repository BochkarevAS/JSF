package ru.company.comporator;

import java.util.Comparator;

public class ListComparator {

    private static ObjectComparator listComparator;

    private static class ObjectComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
        }
    }

    public static Comparator getInstance() {
        return listComparator == null ? new ObjectComparator() : listComparator;
    }

}
