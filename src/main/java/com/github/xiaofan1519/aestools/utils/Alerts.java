package com.github.xiaofan1519.aestools.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * 功能描述
 *
 * @author fan
 */
public class Alerts {

  private static GridPane createExpandablePane(Exception errorMsg) {
    StringWriter sWriter = new StringWriter();
    PrintWriter pWriter = new PrintWriter(sWriter);
    errorMsg.printStackTrace(pWriter);
    String exceptionText = sWriter.toString();

    Label label = new Label("The exception stacktrace was:");

    TextArea textArea = new TextArea(exceptionText);
    textArea.setEditable(false);
    textArea.setWrapText(true);

    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);

    GridPane expContent = new GridPane();
    expContent.setMaxWidth(Double.MAX_VALUE);
    expContent.add(label, 0, 0);
    expContent.add(textArea, 0, 1);
    return expContent;
  }

  private static Optional<ButtonType> alert(AlertType type, String title, String headerText,
    String contentText) {
    return alert(type, title, headerText, contentText, null);
  }

  private static Optional<ButtonType> alert(AlertType type, String title, String headerText,
    String contentText, Exception e) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(contentText);

    if (null != e) {
      alert.getDialogPane().setExpandableContent(createExpandablePane(e));
    }
    return alert.showAndWait();
  }

  /**
   * 警告
   *
   * @param text 警告信息
   */
  public static Optional<ButtonType> warning(String text, Exception e) {
    return alert(AlertType.WARNING, "警告", text, null, e);
  }

  /**
   * 警告
   *
   * @param text 警告信息
   */
  public static Optional<ButtonType> warning(String text) {
    return warning(text, null);
  }

  /**
   * 通知
   *
   * @param text 通知信息
   */
  public static Optional<ButtonType> info(String text) {
    return alert(AlertType.INFORMATION, "通知", text, null);
  }

  /**
   * 确认
   *
   * @param text 通知信息
   */
  public static Optional<ButtonType> confirm(String text, String details) {
    return alert(AlertType.CONFIRMATION, "确认信息", text, details);
  }
}