package pro.karagodin;

import io.grpc.stub.StreamObserver;

public class MessageServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {


    @Override
    public StreamObserver<Telegram.Message> send(StreamObserver<Telegram.Message> responseObserver) {
        return new StreamObserver<Telegram.Message>() {
            @Override
            public void onNext(Telegram.Message message) {
                System.out.println("Server got message - " + message.getText());
                Telegram.Message response = Telegram.Message.newBuilder().setText("Server got your message successfully").build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        };
    }
}
