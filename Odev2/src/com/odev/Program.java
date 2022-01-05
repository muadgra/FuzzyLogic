package com.odev;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

public class Program {

	public static void main(String[] args) throws IOException, URISyntaxException {
		
		YSA sinirAgi = new YSA(2500, 0.1, 0.9);
		sinirAgi.trainAndTest();
	}
		

}
