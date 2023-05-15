package com.ntu.ServiceOrchestrator.database;

import com.ntu.ServiceOrchestrator.Trip;
import com.ntu.ServiceOrchestrator.idData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class jdbc {
    private final JdbcTemplate jdbcTemplate;
    public jdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void insert(Trip trip) {
        String sql = "insert into trips (userID, tripID, title, date, latitude, longitude, location) values (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                trip.getUserID(), trip.getTripID(), trip.getTitle(),
                trip.getDate(), trip.getLongitude(), trip.getLatitude(),
                trip.getLocation()
        );
    }

    public Trip[] getAlltrips() {
        String sql = "select * from trips";
        List<Trip> trips = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper(Trip.class)
        );
        return trips.toArray(new Trip[0]);
    }

    public Trip[] getTrips(String location) {
        String sql = "select * from trips where location = '"+location;
        System.out.println(sql);
        List<Trip> trips = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper(Trip.class)
        );
        return trips.toArray(new Trip[0]);
    }

    public idData[] getInterest(String tripID) {
        String sql = "select userID from interest where tripID = '"+tripID+"'";
        List<Trip> trips = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper(Trip.class)
        );
        System.out.println(trips.size());
        idData[] data = new idData[trips.size()];
        Integer j = 0;
        for (Trip i : trips)
        {
            data[j] = new idData(i.getUserID());
            j++;
        }
        return data;
    }

    public void update(String tripID, String userID) {
        String sql = "update trips set interest = interest + 1 where tripID = ?";
        jdbcTemplate.update(sql, tripID);

        String sql2 = "insert into interest (tripID, userID) values (?, ?)";
        jdbcTemplate.update(sql2, tripID, userID);
    }


}
