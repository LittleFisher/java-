package bigHomework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

public class foodShow extends JPanel {

	/**
	 * 	�����������չʾ��չʾ����˼�����
	 * 	չʾ4������˵�ͼƬ���۸�ȡ�
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image image = null;
	private Image image2 = null;
	public Image image3 = null;
	public Image image4 = null;
	public int id,price;
	private Menu menu;
	private String url,url2,url3,url4,userName;
	
	public foodShow(int id,User user) {
		super();
		this.id = id;
		
		this.setLayout(null);//��������
		this.userName=user.getUserName();
		this.setBackground(new Color(240,248,250));
		this.setPreferredSize(new Dimension(480, 700));
		
		JButton orderFood = new JButton("�㵥");//������ť
		orderFood.setBounds(120,450, 120, 20);
		orderFood.setContentAreaFilled(false);
		orderFood.setForeground(Color.pink);
		//orderFood.setBorderPainted(false);
		this.add(orderFood);
		
		JButton connBuyer = new JButton("��ϵ����");//��ϵ���Ұ�ť
		connBuyer.setBounds(260,450, 120, 20);
		connBuyer.setContentAreaFilled(false);
		connBuyer.setForeground(Color.pink);
		//orderFood.setBorderPainted(false);
		this.add(connBuyer);
		
		
		GetConnection con = new GetConnection();//���ݿ��ȡid����Ϣ
		Statement st = con.getStatement();
		String sql = "select * from menu where id ="+id;
		try {
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				price = rs.getInt("price");
				menu = new Menu(rs.getNString("foodName"),price, rs.getInt("valuation"), rs.getNString("address"), rs.getNString("shopName"));
				url = rs.getNString("url");
				url2 = rs.getNString("url2");
				url3 = rs.getNString("url3");
				url4 = rs.getNString("url4");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally{
			con.release();
		}
		

		orderFood.addMouseListener(new MouseAdapter()
		{
			/**
			 * �����Ϣ,չʾ����ʾ������Ϣ
			 */
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				super.mouseReleased(e);
				
				new orderPanel("�㵥",price,user,id);//�����㵥��
			}
		});
		
		connBuyer.addMouseListener(new MouseAdapter()
		{
			/**
			 * �����Ϣ,չʾ����ʾ������Ϣ
			 */
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				super.mouseReleased(e);
				new shopConnect(menu.getShopName(),userName);//�̼���ϵ�û����棬Ϊ�˼����̲�����дһ���̼ҿͻ���
				new contactSeller(menu.getShopName(),userName);//�û���ϵ�̼ҽ���
			}
		});
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.pink); 
		g.setFont(new Font("����", Font.ITALIC, 16));
		//����Ʒͼ���ڽ�����
		try {
			image = ImageIO.read(new File(url));
			g.drawImage(image, 0,0,240,200, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			image2 = ImageIO.read(new File(url2));
			g.drawImage(image2, 260,0,240,200, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			image3 = ImageIO.read(new File(url3));
			g.drawImage(image3, 0,220,240,200, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			image4 = ImageIO.read(new File(url4));
			g.drawImage(image4, 260,220,240,200, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawString(""+menu.getFoodName()+"/"+menu.getShopName()+"/"+"����"+menu.getValuation()+"��/"+menu.getPrice()+"Ԫ/"+menu.getAddress(), 100, 440);
		//��Ʒ�۸����Ϣ
	}

}
