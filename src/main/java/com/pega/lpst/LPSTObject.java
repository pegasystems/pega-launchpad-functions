package com.pega.lpst;

import java.io.Serializable;

/**
 * Simple object that can be used for things like parsing a delimited string to a List of Objects that are easier to
 * manipulate in an automation.
 */
public class LPSTObject implements Serializable  {
    public String Name;

    @Override
    public String toString() {
        return "LPSTObject{" +
                "Name='" + Name + '\'' +
                '}';
    }
}
