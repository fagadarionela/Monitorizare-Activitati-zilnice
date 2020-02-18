package MonitoredData;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonitoredData<R> {
	private static final Consumer<? super MonitoredData> String = null;
	Date startTime;
	Date endTime;
	String activity;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
	String[] activitate= { "Leaving	", "Toileting	", "Showering	", "Sleeping", "Breakfast	", "Lunch", "Dinner", "Snack	", "Spare_Time/TV", "Grooming	"};

	public MonitoredData(){
		super();
	}
	MonitoredData(Date startTime,Date endTime, String activity){
		this.startTime = startTime;
		this.endTime = endTime;
		this.activity = activity;
	}
	
	public List<MonitoredData> readActivities() {
		String fileName = "Activities.txt";
		List<String> list = new ArrayList<>();
		List<MonitoredData> mdList = new ArrayList<MonitoredData>();
		
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
//TODO: https://www.mkyong.com/java8/java-8-stream-read-a-file-line-by-line/
			// convert it into a List
			list = stream.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> listaParsata = new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			listaParsata = Stream.of(list.get(i).split("		"))
				.map(elem -> new String(elem))
				.collect(Collectors.toList());
			
			MonitoredData md;
			try {
				md = new MonitoredData(dateFormat.parse(listaParsata.get(0)),dateFormat.parse(listaParsata.get(1)),listaParsata.get(2));
				mdList.add(md);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mdList;
	}
	//2
	public long howManyDays(List<MonitoredData> mdList) {
		System.out.println("\nCount how many days of monitored data appears in the log. ");
		return mdList.stream().map( m -> m.getStartTime().getDate()).distinct().count();
	}
	//3
	public HashMap<String,Integer> howManyActivities(List<MonitoredData> mdList) {
		System.out.println("\nCount how many times has appeared each activity over the entire monitoring period. ");
		HashMap<String,Integer> map = new HashMap<String,Integer>();
				for(int i=0;i<activitate.length;i++) {
			String a = activitate[i];
			int count =(int) mdList.stream().map(m -> m.getActivity()).filter(m -> m.equals(a)).count();
			map.put(a, count);
		}
		return map;
	}
	//4
	public void howManyActivitiesPerDay(List<MonitoredData> mdList) {
		System.out.println("\nCount how many times has appeared each activity for each day over the monitoring period ");
		List<Integer> date = mdList.stream().map( m -> m.getStartTime().getDate()).distinct().collect(Collectors.toList());
		for(int i=0;i<date.size();i++) {
			int data = date.get(i);
			System.out.println("\nZIUA:"+data);
			List<MonitoredData> activitatizilnice = mdList.stream().filter( m -> m.getStartTime().getDate() == data || m.getEndTime().getDate() == data).collect(Collectors.toList());
			for(int j=0;j<activitate.length;j++) {
				String a = activitate[j];
				System.out.println(a+" "+activitatizilnice.stream().filter( m -> m.getActivity().equals(a)).count());
			}
		}
	}
	//5
	public List<Long> duration(List<MonitoredData> mdList) { // in secunde
		System.out.println("\nFor each line from the file map for the activity label the duration recorded on that line (end_time-start_time)");
		return mdList.stream().map( m-> (m.getEndTime().getTime() - m.getStartTime().getTime())/1000).collect(Collectors.toList());
	}
	
	//6
	public void durataTotala(List<MonitoredData> mdList) {
		System.out.println("\nFor each activity compute the entire duration over the monitoring period");
		for(int i = 0; i<activitate.length;i++) {
			String a = activitate[i];
			//List<MonitoredData> toataActivitatea = mdList.stream().filter( m -> m.getActivity().equals(a)).collect(Collectors.toList());
			//List<Long> durate =  toataActivitatea.stream().map( m-> (m.getEndTime().getTime() - m.getStartTime().getTime())/1000).collect(Collectors.toList());
			//System.out.println(a+" "+durate);
			//System.out.println(a+" "+Math.abs(mdList.stream().mapToLong(Long::longValue).sum()));
			System.out.println(a+" "+Math.abs(mdList.stream().filter( m -> m.getActivity().equals(a)).map( m-> (m.getEndTime().getTime() - m.getStartTime().getTime())/1000).mapToLong(Long::longValue).sum()));
		}
	}
	//7
	public void ex7(List<MonitoredData> mdList) {
		System.out.println("\nFilter the activities that have 90% of the monitoring records with duration less than 5 minutes ");
		/*Long nrActivitatiTotale = mdList.stream().count();
		for(int i = 0; i<activitate.length;i++) {
			boolean deAfisat = false;
			String a = activitate[i];
			long nrActivitate = mdList.stream().filter( m -> m.getActivity().equals(a)).count();
			if (nrActivitate>= (90*nrActivitatiTotale/100)) {
				deAfisat = true;
				List<Long> durate =  mdList.stream().filter( m -> m.getActivity().equals(a)).map( m-> (m.getEndTime().getTime() - m.getStartTime().getTime())/1000).collect(Collectors.toList());
				for(int j=0;j<durate.size();j++)
					if (durate.get(j) > 300) deAfisat = false;
			}
			if (deAfisat == true) System.out.println(a);
		}*/
		for(int i=0;i<activitate.length;i++)
		{
			String a = activitate[i];
			Long durataActivitate = mdList.stream().filter( m -> m.getActivity().equals(a)).count();
			Long durataActivitate5 = mdList.stream().filter( m -> m.getActivity().equals(a)).filter( m-> ((m.getEndTime().getTime() - m.getStartTime().getTime())/1000)<300).count();

			if (durataActivitate5>=90*durataActivitate/100) System.out.println(a);
		}
		
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	@Override
	public String toString() {
		return "MonitoredData [startTime=" + dateFormat.format(startTime) + ", endTime=" + dateFormat.format(endTime) + ", activity=" + activity + "]";
	}
	
	
}
