package tmcit.hokekyo1210.SolverUI.UI;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.TransferHandler;

import tmcit.hokekyo1210.SolverUI.Main;
import tmcit.hokekyo1210.SolverUI.Util.HttpUtil;

public class DropFileHandler extends TransferHandler{

	private MainFrame frame;

	public DropFileHandler(MainFrame mainFrame) {
		this.frame = mainFrame;
	}

	@Override
    public boolean canImport(TransferSupport support) {
		if(!support.isDrop()){
			return false;
		}
        return true;
    }

	@Override
	public boolean importData(TransferSupport support){
		if(!canImport(support)){
			return false;
		}
		Transferable t = support.getTransferable();
		try {
			@SuppressWarnings("unchecked")
			List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
			if(files.size()==1){
				File file = files.get(0);
				if(isImage(file)){
					frame.sendImage(file.getAbsolutePath());
				}
				if(isText(file)){
					HttpUtil hutil = new HttpUtil(Main.teamToken,Main.problemID,readTxt(file));
					hutil.sendAnswer();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean isImage(File file){
		String fileName = file.getName();
		if(fileName.indexOf(".png")!=-1||fileName.indexOf(".jpg")!=-1||fileName.indexOf(".ppm")!=-1){
			return true;
		}
		return false;
	}
	public static boolean isText(File file){
		String fileName = file.getName();
		if(fileName.indexOf(".txt")!=-1){
			return true;
		}
		return false;
	}

	public static String readTxt(File file) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String answer = "";
		String str;
		while((str = br.readLine()) != null){
			answer+=str+System.getProperty("line.separator");
		}
		br.close();
		System.out.println(answer);
		return answer;
	}


}
