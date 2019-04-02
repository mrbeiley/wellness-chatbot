import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class MoodGraph extends JFrame{
	private static final long serialVersionUID = 1L;
	private Account account;
	
	public MoodGraph(String applicationTitle, String chartTitle, Account account) {
        super(applicationTitle);
        this.account = account;
        
        // This will create the dataset
        DefaultCategoryDataset dataset = createDataset();
        
        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset, chartTitle);
        
        int width = 640;
        int height = 480;
        File lineChart = new File("LineChart" + account.getUsername() + ".jpeg");
        try {
			ChartUtils.saveChartAsJPEG(lineChart, chart, width, height);
			System.out.println("Mood graph saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
//        // we put the chart into a panel
//        ChartPanel chartPanel = new ChartPanel(chart);
//        
//        // default size
//        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//        
//        // add it to our application
//        setContentPane(chartPanel);

    }

    /**
     * Creates a sample dataset
     */
    @SuppressWarnings("deprecation")
	private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        ArrayList<Mood> moods = account.getRecentMoods();
        
        HashMap<Integer, String> numToDay = new HashMap<Integer, String>();
        numToDay.put(0, "Sun");
        numToDay.put(1, "Mon");
        numToDay.put(2, "Tue");
        numToDay.put(3, "Wed");
        numToDay.put(4, "Thu");
        numToDay.put(5, "Fri");
        numToDay.put(6, "Sat");
        
        HashMap<Integer, String> hourToTime = new HashMap<Integer, String>();
        hourToTime.put(0, "12 AM");
        hourToTime.put(1, "1 AM");
        hourToTime.put(2, "2 AM");
        hourToTime.put(3, "3 AM");
        hourToTime.put(4, "4 AM");
        hourToTime.put(5, "5 AM");
        hourToTime.put(6, "6 AM");
        hourToTime.put(7, "7 AM");
        hourToTime.put(8, "8 AM");
        hourToTime.put(9, "9 AM");
        hourToTime.put(10, "10 AM");
        hourToTime.put(11, "11 AM");
        hourToTime.put(12, "12 PM");
        hourToTime.put(13, "1 PM");
        hourToTime.put(14, "2 PM");
        hourToTime.put(15, "3 PM");
        hourToTime.put(16, "4 PM");
        hourToTime.put(17, "5 PM");
        hourToTime.put(18, "6 PM");
        hourToTime.put(19, "7 PM");
        hourToTime.put(20, "8 PM");
        hourToTime.put(21, "9 PM");
        hourToTime.put(22, "10 PM");
        hourToTime.put(23, "11 PM");
        
        for(int i=moods.size()-1; i>=0; i--) {
        	String date = "";
        	
        	// Turns day as an int to String, add to 'date'
        	date = numToDay.get(moods.get(i).getCreation().getDay());
        	date += " @ ";
        	date += hourToTime.get(moods.get(i).getCreation().getHours());
        	
        		
        	System.out.println(date);
        	System.out.println(i);
        	result.addValue(i*10, "mood", date);
        }

        return result;
    }


	/**
     * Creates a chart
     */
    private JFreeChart createChart(DefaultCategoryDataset dataset, String title) {

        JFreeChart chart = ChartFactory.createLineChart(title, "Days", "Mood", dataset);

        return chart;
    }


}
