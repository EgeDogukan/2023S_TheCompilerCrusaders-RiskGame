package uipackage;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import RiskPackage.Player;
import RiskPackage.Territory;
import java.util.Collections;
import javax.imageio.ImageIO;

public class WorldMap {

    private JComponent ui = null;
    JLabel output = new JLabel();
    public static final int SIZE = 750;
    BufferedImage image;
    Area area;
    ArrayList<Shape> shapeList2=null;
    ArrayList<Shape> shapeList = new ArrayList<>();
    static ArrayList<Color> colorList = new ArrayList<>();
    private Shape clickedShape;
    private MouseListener ml;
    private BufferedImage bi;
    private Graphics2D g;
    public static int clickedShapeIndex;
    final int areaThreshold = 450;
    public static boolean isEveryTerritorySelected = false;
    public int numofSelectedTerritory = 0;
    private boolean isInBuildingMode = true;
    private ArrayList<Shape> selectedShapeList = new ArrayList<>();
    

    public WorldMap() {
        try {
            initUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public final void initUI() throws Exception {
        if (ui != null) {
            return;
        }
        //URL url = new URL("https://i.stack.imgur.com/N4eOn.png");
        //image = ImageIO.read(url);
        try
        {
            String cwd = System.getProperty("user.dir");
            image = ImageIO.read(new File(cwd+"/MAP3.png"));
            

        }
        catch (IOException e) {
            System.out.println("couldnt load map image!");
        }

        long then = System.currentTimeMillis();
        System.out.println("" + then);
        area = getOutline(Color.WHITE, image, 12);
        long now = System.currentTimeMillis();
        System.out.println("Time in mins: " + (now - then) / 60000d);
        shapeList2 = separateShapeIntoRegions(area);
        System.out.println("boyut:"+shapeList2.size());
        for(int i = 0; i < shapeList2.size(); i++){
            Shape shape =shapeList2.get(i);
            
            if((int) (shape.getBounds2D().getHeight()*shape.getBounds2D().getWidth()) > areaThreshold)
                shapeList.add(shape);
        }
        
        System.out.println("boyut2:"+shapeList.size());
        shapeList= new ArrayList<>(shapeList.subList(0, Math.min(shapeList.size(), 10)));
        System.out.println("boyut3:"+shapeList.size());
        
        ui = new JPanel(new BorderLayout(4, 4));
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));
        
        output.addMouseMotionListener(new MousePositionListener());
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	System.out.println("Butona basildi");
                Point p = MouseInfo.getPointerInfo().getLocation();
                Point p1 = output.getLocationOnScreen();
                
                int x = p.x - p1.x;
                int y = p.y - p1.y;
                Point pointOnImage = new Point(x, y);

                if(isInBuildingMode == true) {
                    for (Shape shape : shapeList) {
                        if (shape.contains(pointOnImage)) {   
                            JOptionPane.showMessageDialog(null, "Clicked!"); 
                            numofSelectedTerritory++;
                            if (numofSelectedTerritory==shapeList.size()) 
                                isEveryTerritorySelected=true;
                                                    
                            clickedShape = shape;
                            WorldMap.clickedShapeIndex=shapeList.indexOf(shape);
                            BuildingModeNew.nextTurn();
                            selectedShapeList.add(shape);
    
                            break;
                        }
                    }
                }
                else if (isInBuildingMode == false) {
                    //running mode functions
                }
                Territory.setClickedShape(clickedShape);
            }
            
            public void mousePressed(MouseEvent e) {
            	
            	Point p = MouseInfo.getPointerInfo().getLocation();
                Point p1 = output.getLocationOnScreen();
                
                int x = p.x - p1.x;
                int y = p.y - p1.y;
                Point pointOnImage = new Point(x, y);
                
                if (e.getButton() == MouseEvent.BUTTON3) {
                    System.out.println("right click is added.");
                    if(isInBuildingMode == true) {
                        for (Shape shape : shapeList) {
                            if (shape.contains(pointOnImage)) {   
                                JOptionPane.showMessageDialog(null, "Clicked!"); 
                                numofSelectedTerritory++;
                                if (numofSelectedTerritory==shapeList.size()) 
                                    isEveryTerritorySelected=true;
                                                        
                                clickedShape = shape;
                                setIndexColor(shapeList.indexOf(shape), Color.gray);
        
                                break;
                            }
                        }
                    }
                }
                
                
            }
        };
        output.addMouseListener(ml);
        ui.add(output);
        refresh();
    }

    public Area getOutline(Color target, BufferedImage bi, int tolerance) {
        // construct the GeneralPath
        GeneralPath gp = new GeneralPath();

        boolean cont = false;
        for (int xx = 0; xx < bi.getWidth(); xx++) {
            for (int yy = 0; yy < bi.getHeight(); yy++) {
                if (isIncluded(new Color(bi.getRGB(xx, yy)), target, tolerance)) {
                    //if (bi.getRGB(xx,yy)==targetRGB) {
                    if (cont) {
                        gp.lineTo(xx, yy); gp.lineTo(xx, yy + 1); gp.lineTo(xx + 1, yy + 1); gp.lineTo(xx + 1, yy); gp.lineTo(xx, yy);
                    } else {
                        gp.moveTo(xx, yy);
                    }
                    cont = true;
                } else {
                    cont = false;
                }
            }
            cont = false;
        }
        gp.closePath();

        // construct the Area from the GP & return it
        return new Area(gp);
    }

    public static ArrayList<Shape> separateShapeIntoRegions(Shape shape) {
        ArrayList<Shape> regions = new ArrayList<>();

        PathIterator pi = shape.getPathIterator(null);
        GeneralPath gp = new GeneralPath();
        int k = 0;
        while (!pi.isDone()) {
            double[] coords = new double[6];
            int pathSegmentType = pi.currentSegment(coords);
            int windingRule = pi.getWindingRule();
            gp.setWindingRule(windingRule);
            if (pathSegmentType == PathIterator.SEG_MOVETO) {
                gp = new GeneralPath();
                gp.setWindingRule(windingRule);
                gp.moveTo(coords[0], coords[1]);
            } else if (pathSegmentType == PathIterator.SEG_LINETO) {
                gp.lineTo(coords[0], coords[1]);
            } else if (pathSegmentType == PathIterator.SEG_QUADTO) {
                gp.quadTo(coords[0], coords[1], coords[2], coords[3]);
            } else if (pathSegmentType == PathIterator.SEG_CUBICTO) {
                gp.curveTo(
                        coords[0], coords[1],
                        coords[2], coords[3],
                        coords[4], coords[5]);
            } else if (pathSegmentType == PathIterator.SEG_CLOSE) {
                gp.closePath();
                regions.add(new Area(gp));
                colorList.add(k, Color.ORANGE.darker());
                k++;
            } else {
                System.err.println("Unexpected value! " + pathSegmentType);
            }

            pi.next();
        }

        return regions;
    }

    class MousePositionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            refresh();
        }
    }

    public static boolean isIncluded(Color target, Color pixel, int tolerance) {
        int rT = target.getRed();
        int gT = target.getGreen();
        int bT = target.getBlue();
        int rP = pixel.getRed();
        int gP = pixel.getGreen();
        int bP = pixel.getBlue();
        return ((rP - tolerance <= rT) && (rT <= rP + tolerance)
                && (gP - tolerance <= gT) && (gT <= gP + tolerance)
                && (bP - tolerance <= bT) && (bT <= bP + tolerance));
    }

    private void refresh() {
        output.setIcon(new ImageIcon(getImage()));
    }

    private BufferedImage getImage() {
        //bi = new BufferedImage(2 * SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
        bi = new BufferedImage(1000, 500 , BufferedImage.TYPE_INT_RGB);
        int colorIndex;
        g = bi.createGraphics();
        g.drawImage(image, 0, 0, output);
        //g.setColor(Color.ORANGE.darker());
        //g.fill(area);
        g.setColor(Color.RED);
        g.draw(area);

        for(Shape k :shapeList) {
            colorIndex = shapeList.indexOf(k);
            g.setColor(colorList.get(colorIndex));
            g.fill(k);
        }
        
        try {    
            Point p = MouseInfo.getPointerInfo().getLocation();
            Point p1 = output.getLocationOnScreen();
            
            int x = p.x - p1.x;
            int y = p.y - p1.y;
            int areaCounter=0;
            Point pointOnImage = new Point(x, y);
            for (Shape shape : shapeList) {
                areaCounter++;
                if (shape.contains(pointOnImage)) {
                    g.setColor(Color.GREEN.darker());
                    g.fill(shape);
                    break;
                }
            }
            //System.out.println("counter:"+shapeList.size());
        } catch (Exception doNothing) {
        }

        g.dispose();

        return bi;
    }

    public JComponent getUI() {
        return ui;
    }

    
    private void removeMouseListener() {
        output.removeMouseListener(ml);
    }

    public ArrayList<Shape> getShapeList() {
        return shapeList;
    }

    public void removeFromShapeList(Shape shape) {
        shapeList.remove(shape);
    }

    public Shape getClickedShape() {
        return clickedShape;
    }

    public void setShapeColor(Shape shape, Color color) {
        int colorIndex = shapeList.indexOf(shape);
        colorList.set(colorIndex, color);
    }

    public void setIndexColor(int index, Color color) {
    	System.out.println("set index color function is called");
        colorList.set(index, color);
        refresh();
    }
    
    public int getClickedShapeIndex() {
    	return this.clickedShapeIndex;
    }

    public boolean getIsInBuildingMode() {
        return isInBuildingMode;
    }

    public void setIsInBuildingMode(boolean bool) {
        isInBuildingMode = bool;
    }
    
    public void setClickedShapeIndex(int clickedShapeIndex) {
    	this.clickedShapeIndex=clickedShapeIndex;
    }
    
    public MouseListener getMouseListener() {
    	return this.ml;
    }
    
    public void setMouseListener(MouseListener ml) {
    	this.ml=ml;
    }
    
    public void checkIfAllSelected(int numClicked, int numTerr){
		if(numClicked==numTerr){
            isEveryTerritorySelected=true;
            System.out.println("true olduuuuu");
        }
	}

    public boolean getIsAllSelected() {
        return isEveryTerritorySelected;
    }
    
    public int getShapeIndex(Shape shape) {
    	return shapeList.indexOf(shape);
    }
    
    public Shape getShape(int index) {
    	return shapeList.get(index);
    }

    public ArrayList<Color> getColorList() {
        return colorList;
    }
    
    public ArrayList<Shape> getSelectedShapes(){
    	return this.selectedShapeList;
    }
   

	public static void main(String[] args) {
        Runnable r = () -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            WorldMap o = new WorldMap();

            JFrame f = new JFrame(o.getClass().getSimpleName());
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLocationByPlatform(true);

            f.setContentPane(o.getUI());
            f.setResizable(false);
            f.pack();

            f.setVisible(true);
        };
        SwingUtilities.invokeLater(r);
    } 
}