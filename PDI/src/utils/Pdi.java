package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Pdi {
	private static final int RATIO = 3;
	private static final int KERNEL_SIZE = 3;
	private static final Size BLUR_SIZE = new Size(3, 3);

	public static Image equalHist(int[] histR, int[] histG, int[] histB, Image img) {
		int w = (int) img.getWidth();
		int h = (int) img.getHeight();

		double size = w * h;

		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = img.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();

		for (int i = 1; i < w; i++) {
			for (int j = 1; j < h; j++) {
				double valueRed, valueGreen, valueBlue, vRed, vGreen, vBlue;

				vRed = histR[(int) (pr.getColor(i, j).getRed() * 255)];
				vGreen = histG[(int) (pr.getColor(i, j).getGreen() * 255)];
				vBlue = histB[(int) (pr.getColor(i, j).getBlue() * 255)];

				valueRed = (254.0 / size) * vRed;
				valueGreen = (254.0 / size) * vGreen;
				valueBlue = (254.0 / size) * vBlue;

				Color cor = new Color(valueRed / 255, valueGreen / 255, valueBlue / 255,
						pr.getColor(i, j).getOpacity());
				pw.setColor(i, j, cor);
			}
		}

		return wi;
	}

	public static int[] histogramaAc(int[] hist) {
		int[] ret = new int[hist.length];
		int vl = hist[0];
		for (int i = 0; i < hist.length - 1; i++) {
			ret[i] = vl;
			vl += hist[i + 1];
		}
		return ret;
	}

	public static int[] histograma(Image img, int canal) {
		int[] qt = new int[256];

		PixelReader pr = img.getPixelReader();

		int w = (int) img.getWidth();
		int h = (int) img.getHeight();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				switch (canal) {
				case 1:
					qt[(int) (pr.getColor(i, j).getRed() * 255)]++;
					break;
				case 2:
					qt[(int) (pr.getColor(i, j).getGreen() * 255)]++;
					break;
				case 3:
					qt[(int) (pr.getColor(i, j).getBlue() * 255)]++;
					break;
				default:
					qt[(int) (pr.getColor(i, j).getRed() * 255)]++;
					qt[(int) (pr.getColor(i, j).getGreen() * 255)]++;
					qt[(int) (pr.getColor(i, j).getBlue() * 255)]++;
					break;
				}
			}
		}
		return qt;
	}

	public static int[] histogramaUnico(Image img) {
		int[] qt = new int[256];

		PixelReader pr = img.getPixelReader();

		int w = (int) img.getWidth();
		int h = (int) img.getHeight();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				qt[(int) (pr.getColor(i, j).getRed() * 255)]++;
				qt[(int) (pr.getColor(i, j).getGreen() * 255)]++;
				qt[(int) (pr.getColor(i, j).getBlue() * 255)]++;
			}
		}
		return qt;
	}

	public static void getGrafico(Image img,BarChart<String, Number> grafico){
		CategoryAxis eixoX = new CategoryAxis();
		NumberAxis eixoY = new NumberAxis();
	    eixoX.setLabel("Intensidade");       
	    eixoY.setLabel("valor");
	    XYChart.Series vlr = new XYChart.Series();
	    vlr.setName("Intensidade");
	    
//	    int[] hist = histogramaUnico(img);
	    int[] hist = new int[256];
	    
	    Image imagem = escalaDeCinza(img);
	    
	    try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			
			int red = 0;
			int green = 0;
			int blue = 0;
			int p = 0;
			
			for(int i=0; i<w;i++ ) {
				for(int j=0;j<h;j++) {
					Color pixel = pr.getColor(i, j);
					
					p = (int)(pixel.getRed() * 255);
					hist[p] = red++;
					
					p = (int)(pixel.getGreen() * 255);
					hist[p] = green++;
					
					p = (int)(pixel.getBlue() * 255);
					hist[p] = blue++;
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    for (int i=0; i<hist.length; i++) {
	    	vlr.getData().add(new XYChart.Data(i+"", hist[i]/1000));
		}
	    
	    grafico.getData().addAll(vlr);
	}
	
	public static Image escalaDeCinza (Image imagem) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w;i++ ) {
				for(int j=0;j<h;j++) {
					Color corA = pr.getColor(i, j);
					
					double media = (corA.getRed() + corA.getBlue() + corA.getGreen()) / 3;
					Color corN = new Color(media, media, media, corA.getOpacity());
					
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
					
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static Image mediaPonderada (Image imagem, Double txtRed, Double txtGreen, Double txtBlue) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w;i++ ) {
				for(int j=0;j<h;j++) {
					Color corA = pr.getColor(i, j);
					double media = ((
								(corA.getRed() * txtRed)
							+ 	(corA.getBlue() * txtBlue)
							+ 	(corA.getGreen() * txtGreen))
							/ 100);
					Color corN = new Color(media, media, media, corA.getOpacity());
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
					
		} catch (Exception e) {
			e.printStackTrace();
			
			return  null;
		}
	}
	
	public static Image limiarizacao (Image imagem, Double limiar) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w;i++ ) {
				for(int j=0;j<h;j++) {
					Color corA = pr.getColor(i, j);
					
					Color corN;
					
					if(corA.getRed() >= limiar) 
						corN = new Color(1, 1, 1, corA.getOpacity());
					else
						corN = new Color(0,0,0, corA.getOpacity());
					
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
					
		} catch (Exception e) {
			e.printStackTrace();
			
			return  null;
		}
	}

	public static Image negativa (Image imagem) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w;i++ ) {
				for(int j=0;j<h;j++) {
					Color corA = pr.getColor(i, j);
					
					double corR = 1 - corA.getRed();
					double corG = 1 - corA.getGreen();
					double corB = 1 - corA.getBlue();
					
					Color corN = new Color(corR, corG, corB, corA.getOpacity());
					
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
					
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static Image ruido(Image img, String tipoVizinho, String tipoMedia) {

		int w = (int) img.getWidth();
		int h = (int) img.getHeight();

		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = img.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();

		int[][] VIZINHO;

		if (tipoVizinho == "tres") {

			VIZINHO = Constants.TRES;

		} else if (tipoVizinho == "cruz") {

			VIZINHO = Constants.CRUZ;

		} else {

			VIZINHO = Constants.X;
		}

		for (int y = 0; y < h - 1; y++) {
			for (int x = 0; x < w - 1; x++) {
				List<Color> neighbors = new ArrayList<Color>();

				Color cor = pr.getColor(x, y);

				neighbors.add(cor);

				for (int[] rowCol : VIZINHO) {
					int posX = x + rowCol[0];
					int posY = y + rowCol[1];

					if (posX >= 0 && posX < w && posY >= 0 && posY < h) {
						neighbors.add(pr.getColor(posX, posY));
					}
				}

				double mediaRed = 0, mediaGreen = 0, mediaBlue = 0;

				if (tipoMedia == "media") {
					for (Color n : neighbors) {
						mediaRed += n.getRed();
						mediaGreen += n.getGreen();
						mediaBlue += n.getBlue();
					}

					mediaRed = mediaRed / neighbors.size();
					mediaGreen = mediaGreen / neighbors.size();
					mediaBlue = mediaBlue / neighbors.size();

					Color newCor = new Color(mediaRed, mediaGreen, mediaBlue, cor.getOpacity());
					pw.setColor(x, y, newCor);
				} else {

					List<Double> calcMedianaRed = new ArrayList<Double>();
					List<Double> calcMedianaGreen = new ArrayList<Double>();
					List<Double> calcMedianaBlue = new ArrayList<Double>();

					for (Color n : neighbors) {
						calcMedianaRed.add(n.getRed());
						calcMedianaGreen.add(n.getGreen());
						calcMedianaBlue.add(n.getBlue());
					}

					Collections.sort(calcMedianaRed);
					Collections.sort(calcMedianaGreen);
					Collections.sort(calcMedianaBlue);

					double medianaRed;
					int sizeRed = calcMedianaRed.size();

					if (sizeRed % 2 == 0) {
						medianaRed = (calcMedianaRed.get(sizeRed / 2) + calcMedianaRed.get(sizeRed / 2 - 1)) / 2;
					} else {
						medianaRed = calcMedianaRed.get(sizeRed / 2);
					}

					double medianaGreen;
					int sizeGreen = calcMedianaGreen.size();

					if (sizeGreen % 2 == 0) {
						medianaGreen = (calcMedianaGreen.get(sizeGreen / 2) + calcMedianaGreen.get(sizeGreen / 2 - 1))
								/ 2;
					} else {
						medianaGreen = calcMedianaGreen.get(sizeGreen / 2);
					}

					double medianaBlue;
					int sizeBlue = calcMedianaBlue.size();

					if (sizeBlue % 2 == 0) {
						medianaBlue = (calcMedianaBlue.get(sizeBlue / 2) + calcMedianaBlue.get(sizeBlue / 2 - 1)) / 2;
					} else {
						medianaBlue = calcMedianaBlue.get(sizeBlue / 2);
					}

					Color newCor = new Color(medianaRed, medianaGreen, medianaBlue, cor.getOpacity());
					pw.setColor(x, y, newCor);
				}
			}
		}

		return wi;
	}

	//nao esta subtraindo
	public static Image AdicaoSubtracao (Image img1, Image img2, double v1, double v2, int tipo) {
		try {
			int wImg1 = (int)img1.getWidth();
			int hImg1 = (int)img1.getHeight();
			
			int wImg2 = (int)img2.getWidth();
			int hImg2 = (int)img2.getHeight();
			
			int w = Math.min(wImg1, wImg2);
			int h = Math.min(hImg1, hImg2);
						
			PixelReader prImg1 = img1.getPixelReader();
			PixelReader prImg2 = img2.getPixelReader();
			
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i< w;i++ ) {
				for(int j=0;j< h;j++) {
					Color cor1 = prImg1.getColor(i, j);
					Color cor2 = prImg2.getColor(i, j);
					
					Color corN;
					
					double r;
					double g;
					double b;
					double op;
					
					if(tipo == 1) {
						r = (cor1.getRed() *(v1/100)) + (cor2.getRed() * (v2/100));
						g = (cor1.getGreen() *(v1/100)) + (cor2.getGreen() * (v2/100));
						b = (cor1.getBlue() *(v1/100)) + (cor2.getBlue() * (v2/100));
						op = (cor1.getOpacity() *(v1/100)) + (cor2.getOpacity() * (v2/100));
					} else {
						r = cor1.getRed() - cor2.getRed();
						g = cor1.getGreen() - cor2.getGreen();
						b = cor1.getBlue() - cor2.getBlue();
						op = cor1.getOpacity() - cor2.getOpacity();
					}

					r = r<0?0:r;
					g = g<0?0:g;
					b = b<0?0:b;
					op = op<0?0:op;
					
					corN = new Color(r, g, b, op);
					
					pw.setColor(i, j, corN);
				}
			}
			return wi;
					
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image dividirImg(Image img1, int q1, int q2, int q3, int q4) {
		Image imgOriginal = img1;
		Image quadrante1;
		Image quadrante2;
		
		int widImg = (int)img1.getWidth();
		int heiImg = (int)img1.getHeight();
		System.out.println(widImg);
		int w = widImg / 2;
		int h = heiImg / 2;
		System.out.println(w);
		
		WritableImage wi = new WritableImage(widImg, heiImg);
		
		PixelReader prImg1 = img1.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		for(int i=0; i< widImg;i++ ) {
			for(int j=0;j< heiImg;j++) {
				pw.setColor(i, j, prImg1.getColor(i, j));
				
				if(i == w && j == h) {
					System.out.println("---");
//					wi = negativa(wi);
					return quadrante1 = wi;
				}
			}
		}
		
		quadrante1 = wi;
		
//		for(int i=w; i < widImg;i++ ) {
//			for(int j=h;j< heiImg;j++) {
//				pw.setColor(i, j, prImg1.getColor(i, j));
//			}
//		}
//		
//		quadrante2 = wi;
		
		return quadrante1;
	}
	
	public static void carregaOpenCV(){
//		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void erosao(){
		
		try {
			carregaOpenCV();
			
			String inputFile = "D:\\documentos\\CCP\\Processamento-digital-imagens\\PDI\\src\\img\\estrela.jpg";
			String outputFile = "D:\\documentos\\CCP\\Processamento-digital-imagens\\PDI\\src\\img\\erosao.jpg";
			
			Mat matImgDst = new Mat();
			Mat matImgSrc = Imgcodecs.imread(inputFile);
			
			int kernel = 5;
			
			Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * kernel + 1, 2 * kernel + 1), new Point(kernel, kernel));
			Imgproc.erode(matImgSrc, matImgDst, element);	
			
			Imgcodecs.imwrite(outputFile, matImgDst);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void dilatacao(){
		
		try {
			carregaOpenCV();
			
			String inputFile = "D:\\documentos\\CCP\\Processamento-digital-imagens\\PDI\\src\\img\\estrela.jpg";
			String outputFile = "D:\\documentos\\CCP\\Processamento-digital-imagens\\PDI\\src\\img\\estrela.jpg";
			
			Mat matImgDst = new Mat();
			Mat matImgSrc = Imgcodecs.imread(inputFile);
			
			int kernel = 5;
			
			Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * kernel + 1, 2 * kernel + 1), new Point(kernel, kernel));
			Imgproc.dilate(matImgSrc, matImgDst, element);	
			
			Imgcodecs.imwrite(outputFile, matImgDst);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Image canny(Image img) {
		img = limiarizacao(img, 128.0/255);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat src = OpenCVUtils.imageToMat(img);
		Mat srcBlur = new Mat();
		Mat detectedEdges = new Mat();
		Mat dst = new Mat();

		Imgproc.blur(src, srcBlur, BLUR_SIZE);
		Imgproc.Canny(srcBlur, detectedEdges, 5, 5 * RATIO, KERNEL_SIZE, false);
		dst = new Mat(src.size(), CvType.CV_8UC3, Scalar.all(0));
		src.copyTo(dst, detectedEdges);
		Image img_3 = OpenCVUtils.matrixToImage(dst);
		return img_3;




	}


	public static void prewitt(){
		
		try {
			carregaOpenCV();
			
			String inputFile = "D:\\documentos\\CCP\\Processamento-digital-imagens\\PDI\\src\\img\\estrela.jpg";
			String outputFile = "D:\\documentos\\CCP\\Processamento-digital-imagens\\PDI\\src\\img\\canny.jpg";
			
			Mat matImgDst = new Mat();
			Mat matImgSrc = Imgcodecs.imread(inputFile);

			int kernelSize = 9;
			
	        Mat kernel = new Mat(kernelSize,kernelSize, CvType.CV_32F) {
	        	{
	        		put(0,0,-1);
	        		put(0,1,0);
	                put(0,2,1);

	                put(1,0-1);
	                put(1,1,0);
	                put(1,2,1);

	                put(2,0,-1);
	                put(2,1,0);
	                put(2,2,1);
	             }
	          };	 
			
			Imgproc.filter2D(matImgSrc, matImgDst, -1, kernel); 
			Imgcodecs.imwrite(outputFile, matImgDst);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sobel(){
		
		try {
			carregaOpenCV();
			
			String inputFile = "D:\\documentos\\CCP\\Processamento-digital-imagens\\PDI\\src\\img\\estrela.jpg";
			String outputFile = "D:\\documentos\\CCP\\Processamento-digital-imagens\\PDI\\src\\img\\canny.jpg";
			
			Mat matImgDst = new Mat();
			Mat matImgSrc = Imgcodecs.imread(inputFile);

			Imgproc.Canny(matImgSrc, matImgDst, 10, 100); 
			Imgcodecs.imwrite(outputFile, matImgDst);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Image segmentacao(int[] histR, int[] histG, int[] histB, int qtdCorte, Image img) {
		int w = (int) img.getWidth();
		int h = (int) img.getHeight();

		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();

		double maiorR = 0.0;
		double maiorG = 0.0;
		double maiorB = 0.0;

		int corte = 255 / qtdCorte;

		int auxR = 1;
		int auxG = 1;
		int auxB = 1;

		double newCorR = 0.0;
		double newCorG = 0.0;
		double newCorB = 0.0;

		for (int i = 0; i < 255; i++) {
			if (i <= (corte * (auxR))) {
				if (histR[i] > maiorR) {
					maiorR = histR[i];
				}
			} else {
				auxR++;
			}

			if (i <= (corte * (auxG))) {
				if (histG[i] > maiorG) {
					maiorG = histG[i];
				}
			} else {
				auxG++;
			}

			if (i <= (corte * (auxB))) {
				if (histB[i] > maiorB) {
					maiorB = histB[i];
				}
			} else {
				auxB++;
			}
		}

		newCorR = maiorR / 255.0;
		newCorG = maiorG / 255.0;
		newCorB = maiorB / 255.0;

		for (int i = 1; i < w; i++) {
			for (int j = 1; j < h; j++) {
				pw.setColor(i, j, new Color(newCorR, newCorG, newCorB, 1));
			}
		}

		return wi;
	}
	
}

