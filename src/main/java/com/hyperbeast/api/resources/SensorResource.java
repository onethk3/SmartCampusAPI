package com.hyperbeast.api.resources;

import com.hyperbeast.api.exceptions.LinkedResourceNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.hyperbeast.api.data.DataStore;
import com.hyperbeast.api.models.Room;
import com.hyperbeast.api.models.Sensor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/sensors")
public class SensorResource {
    private DataStore data = DataStore.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSensor(Sensor sensor) {
        Room room = data.getRooms().get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException("Room ID " + sensor.getRoomId() + " does not exist.");
        }

        data.getSensors().put(sensor.getId(), sensor);

        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        List<Sensor> sensorList = new ArrayList<>(data.getSensors().values());

        if (type != null && !type.isEmpty()) {
            return sensorList.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }
        return sensorList;
    }

    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor s = data.getSensors().get(sensorId);
        return s != null ? Response.ok(s).build() : Response.status(404).build();
    }

    @PUT
    @Path("/{sensorId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSensor(@PathParam("sensorId") String sensorId, Sensor updatedSensor) {
        Sensor existing = data.getSensors().get(sensorId);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (updatedSensor.getStatus() != null) existing.setStatus(updatedSensor.getStatus());
        if (updatedSensor.getType() != null) existing.setType(updatedSensor.getType());
        
        return Response.ok(existing).build();
    }

    @Path("/{sensorId}/read")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}