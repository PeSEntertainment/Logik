package com.pes.logik.Sprites;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pes.logik.Screens.PlayScreen;

import static com.pes.logik.Constants.Constants.MAXROWS;
import static com.pes.logik.Global.Game.actualRow;
import static com.pes.logik.Global.Game.begCol;
import static com.pes.logik.Global.Game.element;
import static com.pes.logik.Global.Game.hSpace;
import static com.pes.logik.Global.Game.numCols;
import static com.pes.logik.Global.Game.sButtons;
import static com.pes.logik.Global.Game.vSpace;
import static com.pes.logik.Global.Game.soundEnabled;
import static com.pes.logik.Global.Game.soundVolume;


public class PlayButton extends Image {
    private PlayScreen screen;
    private Integer buttonColor;
    private boolean locked;
    private boolean inDesk;
    private int col, row;
    private String textureName;

    public PlayButton(PlayScreen ascreen, Integer abuttonColor, float aposX, float aposY, int aelement, boolean alocked) {
        super(ascreen.textureAtlas.findRegion("btn"+sButtons+"0"));
        screen = ascreen;
        locked = alocked;
        buttonColor = abuttonColor;
        inDesk = alocked;
        textureName = "btn" +sButtons + buttonColor.toString();
        setDrawable(new TextureRegionDrawable(screen.textureAtlas.findRegion(textureName)));
        setPosition(aposX, aposY);
        setSize(aelement, aelement);
    }

    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        locked = true;
    }

    public int getButtonColor() {
        return buttonColor;
    }

    public int get() {
        int get = 99;
        if (inDesk) {
            inDesk = false;
            screen.desk[row][col] = null;
            get = row;
            if (row == actualRow) screen.controlActualRow();
            col = 0;
            row = 0;
            setDrawable(new TextureRegionDrawable(screen.textureAtlas.findRegion(textureName)));
        }
        return get;
    }

    public void put(float aX, float aY) {
        int calcRow, calcCol;
        calcRow = calcYToRow(aY);
        calcCol = calcXToCol(aX);
        if (calcRow >= actualRow && calcRow < MAXROWS && calcCol >= 0 && calcCol < numCols) {
            row = calcRow;
            col = calcCol;
            if (screen.desk[row][col] != null && screen.desk[row][col] != this)
                screen.desk[row][col].remove();
            if (soundEnabled) screen.soundTap.play(soundVolume);
            setPosition(hSpace + (col + begCol) * element, vSpace + row * element);
            setDrawable(new TextureRegionDrawable(screen.textureAtlas.findRegion(textureName)));
            screen.desk[row][col] = this;
            inDesk = true;
            locked = false;
            if (row == actualRow) screen.controlActualRow();
        }
        else remove();
    }

    private int calcXToCol(float aposX) {
        float res;
        res = ((aposX - hSpace/2) / element) - begCol;
        int resint;
        resint = (int) res ;
        return resint;
//        return (Math.round((aposX - hSpace) / element)) - begCol;
    }

    private int calcYToRow(float aposY) {
        float res;
        res = ((aposY - vSpace/2) / element);
        int resint;
        resint = (int) res ;
        return resint;
//        return Math.round((aposY - vSpace) / element);
    }

    public void updateTexture(){
        textureName = "btn"+sButtons+buttonColor.toString();
        setDrawable(new TextureRegionDrawable(screen.textureAtlas.findRegion(textureName)));
    }
}
