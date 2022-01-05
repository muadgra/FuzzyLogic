package com.odev;

import java.net.URISyntaxException;
import java.util.Scanner;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Rule;

public class Main {

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		System.out.print("Tecrube: ");
		double tecrube = in.nextDouble();
		System.out.print("Cinsiyet: (1 = Erkek, 2 = Kadin) ");
		double cinsiyet = in.nextDouble();
		System.out.print("Yas: ");
		double yas = in.nextDouble();
		
		try {
			Parca parca = new Parca(tecrube, yas, cinsiyet);
			FIS fis = parca.getModel();
			System.out.println("Uretilen Parca Sayisi: " + parca.getParcaSayisi());
			
			//Kurallar yazdýrýlýr.
			for(Rule r : fis.getFunctionBlock("Parca").getFuzzyRuleBlock("kuralBlok").getRules()) {
				if(r.getDegreeOfSupport() > 0) System.out.println(r);
				
			}
			//Bütün modeli getirir.
			JFuzzyChart.get().chart(parca.getModel());
			
			//Sadece taralý alaný getirir.
			//JFuzzyChart.get().chart(fis.getVariable("parcaSayisi").getDefuzzifier(), "Parca Sayisi", true);
		} catch (URISyntaxException e) {
			
			System.out.println(e.getMessage());
		}
	}

}
