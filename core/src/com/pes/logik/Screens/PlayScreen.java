package com.pes.logik.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pes.logik.Constants.Languages;
import com.pes.logik.Logik;
import com.pes.logik.Constants.Constants;
import com.pes.logik.Sprites.DeskItem;
import com.pes.logik.Sprites.PlayButton;
import com.pes.logik.Sprites.ResultButton;
import com.pes.logik.Interfaces.Platform;

import java.util.Random;

import static com.pes.logik.Constants.Constants.MAXCOLS;
import static com.pes.logik.Constants.Constants.MAXROWS;
import static com.pes.logik.Constants.Languages.LANG_CZECH;
import static com.pes.logik.Constants.Languages.strings;
import static com.pes.logik.Global.Game.actualRow;
import static com.pes.logik.Global.Game.begCol;
import static com.pes.logik.Global.Game.canRepeat;
import static com.pes.logik.Global.Game.element;
import static com.pes.logik.Global.Game.hSpace;
import static com.pes.logik.Global.Game.language;
import static com.pes.logik.Global.Game.learnMode;
import static com.pes.logik.Global.Game.numColors;
import static com.pes.logik.Global.Game.numCols;
import static com.pes.logik.Global.Game.sButtons;
import static com.pes.logik.Global.Game.sDesk;
import static com.pes.logik.Global.Game.sResults;
import static com.pes.logik.Global.Game.soundEnabled;
import static com.pes.logik.Global.Game.soundVolume;
import static com.pes.logik.Global.Game.vSpace;


public class PlayScreen implements Screen, InputProcessor {
    // GLOBAL VARIABLES
    private PlayScreen screen;

    private FitViewport fitViewport;
    private int virtualWidth, virtualHeight;

    private Logik game;
    private  Skin skin;
    public  Sound soundTap;
    private Stage stage = null;

    private int choiceColors[] = new int[12];
    public PlayButton desk[][] = new PlayButton[12][6];  //[row][col]

    private ResultButton result[] = new ResultButton[12];
    private int problem[] = new int[6];
    private boolean newGame;
    private boolean mainScreen = true;

    public TextureAtlas textureAtlas;

    private SpriteBatch spriteBatch;
    private Image imgBackground;
    // DestItems
    private Array<DeskItem> deskItems;
    private Array<DeskItem> headerItems;
    private Array<PlayButton> colorButtons;
    //
    private boolean canPlay = false;

    private InputMultiplexer im;

    public PlayScreen(Logik pGame) {
        game = pGame;
        screen = this;

        // init graphic
        initialDeskCompute();
        // init graphic
        OrthographicCamera camera = new OrthographicCamera(virtualWidth, virtualHeight);
        camera.setToOrtho(false, virtualWidth, virtualHeight);
        fitViewport = new FitViewport(virtualWidth, virtualHeight, camera);
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(camera.combined); fitViewport.apply();
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        // prepare assets
        prepareAssets();
        // set input
        Gdx.input.setCatchBackKey(true);
        im = new InputMultiplexer();
        im.addProcessor(this);
        //
        newGame = true;
        actualRow = 0;
        deskHeader();  // grafika pozadi hraciho pole
        buttonsResultPrepare();
        StageMain();
    }

    private void initialDeskCompute(){
        float aspectRatio = (float) Gdx.graphics.getHeight()/Gdx.graphics.getWidth();
        virtualWidth = Math.round(8.5f*element);
        virtualHeight = Math.round(aspectRatio * virtualWidth);
        if (virtualHeight < (15f*element)) {
            element = virtualHeight/15;
            hSpace = element/2;
        }
        vSpace = (virtualHeight - 14*element)/2;
    }

    private void prepareAssets(){
        skin = game.assetManager.get(Constants.S_SKIN);
        soundTap = game.assetManager.get(Constants.S_SOUNDTAP, Sound.class);
        // TEXTURE ATLAS
        textureAtlas = game.assetManager.get(Constants.S_ATLAS, TextureAtlas.class);

        // basic background
        background();
        //
        deskItems = new Array<DeskItem>();
        headerItems = new Array<DeskItem>();
        colorButtons = new Array<PlayButton>();

    }

    private void background(){
        imgBackground = new Image(textureAtlas.findRegion(Constants.S_BCKGRMAIN+sDesk));
        imgBackground.setSize(virtualWidth, virtualHeight);
        imgBackground.setPosition(0, 0);
        imgBackground.setDebug(false);
    }

    private void desk(int cols, int rows, int abegCol) { // jen cista hraci deska bez pindiku  s vymezenim rozmeru
        if (!deskItems.isEmpty()) deskItems.clear();
        int y = vSpace;
        for (int i = 0; i < rows; i++) {
            int x = hSpace;
            for (int j = 0;j<abegCol; j++) {
                deskItems.add(new DeskItem(textureAtlas.findRegion("deskInactive"+sDesk),x,y,element,element));
                x += element;
            }
            for (int j = 0; j < cols; j++) {
                deskItems.add(new DeskItem(textureAtlas.findRegion("deskActive"+sDesk),x,y,element,element));
                x += element;
            }
            for (int j = cols+begCol; j < MAXCOLS; j++) {
                deskItems.add(new DeskItem(textureAtlas.findRegion("deskInactive"+sDesk),x,y,element,element));
                x += element;
            }
            y += element;
        }
    }

    private void deskHeader(){
        if (!headerItems.isEmpty()) headerItems.clear();
        int y = vSpace + 12*element;
        for (int i=0;i<2;i++) {
            int x = hSpace;
            for (int j = 0; j< 6; j++) {
                headerItems.add(new DeskItem(textureAtlas.findRegion("deskHeader"+sDesk),x,y,element,element));
                x += element;
            }
            y+=element;
        }
    }

    // Todo da se sloucit s funkci prepare CleanResult kdyz se prida parametr
    private void buttonsResultPrepare() {
        int x = hSpace + 6* element;
        int y = vSpace;
        for (int i = 0; i < 12; i++) {
            final ResultButton resultButton = new ResultButton(screen,x,y,element, i, false);

            resultButton.addListener(new ActorGestureListener() {
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    if (canPlay && count == 1) // 1 kliknuti na aktivni radu evaluate
                        if (resultButton.getRow() == actualRow) {
                            if (resultButton.go()) {
                                if (soundEnabled) soundTap.play(soundVolume);
                                evaluate();
                            }
                            else {
                                dialogShowSolution();
                            }
                        }
                    if (canPlay && count == 2) { // 2 kliknuti na odemcenou radu vymazani
                        if (!resultButton.isLocked()) { //smazat radu
                            int row = resultButton.getRow();
                            for (int i = 0;i<numCols;i++) {
                                if (desk[row][i]!=null && !desk[row][i].isLocked()) desk[row][i].remove();
                                desk[row][i] = null;
                            }
                            if (row==actualRow) result[actualRow].disallow();
                        }
                        else { // 2 kliknuti na zamcenou radu = zkopirovat zamcenou radu do aktivni
                            int row = resultButton.getRow();
                            boolean tempSoundEnabled = soundEnabled;
                            soundEnabled = false;
                            for (int i=0;i<numCols;i++){
                                int posY = vSpace + actualRow*element;
                                int posX = hSpace+(i+begCol)*element;
                                PlayButton newButton = buttonPlayCreate(desk[row][i].getButtonColor(), posX, posY, false);
                                newButton.put(posX,posY);
                            }
                            soundEnabled = tempSoundEnabled;
                            if (soundEnabled) soundTap.play(soundVolume);
                        }
                    }
                }
            });
            result[i] = resultButton;
            y += element;
        }
    }


    private void buttonsResult() {
        for (int i = 0; i < 12; i++) {
            stage.addActor(result[i]); //Todo
        }
    }


    private void buttonsSelectColor(){
        if (!colorButtons.isEmpty()) colorButtons.clear();
        int restColors=numColors;
        if (numColors > numCols) { //2 rady
            int posY = vSpace + 12*element;
            int posX = hSpace+begCol*element;
            for (int i=numCols; i<numColors; i++) {
                colorButtons.add(buttonPlayCreate(choiceColors[i], posX, posY, true));
                posX += element;
            }
            restColors = numCols;
        }
        int posY = vSpace + 13*element;
        int posX = hSpace+begCol*element;

        for (int i=0; i<restColors; i++) {
            colorButtons.add(buttonPlayCreate(choiceColors[i], posX, posY, true));
            posX += element;
        }

    }

    private PlayButton buttonPlayCreate(int acolor, final float aposX, final float aposY, boolean alocked){
        final PlayButton playButton = new PlayButton(screen, acolor,aposX,aposY,element,alocked);
        playButton.addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                if (canPlay && !playButton.isLocked()) {
                    playButton.get();
                    event.getListenerActor().moveBy(deltaX, deltaY);
                }
            }
            // když zvednu prst z buttonu
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (canPlay && !playButton.isLocked()) {
                    playButton.put(event.getListenerActor().getX(), event.getListenerActor().getY());
                }
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (canPlay && playButton.isLocked()) {
//                    if (soundEnabled) soundTap.play(soundVolume);
                    boolean full = true;
                    int freeCol, testCol;
                    testCol = 0; freeCol = 0;
                    while (testCol<numCols&&full) {
                        if (desk[actualRow][testCol]==null) {
                            full = false;
                            freeCol = testCol;
                        }
                        testCol++;
                    }
                    if (!full) {
                        PlayButton newButton =  buttonPlayCreate(playButton.getButtonColor(), hSpace+(begCol+freeCol)*element, vSpace+actualRow*element,false);
                        newButton.put(hSpace+(begCol+freeCol)*element, vSpace+actualRow*element);
                    }
                }
            }
        });
        stage.addActor(playButton);
        return playButton;
    }

    private void showProblem(int arow){
        int posY = vSpace + arow*element;
        int posX = hSpace+begCol*element;
        for (int i=0; i<numCols; i++) {
            buttonPlayCreate(problem[i], posX, posY, true);
            posX += element;
        }
    }

    public void controlActualRow(){
        boolean full = true;
        for (int i = 0; i<numCols; i++) {
            if (desk[actualRow][i]==null) full = false;
        }
        if (full) result[actualRow].allow();
        else result[actualRow].disallow();
    }

    private void generateChoiceColors(int anumColors){
        int actualPos = 0; int randomColor=0;
        while (actualPos < anumColors) {
            boolean generated = false;
            while (!generated) {
                randomColor = generateColor(13); //ve skinu je 14 barev
                    int i = 0;
                    boolean canBe = true;
                    while (i < actualPos && canBe) {
                        if (choiceColors[i] == randomColor) canBe = false;
                        i++;
                    }
                    if (canBe) generated = true;
            }
            choiceColors[actualPos]=randomColor;
            actualPos++;
        }
    }

    private void generateProblem(){
        int actualPos = 0; int randomColor=0;
        while (actualPos < numCols) {
            boolean generated = false;
            while (!generated) {
                randomColor = generateColor(numColors);
                if (canRepeat) {
                    generated = true;
                }
                else {
                    int i = 0;
                    boolean canBe = true;
                    while ((i < actualPos) && canBe) {
                        if (problem[i] == randomColor) canBe = false;
                        i++;
                    }
                    if (canBe) generated = true;
                }
            }
            problem[actualPos]=randomColor;
            actualPos++;
        }
        for (int i=0; i<numCols; i++){
            problem[i]=choiceColors[problem[i]];
        }
    }

    private int generateColor(int amaxColor) {
        Random random = new Random();
        return random.nextInt(amaxColor);
    }

    private void StagePlay(){
        newStage();
        mainScreen = false;
        canPlay = true;
        if (newGame) {
            if (numCols==3 || numCols == 4) begCol = 1;
            else begCol = 0;
            // generate choceColors
            generateChoiceColors(numColors);
            // prepare problem
            generateProblem();
            // vymazani desky
            for (int j=0; j<12; j++)  for (int i=0;i<6; i++)
                if (desk[j][i]!=null) {
                    desk[j][i].remove();
                    desk[j][i] = null;
                }
            // vynulovani vysledku
//            for (int j=0; j<12; j++) if(result[j]!=null) result[j].setInactive();
            for (int j=0; j<12; j++) if(result[j]!=null) result[j].clean();
            //
            actualRow = 0;
        }
        desk(numCols, MAXROWS,begCol);
        buttonsResult();
        buttonsSelectColor();
        buttonsPlay();
        if (learnMode) showProblem(12);
        result[actualRow].setActive(); controlActualRow();
        newGame = false;

        // Return button
        buttonReturn();
    }

    private void buttonsPlay(){
        for (int j=0;j<MAXROWS;j++)
            for (int i=0;i<numCols;i++) {
                if (desk[j][i]!=null) stage.addActor(desk[j][i]);
            }
    }

    private void evaluate(){
        // lock row
        for (int i = 0; i<numCols; i++) desk[actualRow][i].lock();
        result[actualRow].lock();
        // zjisti, jestli bylo vyreseno
        boolean solved = false;
        int numBlacks = 0;
        int numWhites = 0;
        boolean evaluatedZad[] = new boolean[numCols];
        boolean evaluatedOdp[] = new boolean[numCols];
        // spocitej CERNE
        for (int i=0; i<numCols; i++) {
            if (problem[i]==desk[actualRow][i].getButtonColor()) {
                numBlacks++;
                evaluatedZad[i] = true;
                evaluatedOdp[i] = true;
            }
            else {
                evaluatedZad[i]=false;
                evaluatedOdp[i]=false;
            }
        }
        if (numBlacks<numCols) {
            // spocitek BILE
            for (int i=0; i<numCols; i++) {

                if (!evaluatedOdp[i]) {
                    boolean looking = true;
                    int j = 0;
                    while (j<numCols &&looking) {
                        if (!evaluatedZad[j]){
                            if (desk[actualRow][i].getButtonColor()==problem[j]) {
                                evaluatedZad[j] = true;
                                numWhites++;
                                looking = false;
                            }
                        }
                        j++;
                    }
                }
            }
        }
        else solved = true;
        result[actualRow].updateState(numBlacks, numWhites);

        if (solved){ //
            newGame = true;
            dialogSolved();

        }
        else {
            actualRow++;
            if (actualRow==MAXROWS) {
                dialogNotSolved();
                for (PlayButton playButton: colorButtons) playButton.remove();
                showProblem(13);
                newGame = true;
                canPlay = false;
            }
            else {
                result[actualRow].setActive();
            }
        }
    }

    private void buttonReturn(){
        Image btnReturn = new Image(screen.textureAtlas.findRegion("btnBack"+sDesk));
        btnReturn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (soundEnabled) soundTap.play(soundVolume);
                StageMain();
            }
        });
        btnReturn.setPosition(screen.virtualWidth-1.5f*element, screen.virtualHeight-2f*element);
        btnReturn.setSize(0.75f*element,0.75f*element);
        stage.addActor(btnReturn);
    }

    private void StageMain(){
        newStage();
        desk(MAXCOLS,MAXROWS,0);
        mainScreen = true;

        // Logik
//        Label labelLogik = setLabelToStage(strings[Languages.ID_PLAY][language], hSpace, vSpace + 10*element, 6*element, element, "title2_"+sDesk,1.5f,1.5f);
        Label labelLogik = setLabelToStage("LOGIK", hSpace, vSpace + 11*element+vSpace+vSpace, 6*element, 2*element, "title_"+sDesk,2,3);
        labelLogik.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (soundEnabled) soundTap.play(soundVolume);
                StagePlay();
            }
        });
        // NewGame
        Label labelPlay = setLabelToStage(strings[Languages.ID_PLAY][language], hSpace, vSpace + 10*element, 6*element, element, "title2_"+sDesk,1.5f,1.5f);
        labelPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (soundEnabled) soundTap.play(soundVolume);
                newGame = true;
                StagePlay();
            }
        });
        // Setup Desk
        Label labelSetup = setLabelToStage(strings[Languages.ID_DESK][language], hSpace, vSpace + 8*element, 6*element, element, "title_"+sDesk,1.5f,1.5f);
        labelSetup.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (soundEnabled) soundTap.play(soundVolume);
                StageSetupGame();
            }
        });
        // Setup Game
        Label labelSetupOther = setLabelToStage(strings[Languages.ID_SETUP][language], hSpace, vSpace + 6*element, 6*element, element, "title_"+sDesk,1.5f,1.5f);
        labelSetupOther.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (soundEnabled) soundTap.play(soundVolume);
                StageSetup();
            }
        });
        // Help
        Label labelHelp = setLabelToStage(strings[Languages.ID_HELP][language], hSpace, vSpace + 4*element, 6*element, element, "title_"+sDesk,1.5f,1.5f);
        labelHelp.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (soundEnabled) soundTap.play(soundVolume);
                StageHelp();
            }
        });
        // Quit
        Image btnQuit = new Image(screen.textureAtlas.findRegion("btnQuit"+sDesk));
        btnQuit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialogSureQuit();
            }
        });
        btnQuit.setPosition(screen.virtualWidth-1.5f*element, screen.virtualHeight-2f*element);
        btnQuit.setSize(0.75f*element,0.75f*element);
        stage.addActor(btnQuit);
        // cup of coffee
        Image buyCoffee = new Image(textureAtlas.findRegion("cupOfCoffee"));
        buyCoffee.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (soundEnabled) soundTap.play(soundVolume);
                StageDonationware();
            };
        });
        buyCoffee.setPosition(screen.virtualWidth-1.7f*element, 1f*element);
        buyCoffee.setSize(element, element);
        stage.addActor(buyCoffee);
    }

    private void StageHelp(){
        newStage();
        desk(0, MAXROWS,0);
        mainScreen = false;
        // help label
        setTitleLabelToStage(strings[Languages.ID_HELP][language], hSpace, vSpace + 11*element+vSpace+vSpace, 6*element, 2*element, "title_"+sDesk);
        // help table
        Table table = prepareTable(7.5f);

        table.left().top();
        table.add(new Label("", skin));table.row();

        table.add(new Label(strings[Languages.ID_GAMETASK][language], skin,"title2_"+sDesk)).left();table.row();
        table.add(new Label(strings[Languages.ID_GAMETASKTEXT][language], skin, "default_"+sDesk)).left();table.row();
        table.add(new Label("", skin));table.row();

        table.add(new Label(strings[Languages.ID_GAMECONTROL][language], skin,"title2_"+sDesk)).left();table.row();
        table.add(new Label(strings[Languages.ID_GAMECONTROLTEXT][language], skin, "default_"+sDesk)).left();table.row();
        table.add(new Label("", skin)).left();table.row();
        // Return button
        buttonReturn();
    }

    private void StageDonationware(){
        newStage();
        desk(0,MAXROWS,0);
        mainScreen = false;
        // donationware label
        setTitleLabelToStage("LOGIK", hSpace, vSpace + 11*element+vSpace+vSpace, 6*element, 2*element, "title_"+sDesk);
        // table
        Table table = prepareTable(6f);
        table.left().top();
        table.add(new Label("", skin));table.row();

        // AUTHOR
        table.add(new Label(strings[Languages.ID_AUTHOR][language], skin,"title2_"+sDesk)).left();table.row();
        Image imageIcon =new Image(textureAtlas.findRegion("icon"));
        imageIcon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (soundEnabled) soundTap.play(soundVolume);
                goToUrl(strings[Languages.ID_CS_OPENHOMEPAGE][language], "http://www.pesentertainment.com");
            }
        });
        table.add(imageIcon).size(60,60).left(); table.row();
        table.add(new Label("PeS entertainment", skin, "default_"+sDesk)).left();table.row();
        table.add(new Label("2019", skin, "default_"+sDesk)).left();table.row();
        table.add(new Label("", skin)).left();table.row();

        // DONATIONWARE
        table.add(new Label(strings[Languages.ID_DONATIONWARE][language], skin,"title2_"+sDesk)).left();table.row();
//        table.add(new Label("", skin)).row();
        // cup of coffee
        Image buyCoffee = new Image(textureAtlas.findRegion("cupOfCoffee"));
        buyCoffee.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (soundEnabled) soundTap.play(soundVolume);
//                dialogThankYou();
                game.platform.buyCoffee();
            };
        });
        table.add(buyCoffee).left().row();
        table.add(new Label("", skin)).row();
        // donationware text
        table.add(new Label(strings[Languages.ID_DONATIONWARETEXT][language], skin, "default_"+sDesk)).left();table.row();
//        table.add(new Label("", skin)).left();table.row();
        // Return button
        buttonReturn();

    }

    private void StageSetupGame(){
        newStage();
        desk(MAXCOLS,MAXROWS,0);
        mainScreen = false;
        // setup label
        setTitleLabelToStage(strings[Languages.ID_DESK][language], hSpace, vSpace + 11*element+vSpace+vSpace, 6*element, 2*element, "title_"+sDesk);
        //
        Table table = prepareTable(6);

        table.add(new Label("", skin));table.row();
        table.add(new Label("", skin));table.row();
        // predefine COLORSLIDE due to COLUMNS
        final Slider colorsSlider = new Slider( numCols, 2*numCols, 1,false, skin );

        // COLUMNS
        final Label numColsLabel = new Label(strings[Languages.ID_COLUMNS][language], skin,"title_"+sDesk);
        table.add(numColsLabel).row();
        final Label numColsValue = new Label(numCols.toString(), skin,"title2_"+sDesk);
        numColsValue.setFontScale(1.5f);
        table.add(numColsValue).row();

        // COLUMNS
        final Slider columnsSlider = new Slider( 3, 6, 1,false, skin );
        columnsSlider.setValue(numCols);
        columnsSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                numCols = Math.round(columnsSlider.getValue());
                numColsValue.setText(numCols.toString());
                colorsSlider.setRange(numCols, 2*numCols);
                if (colorsSlider.getValue()>2*numCols) colorsSlider.setValue(2*numCols);
                if (colorsSlider.getValue()<numCols) colorsSlider.setValue(numCols);
                newGame = true;
                return false;
            }
        });
        table.add(columnsSlider).row();
        table.add(new Label("", skin));table.row();

        // COLORS
        final Label numColorsLabel = new Label(strings[Languages.ID_COLORS][language], skin,"title_"+sDesk);
        table.add(numColorsLabel).row();
        final Label numColorsValue = new Label(numColors.toString(), skin,"title2_"+sDesk);
        numColorsValue.setFontScale(1.5f);
        table.add(numColorsValue).row();

        table.add(new Label("", skin));table.row();
        // COLUMNS
        colorsSlider.setValue(numColors);
        colorsSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                numColors = Math.round(colorsSlider.getValue());
                numColorsValue.setText(numColors.toString());
                newGame = true;
                return false;
            }
        });
        table.add(colorsSlider).row();
        table.add(new Label("", skin));table.row();

        // COLORS CAN REPEAT
        Label canRepeatLabel = new Label(strings[Languages.ID_CANREPEAT][language], skin,"title_"+sDesk);
        table.add(canRepeatLabel).row();
        final CheckBox canRepeatBox = new CheckBox(null, skin);
        canRepeatBox.setChecked(canRepeat);
        canRepeatBox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (canRepeat != canRepeatBox.isChecked()) {
                    if (soundEnabled) soundTap.play(soundVolume);
                    canRepeat = canRepeatBox.isChecked();
                    newGame = true;

                }
                return true;
            }
        });
        table.add(canRepeatBox).row();
        table.add(new Label("", skin));table.row();

        // LEARN MODE //Todo pridat mozna pozdeji
/*
        Label learnModeLabel = new Label("LEARN MODE", skin,"title_"+atlasDesk);
        table.add(learnModeLabel).row();
        Label learnModeLabel2 = new Label("(PROBLEM VISIBLE)", skin);
        table.add(learnModeLabel2).row();
        final CheckBox learnModeBox = new CheckBox(null, skin);
        learnModeBox.setChecked(canRepeat);
        learnModeBox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                learnMode = learnModeBox.isChecked();
                if (learnMode) {
                    //Todo omezit nastaveni Cols a Colors
                }
                newGame = true;
                return true;
            }
        });
        table.add(learnModeBox).row();
        table.add(new Label("", skin));table.row();
*/
        // Return button
        buttonReturn();

    }

    private void StageSetup(){
        newStage();
        desk(MAXCOLS,MAXROWS,0);
        mainScreen = false;
        // setup label
//        setTitleLabelToStage("SETUP", hSpace, vSpace + 12*element+vSpace, 6*element, 2*element, skin);
        setTitleLabelToStage(strings[Languages.ID_SETUP][language], hSpace, vSpace + 11*element+vSpace+vSpace, 6*element, 2*element, "title_"+sDesk);

        //
        Table table = prepareTable(6f);

        table.add(new Label("", skin));table.row();
        // SOUND LABEL
        Label soundOnOffLabel = new Label(strings[Languages.ID_SOUND][language], skin,"title_"+sDesk);
        table.add(soundOnOffLabel);table.row();
        // SOUND CHECK BOX
        final CheckBox soundCheckbox = new CheckBox(null, skin);
        soundCheckbox.setChecked( soundEnabled );
        soundCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (soundEnabled!= soundCheckbox.isChecked()){
                    if (!soundEnabled) soundTap.play(soundVolume);
                    soundEnabled = soundCheckbox.isChecked();
                }
                return false;
            }
        });
        table.add(soundCheckbox).center();table.row();
        // SOUND VOLUME LABEL
        Label volumeSoundLabel = new Label(strings[Languages.ID_SOUNDVOLUME][language], skin, "default_"+sDesk);
        table.add(volumeSoundLabel);table.row();
        // SOUND VOLUME SLIDER
        final Slider volumeSoundSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        volumeSoundSlider.setValue(soundVolume);
        volumeSoundSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (!volumeSoundSlider.isDragging()) {
                    if (soundVolume!=volumeSoundSlider.getValue()) {
                        soundVolume = (volumeSoundSlider.getValue());
                        if (soundEnabled) soundTap.play(soundVolume);
                    }
                }
                return false;
            }
        });
        table.add(volumeSoundSlider).center();table.row();
        table.add(new Label("", skin));table.row();

        // LANGUAGE
        Label languageLabel = new Label(strings[Languages.ID_LANGUAGE][language], skin,"title_"+sDesk);
        table.add(languageLabel);table.row();
        Table languageTable = new Table();
        final ButtonGroup languageChoiceGroup = new ButtonGroup<CheckBox>();
        CheckBox languageEnglish = new CheckBox(strings[Languages.ID_LANGUAGES][Languages.LANG_ENGLISH], skin); languageEnglish.setName(strings[Languages.ID_LANGUAGES][Languages.LANG_ENGLISH]);
        CheckBox languageCzech = new CheckBox(strings[Languages.ID_LANGUAGES][Languages.LANG_CZECH], skin); languageCzech.setName(strings[Languages.ID_LANGUAGES][Languages.LANG_CZECH]);
        languageChoiceGroup.add(languageEnglish);
        languageChoiceGroup.add(languageCzech);
        languageChoiceGroup.setMaxCheckCount(1);
        languageChoiceGroup.setMinCheckCount(1);
        languageChoiceGroup.setUncheckLast(true);
        languageChoiceGroup.setChecked(strings[Languages.ID_LANGUAGES][language]);
        languageTable.add(languageEnglish).center().padBottom(15).row();
        languageTable.add(languageCzech).center().padBottom(15).row();
        languageTable.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (languageChoiceGroup.getChecked()!=null){
                    String choiseL = languageChoiceGroup.getChecked().getName();
                    if (!language.equals(choiseL)) {
                        if(soundEnabled) soundTap.play(soundVolume);
                        if (choiseL.equals(strings[Languages.ID_LANGUAGES][Languages.LANG_CZECH])) language = Languages.LANG_CZECH;
                        else language = Languages.LANG_ENGLISH;
                        StageSetup();
                    }
                }
            }
        });
        table.add(languageTable);table.row();
        table.add(new Label("", skin));table.row();

        // DESK
        Label skinDeskLabel = new Label(strings[Languages.ID_DESK][language], skin,"title_"+sDesk);
        table.add(skinDeskLabel);table.row();
        table.add(new Label("", skin));table.row();
        // DESK A
        Image imgDesk1 = new Image(textureAtlas.findRegion("deskA"));
        imgDesk1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!sDesk.equals("A")) changeDeskSkin("A");
            }
        });
        table.add(imgDesk1).size(64,64).row();
        table.add(new Label("", skin));table.row();
        // DESK B
        Image imgDesk2 = new Image(textureAtlas.findRegion("deskB"));
        imgDesk2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!sDesk.equals("B")) changeDeskSkin("B");
            }
        });
        table.add(imgDesk2).size(64,64).row();
        table.add(new Label("", skin));table.row();


        // BUTTONS
        Label skinButtonsLabel = new Label(strings[Languages.ID_BUTTONS][language], skin,"title_"+sDesk);
        table.add(skinButtonsLabel);table.row();
        table.add(new Label("", skin));table.row();
        // BUTTONS A
        Image imgButtonsA = new Image(textureAtlas.findRegion("btnA5"));
        // BUTTONS B
        Image imgButtonsB = new Image(textureAtlas.findRegion("btnB2"));
        // BUTTONS C
        Image imgButtonsC = new Image(textureAtlas.findRegion("btnC9"));
        // BUTTONS
        Table buttonsTable = new Table();
        final ButtonGroup buttonsChoiceGroup = new ButtonGroup<CheckBox>();
        CheckBox buttonsA = new CheckBox("A", skin); buttonsA.setName("A");
        CheckBox buttonsB = new CheckBox("B", skin); buttonsB.setName("B");
        CheckBox buttonsC = new CheckBox("C", skin); buttonsC.setName("C");
        buttonsChoiceGroup.add(buttonsA);
        buttonsChoiceGroup.add(buttonsB);
        buttonsChoiceGroup.add(buttonsC);
        buttonsChoiceGroup.setMaxCheckCount(1);
        buttonsChoiceGroup.setMinCheckCount(1);
        buttonsChoiceGroup.setUncheckLast(true);
        if (sButtons.equals("A")) buttonsChoiceGroup.setChecked("A");
        else if (sButtons.equals("B")) buttonsChoiceGroup.setChecked("B");
        else buttonsChoiceGroup.setChecked("C");
        buttonsTable.add(buttonsA);
        buttonsTable.add(imgButtonsA).size(64,64).row();
        buttonsTable.add(buttonsB);
        buttonsTable.add(imgButtonsB).size(64,64).row();
        buttonsTable.add(buttonsC);
        buttonsTable.add(imgButtonsC).size(64,64).row();
        buttonsTable.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (buttonsChoiceGroup.getChecked()!=null){
                  String choise = buttonsChoiceGroup.getChecked().getName();
                    if (!sButtons.equals(choise)) {
                        changeButtons(choise);
                    }
                }
            }
        });
        table.add(buttonsTable).row();

        // RESULT BUTTONS
        Label skinRButtonsLabel = new Label(strings[Languages.ID_EVALUATION][language], skin,"title_"+sDesk);
        table.add(skinRButtonsLabel);table.row();
        table.add(new Label("", skin));table.row();
        // BUTTONS A
        Image imgRButtonsA = new Image(textureAtlas.findRegion("A42"));
        // BUTTONS B
        Image imgRButtonsB = new Image(textureAtlas.findRegion("B42"));
        // BUTTONS
        Table rbuttonsTable = new Table();
        final ButtonGroup rbuttonsChoiceGroup = new ButtonGroup<CheckBox>();
        CheckBox rbuttonsA = new CheckBox("A", skin); rbuttonsA.setName("A");
        CheckBox rbuttonsB = new CheckBox("B", skin); rbuttonsB.setName("B");
        rbuttonsChoiceGroup.add(rbuttonsA);
        rbuttonsChoiceGroup.add(rbuttonsB);
        rbuttonsChoiceGroup.setMaxCheckCount(1);
        rbuttonsChoiceGroup.setMinCheckCount(1);
        rbuttonsChoiceGroup.setUncheckLast(true);
        if (sResults.equals("A")) rbuttonsChoiceGroup.setChecked("A");
        else rbuttonsChoiceGroup.setChecked("B");
        rbuttonsTable.add(rbuttonsA);
        rbuttonsTable.add(imgRButtonsA).size(72,48).row();
        rbuttonsTable.add(rbuttonsB);
        rbuttonsTable.add(imgRButtonsB).size(72,48).row();
        rbuttonsTable.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (rbuttonsChoiceGroup.getChecked()!=null){
                    String rchoise = rbuttonsChoiceGroup.getChecked().getName();
                    if (!sResults.equals(rchoise)) {
                        changeRButtons(rchoise);
                    }
                }
            }
        });
        table.add(rbuttonsTable).row();

        // Return button
        buttonReturn();
    }

    private void changeRButtons(String aRButtons) {
        if (soundEnabled) game.assetManager.get(Constants.S_SOUNDTAP, Sound.class).play(soundVolume);
        sResults = aRButtons;
        for (int i = 0; i < MAXROWS; i++) if (result[i] != null) result[i].updateTexture();
    }

    private void changeButtons(String aButtons){
        if (soundEnabled) game.assetManager.get(Constants.S_SOUNDTAP, Sound.class).play(soundVolume);
        sButtons = aButtons;
        for (int j=0; j<MAXCOLS; j++)  //change buttons graphic
            for (int i=0;i<numCols;i++)
                if (desk[j][i]!=null) desk[j][i].updateTexture();
    }

    private void changeDeskSkin(String aDesk){
        if (soundEnabled) game.assetManager.get(Constants.S_SOUNDTAP, Sound.class).play(soundVolume);
        sDesk = aDesk;
        background();
        desk(MAXCOLS,MAXROWS,0);
        deskHeader();
        StageSetup();
    }

//"title_"+sDesk
    private Label setLabelToStage(String aText, int aposX, int aposY, int awidth, int aheight, String astyleName,float afontScaleX, float afontScaleY) {
        Label label = new Label(aText, skin, astyleName);
        label.setFontScale(afontScaleX, afontScaleY);
        label.setPosition(aposX,aposY);
        label.setSize(awidth, aheight);
        label.setAlignment(Align.center, Align.bottom);
        stage.addActor(label);
        return label;
    }

    private Label setTitleLabelToStage(String aText, int aposX, int aposY, int awidth, int aheight, String astyleName) {
        Label titleLabel = setLabelToStage(aText, aposX, aposY, awidth, aheight, astyleName,2,3);
        titleLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (soundEnabled) soundTap.play(soundVolume);
                StageMain();
            }
        });
        return titleLabel;
    }

    private void newStage(){
        if (stage!=null) {
            im.removeProcessor(stage);
            stage.dispose();
        }
        stage = new Stage(fitViewport);
        im.addProcessor(stage);
    }

    private Table prepareTable(float awidth){
        Table table = new Table();
        ScrollPane scroll = new ScrollPane(table, skin);
        stage.addActor(scroll);
        table.center().top();
        scroll.setPosition(hSpace, vSpace);
        scroll.setSize(awidth*element, 12*element);
        scroll.setScrollingDisabled(true, false);
        scroll.setTouchable(Touchable.enabled);
        scroll.setOverscroll(false, false);
        scroll.setFlickScroll(true);
        scroll.setScrollbarsVisible(false);
        scroll.setupFadeScrollBars(0,0);
        scroll.updateVisualScroll();
        return table;
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(im);
    }

    public void render(float delta) {
        // desk
        spriteBatch.begin();
        // background picture
        imgBackground.draw(spriteBatch,1f);
        // draw desk
        for (Sprite deskItem: deskItems) deskItem.draw(spriteBatch, 0.5f);
        for (Sprite headerItem: headerItems) headerItem.draw(spriteBatch, 0.5f);
        spriteBatch.end();
        // stage
        stage.act();
//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }


    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK) {
            if (mainScreen) {
                if (soundEnabled) soundTap.play(soundVolume);
                dialogSureQuit();
                return true;
            }
            else {
                StageMain();
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character == Input.Keys.Q) {
//            game.setScreen(new LevelsScreen(game));
            dispose();
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void dialogNotSolved(){
        Dialog dialog = new Dialog(" ", skin) {
            public void result(Object obj) {
                if (soundEnabled) soundTap.play(soundVolume);
                if ((Boolean) obj) {
                    remove();
                }
            }
        }.text(strings[Languages.ID_NOTSOLVED][language]).button("OK", true);
        if (soundEnabled) {
            game.assetManager.get(Constants.S_SOUNDDIALOG, Sound.class).play(soundVolume);
        }
        dialog.setColor(Color.YELLOW);
        dialog.show(stage) ;
    }

    private void dialogSolved(){
        actualRow++;
        Dialog dialog = new Dialog(" ", skin) {
            public void result(Object obj) {
                if (soundEnabled) soundTap.play(soundVolume);
                if ((Boolean) obj) {
                    remove();
                    StageMain();
                }
            }
        }.text(strings[Languages.ID_SOLVED][language]+actualRow.toString()+strings[Languages.ID_MOVES][language]).button("OK", true);
        if (soundEnabled) {
            game.assetManager.get(Constants.S_SOUNDWIN, Sound.class).play(soundVolume);
        }
        dialog.setColor(Color.YELLOW);
        dialog.show(stage) ;
    }

    private void dialogThankYou(){
        Dialog dialog = new Dialog(" ", skin) {
            public void result(Object obj) {
                if (soundEnabled) soundTap.play(soundVolume);
                if ((Boolean) obj) {
                    remove();
                    StageMain();
                }
            }
        }.text(strings[Languages.ID_THANKYOU][language]).button("OK", true);
        dialog.setColor(Color.YELLOW);
        dialog.show(stage) ;
    }



    private void dialogPlayNewGame(){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.RED;
        Dialog dialog = new Dialog(" ", skin) {
            public void result(Object obj) {
                if (soundEnabled) soundTap.play(soundVolume);
                if ((Boolean) obj) {
                    newGame = true;
                    StagePlay();
                    remove();
                }
                else {
                    StagePlay();
                    remove();
                }
            }
        }.text(strings[Languages.ID_YOUHAVEACTIVE][language], labelStyle).button(strings[Languages.ID_CONTINUE][language],false).button(strings[Languages.ID_NEWGAME][language],true);
        if (soundEnabled) {
            game.assetManager.get(Constants.S_SOUNDDIALOG, Sound.class).play(soundVolume);
        }
        dialog.show(stage) ;
    }

    private void dialogShowSolution(){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.RED;
        Dialog dialog = new Dialog(" ", skin) {
            public void result(Object obj) {
                if (soundEnabled) soundTap.play(soundVolume);
                if ((Boolean) obj) {
                    for (PlayButton playButton: colorButtons) playButton.remove();
                    showProblem(13);
                    canPlay = false;
                    newGame = true;
                }
                remove();
            }
        }.text(strings[Languages.ID_WANTSHOWSOLUTION][language], labelStyle).button(strings[Languages.ID_YES][language], true).button(strings[Languages.ID_NO][language], false);
        if (soundEnabled) {
            game.assetManager.get(Constants.S_SOUNDDIALOG, Sound.class).play(soundVolume);
        }
        dialog.show(stage) ;
    }


    private void dialogSureQuit(){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.RED;
        Dialog dialog = new Dialog(" ", skin) {
            public void result(Object obj) {
                if (soundEnabled) soundTap.play(soundVolume);
                if ((Boolean) obj) {
                    game.save();
                    Gdx.app.exit();
                }
                else {
                    remove();
                }
            }
        }.text(strings[Languages.ID_QUITGAME][language], labelStyle).button(strings[Languages.ID_YES][language], true).button(strings[Languages.ID_NO][language], false);
        if (soundEnabled) {
            game.assetManager.get(Constants.S_SOUNDDIALOG, Sound.class).play(soundVolume);
        }
        dialog.show(stage) ;
    }

    private void goToUrl(String infoText, final String aUrl){
        Dialog dialog = new Dialog(" ", skin) {
            public void result(Object obj) {
                if (soundEnabled) soundTap.play(soundVolume);
                if ((Boolean) obj) {
                    Gdx.net.openURI(aUrl);
                    remove();
                }
                else remove();
            }
        }.text(infoText).button(strings[Languages.ID_YES][language], true).button(strings[Languages.ID_NO][language], false);
        if (soundEnabled) game.assetManager.get(Constants.S_SOUNDDIALOG, Sound.class).play(soundVolume);
        dialog.setColor(1f, 0.3f, 0.3f,1f);
        dialog.show(stage) ;
    }


}
