//package dev.vsviniciuslima.whatsapp.connections.whatsapp;
//
//import dev.vsviniciuslima.whatsapp.connections.model.ConnectionMode;
//import dev.vsviniciuslima.whatsapp.connections.model.CreateConnection;
//import it.auties.whatsapp.api.QrHandler;
//import it.auties.whatsapp.api.WebOptionsBuilder;
//import it.auties.whatsapp.api.Whatsapp;
//
//import java.util.Optional;
//
//public class WhatsappConnectionConfig {
//
//    public Whatsapp createConnection(CreateConnection newConnection) {
//
//        ConnectionMode connectionMode = newConnection.connectionMode();
//
//        WebOptionsBuilder connection = Whatsapp.webBuilder()
//                .newConnection()
//                .name("Outcome Cloud")
//                ;
//
//        if(connectionMode == ConnectionMode.QR_CODE) {
//            return connection
//                    .unregistered(QrHandler.toTerminal())
//                    .connect()
//                    .join();
//        }
//
//        Optional<Long> phoneNumber = newConnection.phoneNumber();
//        if(phoneNumber.isEmpty()) {
//            return connection
//                    .unregistered(QrHandler.toTerminal())
//                    .connect()
//                    .join();
//        }
//        if(Whatsapp.isConnected(phoneNumber.get())) {
//            return Whatsapp.get(phoneNumber.get());
//        }
//
//    }
//}
