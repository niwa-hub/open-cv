/**
 *
 */
package niwa.sample.opencv;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import niwa.sample.opencv.controller.FittingController;

/**
 * @author hiroki
 *
 */
public class OpenCVApplication extends Application {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Class<?> targetClass = FittingController.class;
		AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource(targetClass.getPackage().getName().replaceAll("\\.", "/") + "/fxml/" + targetClass.getSimpleName() + ".fxml"));
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("OpenCVSample");
        primaryStage.show();
	}

}
