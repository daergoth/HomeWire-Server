package net.daergoth.homewire;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class NetworkServer extends Thread {

  private final Logger logger = LoggerFactory.getLogger(NetworkServer.class);

  private final ObjectMapper objectMapper;

  private Consumer<DeviceData> dataProcessor;

  private ServerSocket serverSocket;

  private List<DeviceCommand> commandList;

  private boolean isRunning = true;

  public NetworkServer(ObjectMapper objectMapper) throws IOException {
    this.objectMapper = objectMapper;
    this.dataProcessor = deviceData -> {
    };

    commandList = new CopyOnWriteArrayList<>();

    serverSocket = new ServerSocket(45678);
  }

  public void sendDeviceCommand(DeviceCommand command) {
    logger.info("Sending device command: {}", command.toString());

    commandList.add(command);
  }

  @Override
  public void run() {
    logger.info("Waiting for network connector to connect on port {}...",
        serverSocket.getLocalPort());

    try (Socket clientSocket = serverSocket.accept()) {
      logger.info("Just connected to {}", clientSocket.getRemoteSocketAddress());

      DataInputStream in = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

      BufferedReader d = new BufferedReader(new InputStreamReader(in));

      while (isRunning) {
        for (DeviceCommand command : commandList) {
          logger.info("Sending command: {}", command.toString());

          String json = objectMapper.writeValueAsString(command);

          logger.info("json: {}", json);

          out.writeBytes(json);
          commandList.remove(command);
        }

        if (in.available() > 0) {
          String dataJson = d.readLine();

          logger.info("Incoming message: {}", dataJson);

          dataProcessor.accept(objectMapper.readValue(dataJson, DeviceData.class));
        }

        sleep(10);
      }

    } catch (Exception e) {
      logger.error("Exception during network server loop: {}", e);
      this.start();
    }

  }

  public void setDataProcessor(Consumer<DeviceData> dataProcessor) {
    this.dataProcessor = dataProcessor;
  }
}
