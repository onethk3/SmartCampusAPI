package com.hyperbeast.api.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.hyperbeast.api.data.DataStore;
import com.hyperbeast.api.models.Room;
import java.util.ArrayList;
import java.util.List;
import com.hyperbeast.api.exceptions.RoomNotEmptyException;
import com.hyperbeast.api.models.ErrorMessage;

@Path("/rooms")
public class SensorRoomResource {
    private DataStore data = DataStore.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> listRooms() {
        return new ArrayList<>(data.getRooms().values());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(Room room) {
        if (room == null || room.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Invalid room data provided.", 400))
                    .build();
        }
        data.getRooms().put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room r = data.getRooms().get(roomId);
        return r != null ? Response.ok(r).build() : Response.status(404).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = data.getRooms().get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }


        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Room is currently occupied by active hardware."
            );
        }

        data.getRooms().remove(roomId);
        return Response.noContent().build();
    }
}
