package bigHomework;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class orderPanel extends JFrame {
	/**
	 * 点单界面
	 */
	public int price;
	public GetConnection getcon = new GetConnection();
	public Statement st = getcon.getStatement();
	public ResultSet rs = null;
	public User user;
	public int needPay;
	public  int menuid,num = 1 ;
	public JButton Balance;
	private static final long serialVersionUID = 1L;
	
	public orderPanel(String title,int price,User user,int menuid) throws HeadlessException 
	{
		super(title);
		this.price = price;
		this.user = user;
		this.menuid = menuid;
		init();
		
	}
	public void init()
	{
		initFrame(this ,300,400);
		JPanel panel = new JPanel();
		this.add(panel);
		JButton rnb = new JButton("花费："+price+"元");
		rnb.setContentAreaFilled(false);
		
		JButton order = new JButton("点单");
		order.setContentAreaFilled(false);
		
		Balance = new JButton("余额："+getBalance()+"元");
		Balance.setContentAreaFilled(false);
		needPay = price;
		
		JButton cancel = new JButton("取消");
		cancel.setContentAreaFilled(false);
		
		// 需要选择的条目
        String[] listData = new String[]{"1", "2", "3", "4", "5"};
        // 创建一个下拉列表框
        final JComboBox<String> comboBox = new JComboBox<String>(listData);
        // 添加条目选中状态改变的监听器
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // 只处理选中的状态
                if (e.getStateChange() == ItemEvent.SELECTED) {
                   // System.out.println("选中: " + comboBox.getSelectedIndex() + " = " + comboBox.getSelectedItem());
                	num = comboBox.getSelectedIndex()+1;
                	needPay = num*price;
                	rnb.setText("花费："+needPay+"元");
                }
            }
        });
        // 设置默认选中的条目
        comboBox.setSelectedIndex(0);
        JFrame f= this;
        
        order.addMouseListener(new MouseAdapter() 
		{

			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				if(needPay<=getBalance()) {
					if(updateOrder(num)) //是否考虑线程同步问题
					{
						updateBalance();
						Balance.setText("余额："+getBalance()+"元");
					}	
				}else {
					JOptionPane.showMessageDialog(f,"余额不足","通知",JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
			
		});

        panel.add(comboBox);
        panel.add(rnb);
        panel.add(Balance);
        panel.add(cancel);
        panel.add(order);
	}
	public int getBalance() {
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
	
	public boolean updateOrder(int num) {
		String sql = "insert into ordertable (userid,datee,menuid,num) values("+"'"+ user.getId()+"',"+"now(),"+"'"+menuid+"',"+"'"+num+"'"+")";
		try {
			int rs =0;
			rs = st.executeUpdate(sql);
			if(rs >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateBalance() {
		String sql = "update user set balance = "+(getBalance()-needPay)+" where id = "+user.getId();
		try {
			int rs =0;
			rs = st.executeUpdate(sql);
			if(rs >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void initFrame(JFrame frame, int width, int height)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();// 获取与系统相关的工具类对象
		Dimension d = toolkit.getScreenSize();// 分辨率
		frame.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
}


