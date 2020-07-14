package bigHomework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *	��ʼ���� ��¼��ע�ᰴť
 * @author ��һվ��Զ
 *
 */
public class Start {
		JFrame frame;
		JButton login,regist;
		JPanel panel;
		public Start()
		{
			frame = new JFrame("С��������");
			JPanel panel = new JPanel();
			panel.setBackground(new Color(127,255,170));
			
			login = new JButton("��¼");
			login.setContentAreaFilled(false);
			login.setForeground(Color.pink);
			
			regist = new JButton("ע��");
			regist.setContentAreaFilled(false);
			regist.setForeground(Color.pink);
			
			frame.add(panel);
			panel.add(login);
			panel.add(regist);
			
			login.addMouseListener(new MouseAdapter() 
			{
				@Override 
				public void mouseReleased(MouseEvent e) 
				{
					super.mouseReleased(e);
					new Login().init();
					frame.dispose();
				}
			});
			
			regist.addMouseListener(new MouseAdapter()
			{
				public void mouseReleased(MouseEvent e) 
				{
					super.mouseReleased(e);
					new Regist();
				}
			});
			initFrame(frame, 400, 600);

		}
		public static void initFrame(JFrame frame, int width, int height)
		{
			/**
			 * ����λ�õĲ���
			 */
			Toolkit toolkit = Toolkit.getDefaultToolkit();// ��ȡ��ϵͳ��صĹ��������
			Dimension d = toolkit.getScreenSize();// �ֱ���
			frame.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
		
}
