package tmcit.hokekyo1210.SolverUI.UI;

import java.awt.Point;
import java.util.List;
import java.util.Locale;

public class ILabelController {
	
	private List<List<ILabel>> labels;
	
	public ILabelController(List<List<ILabel>> imageLabels){
		this.labels = imageLabels;
	}
	
	private int pickedLabelr = 0;
	private int pickedLabelc = 0;
	
	public void pick(int atr,int atc){
		pickedLabelr = atr;
		pickedLabelc = atc;
	}
	public void move(char command){
		ILabel picking = labels.get(pickedLabelr).get(pickedLabelc);
		ILabel to = null;
		int tor = pickedLabelr;int toc = pickedLabelc;
		if(command=='U'){
			tor += -1;
		}else if(command=='D'){
			tor += 1;
		}else if(command=='L'){
			toc += -1;
		}else if(command=='R'){
			toc += 1;
		}
		try{
			to = labels.get(tor).get(toc);
		}catch(Exception e){
			return;
		}
		if(to != null){
			Point pickingLoc = picking.getLocation();
			picking.setLocation(to.getLocation());
			to.setLocation(pickingLoc);
			ILabel a = picking;
			ILabel b = to;
			labels.get(pickedLabelr).set(pickedLabelc, b);
			labels.get(tor).set(toc, a);
			pickedLabelr = tor;
			pickedLabelc = toc;
		}
	}

}
