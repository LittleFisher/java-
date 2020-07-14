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
	 * 	点击菜名后在展示区展示这道菜及购买
	 * 	展示4张这道菜的图片，价格等。
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
		
		this.setLayout(null);//背景处理
		this.userName=user.getUserName();
		this.setBackground(new Color(240,248,250));
		this.setPreferredSize(new Dimension(480, 700));
		
		JButton orderFood = new JButton("点单");//订单按钮
		orderFood.setBounds(120,450, 120, 20);
		orderFood.setContentAreaFilled(false);
		orderFood.setForeground(Color.pink);
		//orderFood.setBorderPainted(false);
		this.add(orderFood);
		
		JButton connBuyer = new JButton("联系卖家");//联系卖家按钮
		connBuyer.setBounds(260,450, 120, 20);
		connBuyer.setContentAreaFilled(false);
		connBuyer.setForeground(Color.pink);
		//orderFood.setBorderPainted(false);
		this.add(connBuyer);
		
		
		GetConnection con = new GetConnection();//数据库获取id等信息
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
			 * 点击信息,展示区显示个人信息
			 */
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				super.mouseReleased(e);
				
				new orderPanel("点单",price,user,id);//弹出点单框
			}
		});
		
		connBuyer.addMouseListener(new MouseAdapter()
		{
			/**
			 * 点击信息,展示区显示个人信息
			 */
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				super.mouseReleased(e);
				new shopConnect(menu.getShopName(),userName);//商家联系用户界面，为了简单流程不重新写一个商家客户端
				new contactSeller(menu.getShopName(),userName);//用户联系商家界面
			}
		});
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.pink); 
		g.setFont(new Font("宋体", Font.ITALIC, 16));
		//把商品图画在界面上
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
		g.drawString(""+menu.getFoodName()+"/"+menu.getShopName()+"/"+"评价"+menu.getValuation()+"分/"+menu.getPrice()+"元/"+menu.getAddress(), 100, 440);
		//商品价格等信息
	}

}
