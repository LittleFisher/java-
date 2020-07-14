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
	 * 	�̼Ҷ˹�������
	 *  1.��ʾ��Ϣ�� ��ʵ�֡�
	 *  2.������Ϣ�� ��ʵ�֡�
	 *  3.��ʾ��ʷ�����¼����ʵ�֡�
	 * 	4.����ͼƬ���ܣ����Լ�һ��ť ��FileDialog ��ȡ��·���� ��дJpanel����ʾһ��ͼƬ ���뵽p1�У���ʱ���š�
	 *  5. ��һ�������ı����� ���ڶ�������������JPanel ��������qq������档
	 *  6.ʱ���¼����ʱ���š�
	 *  ?ͬʱд��һ���ļ��᲻���ж��߳����⣺Ҫ��Ҫд����txt����¼�����¼��  Ҫ��
	 */
	public String shopName;
	public JTextArea jta,jta2;
	public String url = "G:\\eclipse\\Java����ҵ\\�����¼\\",userName;
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
			fos = new FileOutputStream(url, true);//�����¼�����ַ
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		init();
	}
	
	public void init() {
		frame = new JFrame(userName);
		panel = new JPanel(new BorderLayout());//��������ڰ������Ϳ� ����ʾ��
		//jta = new JTextArea();//�汾1 ���첻��������ʾ
		//jta.setFont(new Font("����", Font.ITALIC, 18));
		//jta.setEditable(false);
		p1 = new JPanel(new FlowLayout(0));//��ʾ�Լ������˵���Ϣ
		p1.setPreferredSize(new Dimension(0, 10000));
		p1.setBackground(Color.WHITE);
		scrollPane = new JScrollPane(
                p1,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
		scrollPane.setPreferredSize(new Dimension(300, 300));
		
		jta2 = new JTextArea();//�û�Ҫ��������
		jta2.setFont(new Font("����", Font.ITALIC, 18));
		pscrollPane = new JScrollPane(
                jta2,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
		pscrollPane.setPreferredSize(new Dimension(240, 100));
		
		sendPane = new JPanel();//���Ͱ�ť
		sendPane.setPreferredSize(new Dimension(60,100));
		sendPane.setBackground(Color.WHITE);
		
		send = new JButton("����");
		send.setPreferredSize(new Dimension(60, 50));
		send.setContentAreaFilled(false);
		send.setForeground(Color.pink);
		send.setBorderPainted(false);
		send.setEnabled(false);//δ�����û�ǰ���ܷ���
		
		sendPane.add(send);
		
		frame.add(panel);
		panel.add(scrollPane,BorderLayout.NORTH);
		panel.add(pscrollPane,BorderLayout.CENTER);
		panel.add(sendPane,BorderLayout.EAST);
		
		getHistoryChat();//��ȡ��ʷ�����¼��ʾ��p1
		
		send.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				String text = jta2.getText();//��ȡ�û�Ҫ��������
				//jta.append(shopName+":"+text+"\n");
				recordChat(shopName+text);//��¼��������
				p1.add(new shopMessage(text,0,shopName));//����Ϣ��ʾ��p1
				p1.revalidate();
				p1.repaint();
				jta2.setText("");
				
				BufferedWriter bw= null;//���͵��̼Ҷ�
				try {
					bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				} catch (IOException e2) {
					
					e2.printStackTrace();
				}
				try {
					bw.write(text);
					bw.newLine();//���ﱻ����ԭ�����ʹ��BufferedWriter ����newline()��flush()�Ļ���
					//�����BufferedReader ��readline()�����Ͷ�ȡ����msg,��һֱ������ȥ��
					bw.flush();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
			}
		});
	
		
		new Thread(new Runnable() {
			/**
			 * 	�����̼Ҷ���Ϣ������ʾ��p1 �Ҽ�¼����ʷ��¼�ļ�
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
				}//��������
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					while(true) {
						try {
							String line = br.readLine();
							//jta.append(line+"\n");
							//line.replace("", "");
							recordChat(userName+line);//��¼����ʷ��¼�ļ�
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
		Toolkit toolkit = Toolkit.getDefaultToolkit();// ��ȡ��ϵͳ��صĹ��������
		Dimension d = toolkit.getScreenSize();// �ֱ���
		frame.setBounds(((d.width - width) / 2)+300, (d.height - height) / 2, width, height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void getHistoryChat()
	{
		/**
		 * 	��ʼ���ǰ���ʷ�����¼���������
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
		//��¼��ʷ����
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