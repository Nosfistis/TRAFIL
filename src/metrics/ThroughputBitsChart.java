package metrics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import src.MetaDataHandler;
import src.TraceFileInfo;
import utilities.DatabaseConnection;

/**
 *
 * @author Drakoulelis
 */
public class ThroughputBitsChart {

    private Double startTime, endTime;
    private ResultSet rs;
    private Statement st;
    private JFreeChart chart;
    private XYSeries series = new XYSeries("Throughput(bits/sec)");
    private Collection<XYSeries> seriesList = new ArrayList<XYSeries>();

    public JFreeChart getChart() {
	return chart;
    }

    /**
     * This constructor creates the throughput bits chart between two nodes that
     * are selected by the user via the GUI.
     *
     * @param startNode the start node selected by the user
     * @param endNode the end node selected by the user
     * @param level the trace level to which the information will refer.
     * @param sampleRate the time interval in which the information will be
     * collected
     * @param metaHandler the meta handler instance for the current trace file.
     * @param traceFile the trace file instance for the current trace file.
     */
    public ThroughputBitsChart(int startNode, int endNode, String level, int sampleRate, MetaDataHandler metaHandler, TraceFileInfo traceFile) {
	try {
	    int packetNumber = 0;
	    double throughput;
	    double currentTime;
	    double packetsSentSize = 0.0;
	    XYDataset xyDataset;
	    series.add(0, 0);
	    st = DatabaseConnection.getSt();
	    if (metaHandler.getNode().indexOf("SourceNode") != -1) {
		if (level.equalsIgnoreCase("Link Layer")) {
		    st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " and "
			    + "" + metaHandler.getReceivedPackets().get(0) + "=" + endNode + ";");
		    rs = st.getResultSet();
		    while (rs.next()) {
			startTime = rs.getDouble(1);
			endTime = rs.getDouble(2);
		    }
		    if ((startTime == null) || (endTime == null)) {
			return;
		    }

		    currentTime = startTime;

		    while (endTime > currentTime) {
			st.execute("select distinct " + metaHandler.getReceivedPackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " as main "
				+ " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getGeneratePackets().get(0) + ""
				+ "=" + startNode + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and event='r';");

			rs = st.getResultSet();
			packetsSentSize = 0.0;
			while (rs.next()) {
			    packetsSentSize += rs.getDouble(2);
			}
			if ((currentTime + sampleRate) < endTime) {
			    throughput = packetsSentSize / (sampleRate);
			} else {
			    throughput = packetsSentSize / (endTime - currentTime);
			}

			currentTime += sampleRate;
			series.add(currentTime, throughput);
		    }
		} else {
		    st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and "
			    + "" + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ";");
		    rs = st.getResultSet();
		    while (rs.next()) {
			startTime = rs.getDouble(1);
			endTime = rs.getDouble(2);
		    }
		    if ((startTime == null) || (endTime == null)) {
			return;
		    }

		    currentTime = startTime;
		    while (endTime > currentTime) {
			st.execute("select distinct " + metaHandler.getReceivedPackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " as main  "
				+ " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getReceivedPackets().get(0) + ""
				+ "=" + endNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and event='r' and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ";");

			rs = st.getResultSet();
			packetsSentSize = 0.0;
			while (rs.next()) {
			    packetsSentSize += rs.getInt(1);
			}
			if ((currentTime + sampleRate) < endTime) {
			    throughput = packetsSentSize / (sampleRate);
			} else {
			    throughput = packetsSentSize / (endTime - currentTime);
			}

			currentTime += sampleRate;
			series.add(currentTime, throughput);
		    }
		}
	    } else {
		st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode + " and "
			+ "" + metaHandler.getReceivedPackets().get(2) + "=" + endNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and " + metaHandler.getNode().get(1) + "='" + level + "';");
		rs = st.getResultSet();
		while (rs.next()) {
		    startTime = rs.getDouble(1);
		    endTime = rs.getDouble(2);
		}
		if ((startTime == null) || (endTime == null)) {
		    return;
		}

		currentTime = startTime;

		while (endTime > currentTime) {
		    st.execute("select distinct " + metaHandler.getReceivedPackets().get(0) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " as main "
			    + " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getGeneratePackets().get(2) + ""
			    + "=" + startNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and event='r' and " + metaHandler.getReceivedPackets().get(2) + "=" + endNode + " and " + metaHandler.getNode().get(1) + "='" + level + "';");
		    rs = st.getResultSet();
		    packetsSentSize = 0.0;
		    while (rs.next()) {
			packetsSentSize += rs.getInt(1);
		    }

		    if ((currentTime + sampleRate) < endTime) {
			throughput = packetsSentSize / (sampleRate);
		    } else {
			throughput = packetsSentSize / (endTime - currentTime);
		    }

		    currentTime += sampleRate;
		    series.add(currentTime, throughput);
		}
	    }

	    seriesList.add(series);
	    System.out.println("Filling chart...");

	    //xyDataset = new XYSeriesCollection(series);
	    xyDataset = createDataset();
	    chart = ChartFactory.createXYLineChart(null, "Time(seconds)", "Throughput(bits/seconds)", xyDataset, PlotOrientation.VERTICAL, true, false, false);
	    chart.setTitle(new org.jfree.chart.title.TextTitle("Throughput vs Time", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16)));
	} catch (SQLException ex) {
	    ex.getSQLState();
	    ex.printStackTrace();

	}
    }

    /**
     * This constructor is employed when a throughput bits chart for a specific
     * node must be created.
     *
     * @param startNode the node the user selected
     * @param level the trace level to which the information will refer.
     * @param sampleRate the time interval un which the information will be
     * collected
     * @param metaHandler the meta handler instance for the current trace file.
     * @param traceFile the trace file instance for the current trace file.
     */
    public ThroughputBitsChart(int startNode, String level, int sampleRate, MetaDataHandler metaHandler, TraceFileInfo traceFile) {
	try {
	    int packetNumber = 0;
	    double throughput;
	    double currentTime;
	    double packetsSentSize = 0.0;
	    XYDataset xyDataset;
	    series.add(0, 0);
	    st = DatabaseConnection.getSt();
	    if (metaHandler.getNode().indexOf("SourceNode") != -1) {
		if (level.equalsIgnoreCase("Link Layer")) {
		    st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + ";");
		    rs = st.getResultSet();
		    while (rs.next()) {
			startTime = rs.getDouble(1);
			endTime = rs.getDouble(2);
		    }
		    if ((startTime == null) || (endTime == null)) {
			return;
		    }

		    currentTime = startTime;

		    while (endTime > currentTime) {
			st.execute("select distinct " + metaHandler.getReceivedPackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " as main "
				+ " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getGeneratePackets().get(0) + ""
				+ "=" + startNode + " and event='r';");

			rs = st.getResultSet();
			packetsSentSize = 0.0;
			while (rs.next()) {
			    packetsSentSize += rs.getDouble(2);
			}
			if ((currentTime + sampleRate) < endTime) {
			    throughput = packetsSentSize / (sampleRate);
			} else {
			    throughput = packetsSentSize / (endTime - currentTime);
			}

			currentTime += sampleRate;
			series.add(currentTime, throughput);
		    }
		} else {
		    st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ";");
		    rs = st.getResultSet();
		    while (rs.next()) {
			startTime = rs.getDouble(1);
			endTime = rs.getDouble(2);
		    }
		    if ((startTime == null) || (endTime == null)) {
			return;
		    }

		    currentTime = startTime;
		    while (endTime > currentTime) {
			st.execute("select distinct " + metaHandler.getReceivedPackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " as main  "
				+ " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and event='r' and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ";");

			rs = st.getResultSet();
			packetsSentSize = 0.0;
			while (rs.next()) {
			    packetsSentSize += rs.getInt(1);
			}
			if ((currentTime + sampleRate) < endTime) {
			    throughput = packetsSentSize / (sampleRate);
			} else {
			    throughput = packetsSentSize / (endTime - currentTime);
			}

			currentTime += sampleRate;
			series.add(currentTime, throughput);
		    }
		}
	    } else {
		st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode
			+ " and " + metaHandler.getNode().get(1) + "='" + level + "';");
		rs = st.getResultSet();
		while (rs.next()) {
		    startTime = rs.getDouble(1);
		    endTime = rs.getDouble(2);
		}
		if ((startTime == null) || (endTime == null)) {
		    return;
		}

		currentTime = startTime;

		while (endTime > currentTime) {
		    st.execute("select distinct " + metaHandler.getReceivedPackets().get(0) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " as main "
			    + " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getGeneratePackets().get(2) + ""
			    + "=" + startNode + " and " + metaHandler.getNode().get(1) + "='" + level + "';");
		    rs = st.getResultSet();
		    packetsSentSize = 0.0;
		    while (rs.next()) {
			packetsSentSize += rs.getInt(1);
		    }

		    if ((currentTime + sampleRate) < endTime) {
			throughput = packetsSentSize / (sampleRate);
		    } else {
			throughput = packetsSentSize / (endTime - currentTime);
		    }

		    currentTime += sampleRate;
		    series.add(currentTime, throughput);
		}
	    }

	    xyDataset = new XYSeriesCollection(series);
	    chart = ChartFactory.createXYLineChart(null, "Time(seconds)", "Throughput(bits/seconds)", xyDataset, PlotOrientation.VERTICAL, true, false, false);
	    chart.setTitle(new org.jfree.chart.title.TextTitle("Throughput vs Time", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16)));
	} catch (SQLException ex) {
	    ex.getSQLState();
	    ex.printStackTrace();

	}
    }

    private XYDataset createDataset() {
	final XYSeriesCollection dataset = new XYSeriesCollection();
	for (XYSeries xy : seriesList) {
	    dataset.addSeries(xy);
	}

	return dataset;
    }
}
