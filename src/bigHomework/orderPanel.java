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
	 * �㵥����
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
		JButton rnb = new JButton("���ѣ�"+price+"Ԫ");
		rnb.setContentAreaFilled(false);
		
		JButton order = new JButton("�㵥");
		order.setContentAreaFilled(false);
		
		Balance = new JButton("��"+getBalance()+"Ԫ");
		Balance.setContentAreaFilled(false);
		needPay = price;
		
		JButton cancel = new JButton("ȡ��");
		cancel.setContentAreaFilled(false);
		
		// ��Ҫѡ�����Ŀ
        String[] listData = new String[]{"1", "2", "3", "4", "5"};
        // ����һ�������б��
        final JComboBox<String> comboBox = new JComboBox<String>(listData);
        // �����Ŀѡ��״̬�ı�ļ�����
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // ֻ����ѡ�е�״̬
                if (e.getStateChange() == ItemEvent.SELECTED) {
                   // System.out.println("ѡ��: " + comboBox.getSelectedIndex() + " = " + comboBox.getSelectedItem());
                	num = comboBox.getSelectedIndex()+1;
                	needPay = num*price;
                	rnb.setText("���ѣ�"+needPay+"Ԫ");
                }
            }
        });
        // ����Ĭ��ѡ�е���Ŀ
        comboBox.setSelectedIndex(0);
        JFrame f= this;
        
        order.addMouseListener(new MouseAdapter() 
		{

			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				if(needPay<=getBalance()) {
					if(updateOrder(num)) //�Ƿ����߳�ͬ������
					{
						updateBalance();
						Balance.setText("��"+getBalance()+"Ԫ");
					}	
				}else {
					JOptionPane.showMessageDialog(f,"����","֪ͨ",JOptionPane.INFORMATION_MESSAGE);
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
		Toolkit toolkit = Toolkit.getDefaultToolkit();// ��ȡ��ϵͳ��صĹ��������
		Dimension d = toolkit.getScreenSize();// �ֱ���
		frame.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
}


