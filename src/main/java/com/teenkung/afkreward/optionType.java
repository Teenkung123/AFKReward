package com.teenkung.afkreward;

public enum optionType {

    REGION, WORLD;

    public static optionType fromValue(String value) {
        for (optionType type : optionType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

}
