package net.daergoth.homewire;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.daergoth.homewire.processing.ProcessableSensorDataDTO;
import net.daergoth.homewire.processing.SensorProcessingService;
import net.daergoth.homewire.statistic.StatisticDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZonedDateTime;

@Component
public class NetworkServer extends Thread {

  private final Logger logger = LoggerFactory.getLogger(NetworkServer.class);

  private final StatisticDataRepository statisticDataRepository;

  private final ObjectMapper objectMapper;

  private final SensorProcessingService processingService;

  private ServerSocket serverSocket;

  private boolean isRunning = true;

  @Autowired
  public NetworkServer(StatisticDataRepository statisticDataRepository,
                       ObjectMapper objectMapper,
                       SensorProcessingService processingService) throws IOException {
    this.statisticDataRepository = statisticDataRepository;
    this.objectMapper = objectMapper;
    this.processingService = processingService;

    serverSocket = new ServerSocket(45678);
  }


  @Override
  public void run() {
    logger.info("Waiting for network connector to connect on port {}...",
        serverSocket.getLocalPort());

    try (Socket clientSocket = serverSocket.accept()) {
      logger.info("Just connected to {}", clientSocket.getRemoteSocketAddress());

      DataInputStream in = new DataInputStream(clientSocket.getInputStream());
      BufferedReader d = new BufferedReader(new InputStreamReader(in));

      while (isRunning) {
        String dataJson = d.readLine();

        logger.info("Incoming message: {}", dataJson);

        handleIncomingData(objectMapper.readValue(dataJson, SensorData.class));
      }

    } catch (Exception e) {
      logger.error("Exception during network server loop: {}", e);
      this.start();
    }


  }

  private void handleIncomingData(SensorData data) {
    processingService.processSensorData(
        new ProcessableSensorDataDTO(data.getId(),
            data.getType(),
            data.getValue(),
            ZonedDateTime.now()));
  }

}
