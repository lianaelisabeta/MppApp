package utils;

import protocol.ClientRpcWorker;
import services.IServer;

import java.net.Socket;

public class RpcConcurrentServer  extends AbsConcurrentServer {
    private IServer server;
    public RpcConcurrentServer(int port, IServer server) {
        super(port);
        this.server = server;
        System.out.println("RpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcWorker worker=new ClientRpcWorker(server, client);
        //ChatClientRpcReflectionWorker worker=new ChatClientRpcReflectionWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }
}

