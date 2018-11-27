package application;
	
import org.opencv.core.Core;
import org.opencv.core.Mat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import utils.OpenCVUtils;
import utils.Pdi;

public class trabfinalController {
	@FXML ImageView imageViewOrign;
	@FXML ImageView imageResult;
	
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
	
	@FXML Slider valueLimiar;
	
	@FXML javafx.scene.control.Button btlimiar;
	
	@FXML
	public void testarImg1() {
		imageViewOrign.setImage(bancoimg1.getImage());
		feed1.setText("Clique em prï¿½-processar");
	}
	@FXML
	public void testarImg2() {
		imageViewOrign.setImage(bancoimg2.getImage());
		feed1.setText("Clique em prï¿½-processar");
	}
	@FXML
	public void testarImg3() {
		imageViewOrign.setImage(bancoimg3.getImage());
		feed1.setText("Clique em prï¿½-processar");
	}
	@FXML
	public void testarImg4() {
		imageViewOrign.setImage(bancoimg4.getImage());
		feed1.setText("Clique em prï¿½-processar");
	}
	@FXML
	public void testarImg5() {
		imageViewOrign.setImage(bancoimg5.getImage());
		feed1.setText("Clique em prï¿½-processar");
	}
	@FXML
	public void testarImg6() {
		imageViewOrign.setImage(bancoimg6.getImage());
		feed1.setText("Clique em prï¿½-processar");
	}

	private void atualizaImage3(Image img) {
		imageResult.setImage(img);
	}

	@FXML
	public void limiar() {
		double limiar = valueLimiar.getValue() / 255;
		Image img = Pdi.limiarizacao(imageResult.getImage(), limiar);
		atualizaImage3(img);
	}
	
	@FXML
	public void tratamento() {
		Image imagem = imageViewOrign.getImage();
		Image escalaCinza,negativa,limiarizacao,equalizacaoHistograma, segmentacao,contraste,canny;
		
		int[] instR,instG,instB;

		escalaCinza = Pdi.escalaDeCinza(imagem);
		contraste = Pdi.contraste(escalaCinza);
		negativa = Pdi.negativa(contraste);

		instR = Pdi.histograma(negativa,1);
		instG = Pdi.histograma(negativa,2);
		instB = Pdi.histograma(negativa,3);
		limiarizacao = Pdi.limiarizacao(negativa, valueLimiar.getValue() / 255);
		
		instR = Pdi.histograma(limiarizacao,1);
		instG = Pdi.histograma(limiarizacao,2);
		instB = Pdi.histograma(limiarizacao,3);
		equalizacaoHistograma = Pdi.equalHist(instR, instG, instB, limiarizacao);
		
		canny = Pdi.canny(equalizacaoHistograma);
		
		feed1.setText("Imagem processada com sucesso");
		feed2.setText("Resultado");
		
		filtro1.setImage(escalaCinza);
		filtro2.setImage(contraste);
		filtro3.setImage(negativa);
		filtro4.setImage(limiarizacao);
		filtro5.setImage(equalizacaoHistograma);
		filtro6.setImage(canny);
		atualizaImage3(canny);
		
		lbFiltro1.setText("Escala de cinza");
		lbFiltro2.setText("Contraste");
		lbFiltro3.setText("negativa");
		lbFiltro4.setText("Limiarização");
		lbFiltro5.setText("Equalização");
		lbFiltro6.setText("Canny");
	}
}