package com.pes.logik.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DeskItem extends Sprite {

    public DeskItem(TextureRegion atextureregion, int aposX, int aposY, int awidth, int aheight) {
        super(atextureregion);
        setPosition(aposX, aposY);
        setSize(awidth, aheight);
    }
}
