package bigHomework;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class shopConnect{

	/**
	 * 	商家端功能需求：
	 *  1.显示信息： 已实现。
	 *  2.发送信息： 已实现。
	 *  3.显示历史聊天记录：已实现。
	 * 	4.传输图片功能：可以加一按钮 用FileDialog 获取到路径。 重写Jpanel来显示一张图片 加入到p1中，暂时留着。
	 *  5. 第一版两个文本区域 ，第二版聊天区域用JPanel 做成类似qq聊天界面。
	 *  6.时间记录：暂时留着。
	 *  ?同时写进一个文件会不会有多线程问题：要不要写两个txt来记录聊天记录。  要。
	 */
	public String shopName;
	public JTextArea jta,jta2;
	public String url = "G:\\eclipse\\Java大作业\\聊天记录\\",userName;
	public JFrame frame;
	public JPanel panel,sendPane,p1;
	public JScrollPane scrollPane,pscrollPane;
	public JButton send;
	public FileOutputStream fos;
	public ServerSocket ss= null;
	public Socket socket = null;

	public shopConnect(String shopName,String userName)
	{
		this.shopName = shopName;
		this.userName = userName;
		url=url+shopName+"_"+userName+".txt";
		try {
			fos = new FileOutputStream(url, true);//聊天记录保存地址
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		init();
	}
	
	public void init() {
		frame = new JFrame(userName);
		panel = new JPanel(new BorderLayout());//大界面用于包括发送框 和显示框
		//jta = new JTextArea();//版本1 聊天不能左右显示
		//jta.setFont(new Font("宋体", Font.ITALIC, 18));
		//jta.setEditable(false);
		p1 = new JPanel(new FlowLayout(0));//显示自己及别人的消息
		p1.setPreferredSize(new Dimension(0, 10000));
		p1.setBackground(Color.WHITE);
		scrollPane = new JScrollPane(
                p1,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
		scrollPane.setPreferredSize(new Dimension(300, 300));
		
		jta2 = new JTextArea();//用户要发送区域
		jta2.setFont(new Font("宋体", Font.ITALIC, 18));
		pscrollPane = new JScrollPane(
                jta2,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
		pscrollPane.setPreferredSize(new Dimension(240, 100));
		
		sendPane = new JPanel();//发送按钮
		sendPane.setPreferredSize(new Dimension(60,100));
		sendPane.setBackground(Color.WHITE);
		
		send = new JButton("发送");
		send.setPreferredSize(new Dimension(60, 50));
		send.setContentAreaFilled(false);
		send.setForeground(Color.pink);
		send.setBorderPainted(false);
		send.setEnabled(false);//未连上用户前不能发送
		
		sendPane.add(send);
		
		frame.add(panel);
		panel.add(scrollPane,BorderLayout.NORTH);
		panel.add(pscrollPane,BorderLayout.CENTER);
		panel.add(sendPane,BorderLayout.EAST);
		
		getHistoryChat();//获取历史聊天记录显示到p1
		
		send.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				String text = jta2.getText();//获取用户要发送内容
				//jta.append(shopName+":"+text+"\n");
				recordChat(shopName+text);//记录聊天内容
				p1.add(new shopMessage(text,0,shopName));//把消息显示到p1
				p1.revalidate();
				p1.repaint();
				jta2.setText("");
				
				BufferedWriter bw= null;//发送到商家端
				try {
					bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				} catch (IOException e2) {
					
					e2.printStackTrace();
				}
				try {
					bw.write(text);
					bw.newLine();//这里被卡，原因：如果使用BufferedWriter 不加newline()和flush()的话，
					//服务端BufferedReader 的readline()方法就读取不到msg,会一直阻塞下去。
					bw.flush();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
			}
		});
	
		
		new Thread(new Runnable() {
			/**
			 * 	接收商家端信息，并显示在p1 且记录到历史记录文件
			 */
			@Override
			public void run() 
			{
				try 
				{
					ss = new ServerSocket(39999);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				try {
					socket = ss.accept();
					send.setEnabled(true);
				} catch (IOException e) {
					
					e.printStackTrace();
				}//处于阻塞
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					while(true) {
						try {
							String line = br.readLine();
							//jta.append(line+"\n");
							//line.replace("", "");
							recordChat(userName+line);//记录到历史记录文件
							p1.add(new userMessage(line,2,userName));
							p1.revalidate();
							p1.repaint();
						} catch (SocketException e) {
							// TODO: handle exception
							break;
						}
						
					}
					br.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		
		initFrame(frame ,350,400);
	}
	
	public static void initFrame(JFrame frame, int width, int height)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();// 获取与系统相关的工具类对象
		Dimension d = toolkit.getScreenSize();// 分辨率
		frame.setBounds(((d.width - width) / 2)+300, (d.height - height) / 2, width, height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void getHistoryChat()
	{
		/**
		 * 	初始化是把历史聊天记录放在聊天框
		 */
		InputStreamReader is=null;
		try {
			is = new InputStreamReader(new FileInputStream(url),"UTF-8");
			BufferedReader br = new BufferedReader(is);
			String s= null;
			try {
				while((s=br.readLine())!=null) {
					//jta.append(s);
					//jta.append("\n");
					if(s.contains(userName)) {
						String t = s.replace(userName,"" );
						p1.add(new userMessage(t,2,userName));
					}else if(s.contains(shopName)) {
						String t = s.replace(shopName,"");
						p1.add(new shopMessage(t,0,shopName));
					}
					p1.revalidate();
					p1.repaint();
				}
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			br.close();
			is.close();
		} catch (UnsupportedEncodingException e1) {
			
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void recordChat(String message) 
	{
		//记录历史聊天
		OutputStreamWriter osw = null;
		BufferedWriter br = null;
		try {
			osw = new OutputStreamWriter(fos,"UTF-8");
			br = new BufferedWriter(osw);
			try {
				br.write(message);
				br.write("\n");
				br.flush();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		}catch (UnsupportedEncodingException e2) {
			
			e2.printStackTrace();
		}
	}
	
}