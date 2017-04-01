package net.daergoth.homewire.statistic;

import com.opencsv.CSVWriter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticDeviceDataService {

  private static final Logger logger = LoggerFactory.getLogger(StatisticDeviceDataService.class);

  private final StatisticDataRepository statisticDataRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public StatisticDeviceDataService(StatisticDataRepository statisticDataRepository,
                                    ModelMapper modelMapper) {
    this.statisticDataRepository = statisticDataRepository;
    this.modelMapper = modelMapper;
  }

  public List<StatisticDataDTO> getStats(DeviceStateEntity.StateInterval stateInterval) {
    return statisticDataRepository
        .getDeviceStateWithInterval(stateInterval)
        .stream()
        .map(entity -> modelMapper.map(entity, StatisticDataDTO.class))
        .collect(Collectors.toList());
  }

  public String exportDataToCsv(Short devId, String devType, String fileNamePrefix) {
    logger.info("Exporting data for device: id:{}, type:{}", devId, devType);
    String fileName =
        (fileNamePrefix + "-" + devId + "-" + devType + "-" + LocalDateTime.now() + ".csv")
            .replace(':', '-');

    try {
      CSVWriter csvWriter =
          new CSVWriter(new FileWriter(new File(System.getProperty("user.dir"), fileName)), '\t',
              '\"', "\r\n");

      statisticDataRepository.getDeviceStateForDevIdAndDevType(devId, devType)
          .forEach(deviceStateEntity -> {
            String stateString[] = {
                deviceStateEntity.getId().toString(),
                deviceStateEntity.getType(),
                deviceStateEntity.getValue().toString(),
                deviceStateEntity.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE),
                deviceStateEntity.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME)
            };

            csvWriter.writeNext(stateString);
          });

      csvWriter.close();
    } catch (IOException e) {
      logger.warn("Export FAILED: {}", e);
      return "";
    }

    return fileName;
  }

}
