package com.odev;

import java.io.File;
import java.net.URISyntaxException;

import net.sourceforge.jFuzzyLogic.FIS;

public class Parca {
	private final FIS fis;
	public Parca(double tecrube, double yas, double cinsiyet) throws URISyntaxException {
		File file = new File(getClass().getResource("Parca.fcl").toURI());
		fis = FIS.load(file.getPath());
		fis.setVariable("tecrube", tecrube);
		fis.setVariable("yas", yas);
		fis.setVariable("cinsiyet", cinsiyet);
		fis.evaluate();
	}
	public FIS getModel() {
		return fis;
	}
	
	public double getParcaSayisi() {
		return fis.getVariable("parcaSayisi").getValue();
	}
}
