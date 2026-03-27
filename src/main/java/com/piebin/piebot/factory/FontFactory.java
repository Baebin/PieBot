package com.piebin.piebot.factory;

import java.awt.*;

public interface FontFactory {
    Font getFont(String name, int style, float size);
    Font getBazziFont(int style, float size);
    Font getBazziFont30fBold();
    Font getArialFont(int style, float size);
    Font getArialFont30f();
    Font getArialFont30fBold();
}
