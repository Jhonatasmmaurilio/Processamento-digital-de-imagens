package utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class Pixel {
	double r;
	double g;
	double b;
	int x;
	int y;
	
	static Pixel[] vizX = new Pixel[4];
	static Pixel[] vizC = new Pixel[4];
	static Pixel[] viz3 = new Pixel[4];
	
	public Pixel(double red, double green, double blue, int x, int y) {
		r = red;
		g = green;
		b = blue;
		x = x;
		y = y;		
	}

	public static Pixel[] VizinhosX(Image imagem, Pixel p) {
		Pixel[] pixels = new Pixel[5];
		
		PixelReader pr = imagem.getPixelReader();
		
		Color cor1 = pr.getColor(p.x, p.y);
		pixels[0] = new Pixel(cor1.getRed(),cor1.getGreen(),cor1.getBlue(), p.x, p.y);
		
		Color cor2 = pr.getColor(p.x-1, p.y+1);
		pixels[0] = new Pixel(cor2.getRed(),cor2.getGreen(),cor2.getBlue(), p.x, p.y);
		
		Color cor3 = pr.getColor(p.x+1, p.y-1);
		pixels[0] = new Pixel(cor3.getRed(),cor3.getGreen(),cor3.getBlue(), p.x, p.y);
		
		Color cor4 = pr.getColor(p.x-1, p.y-1);
		pixels[0] = new Pixel(cor4.getRed(),cor4.getGreen(),cor4.getBlue(), p.x, p.y);
		
		Color cor5 = pr.getColor(p.x+1, p.y+1);
		pixels[0] = new Pixel(cor5.getRed(),cor5.getGreen(),cor5.getBlue(), p.x, p.y);
		
		return pixels;
	}
	
	public static Pixel[] VizinhosC(Image imagem, Pixel p) {
		Pixel[] pixels = new Pixel[5];
		
		PixelReader pr = imagem.getPixelReader();
		
		Color cor1 = pr.getColor(p.x, p.y);
		pixels[0] = new Pixel(cor1.getRed(),cor1.getGreen(),cor1.getBlue(), p.x, p.y);
		
		Color cor2 = pr.getColor(p.x-1, p.y);
		pixels[1] = new Pixel(cor2.getRed(),cor2.getGreen(),cor2.getBlue(), p.x, p.y);
		
		Color cor3 = pr.getColor(p.x+1, p.y);
		pixels[2] = new Pixel(cor3.getRed(),cor3.getGreen(),cor3.getBlue(), p.x, p.y);
		
		Color cor4 = pr.getColor(p.x, p.y-1);
		pixels[3] = new Pixel(cor4.getRed(),cor4.getGreen(),cor4.getBlue(), p.x, p.y);
		
		Color cor5 = pr.getColor(p.x, p.y+1);
		pixels[4] = new Pixel(cor5.getRed(),cor5.getGreen(),cor5.getBlue(), p.x, p.y);
		
		return pixels;
	}
	
	public static Pixel[] Vizinhos(Image imagem, Pixel p) {
		Pixel[] pixels = new Pixel[9];
		Pixel[] vizX = new Pixel[5];
		Pixel[] vizC = new Pixel[5];
		
		int j = 0;
		
		for(int i=0;i<vizX.length;i++) {
			pixels[j] = vizX[i];
			j++;
		}
		
		for(int i=5;i<vizC.length;i++) {
			pixels[i] = vizC[i];
			j++;
		}
		
		return pixels;
	}
	
	public static void OrdenaVetor(Double[] vet) {
		Double aux = 0.0;
		
		for(int i = 0; i<vet.length; i++){
	        for(int j = 0; j<vet.length; j++){
	            if(vet[j] > vet[j + 1]){
	                aux = vet[j];
	                vet[j] = vet[j+1];
	                vet[j+1] = aux;
	            }
	        }
	    }
	}

	public static void LerPixel(Image imagem, Pixel p) {
		vizX = VizinhosX(imagem, p);
		vizC = VizinhosC(imagem, p);
		viz3 = Vizinhos(imagem, p);
	}
}
