package com.pes.logik.Sprites;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pes.logik.Screens.PlayScreen;

import static com.pes.logik.Global.Game.sDesk;
import static com.pes.logik.Global.Game.sResults;


public class ResultButton extends Image {
    private PlayScreen screen;
    private int row;
    private boolean locked;  //locked row
    private Integer blacks;
    private Integer whites;
    private String textureName;
    private boolean allowed = false; // is evaluation allowed?

    public ResultButton(PlayScreen ascreen, float aposX, float aposY, int aelement, int arow, boolean alocked){
        super(ascreen.textureAtlas.findRegion("inactive"));
        screen = ascreen;
        locked = alocked;
        whites = 9; blacks = 0;
        row = arow;
        setPosition(aposX, aposY);
        setSize(1.5f*aelement,aelement);
    }

    public boolean isLocked(){ return locked;}

    public int getRow() { return row; }

    public void lock(){
        locked = true;
    }

    public void setActive(){
        textureName = "eval_red"+sDesk;updateGraphic();
    }

    public void clean(){
        whites = 9; blacks = 0;
        textureName = "inactive"; updateGraphic();
    }


    public void setInactive(){
        textureName = "inactive"; updateGraphic();
    }

    public void updateState(int ablacks, int awhites){
        blacks = ablacks;
        whites = awhites;
        textureName = sResults+blacks.toString()+whites.toString(); updateGraphic();
    }

    public boolean go(){
        boolean go = false;
        if (allowed) {
            textureName = "eval_red"+sDesk; updateGraphic();
            allowed = false;
            go = true;
        }
        return go;
    }

    public void allow(){
        allowed = true;
        textureName = "eval_green"+sDesk; updateGraphic();
    }
    public void disallow(){
        allowed = false;
        textureName = "eval_red"+sDesk; updateGraphic();
    }

    public void updateGraphic(){
        setDrawable(new TextureRegionDrawable(screen.textureAtlas.findRegion(textureName)));
    }

    public void updateTexture(){
        if (whites!=9) {
            textureName = sResults+blacks.toString()+whites.toString();
        }
        else textureName = "inactive";
        updateGraphic();
    }


}
