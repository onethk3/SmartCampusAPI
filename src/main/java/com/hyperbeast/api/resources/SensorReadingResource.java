package com.hyperbeast.api.resources;

import com.hyperbeast.api.exceptions.SensorUnavailableException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.hyperbeast.api.data.DataStore;
import com.hyperbeast.api.models.Sensor;
import com.hyperbeast.api.models.SensorReading;
import java.util.ArrayList;
import java.util.List;

public class SensorReadingResource {
    private DataStore data = DataStore.getInstance();
    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getHistory() {
        return data.getHistoricalReadings().getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        if (reading == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        Sensor sensor = data.getSensors().get(sensorId);

        if (sensor == null) return Response.status(404).build();

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is currently in MAINTENANCE and cannot accept readings.");
        }

        sensor.setCurrentValue(reading.getValue());

        data.getHistoricalReadings()
                .computeIfAbsent(sensorId, k -> new ArrayList<>())
                .add(reading);

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}