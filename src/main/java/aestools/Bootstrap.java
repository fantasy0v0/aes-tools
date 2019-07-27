package aestools;

import aestools.pane.AesPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author xiaof
 */
public class Bootstrap extends Application {

  @Override
  public void start(Stage stage) {
    Scene scene = new Scene(new AesPane());
    stage.setMaximized(false);
    stage.setResizable(false);
    stage.setTitle("AES加解密工具");
    stage.setScene(scene);
    stage.show();
  }
}
