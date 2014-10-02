package tmcit.hokekyo1210.SolverUI.UI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener{
	
	ILabelController controller;

	public MyKeyListener(ILabelController iLabelController) {
		this.controller = iLabelController;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		char c = event.getKeyChar();
		if(c=='a'){
			controller.move('L');
		}else if(c=='d'){
			controller.move('R');
		}else if(c=='w'){
			controller.move('U');
		}else if(c=='s'){
			controller.move('D');
		}else if(c=='\n'){
			
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}

}
