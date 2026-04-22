package ru.iu3.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import ru.iu3.entity.Pass;
import ru.iu3.exceptions.ConflictException;
import ru.iu3.exceptions.NotFoundExeption;
import ru.iu3.exceptions.ValidationException;
import ru.iu3.service.interfaces.PassService;

public class PassServiceClient implements PassService {
    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;

    public PassServiceClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = ReferenceServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public void issuePass(int id, String holderName) {
        try {
            stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .issuePass(ReferenceContract.IssuePassRequest.newBuilder()
                            .setId(id)
                            .setHolderName(holderName)
                            .setTraceId(newTraceId())
                            .build());
        } catch (StatusRuntimeException e) {
            throw mapGrpcException(e);
        }
    }

    @Override
    public void deactivatePass(int id) {
        try {
            stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .deactivatePass(ReferenceContract.PassIdRequest.newBuilder()
                            .setId(id)
                            .setTraceId(newTraceId())
                            .build());
        } catch (StatusRuntimeException e) {
            throw mapGrpcException(e);
        }
    }

    @Override
    public List<Pass> getAllPasses() {
        try {
            ReferenceContract.PassListResponse response = stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .listPasses(ReferenceContract.TraceRequest.newBuilder()
                            .setTraceId(newTraceId())
                            .build());

            List<Pass> passes = new ArrayList<>();
            for (ReferenceContract.PassResponse passResponse : response.getPassesList()) {
                passes.add(mapPass(passResponse));
            }
            return passes;
        } catch (StatusRuntimeException e) {
            throw mapGrpcException(e);
        }
    }

    @Override
    public Pass getPassById(int id) {
        try {
            ReferenceContract.PassResponse response = stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .getPass(ReferenceContract.PassIdRequest.newBuilder()
                            .setId(id)
                            .setTraceId(newTraceId())
                            .build());
            return mapPass(response);
        } catch (StatusRuntimeException e) {
            throw mapGrpcException(e);
        }
    }

    private Pass mapPass(ReferenceContract.PassResponse response) {
        Pass pass = new Pass(response.getId(), response.getHolderName());
        if (!response.getActive()) {
            pass.deactivate();
        }
        return pass;
    }

    private RuntimeException mapGrpcException(StatusRuntimeException e) {
        Status status = e.getStatus();
        String message = status.getDescription() == null ? "Ошибка gRPC при работе с пропусками." : status.getDescription();

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
