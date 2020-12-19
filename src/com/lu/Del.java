package com.lu;

import java.io.File;
import java.util.Objects;

public class Del {
    private static boolean p;

    public Del() {
        if (new File("output").listFiles() != null)
            for (File i : Objects.requireNonNull(new File("output").listFiles()))
                p = i.delete();
    }

    public static boolean getP() {
        return p;
    }
}
