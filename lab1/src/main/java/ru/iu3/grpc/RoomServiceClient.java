package ru.iu3.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import ru.iu3.entity.interfaces.Room;
import ru.iu3.enums.RoomEnum;
import ru.iu3.exceptions.ConflictException;
import ru.iu3.exceptions.NotFoundExeption;
import ru.iu3.exceptions.ValidationException;
import ru.iu3.service.DefaultRoomFactory;
import ru.iu3.service.interfaces.RoomFactory;
import ru.iu3.service.interfaces.RoomService;

public class RoomServiceClient implements RoomService {
    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;
    private final RoomFactory roomFactory;

    public RoomServiceClient(String host, int port, RoomFactory roomFactory) {
        this.roomFactory = roomFactory;
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = ReferenceServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public void addRoom(RoomEnum type, int id, String name, int hourlyRate) {
        try {
            stub.withDeadlineAfter(2, TimeUnit.SECONDS) // Не будем спорить с докой :3c
                    .addRoom(ReferenceContract.AddRoomRequest.newBuilder()
                            .setType(type.name())
                            .setId(id)
                            .setName(name)
                            .setHourlyRate(hourlyRate)
                            .setTraceId(newTraceId())
                            .build());
        } catch (StatusRuntimeException e) {
            throw mapGrpcException(e);
        }
    }

    @Override
    public List<Room> getAllRooms() {
        try {
            ReferenceContract.RoomListResponse response = stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .listRooms(ReferenceContract.TraceRequest.newBuilder()
                            .setTraceId(newTraceId())
                            .build());

            List<Room> rooms = new ArrayList<>();
            for (ReferenceContract.RoomResponse roomResponse : response.getRoomsList()) {
                rooms.add(mapRoom(roomResponse));
            }
            return rooms;
        } catch (StatusRuntimeException e) {
            throw mapGrpcException(e);
        }
    }

    @Override
    public void lockRoom(int id) {
        try {
            stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .lockRoom(ReferenceContract.RoomIdRequest.newBuilder()
                            .setId(id)
                            .setTraceId(newTraceId())
                            .build());
        } catch (StatusRuntimeException e) {
            throw mapGrpcException(e);
        }
    }

    @Override
    public Room getRoomById(int id) {
        try {
            ReferenceContract.RoomResponse response = stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .getRoom(ReferenceContract.RoomIdRequest.newBuilder()
                            .setId(id)
                            .setTraceId(newTraceId())
                            .build());
            return mapRoom(response);
        } catch (StatusRuntimeException e) {
            throw mapGrpcException(e);
        }
    }

    private Room mapRoom(ReferenceContract.RoomResponse response) {
        RoomEnum type = RoomEnum.valueOf(response.getType());
        Room room = roomFactory.create(type, response.getId(), response.getName(), response.getHourlyRate());

        if (response.getLocked()) {
            room.lock();
        }

        return room;
    }

    private RuntimeException mapGrpcException(StatusRuntimeException e) {
        Status status = e.getStatus();
        String message = status.getDescription() == null ? "Ошибка gRPC при работе с комнатами."
                : status.getDescription();

        return switch (status.getCode()) {
            case NOT_FOUND -> new NotFoundExeption(message);
            case INVALID_ARGUMENT -> new ValidationException(message);
            case ALREADY_EXISTS -> new ConflictException(message);
            case UNAVAILABLE, DEADLINE_EXCEEDED ->
                new ValidationException("Reference Service недоступен. Попробуйте позже.");
            default -> new RuntimeException(message, e);
        };
    }

    private String newTraceId() {
        return UUID.randomUUID().toString();
    }
}
