package uipackage;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class IntermediaryBetweenPreBuildingAndBuilding {
	
	BuildingModeNew buildingModeNew;

	public IntermediaryBetweenPreBuildingAndBuilding() throws Exception {
		
		PreBuildingMode preBuildingMode = new PreBuildingMode();
		
		preBuildingMode.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {

				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				buildingModeNew = new BuildingModeNew(preBuildingMode.getNumberOfPlayer(),preBuildingMode.getNumberOfCompPlayer());
				
				System.out.println(preBuildingMode.getNumberOfPlayer()+preBuildingMode.getNumberOfCompPlayer());
				
				
				buildingModeNew.setVisible(true);
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public BuildingModeNew getBuildingModeNew() {
		return this.buildingModeNew;
	}
	
	public static void main(String[] args) throws Exception {
		IntermediaryBetweenPreBuildingAndBuilding screen = new IntermediaryBetweenPreBuildingAndBuilding();
		
	}

}
