package com.tjh.util;

import java.util.ResourceBundle;

public class ResourceBundleBackedSingleton {
    public static ResourceBundle getResourceBundle(final Class<?> aClass, final String name) {
        return ResourceBundle.getBundle(aClass.getPackage().getName() + "." + aClass.getSimpleName() + name);
    }
}
