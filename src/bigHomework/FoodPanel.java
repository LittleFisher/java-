package bigHomework;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class FoodPanel extends JPanel{
	/**
	 * 	����̳��࣬������ʾһ���ˣ���������������չʾ
	 * 	һ����һ��panel ����˵�id ����
	 */
	private static final long serialVersionUID = 1L;
	private Image image = null;
	JButton dianming,caiming,price,address;
	String url;//����ͼƬλ��
	Menu menu;
	private int id;
	User user;
	
	public FoodPanel(int id,JPanel p3,User user) {//չʾ��p3 ����Ϊ�˺�����չʾ����չʾ��Ʒ��ϸ��Ϣ
		super();
		this.id = id;
		this.user = user;
		init(p3);
	}

	public void init(JPanel p3) {
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(240, 265));
		
		//�������ݿ�
		GetConnection con = new GetConnection();
		Statement st = con.getStatement();
		String sql = "select * from menu where id ="+getId();
		
		try {
			ResultSet rs = st.executeQuery(sql);
			//�ж�������˺Ŷ�Ӧ�������Ƿ���ȷ
			if(rs.next()){
				menu = new Menu(rs.getNString("foodName"), rs.getInt("price"), rs.getInt("valuation"), rs.getNString("address"), rs.getNString("shopName"));
				url = rs.getNString("url");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally{
			con.release();
		}
		
		dianming = new JButton(menu.getShopName());//���� ���� �۸� ��ַ
		caiming = new JButton(menu.getFoodName());
		price = new JButton(menu.getPrice()+"Ԫ");
		address = new JButton(menu.getAddress());
		setButton(dianming,0, 200);
		setButton(caiming,120, 200);
		setButton(price,0, 240);
		setButton(address,120,240);
		
		caiming.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				
				super.mouseReleased(e);
				p3.removeAll();
				p3.add(new foodShow(id,user));//չʾ��չʾ����
				p3.validate();
			}
			
		});
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setButton(JButton button,int x,int y) {//�ظ�����,���ٴ�����
		button.setBounds(x,y, 120, 20);
		button.setContentAreaFilled(false);
		button.setForeground(Color.pink);
		button.setBorderPainted(false);
		this.add(button);
	}
	
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		try {
			image = ImageIO.read(new File(url));
			g.drawImage(image, 0,0,240,200, null);//��ͼ
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
