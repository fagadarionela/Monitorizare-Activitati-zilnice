package Main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import MonitoredData.MonitoredData;

public class Main {
	public static void main(String args[]) {
		MonitoredData<?> md = new MonitoredData();
		List<MonitoredData> mdList = new ArrayList<MonitoredData>();
		mdList = md.readActivities();
		//mdList.forEach(System.out::println); 
		
		System.out.println(md.howManyDays(mdList));
		HashMap<String,Integer> countAct = new HashMap<String,Integer>();
		countAct = md.howManyActivities(mdList);
		System.out.println(countAct.toString());
		md.howManyActivitiesPerDay(mdList);
		System.out.println(md.duration(mdList));
		md.durataTotala(mdList);
		md.ex7(mdList);
	}
}
