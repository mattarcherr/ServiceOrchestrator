package com.ntu.ServiceOrchestrator;

import com.ntu.ServiceOrchestrator.database.jdbc;
import com.ntu.ServiceOrchestrator.database.mysqlDatasource;
import com.ntu.ServiceOrchestrator.exception.restApiException;
import org.apache.coyote.Response;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.json.simple.JSONObject;

import javax.naming.CommunicationException;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/genID")
    public idData genID() {
        try {
            RestService restService = new RestService(new RestTemplateBuilder());
            String generatedID = restService.getID();
            return new idData(generatedID);
        } catch (Exception e) {
            throw new restApiException("id generation failed");
        }
    }

    @GetMapping("/trip/query")
    public JSONObject queryTrip(){
        try {
            DataSource dataSource = new mysqlDatasource().dataSource();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbc jdbc = new jdbc(jdbcTemplate);

            Trip[] trips= jdbc.getAlltrips();

    //         Add weather to each trip
            for (int i = 0; i <= trips.length-1; i++) {
                if (trips[i].getDate().isBefore(LocalDate.now().plusDays(7))) {
                    RestService restService = new RestService(new RestTemplateBuilder());
                    forecastData[] forecastData = restService.getForecast(trips[i]);

                    trips[i].setWeather(forecastData);
                }
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("trips", trips);
            return jsonObject;
        } catch (Exception e) {
            throw new restApiException("trip query failed");
        }
    }

    @GetMapping("/trip/query/param")
    public JSONObject queryTripPararm(@RequestParam("location") String location){
        try {
            DataSource dataSource = new mysqlDatasource().dataSource();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbc jdbc = new jdbc(jdbcTemplate);

            Trip[] trips= jdbc.getTrips(location);

    //         Add weather to each trip
            for (int i = 0; i <= trips.length-1; i++) {
                if (trips[i].getDate().isBefore(LocalDate.now().plusDays(7))) {
                    RestService restService = new RestService(new RestTemplateBuilder());
                    forecastData[] forecastData = restService.getForecast(trips[i]);

                    trips[i].setWeather(forecastData);
                }
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("trips", trips);
            return jsonObject;
        } catch (Exception e) {
            throw new restApiException("location not found");
        }
    }

    @PostMapping("/trip/propose")
    public boolean proposeTrip(@RequestBody Trip trip){
        try {
            DataSource dataSource = new mysqlDatasource().dataSource();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbc jdbc = new jdbc(jdbcTemplate);

            jdbc.insert(trip);

            return true;
        } catch (Exception e) {
            throw new restApiException("trip proposal failed");
        }
    }

    @PostMapping("/interest/express")
    public boolean expressTripInterest(@RequestBody Trip trip){
        try {
            DataSource dataSource = new mysqlDatasource().dataSource();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbc jdbc = new jdbc(jdbcTemplate);

            jdbc.update(trip.getTripID(), trip.getUserID());
            return true;
        } catch (Exception e) {
            throw new restApiException("express interest failed");
        }
    }

    @GetMapping("/interest/query")
    public JSONObject expressTripInterest(@RequestParam("tripID") String tripID){
        try {
            DataSource dataSource = new mysqlDatasource().dataSource();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbc jdbc = new jdbc(jdbcTemplate);

            idData[] idData = jdbc.getInterest(tripID);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("interest", idData);
            return jsonObject;
        } catch (Exception e) {
            throw new restApiException("tridID not found");
        }
    }
}
