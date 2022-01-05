package com.odev;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;

public class YSA {
	File dosya;
	NeuralNetwork<BackPropagation> sinirAgiMbp;
	NeuralNetwork<BackPropagation> sinirAgiBp;
	DataSet data;
	int epochSayisi;
	MomentumBackpropagation mbp;
	BackPropagation bp;
	Normalizer normalizer;
	double[] hatalar;
	
	public YSA(int epoch, double learningRate, double momentum) throws URISyntaxException {
		dosya = new File(YSA.class.getResource("veri.txt").toURI());
		data = DataSet.createFromFile(dosya.getPath(), 3, 1, " ");
		sinirAgiMbp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 3, 20, 1);
		sinirAgiBp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 3, 20, 1);
		epochSayisi = epoch;
		hatalar = new double[(int)epochSayisi];
		
		mbp = new MomentumBackpropagation();
		bp = new BackPropagation();
		
		mbp.setMaxIterations(epoch);
		bp.setMaxIterations(epoch);
		
		mbp.setLearningRate(learningRate);
		bp.setLearningRate(learningRate);
		
		mbp.setMomentum(momentum);
		
		sinirAgiMbp.setLearningRule(mbp);
		sinirAgiBp.setLearningRule(bp);
	}
	public void trainAndTest() throws IOException {
		double mbpToplamEgitimHata = 0, mbpToplamTestHata = 0;
		DataSet[] trainTestSplit = data.split(0.7, 0.3);
		
	    DataSet trainingSet = trainTestSplit[0];
	    DataSet testSet = trainTestSplit[1];
	    
	    //Neuroph kütüphanesi içerisindeki normalizerý kullanýyorum.
	    Normalizer normalizer = new MaxNormalizer(trainingSet);
	    normalizer.normalize(trainingSet);
	    normalizer.normalize(testSet);
	    
	    
	    //Öðrenme iþlemleri gerçekleþtirilir.
	    sinirAgiMbp.learn(trainingSet);
	    
	    for(int i = 0; i< epochSayisi; i++) {
			sinirAgiBp.getLearningRule().doOneLearningIteration(trainingSet);
			if(i == 0) {
				hatalar[i] = 0.5;
			}
			else {
				hatalar[i] = sinirAgiBp.getLearningRule().getPreviousEpochError();
			}
		}
	    
	    
	    mbpToplamEgitimHata += mbp.getTotalNetworkError();
	    System.out.println("Momentumlu Backpropagation Toplam Eðitim Hatasý: " + mbpToplamEgitimHata);
	    System.out.println("Momentumsuz Backpropagation Toplam Eðitim Hatasý: " + bp.getTotalNetworkError());
	    
	    System.out.println("Momentumlu Backpropagation Toplam Test Hatasý: " + test(sinirAgiMbp, testSet));
	    System.out.println("Momentumsuz Backpropagation Toplam Test Hatasý: " + test(sinirAgiBp, testSet));
	}
	private double test(NeuralNetwork<BackPropagation> sinirAgi, DataSet testVeriSeti) {
		double toplamHata = 0;
		for(DataSetRow dr : testVeriSeti) {
			sinirAgi.setInput(dr.getInput());
			//ileri besleme
			sinirAgi.calculate();
			toplamHata += mse(dr.getDesiredOutput(), sinirAgi.getOutput());
		}
		return toplamHata / testVeriSeti.size();
	}
	private double mse(double[] beklenen, double[] cikti) {
		double birSatirVeriHata = 0;
		for(int i = 0; i<cikti.length; i++) {
			birSatirVeriHata += Math.pow(beklenen[i] - cikti[i], 2);
		}
		return birSatirVeriHata / cikti.length;
	}
}
