package utils;

import protobufprotocol.ProtoWorker;
import services.IServer;

import java.net.Socket;

public class ProtoConcurrentServer extends AbsConcurrentServer {
    private IServer chatServer;
    public ProtoConcurrentServer(int port, IServer chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("ProtobuffConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProtoWorker worker=new ProtoWorker(chatServer,client);
        Thread tw=new Thread(worker);
        return tw;
    }
}