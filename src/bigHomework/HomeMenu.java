package bigHomework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
/**
 * 	这部分是用户成功登录的主界面
 * @author 下一站永远
 *	分为4个大区域 ：
 *	菜单条区barPanel
 *	个人信息区 p1
 *	商品区 p2,searchPanel(搜索区)
 *	展示区 p3
 */
public class HomeMenu {
	public JFrame frame;
	public JMenuBar bar,bar2,bar3;
	public JMenu my,menu,show;
	public JPanel barPanel,panel,p1,p22,p2,p3,searchPanel;
	public JTextField search;
	public JButton jb;
	public  User user;
	public GetConnection getcon = new GetConnection();
	public Statement st = getcon.getStatement();
	public ResultSet rs = null;
	public int num;
	public HomeMenu(User user) {
		super();
		this.user =user;
		frame = new JFrame("小鱼人外卖");
		
		panel = new JPanel(new BorderLayout());
		
		barPanel = new JPanel(new BorderLayout()); 
		barPanel.setPreferredSize(new Dimension(850,30));
		
		bar = new JMenuBar();
		bar.setPreferredSize(new Dimension(60,30));
		bar.setBackground(new Color(255,222,173));
		my = new JMenu(user.getUserName());
		
		bar2 = new JMenuBar();
		bar2.setPreferredSize(new Dimension(270,30));
		bar2.setBackground(new Color(127,255,170));
		menu = new JMenu("商品");	
		
		bar3 = new JMenuBar();
		bar3.setPreferredSize(new Dimension(520,30));
		bar3.setBackground(new Color(0,191,255));
		show = new JMenu("展示");
		
		p1 =new JPanel(new BorderLayout());//个人信息区 
		p1.setPreferredSize(new Dimension(60, 570));
		
		p2 = new JPanel(new BorderLayout());//商品区 
		p2.setPreferredSize(new Dimension(270,570));
		
		p22 = new JPanel();//商品区 
		num = getMenuNum();//获取数据库所有菜的数量
		p22.setPreferredSize(new Dimension(240, num*275));
		p22.setBackground(new Color(240,255,255));
		
		JScrollPane p2scrollPane = new JScrollPane(
                p22,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER//
        );
		p2scrollPane.setPreferredSize(new Dimension(270, 540));
		
		searchPanel = new JPanel(new BorderLayout());//搜索菜品
		searchPanel.setPreferredSize(new Dimension(270, 30));
		search = new JTextField("🔍",15);
		search.setPreferredSize(new Dimension(210, 30));
		jb = new JButton("搜索");
		jb.setPreferredSize(new Dimension(60, 30));
		jb.setForeground(Color.pink);
		jb.setContentAreaFilled(false);
		jb.setBorderPainted(true);
		
		p3 =new JPanel(new FlowLayout(FlowLayout.LEFT));//展示区 p3
		p3.setPreferredSize(new Dimension(700, 1000));
		p3.setBackground(new Color(240,248,250));
		
		JScrollPane p3scrollPane = new JScrollPane(
                p3,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED//HORIZONTAL_SCROLLBAR_NEVER
        );
		p3scrollPane.setPreferredSize(new Dimension(520, 570));
		
		frame.add(panel);
		
		panel.add(barPanel,BorderLayout.NORTH);
		panel.add(p1,BorderLayout.WEST);
		panel.add(p2,BorderLayout.CENTER);
		panel.add(p3scrollPane,BorderLayout.EAST);
		
		barPanel.add(bar,BorderLayout.WEST);
		barPanel.add(bar2,BorderLayout.CENTER);
		barPanel.add(bar3,BorderLayout.EAST);
		
		p2.add(searchPanel,BorderLayout.NORTH);
		p2.add(p2scrollPane,BorderLayout.CENTER);

		bar.add(my);
		bar2.add(menu);
		bar3.add(show);
		
		p1.add(new MyMessage(user,p3));
		
		searchPanel.add(search,BorderLayout.CENTER);
		searchPanel.add(jb,BorderLayout.EAST);
		
		for (int i = 1; i <= num ; i++) {
			p22.add(new FoodPanel(i,p3,user));//把所有的菜展示在商品区供用户浏览
		}
		actionlister();
		initFrame(frame, 850, 630);
	}

	public void actionlister() {
		jb.addMouseListener(new MouseAdapter() //搜索按钮事件，先把所有菜从展示区清除再展示搜索到的内容，点击返回商品区，
		{
			//清空所有搜索商品，回到所有商品中   
			//没有做搜索结果的正则表达式 做模糊搜索，待加入。
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				String text = search.getText();
				
				JButton refood = new JButton("返回全部商品");
				refood.setPreferredSize(new Dimension(120, 30));
				refood.setForeground(Color.pink);
				refood.setContentAreaFilled(false);
				refood.setBorderPainted(true);
				refood.addMouseListener(new MouseAdapter() 
				{

					@Override
					public void mouseReleased(MouseEvent e)
					{
						super.mouseReleased(e);
						p22.removeAll();
						for (int i = 1; i <= num ; i++) {
							p22.add(new FoodPanel(i,p3,user));
							p22.repaint();
							p22.revalidate();
						}
					}
				});
				
				if(isWhat(text,"foodName")) 
				{
					String sql = "select id from menu where foodName ="+"\""+text+"\"";
					p22.removeAll();
					p22.add(refood);
					try {
						rs = st.executeQuery(sql);
						while(rs.next())
						{	
							p22.add(new FoodPanel( rs.getInt("id"), p3,user));
							p22.repaint();
							p22.revalidate();
						}
					} catch (SQLException e1) {
						
						e1.printStackTrace();
					}
				}else if(isWhat(text,"shopName"))
				{
					
					String sql = "select id from menu where shopName ="+"\""+text+"\"";
					p22.removeAll();
					p22.add(refood);
					try {
						rs = st.executeQuery(sql);
						while(rs.next())
						{	
							p22.add(new FoodPanel( rs.getInt("id"), p3,user));
							p22.repaint();
							p22.revalidate();
						}
					} catch (SQLException e1) {
						
						e1.printStackTrace();
					}
				}else if(isWhat(text,"address")) 
				{
					String sql = "select id from menu where address ="+"\""+text+"\"";
					p22.removeAll();
					p22.add(refood);
					try {
						rs = st.executeQuery(sql);
						while(rs.next())
						{	
							p22.add(new FoodPanel( rs.getInt("id"), p3,user));
							p22.repaint();
							p22.revalidate();
						}
					} catch (SQLException e1) {
						
						e1.printStackTrace();
					}
				}else {
					JOptionPane.showMessageDialog(frame,"查询没有结果","通知",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}
	
	/*public static void main(String[] args) {
		User user = new User(0, "asd", "yu", 1234);
		new HomeMenu(user);
	}*/
	
	public Boolean isWhat(String text,String name)
	{
		/**
		 * 判断是店还是地址
		 */
		String sql = "select "+name+" "+"from menu";
		try {
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				if(text.equals(rs.getString(name)))
				{
					return true;
				}
			}
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
		return false;
	}

	public static void initFrame(JFrame frame, int width, int height) 
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();// 获取与系统相关的工具类对象
		Dimension d = toolkit.getScreenSize();// 分辨率
		frame.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
		frame.setVisible(true);
		//frame.setResizable(false); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public int getMenuNum()//获取数据库所有菜的数量
	{
		int num=0;
		GetConnection getcon = new GetConnection();
		Statement st = getcon.getStatement();
		ResultSet rs = null;
		String sql = "select id from menu";
		try {
			rs = st.executeQuery(sql);
			rs.last();    //2、将rs的指标移动到最后一位
			num = rs.getRow();//3、获取当前数据的行数
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
}




