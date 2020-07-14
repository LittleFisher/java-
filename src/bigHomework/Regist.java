package bigHomework;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Regist {
	public JFrame frame;
	public JPanel panel;
	public JLabel username,passwd,apasswd,phone; 
	public JTextField usernameInput,phonenum;
	public JPasswordField passwdInput,passwdInputA; 
	public JButton jb;
	private User u;
	
	public Regist() {
		frame =new JFrame("ע��");
		panel = new JPanel(null);
		panel.setBackground(new Color(127,255,170));
		username = new JLabel("�û���:");//�û������
		usernameInput = new JTextField(15);//����д��15���ֵ�
		username.setBounds(100, 1, 50, 20);
		usernameInput.setBounds(180, 1, 150, 20);
		
		passwd = new JLabel("����: ");//�����
		passwdInput = new JPasswordField(15);
		passwd.setBounds(100, 21, 50, 20);
		passwdInput.setBounds(180, 21, 150, 20);
		 
		apasswd = new JLabel("�ٴ���������: ");//�ٴ�ȷ������
		passwdInputA = new JPasswordField(15);
		apasswd.setBounds(100, 41, 80, 20);
		passwdInputA.setBounds(180, 41, 150, 20);
		phone = new JLabel("�绰����: ");//�ٴ�ȷ������
		phonenum = new JTextField(15);
		phone.setBounds(100, 61, 80, 20);
		phonenum.setBounds(180, 61, 150, 20);
		
		JButton jb = new JButton("ע��");
		jb.setBounds(170, 500, 60, 30);
		jb.setForeground(Color.pink);
		jb.setBorder(BorderFactory.createLineBorder(Color.blue));
		jb.setContentAreaFilled(false);
		
		frame.add(panel);
		panel.add(username);
		panel.add(usernameInput);
		panel.add(passwd);
		panel.add(passwdInput);
		panel.add(apasswd);
		panel.add(passwdInputA);
		panel.add(phone);
		panel.add(phonenum);
		panel.add(jb);
		HomeMenu.initFrame(frame, 400, 600);
		jb.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				super.mouseReleased(e);
				if(passwdSame()&&!usernameInput.getText().isEmpty()) 
				{
					String userName = usernameInput.getText();
					String key = new String(passwdInput.getPassword());
					String tel = phonenum.getText();
					long userId = getUserNextId();
					long usertel = Long.parseLong(tel);
					
					if(isNumber(tel)&&userName!=null&&key!=null) 
					{
						u = new User(userId, key, userName, usertel);
						if(addUser(u)) {//�����ݿ�����������û�
							JOptionPane.showMessageDialog(frame,"���ס���idΪ��"+userId,"֪ͨ",JOptionPane.INFORMATION_MESSAGE);
							frame.dispose();
						}else {
							JOptionPane.showMessageDialog(frame,"��������δ֪����","֪ͨ",JOptionPane.INFORMATION_MESSAGE);
						}
						
					}
				}
				else {
					JOptionPane.showMessageDialog(frame,"�������벻һ�»�������Ϊ��","֪ͨ",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}
	
	public boolean isNumber(String str)
	{
		/**
		 *    	 �ж��Ƿ�����    
		 */
		if(str.length()<=0)
			return false;
		for(int i = 0;i < str.length();i++)
		{
			char c = str.charAt(i);
			if(!Character.isDigit(c))
				return false;
		}
		
		return true;
	}
	
	public long getUserNextId()
	{
		/**
		 * 	���ݼ�¼��������Ϊ��һ���û���id
		 */
		GetConnection getcon = new GetConnection();
		Statement st = getcon.getStatement();
		ResultSet rs = null;
		String sql = "select id from user";
		try {
			rs = st.executeQuery(sql);
			rs.last();    //2����rs��ָ���ƶ������һλ
			return rs.getRow()+1;//3����ȡ��ǰ���ݵ�����
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			getcon.releaseAll(rs);
		}
		return -1;
	}
	
	public boolean addUser(User u)
	{
		/**
		 * �����ݿ������û�
		 */
		if(u == null)
		{
			return false;
		}
		GetConnection getcon = new GetConnection();
		Statement st = getcon.getStatement();
		int rs = 0;
		String sql = "insert into user values("+"'"+u.getUserName()+"'"+","+"'"+u.getPhoneNumber()+"'"+","+"'"+u.getId()+"'"+","+"'"+u.getPassword()+"'"+","+0+")";
		try {
			rs = st.executeUpdate(sql);
			if(rs >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			getcon.release();
		}
		return false;
	}
	
	public boolean passwdSame()
	{
		/**
		 * 	�������ٴ����������Ƿ���ͬ
		 */
		String password=new String(passwdInput.getPassword());
		String password1=new String(passwdInputA.getPassword());
		if(password.equals(password1)&&!password.isEmpty()&&!password.isEmpty()) {
			return true;
		}
		return false;
	}
}
