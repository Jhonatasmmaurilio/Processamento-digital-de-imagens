package application;
	
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import utils.OpenCVUtils;
import utils.Pdi;

public class trabfinalController {
	@FXML ImageView imageViewOrign;
	@FXML ImageView imageResult;

	private Image img1;
	private Image img3;
	
	@FXML Slider slider;
	
	//label filtros
	@FXML Label lbFiltro1;
	@FXML Label lbFiltro2;
	@FXML Label lbFiltro3;
	@FXML Label lbFiltro4;
	@FXML Label lbFiltro5;
	@FXML Label lbFiltro6;
	@FXML Label feed1;
	@FXML Label feed2;
	
	//resultado filtros
	@FXML ImageView filtro1;
	@FXML ImageView filtro2;
	@FXML ImageView filtro3;
	@FXML ImageView filtro4;
	@FXML ImageView filtro5;
	@FXML ImageView filtro6;
	
	//banco imagens
	@FXML ImageView bancoimg1;
	@FXML ImageView bancoimg2;
	@FXML ImageView bancoimg3;
	@FXML ImageView bancoimg4;
	@FXML ImageView bancoimg5;
	@FXML ImageView bancoimg6;
	
	@FXML
	public void testarImg1() {
		imageViewOrign.setImage(bancoimg1.getImage());
		feed1.setText("Clique em pr�-processar");
	}
	@FXML
	public void testarImg2() {
		imageViewOrign.setImage(bancoimg2.getImage());
		feed1.setText("Clique em pr�-processar");
	}
	@FXML
	public void testarImg3() {
		imageViewOrign.setImage(bancoimg3.getImage());
		feed1.setText("Clique em pr�-processar");
	}
	@FXML
	public void testarImg4() {
		imageViewOrign.setImage(bancoimg4.getImage());
		feed1.setText("Clique em pr�-processar");
	}
	@FXML
	public void testarImg5() {
		imageViewOrign.setImage(bancoimg5.getImage());
		feed1.setText("Clique em pr�-processar");
	}
	@FXML
	public void testarImg6() {
		imageViewOrign.setImage(bancoimg6.getImage());
		feed1.setText("Clique em pr�-processar");
	}
	
	public void histogramaEqua() {
		Image img = imageViewOrign.getImage();
		
		int[] instR = Pdi.histograma(img,1);
		int[] instG = Pdi.histograma(img,2);
		int[] instB = Pdi.histograma(img,3);
		
		img3 = Pdi.equalHist(instR, instG, instB, img);
	}
	
	public void limiarizacao() {
		double valor = slider.getValue();
		valor = valor / 255;
		img1 = Pdi.escalaDeCinza(img1);
		img3 = Pdi.limiarizacao(img1, valor);
	}

	private byte saturate(double val) {
		int iVal = (int) Math.round(val);
		iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
		return (byte) iVal;
	}



	@FXML
	public Image contraste(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = OpenCVUtils.imageToMat(imageViewOrign.getImage());
		Mat newImage = Mat.zeros(image.size(), image.type());
		double alpha = 1.0; /*< Simple contrast control */
		int beta = 41;       /*< Simple brightness control */
		byte[] imageData = new byte[(int) (image.total()*image.channels())];
		image.get(0, 0, imageData);
		byte[] newImageData = new byte[(int) (newImage.total()*newImage.channels())];
		for (int y = 0; y < image.rows(); y++) {
			for (int x = 0; x < image.cols(); x++) {
				for (int c = 0; c < image.channels(); c++) {
					double pixelValue = imageData[(y * image.cols() + x) * image.channels() + c];
					pixelValue = pixelValue < 0 ? pixelValue + 256 : pixelValue;
					newImageData[(y * image.cols() + x) * image.channels() + c]
							= saturate(alpha * pixelValue + beta);
				}
			}
		}
		newImage.put(0, 0, newImageData);
		img3 = OpenCVUtils.matrixToImage(newImage);
		return img3;

	}
	
	public void negativa() {
		img3 = Pdi.negativa(img1);
	}

	public void escalaDeCinzaMedia() {
		img3 = Pdi.escalaDeCinza(img1);
	}

	private void atualizaImage3(Image img) {
		imageResult.setImage(img);
	}

	public void canny(){
		Pdi.canny();
	}

	public void prewitt(){
		Pdi.prewitt();
	}

	public void sobel(){
		Pdi.sobel();
	}

	@FXML
	public void tratamento() {
		Image imagem = imageViewOrign.getImage();
		Image escalaCinza,negativa,limiarizacao,equalizacaoHistograma;
		
		escalaCinza = Pdi.escalaDeCinza(imagem);
		negativa = Pdi.negativa(escalaCinza);
		limiarizacao = Pdi.limiarizacao(negativa, 0.1);
		
		int[] instR = Pdi.histograma(limiarizacao,1);
		int[] instG = Pdi.histograma(limiarizacao,2);
		int[] instB = Pdi.histograma(limiarizacao,3);
		
		equalizacaoHistograma = Pdi.equalHist(instR, instG, instB, limiarizacao);
		
		feed1.setText("Imagem processada com sucesso");
		feed2.setText("Resultado");
		
		filtro1.setImage(escalaCinza);
		filtro2.setImage(negativa);
		filtro3.setImage(limiarizacao);
		filtro4.setImage(equalizacaoHistograma);
		filtro5.setImage(contraste());
		
		atualizaImage3(equalizacaoHistograma);
		
		lbFiltro1.setText("Escala de cinza");
		lbFiltro2.setText("Negativa");
		lbFiltro3.setText("Limiariza��o");
		lbFiltro4.setText("Equaliza��o");
	}
}