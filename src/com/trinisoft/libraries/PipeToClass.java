package com.trinisoft.libraries;

import java.util.*;

/**
 * This class converts a string that is piped to a collection. 
 * If a String is "foo:bar:baz:voom", it can convert it to a Vector, List etc
 * If a String is "foo:bar, bax:baz, voom:doom", it can convert it to a Map Kind of Data Structure, Hashtable, Hashmap etc
 */
public class PipeToClass {

    /**Developers call this method. This method takes a type and a string. 
     * If the String can be successfully converted to the type, it does so, else it throws an IllegalArgumentException
     * @param anyValue This is the Class you want to convert the String to.
     * @param piped This is the String you want to convert
     * @return anyValue Returns the anyValue after appending the parsed String to it
     */
    public Object pipeToClass(Object anyValue, String piped, String delim) {
        if (piped.contains(",")) {
            if (anyValue instanceof Map) {
                addMapTypeCollection(anyValue, piped, delim);
            } else {
                throw new IllegalArgumentException("The argument passed can not be mapped to the String passed.\n"
                        + "Expected: java.util.Map, Found: " + anyValue.getClass().getName());
            }
        } else {
            addOtherTypeCollection(anyValue, piped, delim);
        }
        return anyValue;
    }

    private Object addMapTypeCollection(Object anyValue, String piped, String delim) {
        String[] split = piped.split(",");
        String key = "";
        String value = "";
        Map v = (Map) anyValue;
        for (int i = 0; i < split.length; i++) {
            String[] keyValue = split[i].split(delim);
            key = keyValue[0].trim();
            value = keyValue[1].trim();
            v.put(key, value);
        }
        anyValue = v;
        return anyValue;
    }

    private void addOtherTypeCollection(Object anyValue, String piped, String delim) {
        AbstractCollection abc = null;
        if (anyValue instanceof AbstractCollection) {
            abc = (AbstractCollection) anyValue;
        } else {
            throw new IllegalArgumentException("The argument passed can not be mapped to the String passed.\n"
                    + "Excepted: A subclass of java.util.AbstractCollection or an iterable object\n"
                    + "Found: " + anyValue.getClass().getName());
        }
        String[] split = piped.split(delim);
        for (int i = 0; i < split.length; i++) {
            abc.add(split[i].trim());
        }
    }

    public int getType(Object anyValue) {
        if (anyValue instanceof Vector) {
            return Constants.TYPE_VECTOR;
        }
        if (anyValue instanceof Enumeration) {
            return Constants.TYPE_ENUMERATION;
        }
        if (anyValue instanceof ArrayList) {
            return Constants.TYPE_ARRAYLIST;
        }
        if (anyValue instanceof Iterator) {
            return Constants.TYPE_ITERATOR;
        } else {
            return 0;
        }
    }
}
