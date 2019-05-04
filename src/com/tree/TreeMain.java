package com.tree;

import java.util.ArrayList;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import com.tree.bst.BSTCoordinate;
import com.tree.bst.BSTCoordinate.Node;
import com.tree.rbt.RBTCoordinate;
import com.tree.util.TreeType;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;

public class TreeMain extends ApplicationWindow {
	
	private Text insertText;
	private Text initArrText;
	private Text deleteText;
	
	private Button initArrButton;
	private Button insertButton;
	private Button deleteButton;
	
	private Group group;
	
	private GC gc = null;
	private int screenWidth = 0;
	private int screenHeight = 0;
	
	List<List<BSTCoordinate<Integer, String>.Node>> bstll = null;
	
	List<List<RBTCoordinate<Integer, String>.Node>> rbtll = null;
	
	int bsth = 0;
	
	int rbth = 0;
	
	private int startX = 40;
	private int startY = 40;
	private int labelW = 20;
	private int labelH = 20;
	private int lableOffsetX = 10;
	private int lableOffsetY = 40;
	List<Integer> arr = new ArrayList<>();
	BSTCoordinate<Integer, String> bst = new BSTCoordinate<>();
	RBTCoordinate<Integer, String> rbt = new RBTCoordinate<>();
	//  1  bst    2  rbt
	private int treeType = 0;

	/**
	 * Create the application window.
	 */
	public TreeMain() {
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
		
		//屏幕分辨率为1920*1040 这里应该改为相对坐标
		
		//初始化
		initArrText = new Text(container, SWT.BORDER);
		initArrText.setBounds(1603, 76, 274, 23);
		initArrText.setText("8,6,10,5,7,1,4,9,12");
		
		initArrButton = new Button(container, SWT.NONE);
		initArrButton.setBounds(1517, 74, 80, 27);
		initArrButton.setText("初始化");
		
		//插入节点
		insertText = new Text(container, SWT.BORDER);
		insertText.setBounds(1603, 123, 274, 23);
		
		insertButton = new Button(container, SWT.NONE);
		insertButton.setBounds(1517, 121, 80, 27);
		insertButton.setText("插入");
		
		//删除节点
		deleteButton = new Button(container, SWT.NONE);
		deleteButton.setBounds(1517, 167, 80, 27);
		deleteButton.setText("删除");
		
		deleteText = new Text(container, SWT.BORDER);
		deleteText.setBounds(1603, 169, 274, 23);
		
		//用来作为节点以及链接的容器
		group = new Group(container, SWT.NONE);
		group.setBounds(10, 10, 1489, 939);
		
		Button treeTypebutton = new Button(container, SWT.NONE);
		treeTypebutton.setBounds(1517, 25, 80, 27);
		treeTypebutton.setText("类型");
		
		Button bstButton = new Button(container, SWT.RADIO);
		bstButton.setBounds(1619, 30, 97, 17);
		bstButton.setText("二叉排序树");
		bstButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//System.out.println("bst");
				treeType = TreeType.BST;
			}
		});
		
		Button rbtButton = new Button(container, SWT.RADIO);
		rbtButton.setBounds(1746, 30, 97, 17);
		rbtButton.setText("红黑树");
		rbtButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//System.out.println("rbt");
				treeType = TreeType.RBT;
			}
		});
		
		initArrButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//清除上一次绘制的连接线
				group.redraw();
				//清除前一次绘制的label组件
				clearComposite(group);
				arr.clear();				
				//初始化arr数组
				String text = initArrText.getText();
				String[] ss = text.split(",");
				for(String s : ss){
					arr.add(Integer.parseInt(s));
				}					
				System.out.println("init arr = " + arr);
				System.out.println("treeType = " + treeType);
				
				if(treeType == TreeType.BST){
					//初始化BST
					bst.clear();
					for(int i=0; i<arr.size(); i++){
						bst.put(arr.get(i), String.valueOf(i));	
					}
					//绘制节点以及链接
					showBSTNode(group);				
					showBSTLink(group);
				}
				
				if(treeType == TreeType.RBT){
					//初始化RBT
					rbt.clear();
					for(int i=0; i<arr.size(); i++){
						rbt.put(arr.get(i), String.valueOf(i));	
					}
					//绘制节点以及链接
					showRBTNode(group);				
					showRBTLink(group);
				}
				
			}
		});
		
		insertButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//清除上一次绘制的连接线
				group.redraw();
				//清除前一次绘制的label组件
				clearComposite(group);
				//获取要添加的节点
				String text = insertText.getText();
				String[] ss = text.split(",");
				
				if(treeType == TreeType.BST){
					for(String s : ss){
						arr.add(Integer.parseInt(s));
						bst.put(Integer.parseInt(s), s);
					}
					//绘制节点以及链接
					showBSTNode(group);				
					showBSTLink(group);
				}
				
				if(treeType == TreeType.RBT){
					for(String s : ss){
						arr.add(Integer.parseInt(s));
						rbt.put(Integer.parseInt(s), s);
					}
					//绘制节点以及链接
					showRBTNode(group);				
					showRBTLink(group);
				}												
				System.out.println("insert = " + arr);		
			}
		});
		
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//清除上一次绘制的连接线
				group.redraw();
				//清除前一次绘制的label组件
				clearComposite(group);
				//获取要删除的节点key集合
				String text = deleteText.getText();
				String[] ss = text.split(",");
				
				if(treeType == TreeType.BST){
					for(String s : ss){
						arr.remove((Object)Integer.parseInt(s));
						bst.delete(Integer.parseInt(s));
					}
					//绘制节点以及链接
					showBSTNode(group);				
					showBSTLink(group);
				}
				
				if(treeType == TreeType.RBT){
					for(String s : ss){
						//List  remove(index)  remove(Object)方法的调用区别
						arr.remove((Object)Integer.parseInt(s));
						rbt.delete(Integer.parseInt(s));
					}
					//绘制节点以及链接
					showRBTNode(group);				
					showRBTLink(group);
				}												
				System.out.println("delete arr = " + arr);
			}
		});
		
		return container;
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
		return null;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		return null;
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
			TreeMain window = new TreeMain();
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
		newShell.setText("TreeShow");
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
	
	private int getNodeLabelStartX(int x){
		return startX + x * lableOffsetX;
	}
	
	private int getNodeLableStartY(int y){
		return startY + y * lableOffsetY;
	}
	
	private void showBSTNode(Group group){
		bstll = bst.calculateNodeCoordinate2();
		bsth = bst.height();
		//绘制节点
		for(int i=1; i<=bsth; i++){
			for(int j=0; j<bstll.get(i-1).size(); j++){	
				Node nc = bstll.get(i-1).get(j);
				if(String.valueOf(nc.key).equals(BSTCoordinate.specialChar))
					continue;
				Label lname = new Label(group, SWT.NONE|SWT.CENTER);				
				lname.setBounds(getNodeLabelStartX(nc.x), getNodeLableStartY(nc.y), labelW, labelH);
				lname.setText(String.valueOf(nc.key));
				//System.out.println("i = " + i + " x = " + nc.getX() + " y = " + nc.getY());
			}
		}
	}
	
	private void showRBTNode(Group group){
		rbtll = rbt.calculateNodeCoordinate2();
		rbth = rbt.height();
		//绘制节点
		for(int i=1; i<=rbth; i++){
			for(int j=0; j<rbtll.get(i-1).size(); j++){	
				RBTCoordinate<Integer, String>.Node nc = rbtll.get(i-1).get(j);
				if(String.valueOf(nc.key).equals(RBTCoordinate.specialChar))
					continue;
				Label lname = new Label(group, SWT.NONE|SWT.CENTER);
				lname.setBounds(getNodeLabelStartX(nc.x), getNodeLableStartY(nc.y), labelW, labelH);
				lname.setText(String.valueOf(nc.key));
				//System.out.println("i = " + i + " x = " + nc.getX() + " y = " + nc.getY());
			}
		}
	}
	
	private void showBSTLink(Group group){
	  //绘制连接	
	  gc = new GC(group);
	  gc.setLineWidth(1);
	  for(int i=1; i<=bsth; i++){
			for(int j=0; j<bstll.get(i-1).size(); j++){			
				Node nc = bstll.get(i-1).get(j);				
				if(nc.left != null) {
					gc.drawLine(
							getNodeLabelStartX(nc.x) + labelW / 2,
							getNodeLableStartY(nc.y) + labelH,
							getNodeLabelStartX(nc.left.x) + labelW / 2,
							getNodeLableStartY(nc.left.y)
							);
				}					
				if(nc.right != null) {
					gc.drawLine(
							getNodeLabelStartX(nc.x)+labelW/2,
							getNodeLableStartY(nc.y)+labelH,
							getNodeLabelStartX(nc.right.x)+labelW/2,
							getNodeLableStartY(nc.right.y)
							);	
				}							
			}
		}//end for 
	}
	
	private void showRBTLink(Group group) {
		// group.redraw();
		// 绘制连接
		gc = new GC(group);
		gc.setLineWidth(2);
		for (int i = 1; i <= rbth; i++) {
			for (int j = 0; j < rbtll.get(i - 1).size(); j++) {
				RBTCoordinate<Integer, String>.Node nc = rbtll.get(i - 1).get(j);
				if (nc.left != null) {
					setGCColorByNode(nc.left);
					gc.drawLine(getNodeLabelStartX(nc.x) + labelW / 2, 
							getNodeLableStartY(nc.y) + labelH,
							getNodeLabelStartX(nc.left.x) + labelW / 2,
							getNodeLableStartY(nc.left.y));
				}
				if (nc.right != null) {
					setGCColorByNode(nc.right);
					gc.drawLine(getNodeLabelStartX(nc.x) + labelW / 2, 
							getNodeLableStartY(nc.y) + labelH,
							getNodeLabelStartX(nc.right.x) + labelW / 2,
							getNodeLableStartY(nc.right.y));

				}
			} // end for
		}
	}
	
	private void setGCColorByNode(RBTCoordinate<Integer, String>.Node nc){
		 if(nc.color)
			 gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		 else
			 gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
	}
}
