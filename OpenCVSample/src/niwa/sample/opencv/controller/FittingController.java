/**
 *
 */
package niwa.sample.opencv.controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

/**
 * 顔認識コントローラ
 *
 * @author hiroki
 */
public class FittingController implements Initializable {

	@FXML
	private TextField inputFileText;

	@FXML
	private TextField outputFileText;

	@FXML
	private AnchorPane inputCanvasPane;

	@FXML
	private Canvas inputCanvas;

	@FXML
	private AnchorPane outputCanvasPane;

	@FXML
	private Canvas outputCanvas;

	/*
	 * (非 Javadoc)
	 *
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		inputCanvasPane.heightProperty().addListener( new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				inputCanvas.setHeight(newValue.doubleValue());
				drawCanvas(inputFileText.getText(), inputCanvas);
			}
		});

		inputCanvasPane.widthProperty().addListener( new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				inputCanvas.setWidth(newValue.doubleValue());
				drawCanvas(inputFileText.getText(), inputCanvas);
			}
		});

		outputCanvasPane.heightProperty().addListener( new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				outputCanvas.setHeight(newValue.doubleValue());
				drawCanvas(outputFileText.getText(), outputCanvas);
			}
		});

		outputCanvasPane.widthProperty().addListener( new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				outputCanvas.setWidth(newValue.doubleValue());
				drawCanvas(outputFileText.getText(), outputCanvas);
			}
		});

	}

	@FXML
	public void doDragOverFileText(DragEvent event) {
		// ファイルの場合だけ受け付ける例
		Dragboard db = event.getDragboard();
		if (db.hasFiles()) {
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}

		event.consume();
	}

	@FXML
	public void doDragDropInputFileText(DragEvent event) {
		boolean success = false;

		// ファイルの場合だけ受け付ける例
		Dragboard db = event.getDragboard();
		if (db.hasFiles()) {
			List<File> list = db.getFiles();
			inputFileText.setText( list.get(0).getAbsolutePath() );
			success = true;
		}
		event.setDropCompleted(success);
		event.consume();
	}

	@FXML
	public void doDragDropOutputFileText(DragEvent event) {
		boolean success = false;

		// ファイルの場合だけ受け付ける例
		Dragboard db = event.getDragboard();
		if (db.hasFiles()) {
			List<File> list = db.getFiles();
			outputFileText.setText( list.get(0).getAbsolutePath() );
			success = true;
		}
		event.setDropCompleted(success);
		event.consume();
	}

	@FXML
	public void doShowInputButton(ActionEvent event) {
		drawCanvas(inputFileText.getText(), inputCanvas);
	}

	@FXML
	public void doFittingButton(ActionEvent event) {

		// Read image.
		doShowInputButton(event);
		File inputFile = Paths.get(inputFileText.getText()).toFile();
		Mat image = Imgcodecs.imread(inputFile.getAbsolutePath());
		if (image == null) {
			throw new IllegalArgumentException("Illegal input file.");
		}

		// Detect faces in the image.
		File settingFile = new File("./setting/haarcascade_frontalface_default.xml");
		if (!settingFile.exists()) {
			throw new RuntimeException("No setting file.");
		}
		MatOfRect faces = new MatOfRect();
		CascadeClassifier faceDetector = new CascadeClassifier(settingFile.getAbsolutePath());
		faceDetector.detectMultiScale(image, faces);

		// Draw a bounding box around each face.
		for (Rect rect : faces.toArray()) {
			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 255, 0));
		}

		// Save the result image of detection.
		String filename = outputFileText.getText();
		System.out.println(String.format("Write %s", filename));
		Imgcodecs.imwrite(filename, image);
		drawCanvas(filename, outputCanvas);
	}

	private void drawCanvas(String fileName, Canvas canvas) {
		// Read image.
		File inputFile = Paths.get(fileName).toFile();
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		canvas.getGraphicsContext2D().drawImage(new Image(inputFile.toURI().toString()), 0, 0,
				canvas.getWidth(), canvas.getHeight());
	}

}
