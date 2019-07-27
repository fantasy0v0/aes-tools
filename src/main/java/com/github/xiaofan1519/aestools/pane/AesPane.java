package com.github.xiaofan1519.aestools.pane;

import com.github.xiaofan1519.aestools.aes.AES;
import com.github.xiaofan1519.aestools.aes.impl.CBC;
import com.github.xiaofan1519.aestools.aes.impl.ECB;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AesPane extends VBox {

  /**
   * 明文输入框
   */
  private TextArea plainTextArea = new TextArea();

  /**
   * 密文输入框
   */
  private TextArea cipherTextArea = new TextArea();

  /**
   * 加密模式选择框
   */
  private ChoiceBox<String> encryptionMode = new ChoiceBox<>();

  /**
   * 填充模式选择框
   */
  private ChoiceBox<String> paddingMode = new ChoiceBox<>();

  /**
   * 密码框
   */
  private TextField keyTextField = new TextField();

  /**
   * 偏移量标签
   */
  private Label ivLabel = new Label("偏移量:");

  /**
   * 偏移量文本框
   */
  private TextField ivTextField = new TextField();

  public AesPane() {
    initialize();
  }

  private void initialize() {
    setPrefWidth(690);
    // setPrefSize(640, 480);

    initEncryptionMode();
    initPaddingMode();

    getChildren().add(new Label("  待加密的文本:"));
    plainTextArea.setPrefHeight(100);
    getChildren().add(plainTextArea);
    getChildren().add(buildTools());

    getChildren().add(new Label("  结果:"));
    cipherTextArea.setPrefHeight(100);
    getChildren().add(cipherTextArea);
    getChildren().add(buildButtonGroup());
  }

  /**
   * 初始化加密模式选择框
   */
  private void initEncryptionMode() {
    encryptionMode.getItems().add("ECB");
    encryptionMode.getItems().add("CBC");
    encryptionMode.setValue("ECB");
    encryptionMode.getSelectionModel()
      .selectedItemProperty().addListener(
      (observable, oldValue, newValue) -> {
        // ECB 不需要向量
        if (newValue.equals("ECB")) {
          ivLabel.setVisible(false);
          ivTextField.setVisible(false);
        } else {
          ivLabel.setVisible(true);
          ivTextField.setVisible(true);
        }
      });
  }

  /**
   * 初始化填充模式选择框
   */
  private void initPaddingMode() {
    paddingMode.getItems().add("PKCS5Padding");
    paddingMode.setValue("PKCS5Padding");
  }

  /**
   * @return 创建输入框
   */
  private Pane buildTools() {
    HBox tools = new HBox();
    tools.setPadding(new Insets(10));

    Insets margin = new Insets(0, 0, 0, 10);
    tools.setAlignment(Pos.CENTER_LEFT);

    Label modelLabel = new Label("AES加密模式:");
    HBox.setMargin(modelLabel, margin);
    tools.getChildren().add(modelLabel);
    tools.getChildren().add(encryptionMode);

    Label paddingLabel = new Label("填充:");
    HBox.setMargin(paddingLabel, margin);
    tools.getChildren().add(paddingLabel);
    tools.getChildren().add(paddingMode);

    Label keyLabel = new Label("密钥:");
    HBox.setMargin(keyLabel, margin);
    tools.getChildren().add(keyLabel);
    tools.getChildren().add(keyTextField);

    HBox.setMargin(ivLabel, margin);
    ivLabel.setVisible(false);
    ivTextField.setVisible(false);
    tools.getChildren().add(ivLabel);
    tools.getChildren().add(ivTextField);
    return tools;
  }

  /**
   * @return 创建按钮组
   */
  private Pane buildButtonGroup() {
    HBox buttonGroup = new HBox();
    buttonGroup.setPadding(new Insets(10));
    buttonGroup.setAlignment(Pos.BASELINE_CENTER);

    Insets margin = new Insets(0, 5, 0, 5);
    Button encryptButton = new Button("加密");
    encryptButton.setOnAction(event -> Platform.runLater(() -> {
      AES aes = buildAES();
      try {
        String result = aes.encrypt(this.plainTextArea.getText());
        this.cipherTextArea.setText(result);
      } catch (GeneralSecurityException e) {
        e.printStackTrace();
      }
    }));
    HBox.setMargin(encryptButton, margin);
    buttonGroup.getChildren().add(encryptButton);

    Button decryptButton = new Button("解密");
    HBox.setMargin(decryptButton, margin);
    buttonGroup.getChildren().add(decryptButton);
    return buttonGroup;
  }

  /**
   * @return 根据用户选择创建对应的AES
   */
  private AES buildAES() {
    AES aes;
    String encryptionMode = this.encryptionMode.getValue();
    String paddingMode = this.paddingMode.getValue();
    byte[] key = this.keyTextField.getText().getBytes(StandardCharsets.UTF_8);

    switch (encryptionMode) {
      case "ECB":
        aes = new ECB(paddingMode, key);
        break;
      case "CBC":
        byte[] iv = this.ivTextField.getText().getBytes(StandardCharsets.UTF_8);
        aes = new CBC(paddingMode, key, iv);
        break;
      default:
        throw new IllegalStateException("不支持的模式: " + encryptionMode);
    }
    return aes;
  }
}
