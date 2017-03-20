package net.daergoth.homewire.statistic;

import com.vaadin.ui.CustomComponent;
import org.vaadin.highcharts.HighChart;

import java.util.Collection;


public class TimeSeriesChart extends CustomComponent {

  private HighChart highChart;

  public TimeSeriesChart(Collection<TimeSeries> seriesList) {
    this.highChart = new HighChart();

    highChart.setHcjs(createOptions(seriesList));
    highChart.setWidth("100%");

    setCompositionRoot(highChart);
  }

  private String createOptions(Collection<TimeSeries> seriesList) {
    String pre =
        "var chartType = 'StockChart'; var options = {"
            + "credits: { enabled: false },"

            + "yAxis: ["
            + "{ id: 'temperature', labels: { format: '{value:.1f}°C' }, title: { text: 'Temperature'}},"
            + "{ id: 'humidity', opposite: false, labels: { format: '{value:.1f}%' }, title: { text: 'Humidity'}},"
            + "{ id: 'soilmoisture', labels: { format: '{value:.1f}%' }, title: { text: 'Soil moisture'}},"
            + "{ id: 'motion', opposite: false, title: { text: 'Motion'}},"
            + "{ id: 'relay', title: { text: 'Relay'}}"
            + "],"

            + "legend: { enabled: true, align: 'center', verticalAlign: 'bottom', layout: 'horizontal'},"

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
      stringBuilder.append(series.toString());
    }

    stringBuilder.append(post);

    return stringBuilder.toString();
  }

  public void setSeriesVisibility(String id, boolean isVisible) {
    highChart
        .manipulateChart("var series = chart.get('" + id + "'); "
            + "series.setVisible(" + String.valueOf(isVisible) + ");"
            + "series.update({showInLegend: " + String.valueOf(isVisible) + ", showInNavigator: "
            + String.valueOf(isVisible) + "});");
  }


}
