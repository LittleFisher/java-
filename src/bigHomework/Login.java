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

public class Login {
	JFrame frame;
	private JPanel panel;
	private JLabel namelabel,passwd,id;
	private JTextField usernameInput,userid;
	private JPasswordField passwdInput;
	public long userID=-1;
	User user;
	public void init() {
		user = new User();
		frame =new JFrame("��¼");
		panel = new JPanel(null);
		panel.setBackground(new Color(127,255,170));		
		namelabel = new JLabel("�û���:");//�û������
		namelabel.setBounds(100, 1, 50, 20);
		usernameInput= new JTextField(15);//����д��15����
		usernameInput.setBounds(150, 1, 150, 20);
		
		passwd = new JLabel("����: ");//�����
		passwdInput = new JPasswordField(15);
		passwd.setBounds(100, 21, 50, 20);
		passwdInput.setBounds(150, 21, 150, 20);
		
		id = new JLabel("�û�id:");//�û������
		id.setBounds(100, 41, 50, 20);
		userid= new JTextField(15);//����д��15����
		userid.setBounds(150, 41, 150, 20);
		 
		JButton jb = new JButton("��¼");
		jb.setBounds(170, 500, 60, 30);
		jb.setForeground(Color.pink);
		jb.setBorder(BorderFactory.createLineBorder(Color.blue));
		jb.setContentAreaFilled(false);
		jb.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				super.mouseReleased(e);
				String userName = usernameInput.getText();
				String key = new String(passwdInput.getPassword());
				if(!userName.isEmpty()&&!key.isEmpty()&&!userid.getText().isEmpty())
				{
					if(isNumber(userid.getText()))
					{
						userID = Long.parseLong(userid.getText());
						
						//�������ݿ�
						GetConnection con = new GetConnection();
						Statement st = con.getStatement();
						String sql = "select * from user where ID ="+userID;
						try {
							ResultSet rs = st.executeQuery(sql);
							//�ж�������˺Ŷ�Ӧ�������Ƿ���ȷ
							if(rs.next())
							{
								if(key .equals(rs.getNString("passwd"))&&userName.equals(rs.getNString("name")))
								{
									user.setId(userID);
									user.setUserName(usernameInput.getText());
									user.setPassword(key);
									new HomeMenu(user);//����������
									frame.dispose();
								}
								else 
								{
									JOptionPane.showMessageDialog(frame,"��������˺Ŵ���","֪ͨ",JOptionPane.INFORMATION_MESSAGE);
								}
							}else 
							{
								JOptionPane.showMessageDialog(frame,"id������","֪ͨ",JOptionPane.INFORMATION_MESSAGE);
							}
						} catch (SQLException e1) 
						{
							e1.printStackTrace();
						}finally
						{
							con.release();
						}
					}
				}else 
				{
					JOptionPane.showMessageDialog(frame,"����������","֪ͨ",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		frame.add(panel);
		panel.add(namelabel);
		panel.add(usernameInput);
		panel.add(passwd);
		panel.add(passwdInput);
		panel.add(id);
		panel.add(userid);
		panel.add(jb);
		Start.initFrame(frame, 400, 600);
	}
	
	public boolean isNumber(String str)//�Ƿ�����
	{
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
}
