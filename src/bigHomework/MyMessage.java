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
 *	 ������Ϣ��
 *	�����Ϣ��ť ��ʾ����ʾ������Ϣ����
 *	������� ��ʾ����ʾ��ʷ����
 *	�漰�������� �����ϵ�ʹ��
 * @author ��һվ��Զ
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
		myM = new JButton("��Ϣ");
		myOrder = new JButton("����");
		butset(myM,0);
		butset(myOrder,30);	
		int balance = getBalance();
		myM.addMouseListener(new MouseAdapter()
		{
			/**
			 * �����Ϣ,չʾ����ʾ������Ϣ
			 */
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				super.mouseReleased(e);
				p3.removeAll();
				JPanel p = new JPanel(new BorderLayout());//�Ѹ�����Ϣ����һ������
				p.setPreferredSize(new Dimension(700,200));
		        // ��ͷ��������
		        Object[] columnNames = {"������Ϣ","NAME", "Id","telphone","���"};
		        // �������������
		        Object[][] rowData = {{"",user.getUserName(),user.getId(),user.getPhoneNumber(),balance}};
		        // ����һ�����ָ�� ���������� �� ��ͷ
		        JTable table = new JTable(rowData, columnNames);
		        // �� ��ͷ ��ӵ�����������ʹ����ͨ���м�������ӱ��ʱ����ͷ �� ���� ��Ҫ�ֿ���ӣ�
		        p.add(table.getTableHeader(), BorderLayout.NORTH);
		        // �� ������� ��ӵ���������
		        p.add(table, BorderLayout.CENTER);
				p3.add(p);
				p3.repaint();
				p3.revalidate();
			}
		});
		
		myOrder.addMouseListener(new MouseAdapter()
		{
			/**
			 * �����Ϣ,չʾ����ʷ����
			 */
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				super.mouseReleased(e);
				p3.removeAll();
				JPanel p = new JPanel(new BorderLayout());//�Ѹ�����Ϣ����һ������
				p.setPreferredSize(new Dimension(700,1000));
				
		        Object[] columnNames = {"��ʷ����","ʱ��","����","����" ,"����","�۸�"};
		        Object[][] rowData = ToRowData();//�ȴӶ������ҳ���������û�������id����һ��������
		        //�ٴ�ÿ������id  ��ȡ��һ�������ݷ��ڼ����У��ٷŽ����������� չʾ��չʾ����
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
		 *	 ��ȡ���
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
		 * 	�����ݷ��ڱ���
		 */
		ArrayList<Integer> id = getOrderId();//��ȡ������id
		Object[][] rowData = new Object[id.size()][6] ;
		ArrayList<Object> l;
		int i = 0;
		for (Integer orderid : id) {
			l= getOrder(orderid);//��ȡ��һ������
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
		 * 	��ȡ�����й��򵥺ŵ�orsderIds
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
		 * 	��ȡ��һ������
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
				Menu menu =getMenu(x);//��order���л�ȡ��һ����menuid �Ӷ���ȡ�õ���һ��������
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
		 * 	��ȡ��������
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
