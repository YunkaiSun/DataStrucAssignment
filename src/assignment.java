import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;


public class assignment {
	public static void main(String[] args){
		//all input files are stored locally
		String inputFilePath = "9.in";
		String outputFilePath = "9.out";
		//the data structure to store the time periods, key: each period's start time, value: each period's end time
		TreeMap<Long, Long> timeTable = new TreeMap<Long, Long>();
		int NumOfPeople = 0;
		long startTime = 0;
		long endTime = 0;
		long startContribution = 0;
		long endContribution = 0;
		long newPeriodStart = 0;
		long newPeriodEnd = 0;
		long totalCoverage = 0;
		//stores each person's unique covering time to the timetable.
		long[] contribution = null;
		
		
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(inputFilePath));
			String line = "";
			
			//first line indicates num of people
			line = br.readLine();
			if (line != null){
				NumOfPeople = Integer.valueOf(line);
			}
			contribution = new long[NumOfPeople];
			for(int i = 0;i<NumOfPeople;i++){
				contribution[i] = 0;
			}
			totalCoverage = 0;
			
			//read the rest of input to get shift start/end times
			int personID = 0;
			while((line = br.readLine())!=null){
				//clear indicators
				startTime = 0;
				endTime = 0;
				startContribution = 0;
				endContribution = 0;
				newPeriodStart = 0;
				newPeriodEnd = 0;
				
				//read input
				String[] lineSplit = line.split(" ");
				startTime = Integer.valueOf(lineSplit[0]);
				endTime = Integer.valueOf(lineSplit[1]);
				//System.out.println(startTime+" "+endTime);
				
				//store into time table
				//process start time
				//if no smaller periods, create this new period
				if (timeTable.floorKey(startTime) == null){
					startContribution = startTime;
					newPeriodStart = startTime;
				}
				//if previous period ends earlier than this start time, create this new period
				else if(timeTable.get(timeTable.floorKey(startTime))<startTime){
					startContribution = startTime;
					newPeriodStart = startTime;
				}
				//if this start time is inside any periods, merge into that period
				else{
					startContribution = timeTable.get(timeTable.floorKey(startTime));
					newPeriodStart = timeTable.floorKey(startTime);
				}
				
				//process end time
				while(startContribution<endTime){
					//if no later periods, end new period creation
					if(timeTable.ceilingKey(startContribution)==null){
						newPeriodEnd = endTime;
						contribution[personID] += endTime - startContribution;
						startContribution = endTime;
					}
					//if later periods starts later than this end time, end new period creation
					else if(timeTable.ceilingKey(startContribution)>endTime){
						newPeriodEnd = endTime;
						contribution[personID] += endTime - startContribution;
						startContribution = endTime;
					}
					//if this end time is within any period, merge into that period
					else{
						newPeriodEnd = timeTable.get(timeTable.ceilingKey(startContribution));
						contribution[personID] += timeTable.ceilingKey(startContribution) - startContribution;
						//"eat" the covered period
						timeTable.remove(timeTable.ceilingKey(startContribution));
						//update the start contribution time to this covering period's end, see what's happened after in while loop's next iteration
						startContribution = newPeriodEnd;
					}
				}
				
				//store the new period to time table
				timeTable.put(newPeriodStart, newPeriodEnd);
				totalCoverage += contribution[personID];
				//System.out.println(timeTable);
				personID++;
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//after reading the input, fire the least contribution person and update total coverage
		Arrays.sort(contribution);
		totalCoverage = totalCoverage - contribution[0];
		System.out.println(totalCoverage);
		
		//write the updated coverage to file
		PrintWriter pw = null;
		try{
			pw = new PrintWriter(new File(outputFilePath));
			pw.write(String.valueOf(totalCoverage));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			pw.close();
		}
		
	}
}
