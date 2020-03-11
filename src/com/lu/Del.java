package com.lu;

import java.io.File;
import java.util.Objects;

public class Del {
    private static boolean p;

    public Del() {
        if (new File("output\\msc").listFiles() != null)
            for (File i : Objects.requireNonNull(new File("output\\msc").listFiles()))
                p = i.delete();
        if (new File("output\\text_row").listFiles() != null)
            for (File i : Objects.requireNonNull(new File("output\\text_row").listFiles()))
                p = i.delete();
        if (new File("output\\config").listFiles() != null)
            for (File i : Objects.requireNonNull(new File("output\\config").listFiles()))
                p = i.delete();
    }

    public static boolean getP() {
        return p;
    }
}
