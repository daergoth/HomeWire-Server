package net.daergoth.homewire.statistic;

import com.vaadin.ui.CustomComponent;
import org.vaadin.highcharts.HighChart;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class TimeSeriesChart extends CustomComponent {

  private HighChart highChart;

  private Map<String, Object> config;

  public TimeSeriesChart(Collection<TimeSeries> seriesList) {
    this.highChart = new HighChart();
    this.config = new HashMap<>();

    highChart.setHcjs(createOptions(seriesList));
    highChart.setWidth("90%");

    setCompositionRoot(highChart);
  }

  private String createOptions(Collection<TimeSeries> seriesList) {
    String pre =
        "var chartType = 'StockChart'; var options = {"
            + "credits: { enabled: false },"

            + "legend: { enabled: true, align: 'right', verticalAlign: 'middle', layout: 'vertical'},"

            + "rangeSelector: { selected: 0, buttons: ["
            + "{type: 'minute', count: 60, text: '1h'},"
            + "{type: 'day', count: 1, text: '1d'},"
            + "{type: 'day', count: 7, text: '1w'},"
            + "{type: 'all', text: 'All'}"
            + "]},"

            + "series: [";

    String post = "]};";

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append(pre);

    for (TimeSeries series : seriesList) {
      stringBuilder
          .append("{ type: 'line', tooltip: {valueDecimals: 1}, showInNavigator: true, name: '")
          .append(series.getName())
          .append("', data: [");

      for (TimeSeries.TimeSeriesData data : series.getDataList()) {
        stringBuilder
            .append("[")
            .append(data.getDate().getTime())
            .append(", ")
            .append(data.getValue())
            .append("],");
      }

      stringBuilder.append("]},");
    }

    stringBuilder.append(post);

    System.err.println(stringBuilder.toString());

    return stringBuilder.toString();
  }


}
