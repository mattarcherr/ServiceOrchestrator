package com.ntu.ServiceOrchestrator;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {
    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getID() {
        // random int between 1 and 9999 from random.org
        String url = "https://www.random.org/integers/?num=1&min=1&max=9999&col=1&base=10&format=plain&rnd=new";

        // returns random int in String form
        return this.restTemplate.getForObject(url, String.class);
    }

    public forecastData[] getForecast(Trip trip) {
        // get forecast data
        Double[] coords = new Double[2];
        coords[0] = 23.5;
        coords[1] = 25.1;
        String url = "https://www.7timer.info/bin/api.pl?lon="+coords[1].toString()+"&lat="+coords[0].toString()+"&product=civillight&output=json";
        String weatherData = this.restTemplate.getForObject(url, String.class);

        forecastData[] forecastDataArray = new forecastData[7];
        for (int i = 0; i <= 6; i++) {
            forecastDataArray[i] = new forecastData();

            int dateIndex =    weatherData.indexOf("date")+8;
            int weatherIndex = weatherData.indexOf("weather")+12;
            int minIndex =     weatherData.indexOf("min")+7;
            int maxIndex =     weatherData.indexOf("max")+7;
            int windIndex =    weatherData.indexOf("wind10m_max")+15;

            //set date
            forecastDataArray[i].setDate(weatherData.substring(dateIndex, dateIndex + 8));

            //set weather
            String tempString = weatherData.substring(weatherIndex);
            forecastDataArray[i].setWeather(weatherData.substring(weatherIndex, weatherIndex + tempString.indexOf("\",")));

            //set max temp
            tempString = weatherData.substring(maxIndex);
            forecastDataArray[i].setMax(Integer.parseInt(weatherData.substring(maxIndex, maxIndex + tempString.indexOf(","))));

            //set min temp
            tempString = weatherData.substring(minIndex);
            forecastDataArray[i].setMin(Integer.parseInt(weatherData.substring(minIndex, minIndex + tempString.indexOf("}")).replaceAll("\\s", "")));

            // set wind max
            tempString = weatherData.substring(windIndex);
            forecastDataArray[i].setWind(Integer.parseInt(weatherData.substring(windIndex, windIndex + tempString.indexOf("}")).replaceAll("\\s", "")));

            weatherData = weatherData.substring(windIndex);
        }

        // returns array of forecast data for each day in a week
        return forecastDataArray;
    }
}