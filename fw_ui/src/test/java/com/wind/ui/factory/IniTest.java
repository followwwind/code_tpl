package com.wind.ui.factory;

import com.wind.core.util.ini.ParseIni;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

public class IniTest {

    @Test
    public void testRead() throws IOException {
        ParseIni ini = ParseIni.getInstance("/app.ini");
        Properties jdbc = ini.getSection("jdbc");
        if(jdbc != null){
            jdbc.list(System.out);
            System.out.println(jdbc.values());
        }
        System.out.println(ini.get("jdbc", "jdbcUrl"));

        Properties dao = ini.getSection("dao");
        if(dao != null){
            System.out.println(dao.keySet().size());
            dao.list(System.out);
        }
    }
}
