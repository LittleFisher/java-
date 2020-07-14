package bigHomework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
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
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class contactSeller extends JFrame {

	/**
	 *  ?同时写进一个文件会不会有多线程问题：要不要写两个txt来记录聊天记录  要
	 */
	private static final long serialVersionUID = 1L;
	public String shopName;
	public JTextArea jta,jta2;
	public String url = "G:\\eclipse\\Java大作业\\聊天记录\\",userName;
	public FileOutputStream fos;
	public Socket socket;
	public JPanel p1;

	public contactSeller(String title,String userName) throws HeadlessException 
	{
		super(title);
		this.shopName = title;
		this.userName = userName;
		url=url+userName+"_"+shopName+".txt";
		try {
			fos = new FileOutputStream(url, true);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		init();
	}
	public void init()
	{
		
		
		try {
			socket = new Socket("127.0.0.1",39999);
		} catch (UnknownHostException e1) {
			
			e1.printStackTrace();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		
		JPanel panel = new JPanel(new BorderLayout());
		p1 = new JPanel(new FlowLayout(0));
		p1.setPreferredSize(new Dimension(0, 10000));
		p1.setBackground(Color.WHITE);
		
		//jta = new JTextArea();
		//jta.setFont(new Font("宋体", Font.ITALIC, 18));
		//jta.setEditable(false);
		JScrollPane p1scrollPane = new JScrollPane(
                p1,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER//
        );
		p1scrollPane.setPreferredSize(new Dimension(300, 300));
		JPanel sendPane = new JPanel();
		this.add(panel);
		
		jta2 = new JTextArea();
		jta2.setFont(new Font("宋体", Font.ITALIC, 18));
		JScrollPane p2scrollPane = new JScrollPane(
                jta2,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER//
        );
		p2scrollPane.setPreferredSize(new Dimension(240, 100));
		
		sendPane.setPreferredSize(new Dimension(60,100));
		sendPane.setBackground(Color.WHITE);
		JButton send = new JButton("发送");
		send.setPreferredSize(new Dimension(60, 50));
		send.setContentAreaFilled(false);
		send.setForeground(Color.pink);
		send.setBorderPainted(false);
		
		sendPane.add(send);
		panel.add(p1scrollPane,BorderLayout.NORTH);
		panel.add(p2scrollPane,BorderLayout.CENTER);
		panel.add(sendPane,BorderLayout.EAST);
		getHistoryChat();//获取历史聊天记录显示到p1
	
		send.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				String text = jta2.getText();
				//jta.append(userName+":"+text+"\n");
				jta2.setText("");
				recordChat(userName+text);
				p1.add(new userMessage(text,0,userName));
				p1.revalidate();
				p1.repaint();
				jta2.setText("");
				BufferedWriter bw= null;
				try {
					bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				} catch (IOException e2) {
					
					e2.printStackTrace();
				}
				try {
					bw.write(text);
					bw.newLine();
					bw.flush();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
			}
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				BufferedReader br=null;
				try {
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
				while(true) {
					String line=null;
					try {
						line = br.readLine();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					//jta.append(line+"\n");
					recordChat(shopName+line);
					p1.add(new shopMessage(line,2,shopName));
					p1.revalidate();
					p1.repaint();
					if("bye".equals(line)) {
						break;
					}
				}
			}
		}).start();
		
		initFrame(this ,350,400);
		
	}
	public static void initFrame(JFrame frame, int width, int height)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();// 获取与系统相关的工具类对象
		Dimension d = toolkit.getScreenSize();// 分辨率
		frame.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
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
						String t = s.replace(userName, "");
						p1.add(new userMessage(t,0,userName));
					}else if(s.contains(shopName)) {
						String t = s.replace(shopName, "");
						p1.add(new shopMessage(t,2,shopName));
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

class userMessage extends JPanel{
	/**
	 * 	重写 用来显示消息
	 */
	private static final long serialVersionUID = 1L;

	public userMessage(String text,int x,String userName) {
		super();
		this.setPreferredSize(new Dimension(300, 30+30*text.length()/5));
		this.setLayout(new FlowLayout(x));
		this.setBackground(Color.WHITE);
		JTextArea m =new JTextArea(userName);
		m.setBackground(new Color(238,130,238));
		JTextArea message =new JTextArea(text+"\n",text.length()/5+1,5);
		message.setLineWrap(true);
		message.setBackground(new Color(152 ,245, 255));
		if(x==0) {
			this.add(m);
			this.add(message);
		}else {
			this.add(message);
			this.add(m);
		}
	}
}
class shopMessage extends JPanel{
	private static final long serialVersionUID = 1L;

	public shopMessage(String text,int x,String shopName) {
		super();
		this.setPreferredSize(new Dimension(300, 60+text.length()/5));
		this.setLayout(new FlowLayout(x));
		this.setBackground(Color.white);
		JTextArea m =new JTextArea(shopName);
		m.setBackground(new Color(238,130,238));
		
		JTextArea message =new JTextArea(text+"\n",text.length()/5+1,5);
		message.setLineWrap(true);
		message.setBackground(new Color(152 ,245, 255));
		if(x==0) {
			this.add(m);
			this.add(message);
		}else {
			this.add(message);
			this.add(m);
		}
		
	}

	
}



