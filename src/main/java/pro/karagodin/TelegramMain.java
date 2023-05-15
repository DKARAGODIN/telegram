package pro.karagodin;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class TelegramMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.print("Eneter your user name - ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String username = br.readLine();

        if (username == null || username.equals("")) {
            System.out.print("Empty user name. Bye.");
        }

        System.out.print("Enter host - ");
        String host = br.readLine();
        if (host == null || host.equals("")) {
            startServer();
        }

        System.out.println("Enter port - ");
        String port = br.readLine();



        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, Integer.parseInt(port))
                .usePlaintext()
                .build();

        MessageServiceGrpc.MessageServiceStub stub = MessageServiceGrpc.newStub(channel);

        StreamObserver<Telegram.Message> requestObserver =
                stub.send(new StreamObserver<Telegram.Message>() {
                    @Override
                    public void onNext(Telegram.Message message) {
                        System.out.println("Got message from server " + message.getText());
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                });

        while (true) {
            String message = br.readLine();
            Telegram.Message newMessage = Telegram.Message.newBuilder().setText(message).build();

            requestObserver.onNext(newMessage);

            if (false)
                break;
        }

        requestObserver.onCompleted();

    }

    private static void startServer() throws IOException, InterruptedException {
        io.grpc.Server server = ServerBuilder
                .forPort(8080)
                .addService(new MessageServiceImpl()).build();

        server.start();
        server.awaitTermination();
    }
}

