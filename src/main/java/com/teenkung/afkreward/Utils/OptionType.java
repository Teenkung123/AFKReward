package com.teenkung.afkreward.Utils;

public enum OptionType {

    REGION, WORLD, BOTH;

    public static OptionType fromValue(String value) {
        for (OptionType type : OptionType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

}
