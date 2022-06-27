package com.pes.logik.Global;

import static com.pes.logik.Constants.Languages.LANG_ENGLISH;

public class Game {
    // program preferences
    public static Float soundVolume = 0.2f;
    public static Boolean soundEnabled = true;
    public static Integer language = LANG_ENGLISH;
    public static Integer numCols = 5;
    public static Integer numColors = 5;
    public static Boolean canRepeat = false;
    public static Boolean learnMode = false;
    public static String sButtons = "B";
    public static String sDesk = "A";
    public static String sResults = "A";

    // global variables
    public static int hSpace = 24;
    public static int vSpace = 24;
    public static int element = 48;
    public static Integer actualRow = 0;
    public static int begCol = 0;

}
