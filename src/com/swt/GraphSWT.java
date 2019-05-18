package com.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import com.graph.BreadthFirstPaths;
import com.graph.BreadthFirstSearch;
import com.graph.CC;
import com.graph.DepthFirstPaths;
import com.graph.DepthFirstSearch;
import com.graph.Graph;

public class GraphSWT extends ApplicationWindow {
	
	private int screenWidth = 0;
	private int screenHeight = 0;
	private int labelW = 20;
	private int labelH = 20;
	private int startX = 40;
	private int startY = 40;
	//无向图x,y偏移量
	private int lableOffsetX = 40;
	private int lableOffsetY = 40;
	private GC gc = null;
	private Group group;
	private Button graphButton1;
	private Button dfsButton;
	private Button bfsButton;
	private Button clearButton;
	private Button singlePathButton;
	private Button shortestSinglePathButton;
	private Button ccButton;
	private Button digraphButton;
	//有向图x,y偏移量
	private int dilableOffsetX = 60;
	private int dilableOffsetY = 60;
	//private Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	//private Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

	/**
	 * Create the application window.
	 */
	public GraphSWT() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		group = new Group(container, SWT.NONE|SWT.SCROLL_LINE);
		group.setBounds(10, 10, 1489, 939);
	
		graphButton1 = new Button(container, SWT.NONE);
		graphButton1.setBounds(1517, 74, 80, 27);
		graphButton1.setText("无向图");
		
		dfsButton = new Button(container, SWT.NONE);
		dfsButton.setBounds(1517, 114, 80, 27);
		dfsButton.setText("DFS");
		
		bfsButton = new Button(container, SWT.NONE);
		bfsButton.setBounds(1517, 154, 80, 27);
		bfsButton.setText("BFS");
		
		singlePathButton = new Button(container, SWT.NONE);
		singlePathButton.setBounds(1517, 194, 80, 27);
		singlePathButton.setText("单点路径");
		
		shortestSinglePathButton = new Button(container, SWT.NONE);
		shortestSinglePathButton.setBounds(1517, 234, 80, 27);
		shortestSinglePathButton.setText("最短单点路径");
		
		ccButton = new Button(container, SWT.NONE);
		ccButton.setBounds(1517, 274, 80, 27);
		ccButton.setText("连通分量");
		
		clearButton = new Button(container, SWT.NONE);
		clearButton.setBounds(1517, 314, 80, 27);
		clearButton.setText("Clear");
		
		digraphButton = new Button(container, SWT.NONE);
		digraphButton.setBounds(1647, 74, 80, 27);
		digraphButton.setText("有向图");
		
		digraphButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//gc对象必须在事件中使用
				DiGraphPoint gp = GraphUtils.getDiGraphPointG1();
				System.out.println("graph = " + gp.getG().toString());
				List<PointG> lp = gp.getLp();				
				drawDiGraph(lp, gp);
			}
		});
		
		graphButton1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//gc对象必须在事件中使用
				GraphPoint gp = GraphUtils.getGraphPointG1();
				System.out.println("graph = " + gp.getG().toString());
				List<PointG> lp = gp.getLp();
				drawGraph(lp, gp);
			}
		});
		
		dfsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GraphPoint gp = GraphUtils.getGraphPointG1();
				List<PointG> lp = gp.getLp();
				drawGraph(lp, gp);
				Graph g = gp.getG();
				DepthFirstSearch dfs = new DepthFirstSearch(g, 0);
				List<Integer> arr = dfs.getDFSVOrder();
				System.out.println("dfs = " + arr);
				drawSearchGraph(arr, lp, gp);	
			}
		});
		
		bfsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {		
				GraphPoint gp = GraphUtils.getGraphPointG1();
				List<PointG> lp = gp.getLp();
				drawGraph(lp, gp);
				Graph g = gp.getG();			
				BreadthFirstSearch bfs = new BreadthFirstSearch(g, 0);
				List<Integer> arr = bfs.getBFSVOrder();
				System.out.println("bfs = " + arr);
				drawSearchGraph(arr, lp, gp);
			}
		});
		
		singlePathButton.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent e) {	
				GraphPoint gp = GraphUtils.getGraphPointG2();
				List<PointG> lp = gp.getLp();
				drawGraph(lp, gp);
				Graph g = gp.getG();
				DepthFirstPaths dfp = new DepthFirstPaths(g, 0);
				drawSinglePathGraph(0, lp, gp, dfp);		
			}
		});
		
		shortestSinglePathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	
				GraphPoint gp = GraphUtils.getGraphPointG2();
				List<PointG> lp = gp.getLp();
				drawGraph(lp, gp);
				Graph g = gp.getG();
				BreadthFirstPaths bfp = new BreadthFirstPaths(g, 0);
				drawShortestSinglePathGraph(0, lp, gp, bfp);		
			}
		});
		
		ccButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	
				GraphPoint gp = GraphUtils.getGraphPointG3();
				List<PointG> lp = gp.getLp();
				drawGraph(lp, gp);
				Graph g = gp.getG();
				CC cc = new CC(g);
				drawCCGraph(lp, gp, cc);
			}
		});
		
		clearButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {			
				clearComposite(group);	
				//清除上一次绘制的连接线
				group.redraw();
			}
		});
		
		return container;
	}
	
	private void clearComposite(Composite container){
		Control[] child = container.getChildren();
		for(int i=0; i<child.length; i++){
			//System.out.println(child[i].getSize());
			if(child[i] instanceof Composite){
				Control[] child1 = ((Composite) child[i]).getChildren();
				for(int j=0; j<child1.length; j++){
					child1[j].dispose();
				}
			}else{
				child[i].dispose();
			}
		}
	}
	
	//绘制有向图
	private void drawDiGraph(List<PointG> lp, DiGraphPoint gp){
		for(int i=0; i<lp.size(); i++){
			Button lname = new Button(group, SWT.NONE|SWT.CENTER);
			lname.setBounds(startX+lp.get(i).x*dilableOffsetX, startY+lp.get(i).y*dilableOffsetY, labelW, labelH);
			lname.setText(String.valueOf(i));
			lname.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			lname.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		}
		gc = new GC(group);
		gc.setLineWidth(2);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		int[] vr = gp.getVr();
		int[] wr = gp.getWr();
		for(int i=0; i<gp.getG().E(); i++){
			PointG p1 = lp.get(vr[i]);
			PointG p2 = lp.get(wr[i]);
			int x1 = startX+p1.x*dilableOffsetX+labelW/2;
			int y1 = startY+p1.y*dilableOffsetY+labelH/2;
			int x2 = 0;
			int y2 = 0;
			//由于箭头绘制特殊性，坐标需要考虑起始点以及终点位置
			//终点在起始点左边   y<--x
			if(p2.x<=p1.x && p2.y==p1.y){
				x2 = startX+p2.x*dilableOffsetX+labelW;
				y2 = startY+p2.y*dilableOffsetY+labelH/2;
			}			
			//终点在起始点右边 x-->y
			else if(p2.x>=p1.x && p2.y==p1.y){
				x2 = startX+p2.x*dilableOffsetX;
				y2 = startY+p2.y*dilableOffsetY+labelH/2;			
			}
			//终点在起始点上边方 x-->y
			else if(p2.x==p1.x && p2.y<=p1.y){
				x2 = startX+p2.x*dilableOffsetX+labelW/2;
				y2 = startY+p2.y*dilableOffsetY+labelH;
			}
			//终点在起始点下边方 x-->y
			else if(p2.x==p1.x && p2.y>=p1.y){
				x2 = startX+p2.x*dilableOffsetX+labelW/2;
				y2 = startY+p2.y*dilableOffsetY;
			}
			//终点在起始点左上方 x-->y
			else if(p2.x<=p1.x && p2.y<=p1.y){
				x2 = startX+p2.x*dilableOffsetX+labelW;
				y2 = startY+p2.y*dilableOffsetY+labelH;			
			}
			//终点在起始点左下方 x-->y
			else if(p2.x<=p1.x && p2.y>=p1.y){
				x2 = startX+p2.x*dilableOffsetX+labelW;
				y2 = startY+p2.y*dilableOffsetY;				
			}
			//终点在起始点右上方 x-->y
			else if(p2.x>=p1.x && p2.y<=p1.y){
				x2 = startX+p2.x*dilableOffsetX;
				y2 = startY+p2.y*dilableOffsetY+labelH;
			}
			//终点在起始点右下方 x-->y
			else if(p2.x>=p1.x && p2.y>=p1.y){
				x2 = startX+p2.x*dilableOffsetX;
				y2 = startY+p2.y*dilableOffsetY;	
			}
			paintk(gc, x1, y1, x2, y2);
		}
		gc.dispose();
	}
	
	//绘制原始图
	private void drawGraph(List<PointG> lp, GraphPoint gp){
		for(int i=0; i<lp.size(); i++){
			Button lname = new Button(group, SWT.NONE|SWT.CENTER);
			lname.setBounds(startX+lp.get(i).x*lableOffsetX, startY+lp.get(i).y*lableOffsetY, labelW, labelH);
			lname.setText(String.valueOf(i));
			lname.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			lname.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		}
		gc = new GC(group);
		gc.setLineWidth(2);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		int[] vr = gp.getVr();
		int[] wr = gp.getWr();
		for(int i=0; i<gp.getG().E(); i++){
			PointG p1 = lp.get(vr[i]);
			PointG p2 = lp.get(wr[i]);
			gc.drawLine(
					startX+p1.x*lableOffsetX+labelW/2, 
					startY+p1.y*lableOffsetY+labelH/2, 
					startX+p2.x*lableOffsetX+labelW/2, 
					startY+p2.y*lableOffsetY+labelH/2);
		}
		gc.dispose();
	}
	
	//绘制搜索过程图变化
	private void drawSearchGraph(List<Integer> arr, List<PointG> lp, GraphPoint gp){
		List<Integer> hasdrawed = new ArrayList<>();
		//按照dfs顺序来绘图
		gc = new GC(group);
		gc.setLineWidth(2);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		for(int j=0; j<arr.size(); j++){
			System.out.println(arr.get(j));
			hasdrawed.add(arr.get(j));
			//System.out.println(startX+300*(j/4) + " " + startY+170*(j+1)%4);
			//dfs每一次节点访问都绘制依次图
			//其中已经访问的顶点hasdrawed
			for(int i=0; i<lp.size(); i++){
				Button lname = new Button(group, SWT.NONE|SWT.CENTER);						
				lname.setBounds(
						startX+300*(j/4)+lp.get(i).x*lableOffsetX, 
						startY+170*((j)%4+1)+lp.get(i).y*lableOffsetY, 
						labelW, 
						labelH);
				
				lname.setText(String.valueOf(i));									
				if(hasdrawed.contains(i)){
					lname.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					lname.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				}else{
					lname.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
					lname.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				}
				
			}													
			int[] vr = gp.getVr();
			int[] wr = gp.getWr();
			for(int k=0; k<gp.getG().E(); k++){
				PointG p1 = lp.get(vr[k]);
				PointG p2 = lp.get(wr[k]);
				gc.drawLine(
						startX+300*(j/4)+p1.x*lableOffsetX+labelW/2, 
						startY+170*((j)%4+1)+p1.y*lableOffsetY+labelH/2, 
						startX+300*(j/4)+p2.x*lableOffsetX+labelW/2, 
						startY+170*((j)%4+1)+p2.y*lableOffsetY+labelH/2);
			}		
		}
		gc.dispose();
	}
	
	//绘制单点路径
	private void drawSinglePathGraph(int s, List<PointG> lp, GraphPoint gp, DepthFirstPaths dfp){
		gc = new GC(group);
		gc.setLineWidth(2);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		for(int j=1; j<gp.getG().V(); j++){			
			for(int i=0; i<lp.size(); i++){
				Button lname = new Button(group, SWT.NONE|SWT.CENTER);						
				lname.setBounds(
						startX+300*(j/4)+lp.get(i).x*lableOffsetX, 
						startY+170*((j)%4+1)+lp.get(i).y*lableOffsetY, 
						labelW, 
						labelH);
				
				lname.setText(String.valueOf(i));									
				if(i==s || i==j){
					lname.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					lname.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				}else{
					lname.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
					lname.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				}
				
			}	
			List<Integer> l = dfp.pathTo2(j);
			System.out.println(dfp.pathTo3(j));
			int[] vr = gp.getVr();
			int[] wr = gp.getWr();
			for(int k=0; k<gp.getG().E(); k++){
				PointG p1 = lp.get(vr[k]);
				PointG p2 = lp.get(wr[k]);
				//路径顶点索引是连续的
				//System.out.println(wr[k] + "---" + vr[k]);
				//该边存在于路径中，并且该边的两个顶点在路径中是相邻的
				if(l.contains(wr[k]) && l.contains(vr[k]) && Math.abs(l.indexOf(wr[k])-l.indexOf(vr[k]))==1){
					gc.setLineWidth(3);
					gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				}else{
					gc.setLineWidth(2);
					gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				}
				
				gc.drawLine(
						startX+300*(j/4)+p1.x*lableOffsetX+labelW/2, 
						startY+170*((j)%4+1)+p1.y*lableOffsetY+labelH/2, 
						startX+300*(j/4)+p2.x*lableOffsetX+labelW/2, 
						startY+170*((j)%4+1)+p2.y*lableOffsetY+labelH/2);
			}
		}
		gc.dispose();
	}
	
	//绘制最短单点路径
	private void drawShortestSinglePathGraph(int s, List<PointG> lp, GraphPoint gp, BreadthFirstPaths bfp){
		gc = new GC(group);
		gc.setLineWidth(2);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		for(int j=1; j<gp.getG().V(); j++){			
			for(int i=0; i<lp.size(); i++){
				Button lname = new Button(group, SWT.NONE|SWT.CENTER);						
				lname.setBounds(
						startX+300*(j/4)+lp.get(i).x*lableOffsetX, 
						startY+170*((j)%4+1)+lp.get(i).y*lableOffsetY, 
						labelW, 
						labelH);
				
				lname.setText(String.valueOf(i));									
				if(i==s || i==j){
					lname.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					lname.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				}else{
					lname.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
					lname.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				}
				
			}	
			List<Integer> l = bfp.pathTo2(j);
			System.out.println(bfp.pathTo3(j));
			int[] vr = gp.getVr();
			int[] wr = gp.getWr();
			for(int k=0; k<gp.getG().E(); k++){
				PointG p1 = lp.get(vr[k]);
				PointG p2 = lp.get(wr[k]);
				//路径顶点索引是连续的
				//System.out.println(wr[k] + "---" + vr[k]);
				//该边存在于路径中，并且该边的两个顶点在路径中是相邻的
				if(l.contains(wr[k]) && l.contains(vr[k]) && Math.abs(l.indexOf(wr[k])-l.indexOf(vr[k]))==1){
					gc.setLineWidth(3);
					gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				}else{
					gc.setLineWidth(2);
					gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				}
				
				gc.drawLine(
						startX+300*(j/4)+p1.x*lableOffsetX+labelW/2, 
						startY+170*((j)%4+1)+p1.y*lableOffsetY+labelH/2, 
						startX+300*(j/4)+p2.x*lableOffsetX+labelW/2, 
						startY+170*((j)%4+1)+p2.y*lableOffsetY+labelH/2);
			}
		}
		gc.dispose();
	}
	
	//绘制连通分量
	private void drawCCGraph(List<PointG> lp, GraphPoint gp, CC cc){
		int count = cc.count();
		int[] id = cc.getId();
		System.out.println(Arrays.toString(id));
		gc = new GC(group);
		gc.setLineWidth(2);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		for(int i=0; i<count; i++){
			List<Integer> l = new ArrayList<>();
			for(int j=0; j<id.length; j++){
				if(id[j]==i) l.add(j);
			}
			System.out.println(l);
			int j=0;
			for(int m=0;  m<l.size(); m++){
				Button lname = new Button(group, SWT.NONE|SWT.CENTER);						
				lname.setBounds(
						startX+300*(j/4)+lp.get(l.get(m)).x*lableOffsetX, 
						startY+170*((j)%4+1)+lp.get(l.get(m)).y*lableOffsetY, 
						labelW, 
						labelH);						
				lname.setText(String.valueOf(l.get(m)));									
				lname.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				lname.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			}
			
			int[] vr = gp.getVr();
			int[] wr = gp.getWr();
			for(int k=0; k<gp.getG().E(); k++){
				if(l.contains(vr[k]) && l.contains(wr[k])){
					PointG p1 = lp.get(vr[k]);
					PointG p2 = lp.get(wr[k]);
					gc.drawLine(
							startX+300*(j/4)+p1.x*lableOffsetX+labelW/2, 
							startY+170*((j)%4+1)+p1.y*lableOffsetY+labelH/2, 
							startX+300*(j/4)+p2.x*lableOffsetX+labelW/2, 
							startY+170*((j)%4+1)+p2.y*lableOffsetY+labelH/2);
				}			
			}											
		}
		gc.dispose();
	}


	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			GraphSWT window = new GraphSWT();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		Rectangle area = Display.getDefault().getClientArea();
		this.screenWidth = area.width;
		this.screenHeight = area.height;
		return new Point(screenWidth, screenHeight);
	}
	
	/**
	 * 画带箭头的线
	 */
	public void paintk(GC g, int x1, int y1, int x2, int y2) {

		double H = 10; // 箭头高度
		double L = 7; // 底边的一半
		int x3 = 0;
		int y3 = 0;
		int x4 = 0;
		int y4 = 0;
		double awrad = Math.atan(L / H); // 箭头角度
		double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
		double[] arrXY_1 = rotateVec(x2 - x1, y2 - y1, awrad, true, arraow_len);
		double[] arrXY_2 = rotateVec(x2 - x1, y2 - y1, -awrad, true, arraow_len);
		double x_3 = x2 - arrXY_1[0]; // (x3,y3)是第一端点
		double y_3 = y2 - arrXY_1[1];
		double x_4 = x2 - arrXY_2[0]; // (x4,y4)是第二端点
		double y_4 = y2 - arrXY_2[1];

		Double X3 = new Double(x_3);
		x3 = X3.intValue();
		Double Y3 = new Double(y_3);
		y3 = Y3.intValue();
		Double X4 = new Double(x_4);
		x4 = X4.intValue();
		Double Y4 = new Double(y_4);
		y4 = Y4.intValue();
		// g.setColor(SWT.COLOR_WHITE);
		// 画线
		g.drawLine(x1, y1, x2, y2);
		// 画箭头的一半
		g.drawLine(x2, y2, x3, y3);
		// 画箭头的另一半
		g.drawLine(x2, y2, x4, y4);

	}

	/**
	 * 取得箭头的绘画范围
	 */
	public double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen) {
		double mathstr[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		if (isChLen) {
			double d = Math.sqrt(vx * vx + vy * vy);
			vx = vx / d * newLen;
			vy = vy / d * newLen;
			mathstr[0] = vx;
			mathstr[1] = vy;
		}
		return mathstr;
	}

}
