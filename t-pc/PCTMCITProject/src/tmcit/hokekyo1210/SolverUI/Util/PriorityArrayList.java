package tmcit.hokekyo1210.SolverUI.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class PriorityArrayList<T,Double> {

	private PriorityQueue<Double> values;
	private HashMap<Double,List<T>> map = new HashMap<Double,List<T>>();
	public PriorityArrayList(){
		Comparator<Double> cp = (Comparator<Double>) new Comp();
		values = new PriorityQueue<Double>(1,cp);

	}

	public void addValue(Double priority,T object){
		if(!values.contains(priority)){
			values.add(priority);
		}
		List<T> array = map.get(priority);
		if(array==null){array = new ArrayList<T>();map.put(priority, array);}
		array.add(object);
	}

	public List<T> getSortedObjects(int priority){
		return map.get(values.toArray()[priority]);
	}

	public int size(){
		return values.size();
	}

}

class Comp implements Comparator <Double> {
	public int compare (Double arg1, Double arg2)
	{
		double k1, k2 = 0.0;
		int sw = 0;
		k1 = arg1.doubleValue();
		k2 = arg2.doubleValue();
		if (k1 < k2)
			sw = 1;
		else if (k1 == k2)
			sw = 0;
		else
			sw = -1;
		return sw;
	}
}