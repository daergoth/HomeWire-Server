package net.daergoth.homewire.statistic;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TimeSeries {

  private final List<TimeSeriesData> seriesDataList;

  private final String id;

  private final String name;

  private final String type;

  public TimeSeries(String id, String name, String type) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.seriesDataList = new LinkedList<>();
  }

  public TimeSeries(String id, String name, String type, List<TimeSeriesData> seriesData) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.seriesDataList = seriesData;
  }

  public void addData(TimeSeriesData seriesData) {
    seriesDataList.add(seriesData);
  }

  public List<TimeSeriesData> getDataList() {
    return seriesDataList;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();

    String unit = "";
    switch (type) {
      case "temperature":
        unit = "°C";
        break;
      case "humidity":
        unit = "%";
        break;
    }

    stringBuilder
        .append("{ id : '")
        .append(id)
        .append("', type: 'line', yAxis: '")
        .append(type)
        .append("', connectNulls: false, tooltip: {valueDecimals: 1, valueSuffix: '")
        .append(unit)
        .append("'}, showInLegend: false, visible: false, showInNavigator: false, name: '")
        .append(name)
        .append("', data: [");

    for (TimeSeries.TimeSeriesData data : seriesDataList) {
      stringBuilder
          .append("[")
          .append(data.getDate().getTime())
          .append(", ")
          .append(data.getValue())
          .append("],");
    }

    stringBuilder.append("]},");

    return stringBuilder.toString();
  }

  public static class TimeSeriesData {
    private final Date date;

    private final Number value;

    public TimeSeriesData(Date date, Number value) {
      this.date = date;
      this.value = value;
    }

    public Date getDate() {
      return date;
    }

    public Number getValue() {
      return value;
    }
  }

}
