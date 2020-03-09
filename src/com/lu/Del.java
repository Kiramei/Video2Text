package com.lu;

import java.io.File;
import java.util.Objects;

public class Del {
    public Del() {
        boolean inr = false;
        if(new File("output\\msc").listFiles()!=null)
            for (File i : Objects.requireNonNull(new File("output\\msc").listFiles()))
                inr=i.delete();
        if(new File("output\\text_row").listFiles()!=null)
            for (File i : Objects.requireNonNull(new File("output\\text_row").listFiles()))
                inr=i.delete();
        if(new File("output\\config").listFiles()!=null)
            for (File i : Objects.requireNonNull(new File("output\\config").listFiles()))
                inr=i.delete();
        System.out.println(inr);
    }
}
