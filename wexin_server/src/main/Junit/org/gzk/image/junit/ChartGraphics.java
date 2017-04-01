package org.gzk.image.junit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.jws.common.constant.Constants;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ChartGraphics {

	BufferedImage image;

	void createImage(String fileLocation) {
		try {
			FileOutputStream fos = new FileOutputStream(fileLocation);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
			encoder.encode(image);
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void graphicsGeneration(String name, String id, String classname,String imgurl) throws FileNotFoundException, IOException {
		int imageWidth = 400;// 图片的宽度
		int imageHeight = 400;// 图片的高度
		String tupian = Constants.imageUrlBack + "moban1.jpg";
		//image = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
		image =  ImageIO.read(new FileInputStream(tupian));  
		System.out.println("###高度："+image.getHeight()+"###宽度："+image.getWidth());
		Graphics graphics = image.getGraphics();
	//    Image image = Toolkit.getDefaultToolkit().createImage("tupian");
		graphics.setColor(Color.white);
//		graphics.fillRect(0, 0, imageWidth, imageHeight);
		//graphics.setColor(Color.black);
//		graphics.setFont(new Font("宋体", Font.BOLD, 50));
		//第一个参数决定横向，值越大越靠右；第二个参数决定高度值越大越往下
//		graphics.drawString( name, 200, 60);
//		graphics.drawString( id, 700, 50);
//		graphics.drawString(classname, 30, 400);
	//	graphics.drawImage(image, 100, 100, null);
	//	graphics.setColor(Color.gray);
	//	graphics.fillRect(100, 150, 200, 200);
		// ImageIcon imageIcon = new ImageIcon(imgurl);
		// graphics.drawImage(imageIcon.getImage(), 230, 0, null);
		// 改成这样:
		BufferedImage bimg = null;
		try {
			bimg = javax.imageio.ImageIO.read(new java.io.File(imgurl));
		} catch (Exception e) {
		}
		if (bimg != null)
			graphics.drawImage(bimg, 230, 0, null);
		graphics.dispose();
		createImage(imgurl);
	}

	public static void main(String[] args) {
		ChartGraphics cg = new ChartGraphics();
		try {
			cg.graphicsGeneration("小黄", "50$", "急需一名工程师", "D://2.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}