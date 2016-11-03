package net.daergoth.homewire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class NetworkServer extends Thread {

  @Autowired
  private SensorDataRepository sensorDataRepository;

  private ServerSocket serverSocket;

  private boolean isRunning = true;

  public NetworkServer() throws IOException {
    serverSocket = new ServerSocket(45678);
  }

  @Override
  public void run() {

    System.out.println("Waiting for network connector to connect on port " + serverSocket.getLocalPort() + "...");

    try (Socket clientSocket = serverSocket.accept()) {

      System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
      DataInputStream in = new DataInputStream(clientSocket.getInputStream());

      while (isRunning) {
        byte[] buffer = new byte[128];
        if (in.read(buffer) > 0) {
          ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
          byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
          handleIncomingData(byteBuffer.getFloat());
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  private void handleIncomingData(Float data) {
    System.out.println("Received data: " + data);
    sensorDataRepository.addData(new SensorDataEntity<>(data, Date.from(ZonedDateTime.now().toInstant())));
  }

}
