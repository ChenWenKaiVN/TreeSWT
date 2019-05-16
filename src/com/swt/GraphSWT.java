package com.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.graph.BreadthFirstSearch;
import com.graph.DepthFirstSearch;
import com.graph.Graph;
import com.tree.util.TreeType;

public class GraphSWT extends ApplicationWindow {
	
	private int screenWidth = 0;
	private int screenHeight = 0;
	private int labelW = 20;
	private int labelH = 20;
	private int startX = 40;
	private int startY = 40;
	private int lableOffsetX = 40;
	private int lableOffsetY = 40;
	private GC gc = null;
	private Group group;
	private Button graphButton1;
	private Button dfsButton;
	private Button bfsButton;
	private Button clearButton;
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
		graphButton1.setText("显示图");
		
		dfsButton = new Button(container, SWT.NONE);
		dfsButton.setBounds(1517, 114, 80, 27);
		dfsButton.setText("DFS");
		
		bfsButton = new Button(container, SWT.NONE);
		bfsButton.setBounds(1517, 154, 80, 27);
		bfsButton.setText("BFS");
		
		clearButton = new Button(container, SWT.NONE);
		clearButton.setBounds(1517, 194, 80, 27);
		clearButton.setText("Clear");
		
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
				Graph g = gp.getG();
				BreadthFirstSearch bfs = new BreadthFirstSearch(g, 0);
				List<Integer> arr = bfs.getBFSVOrder();
				System.out.println("bfs = " + arr);
				drawSearchGraph(arr, lp, gp);
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

}
