package com.lu;

public class Reader {
    private Reader() {
    }

    public static void reading() {
        try {
            Process p = Runtime.getRuntime().exec("cmd /k start .\\Runner.exe");
            p.getOutputStream().close();
            p.getInputStream().close();
            p.getErrorStream().close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
