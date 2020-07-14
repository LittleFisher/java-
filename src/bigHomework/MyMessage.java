package bigHomework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
/**
 *	 个人信息区
 *	点击信息按钮 显示框显示个人信息资料
 *	点击订单 显示框显示历史订单
 *	涉及对象数组 及集合的使用
 * @author 下一站永远
 *
 */
public class MyMessage extends JPanel {
	private static final long serialVersionUID = 1L;
	public JButton myM,myOrder;
	User user;
	public GetConnection getcon = new GetConnection();
	public Statement st = getcon.getStatement();
	public ResultSet rs = null;
	public MyMessage(User user,JPanel p3)
	{
		this.user = user;
		this.setBackground(new Color(0,128,128));
		this.setPreferredSize(new Dimension( 60, 570));
		myM = new JButton("信息");
		myOrder = new JButton("订单");
		butset(myM,0);
		butset(myOrder,30);	
		int balance = getBalance();
		myM.addMouseListener(new MouseAdapter()
		{
			/**
			 * 点击信息,展示区显示个人信息
			 */
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				super.mouseReleased(e);
				p3.removeAll();
				JPanel p = new JPanel(new BorderLayout());//把个人信息放在一个表上
				p.setPreferredSize(new Dimension(700,200));
		        // 表头（列名）
		        Object[] columnNames = {"个人信息","NAME", "Id","telphone","余额"};
		        // 表格所有行数据
		        Object[][] rowData = {{"",user.getUserName(),user.getId(),user.getPhoneNumber(),balance}};
		        // 创建一个表格，指定 所有行数据 和 表头
		        JTable table = new JTable(rowData, columnNames);
		        // 把 表头 添加到容器顶部（使用普通的中间容器添加表格时，表头 和 内容 需要分开添加）
		        p.add(table.getTableHeader(), BorderLayout.NORTH);
		        // 把 表格内容 添加到容器中心
		        p.add(table, BorderLayout.CENTER);
				p3.add(p);
				p3.repaint();
				p3.revalidate();
			}
		});
		
		myOrder.addMouseListener(new MouseAdapter()
		{
			/**
			 * 点击信息,展示区历史订单
			 */
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				super.mouseReleased(e);
				p3.removeAll();
				JPanel p = new JPanel(new BorderLayout());//把个人信息放在一个表上
				p.setPreferredSize(new Dimension(700,1000));
				
		        Object[] columnNames = {"历史订单","时间","店名","菜名" ,"分量","价格"};
		        Object[][] rowData = ToRowData();//先从订单表找出这个所有用户订单的id放在一个集合中
		        //再从每个订单id  获取这一单的数据放在集合中，再放进对象数组中 展示在展示表中
		        JTable table = new JTable(rowData, columnNames);
		        
		        p.add(table.getTableHeader(), BorderLayout.NORTH);
		        p.add(table, BorderLayout.CENTER);p3.repaint();
		        p3.add(p);
				p3.repaint();
				p3.revalidate();
			}
		});
	}
	public int getBalance() 
	{
		/**
		 *	 获取余额
		 */
		String sql = "select balance from user where id ="+user.getId();
		try {
			rs = st.executeQuery(sql);
			while(rs.next())
			{	
				return rs.getInt("balance");
			}
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
		return 0;
	}
	
	public Object[][] ToRowData() {
		/**
		 * 	把数据放在表上
		 */
		ArrayList<Integer> id = getOrderId();//获取到单号id
		Object[][] rowData = new Object[id.size()][6] ;
		ArrayList<Object> l;
		int i = 0;
		for (Integer orderid : id) {
			l= getOrder(orderid);//获取到一单数据
			rowData[i][0]=i+1;
			for (int j = 1; j <6; j++) {
				rowData[i][j]=l.get(j);
			}
			i++;
		}
		return rowData;
	}
	
	public ArrayList<Integer> getOrderId() {
		/**
		 * 	获取到所有购买单号的orsderIds
		 */
		String sql = "select orderid from ordertable where userid ="+user.getId();
		ArrayList<Integer> orsderIds  = new ArrayList<Integer>();
		try {
			rs = st.executeQuery(sql);
			while(rs.next())
			{	
				orsderIds.add(rs.getInt("orderid"));
			}
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
		return orsderIds;
	}
	
	public  ArrayList<Object> getOrder(int orderid) {
		/**
		 * 	获取到一单数据
		 */
		String sql = "select * from ordertable where orderid ="+orderid;
		ArrayList<Object> l  = new ArrayList<Object>();
		try {
			rs = st.executeQuery(sql);
			while(rs.next())
			{	
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateStr = format.format( rs.getTimestamp("datee"));
				int x = rs.getInt("menuid");
				int num = rs.getInt("num");
				Menu menu =getMenu(x);//由order表中获取这一单的menuid 从而获取得到这一单的数据
				l.add(0," ");
				l.add(1,dateStr); 
				l.add(2,menu.getShopName());
				l.add(3,menu.getFoodName());
				l.add(4,num);
				l.add(5,menu.getPrice()*num);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return l;
	}
	
	public Menu getMenu(int menuid) { 
		/**
		 * 	获取订单详情
		 */
		Menu menu = null;
		String sql = "select * from menu where id ="+menuid;
		
		try {
			rs = st.executeQuery(sql);
			while(rs.next())
			{	
				menu = new Menu(rs.getString("foodName"), rs.getInt("price"), rs.getString("shopName"));
			}
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
		return menu;
	}
	
	public void butset(JButton button,int y) 
	{
		button.setPreferredSize(new Dimension( 60, 30));
		button.setContentAreaFilled(false);
		button.setForeground(Color.pink);
		button.setBorderPainted(false);
		this.add(button);
	}
	
}
