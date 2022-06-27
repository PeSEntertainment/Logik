package com.pes.logik.Constants;

public class Languages {
    public static final Integer LANG_ENGLISH = 0;
    public static final Integer LANG_CZECH = 1;

    public static final int ID_LANGUAGES = 0;
    public static final int ID_PLAY = 1;
    public static final int ID_DESK = 2;
    public static final int ID_SETUP = 3;
    public static final int ID_HELP = 4;
    public static final int ID_YOUHAVEACTIVE = 5;
    public static final int ID_CONTINUE = 6;
    public static final int ID_NEWGAME = 7;
    public static final int ID_COLUMNS = 8;
    public static final int ID_COLORS = 9;
    public static final int ID_CANREPEAT = 10;
    public static final int ID_SOUND = 11;
    public static final int ID_SOUNDVOLUME = 12;
    public static final int ID_LANGUAGE = 13;
    public static final int ID_BUTTONS = 14;
    public static final int ID_GAMETASK = 15;
    public static final int ID_GAMETASKTEXT = 16;
    public static final int ID_GAMECONTROL = 17;
    public static final int ID_GAMECONTROLTEXT = 18;
    public static final int ID_AUTHOR = 19;
    public static final int ID_YES = 20;
    public static final int ID_NO = 21;
    public static final int ID_QUITGAME = 22;
    public static final int ID_NOTSOLVED = 23;
    public static final int ID_SOLVED = 24;
    public static final int ID_MOVES = 25;
    public static final int ID_DONATIONWARE = 26;
    public static final int ID_DONATIONWARETEXT = 27;
    public static final int ID_CS_OPENHOMEPAGE = 28;
    public static final int ID_THANKYOU=29;
    public static final int ID_WANTSHOWSOLUTION=30;
    public static final int ID_EVALUATION=31;


    public static final String[][] strings = {
            // hlavni menu
            {"english", "česky"},            //0
            {"NEW", "NOVÁ"},
            {"DESK", "DESKA"},
            {"SETUP", "NASTAV"},
            {"HELP", "POMOC"},
            // dialog rozehrana hra
            {"\n YOU HAVE ACTIVE GAME \nDO YOU WANT CONTINUE\nOR PLAY NEW GAME?\n",
                    "\n MÁŠ ROZEHRANOU HRU \nCHCEŠ V NÍ POKRAČOVAT\nNEBO ZAČÍT NOVOU?\n"},
            {"continue", "pokračuj"},
            {"new game", "nová hra"},
            // nastaveni desk
            {"COLUMNS", "SLOUPCE "},
            {"COLORS", "BARVY "},
            {"COLORS\nCAN REPEAT", "BARVY SE\nSMÍ OPAKOVAT"}, //10
            // nastaveni setup
            {"SOUND", "ZVUK"},
            {"sound volume", "hlasitost"},
            {"LANGUAGE", "JAZYK"},
            {"BUTTONS", "FIGURKY"},
            // help
            {"GAME TASK", "CÍL HRY"},
            {"Is generated random position\nof collors\nYou task is find it\nOn every move\nyou obtain information\n" +
                    "how many colors are\nright color on the\nright place - black\nand just right color\non bad place - white",
                    "Je vygenerována náhodná řada\nbarev. Tvým úkolem je ji uhodnout.\nPomůže Ti nápověda.\n" +
                            "Za uhádnutou barvu i pozici\ndostaneš čený bod.\nKdyž uhádneš jen barvu,\nale ne pozici tak bílý."},
            {"GAME CONTROL", "OVLÁDÁNÍ"},
            {"On top are colors\nfrom witch you can choice\ntap on them will be created button\n on first free position at active row\nwhen row is full no button is created\n" +
                    "Buttons on desk you can freely\nmove by draging.\nDraging button out of play area\nwill detele button\nWhen row is filled\nyou can let evaluate move\nby clicking on button in right\n" +
                    "Then you obtain information\nhow succefully was you attempt\nDouble click on right side\non evaluated rows\nwill copy row to active row\n" +
                    "Double clicking od right\non not evaluated rows\nwill erase all row\nYou can also tap on buttons\nin evaluated rows. This will\ncopy button to active row\nto first free position on left",
                    "V horní části jsou barvy,\nze kterých můžete vybírat.\nŤuknutím na barvu ji zkopírujete\ndo hracího pole na první volnou\n" +
                            "pozici, ale jen dokud řada\nnebude plná. V tu chvíli\nmůžete stisknout tlačítko vpravo\na nechat svůj tah vyhodnotit.\n" +
                            "S figurkou můžete tažením\npo hrací ploše pohybovat.\nKdyž ji přetáhnete mimo zónu\npro umístění, figurka se smaže.\n" +
                            "Pohlepáním na řadu,\nkterá není ještě vyhodnocená,\nji smažete celou.\nPoklepáním na vyhodnocenou řadu\nji zkopírujete do aktivní řady.\n"+
                            "Jednotlivou barvu do aktivní řady\nmůžete zkopírovat i poklepáním na\nbarvu v už vyhodnocených řadách."},
            {"AUTHOR", "AUTOR"},
            //
            {"Yes", "Ano"},
            {"No", "Ne"},
            {"\n  QUIT GAME?  \n", "\n  UKONČIT HRU?  \n"},
            {"\n     SORRY\n   NOT SOLVED   \n  LOOK FOR SOLUTION  \n", "\n  NEVYŠLO TO\n  PODÍVEJ SE NA ZADÁNÍ  \n"},
            {"\n W-I-N \nSOLVED IN ", "\n  A MÁŠ TO \nVYŘEŠENO V "},
            {" MOVES", " TAZÍCH"},
            {"DONATIONWARE", "DONATIONWARE"},
            {"I dont like adds in software\nso no adds are in this game\nIf you like my game\nI'll be glad if you send me\nfor coffee. (by tap on cup)\nThanks for advance. :-)",
                    "Nemám ráda reklamy ve hrách,\na proto nejsou v téhle hře.\nJestli se vám hra líbí,\nbudu ráda, když mi pošlete\nna kávu (ťuknutím na šálek)\n Předem děkuji :-)"},
            {"Open our web pages?", "Otevřít naše\nwebové stránky?"},
            {"\n   THANK YOU   \n", "\n   Děkuji   \n"},
            {"\n      ABANDON\n  AND SHOW SOLUTION?  ", "\n   CHCEŠ TO VZDÁT\n A UKÁZAT ŘEŠENÍ? "},
            {"EVALUATION", "VYHODNOCEŃÍ"}
    };
}
