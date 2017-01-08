package net.daergoth.homewire.statistic;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TimeSeries {

  private final List<TimeSeriesData> seriesDataList;

  private final String name;

  public TimeSeries(String name) {
    this.name = name;
    this.seriesDataList = new LinkedList<>();
  }

  public TimeSeries(String name, List<TimeSeriesData> seriesData) {
    this.name = name;
    this.seriesDataList = seriesData;
  }

  public void addData(TimeSeriesData seriesData) {
    seriesDataList.add(seriesData);
  }

  public List<TimeSeriesData> getDataList() {
    return seriesDataList;
  }

  public String getName() {
    return name;
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
