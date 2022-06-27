package com.pes.logik;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pes.logik.Constants.Constants;
import com.pes.logik.Interfaces.Platform;
import com.pes.logik.Screens.PlayScreen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.pes.logik.Global.Game.canRepeat;
import static com.pes.logik.Global.Game.learnMode;
import static com.pes.logik.Global.Game.numColors;
import static com.pes.logik.Global.Game.numCols;
import static com.pes.logik.Global.Game.sButtons;
import static com.pes.logik.Global.Game.sDesk;
import static com.pes.logik.Global.Game.sResults;
import static com.pes.logik.Global.Game.soundEnabled;
import static com.pes.logik.Global.Game.soundVolume;
import static com.pes.logik.Global.Game.language;


public class Logik extends Game {
	// global variables
    public final Platform platform;
	public AssetManager assetManager;

	public Logik(Platform platform){
		this.platform = platform;
    }

	@Override
	public void create() {
		loadAssets();
		load();
		setScreen(new PlayScreen(this));
	}

	private void loadAssets() {
		assetManager = new AssetManager();
		assetManager.load(Constants.S_SKIN, Skin.class);
		assetManager.load(Constants.S_ATLAS, TextureAtlas.class);
		assetManager.load(Constants.S_SOUNDTAP, Sound.class);
		assetManager.load(Constants.S_SOUNDWIN, Sound.class);
		assetManager.load(Constants.S_SOUNDDIALOG, Sound.class);
		assetManager.finishLoading();
	}

	@Override
	public void dispose() {
		super.dispose();
		assetManager.dispose();
	}

	private void load() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(Gdx.files.local(Constants.PREFERENCES).file());

			Element rootElement = doc.getDocumentElement();
			if (rootElement.getElementsByTagName("Sound").getLength()>0)
				soundEnabled = Boolean.parseBoolean(rootElement.getElementsByTagName("Sound").item(0).getTextContent());
			if (rootElement.getElementsByTagName("SoundVolume").getLength()>0)
				soundVolume = Float.parseFloat(rootElement.getElementsByTagName("SoundVolume").item(0).getTextContent());
			if (rootElement.getElementsByTagName("Buttons").getLength()>0)
				sButtons = rootElement.getElementsByTagName("Buttons").item(0).getTextContent();
			if (rootElement.getElementsByTagName("Desk").getLength()>0)
				sDesk = rootElement.getElementsByTagName("Desk").item(0).getTextContent();
            if (rootElement.getElementsByTagName("Results").getLength()>0)
                sResults = rootElement.getElementsByTagName("Results").item(0).getTextContent();
			if (rootElement.getElementsByTagName("Language").getLength()>0)
				language = Integer.parseInt(rootElement.getElementsByTagName("Language").item(0).getTextContent());
			if (rootElement.getElementsByTagName("Cols").getLength()>0)
				numCols = Integer.parseInt(rootElement.getElementsByTagName("Cols").item(0).getTextContent());
			if (rootElement.getElementsByTagName("Colors").getLength()>0)
				numColors = Integer.parseInt(rootElement.getElementsByTagName("Colors").item(0).getTextContent());
			if (rootElement.getElementsByTagName("CanRepeat").getLength()>0)
				canRepeat = Boolean.parseBoolean(rootElement.getElementsByTagName("CanRepeat").item(0).getTextContent());
			if (rootElement.getElementsByTagName("LearnMode").getLength()>0)
				learnMode = Boolean.parseBoolean(rootElement.getElementsByTagName("LearnMode").item(0).getTextContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		FileHandle file = Gdx.files.local(Constants.PREFERENCES);
		file.writeString("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n", false);
		file.writeString("<Settings>\n", true);

		file.writeString("<Sound>"+soundEnabled.toString()+"</Sound>\n", true);
		file.writeString("<SoundVolume>"+soundVolume.toString()+"</SoundVolume>\n", true);
		file.writeString("<Buttons>"+sButtons+"</AtlasButtons>\n", true);
		file.writeString("<Desk>"+sDesk+"</Desk>\n", true);
        file.writeString("<Results>"+sResults+"</Results>\n", true);
		file.writeString("<Language>"+language.toString()+"</Language>\n", true);
		file.writeString("<Cols>"+numCols.toString()+"</Cols>\n", true);
		file.writeString("<Colors>"+numColors.toString()+"</Colors>\n", true);
		file.writeString("<CanRepeat>"+canRepeat.toString()+"</CanRepeat>\n", true);
		file.writeString("<LearnMode>"+learnMode.toString()+"</LearnMode>\n", true);

		file.writeString("</Settings>\n", true);
	}




}
