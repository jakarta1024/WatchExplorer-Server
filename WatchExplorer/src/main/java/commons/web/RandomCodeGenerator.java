package commons.web;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;


/**
 * Servlet implementation class RandomCodeServlet
 */
@WebServlet(urlPatterns = { "/randomCode.do" })
public class RandomCodeGenerator extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String CODE_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    private static final int HEIGHT = 20;
    private static final int FONT_NUM = 4;
    private int width = 0;
//    private int height = 0;
    private int iNum = 0;
    private String codeList = "";
    private boolean drawBgFlag = false;
    
    private int rBg = 0;
    private int gBg = 0;
    private int bBg = 0;
	
    public void setBgColor(int r,int g,int b){
        drawBgFlag = true;
        this.rBg = r;
        this.gBg = g;
        this.bBg = b;
    }
    private Color getRandomColor(int fc, int bc) { 
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RandomCodeGenerator() {
		super();
        this.width = 13 * FONT_NUM + 12;
        this.iNum = FONT_NUM;
        this.codeList = CODE_LIST;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/jpeg");   //设置输出类型，必须的
        String randomString="";   //随机字符串
        
        //New Random image generator
        BufferedImage image = new BufferedImage(width, HEIGHT, BufferedImage.TYPE_INT_RGB);
        
        Graphics g = image.getGraphics();
        
        Random random = new Random();
        
        if ( drawBgFlag ){
            g.setColor(new Color(rBg,gBg,bBg));
            g.fillRect(0, 0, width, HEIGHT);
        }else{
            g.setColor(getRandomColor(200, 250)); 
            g.fillRect(0, 0, width, HEIGHT);
            
            for (int i = 0; i < 155; i++) {
                g.setColor(getRandomColor(140, 200));
                int x = random.nextInt(width);
                int y = random.nextInt(HEIGHT);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl); 
            }
        }
        
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        
        for (int i=0;i<iNum;i++){
            int rand=random.nextInt(codeList.length());
            String strRand=codeList.substring(rand,rand+1);
            randomString+=strRand;
            g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
            g.drawString(strRand,13*i+6,16); 
        }
        g.dispose();
        try{
        	String attributeId = request.getParameter("id");
        	if (attributeId == null) {
        		System.out.println("Warning: use common attribute id for store random string to session!");
        		attributeId = "RandomCode";
        	}
        	request.getSession(true).setAttribute(attributeId, randomString); //放到session中
        	ServletOutputStream out=response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
        }catch(IOException e){
            
        }
	}
}
