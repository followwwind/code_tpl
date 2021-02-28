package com.wind.ui.code;

import com.wind.core.util.ini.ParseIni;
import com.wind.ui.factory.GenFactory;
import org.junit.Test;

import java.io.IOException;

public class GenTest {

    @Test
    public void init() throws IOException {
        ParseIni ini = ParseIni.getInstance("/app.ini");
        GenFactory tool = GenFactory.getInstance(ini);
        tool.genCode();
    }
}
