package net.daergoth.homewire;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class NetworkServer extends Thread {

  @Autowired
  private SensorDataRepository sensorDataRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private ServerSocket serverSocket;

  private boolean isRunning = true;

  public NetworkServer() throws IOException {
    serverSocket = new ServerSocket(45678);
  }

  @Override
  public void run() {

    System.out.println(
        "Waiting for network connector to connect on port " + serverSocket.getLocalPort() + "...");

    try (Socket clientSocket = serverSocket.accept()) {

      System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
      DataInputStream in = new DataInputStream(clientSocket.getInputStream());
      BufferedReader d = new BufferedReader(new InputStreamReader(in));

      while (isRunning) {
        String dataJson = d.readLine();

        System.out.println("Incoming JSON: " + dataJson);

        handleIncomingData(objectMapper.readValue(dataJson, SensorData.class));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  private void handleIncomingData(SensorData data) {
    System.out.println("Received data: " + data);
    sensorDataRepository
        .addData(new SensorDataEntity<>(data.getId(), data.getType(), data.getValue(),
            Date.from(ZonedDateTime.now().toInstant())));
  }

}
