package com.pega.lpst;

import java.io.Serializable;

/**
 * Simple object that can be used for things like parsing a delimited string to a List of Objects that are easier to
 * manipulate in an automation.
 */
public class LPSTObject implements Serializable  {
    public String Name;

    public String Field1;
    public String Field2;
    public String Field3;
    public String Field4;
    public String Field5;
    public String Field6;
    public String Field7;
    public String Field8;
    public String Field9;
    public String Field10;

    public int NumActiveFields = 0;

    @Override
    public String toString() {
        String str = "LPSTObject{";
        if (Name != null) str += "Name='" + Name + '\'';

        if (NumActiveFields > 0) {
            str += "'" + Field1 + "'";
            if (NumActiveFields > 1) {
                str += ", '" + Field2 + "'";
                if (NumActiveFields > 2) {
                    str += ", '" + Field3 + "'";
                    if (NumActiveFields > 3) {
                        str += ", '" + Field4 + "'";
                        if (NumActiveFields > 4) {
                            str += ", '" + Field5 + "'";
                            if (NumActiveFields > 5) {
                                str += ", '" + Field6 + "'";
                                if (NumActiveFields > 6) {
                                    str += ", '" + Field7 + "'";
                                    if (NumActiveFields > 7) {
                                        str += ", '" + Field8 + "'";
                                        if (NumActiveFields > 8) {
                                            str += ", '" + Field9 + "'";
                                            if (NumActiveFields > 9) {
                                                str += ", '" + Field10 + "'";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        str += '}';

        return str;
    }
}
