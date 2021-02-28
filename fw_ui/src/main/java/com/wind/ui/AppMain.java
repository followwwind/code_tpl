package com.wind.ui;

import com.wind.core.util.ini.ParseIni;
import com.wind.ui.factory.GenFactory;

import java.io.IOException;

/**
 * @package com.wind.ui
 * @className AppMain
 * @note 程序入口
 * @author wind
 * @date 2020/12/9 23:19
 */
public class AppMain {

    public static void main(String[] args) throws IOException {
        ParseIni config = args.length == 1 ? new ParseIni(args[0]) :
                ParseIni.getInstance("/app.ini");
        GenFactory tool = GenFactory.getInstance(config);
        tool.genCode();
    }
}
