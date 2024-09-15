package com.example.demo;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PredictionController {

    @Autowired
    private BitcoinPricePredictor predictor;

    @GetMapping("/predict")
    public String predict(@RequestParam(name = "hours", defaultValue = "24") int hours, Model model) {
        List<Double> predictions = predictor.predict(hours);
        List<Double> monthlyData = predictor.getMonthlyData();

        model.addAttribute("monthlyData", monthlyData);
        model.addAttribute("predictions", predictions);

        return "graph";
    }
}