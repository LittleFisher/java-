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
 *	开始界面 登录和注册按钮
 * @author 下一站永远
 *
 */
public class Start {
		JFrame frame;
		JButton login,regist;
		JPanel panel;
		public Start()
		{
			frame = new JFrame("小鱼人外卖");
			JPanel panel = new JPanel();
			panel.setBackground(new Color(127,255,170));
			
			login = new JButton("登录");
			login.setContentAreaFilled(false);
			login.setForeground(Color.pink);
			
			regist = new JButton("注册");
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
			 * 界面位置的布局
			 */
			Toolkit toolkit = Toolkit.getDefaultToolkit();// 获取与系统相关的工具类对象
			Dimension d = toolkit.getScreenSize();// 分辨率
			frame.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
		
}
