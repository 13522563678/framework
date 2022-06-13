package com.kcwl.framework.utils;

public class StringPaddingBuilder {

    private StringBuilder builder;

    public StringPaddingBuilder() {
        builder = new StringBuilder();
    }

    public StringPaddingBuilder appendByLeftZero(String str, int paddingLen) {
        if ( str != null ) {
            int leftLen = paddingLen - str.length();
            for (int i = 0; i < leftLen; i++) {
                builder.append("0");
            }
            builder.append(str);
        }
        return this;
    }

    public String toString() {
        return builder.toString();
    }
}
