package com.example.demo;

import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class BitcoinPricePredictor {

    private static final int SEQ_LENGTH = 24 * 30; // 30일 * 24시간
    private static final int FEATURES = 1;
    private static final Random RANDOM = new Random(42);

    private MultiLayerNetwork model;
    private List<Double> monthlyData;
    private CompletableFuture<Void> initFuture;
    private double min, max;

    @PostConstruct
    public void init() {
        monthlyData = generateMonthlyData();
        initFuture = CompletableFuture.runAsync(this::trainModel);
    }

    private void trainModel() {
        INDArray input = Nd4j.create(SEQ_LENGTH - 24, FEATURES, 24);
        INDArray labels = Nd4j.create(SEQ_LENGTH - 24, FEATURES, 24);

        // 데이터 정규화
        max = monthlyData.stream().mapToDouble(Double::doubleValue).max().orElse(1.0);
        min = monthlyData.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);

        for (int i = 0; i < SEQ_LENGTH - 24; i++) {
            for (int j = 0; j < 24; j++) {
                double normalizedValue = (monthlyData.get(i + j) - min) / (max - min);
                input.putScalar(new int[]{i, 0, j}, normalizedValue);
                labels.putScalar(new int[]{i, 0, j}, (monthlyData.get(i + j + 1) - min) / (max - min));
            }
        }

        DataSet dataSet = new DataSet(input, labels);

        model = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .seed(42)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(0.001))
                .list()
                .layer(new LSTM.Builder().nIn(FEATURES).nOut(50).activation(Activation.TANH).build())
                .layer(new LSTM.Builder().nIn(50).nOut(25).activation(Activation.TANH).build())
                .layer(new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY).nIn(25).nOut(FEATURES).build())
                .build());

        model.init();

        for (int i = 0; i < 100; i++) {
            model.fit(dataSet);
        }
    }

    public List<Double> predict(int hours) {
        try {
            initFuture.get(); // 모델 초기화가 완료될 때까지 대기
        } catch (Exception e) {
            throw new RuntimeException("모델 초기화 중 오류 발생", e);
        }

        INDArray testInput = Nd4j.create(1, FEATURES, 24);
        for (int i = 0; i < 24; i++) {
            testInput.putScalar(new int[]{0, 0, i}, (monthlyData.get(monthlyData.size() - 24 + i) - min) / (max - min));
        }

        List<Double> predictions = new ArrayList<>();
        for (int i = 0; i < hours; i++) {
            INDArray output = model.output(testInput);
            double predictedPrice = output.getDouble(0, 0, 23) * (max - min) + min;
            predictions.add(predictedPrice);

            // 입력 데이터를 한 칸씩 밀고 새로운 예측값을 추가
            for (int j = 0; j < 23; j++) {
                testInput.putScalar(new int[]{0, 0, j}, testInput.getDouble(0, 0, j + 1));
            }
            testInput.putScalar(new int[]{0, 0, 23}, (predictedPrice - min) / (max - min));
        }

        return predictions;
    }

    private List<Double> generateMonthlyData() {
        List<Double> data = new ArrayList<>();
        double price = 50000.0;
        for (int i = 0; i < SEQ_LENGTH; i++) {
            price += RANDOM.nextGaussian() * 100;
            data.add(price);
        }
        return data;
    }

    public List<Double> getMonthlyData() {
        return monthlyData;
    }
}