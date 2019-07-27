package aestools.pane;

import aestools.aes.AES;
import aestools.aes.impl.CBC;
import aestools.aes.impl.ECB;
import aestools.utils.Alerts;
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
    plainTextArea.setWrapText(true);
    getChildren().add(plainTextArea);
    getChildren().add(buildTools());

    getChildren().add(new Label("  结果:"));
    cipherTextArea.setPrefHeight(100);
    cipherTextArea.setWrapText(true);
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
    Button encryptButton = new Button("↓加密");
    encryptButton.setOnAction(event -> Platform.runLater(() -> {
      try {
        this.cipherTextArea.setText(
          encrypt(this.plainTextArea.getText()));
      } catch (Exception e) {
        Alerts.warning(e.getMessage(), e);
      }
    }));
    HBox.setMargin(encryptButton, margin);
    buttonGroup.getChildren().add(encryptButton);

    Button decryptButton = new Button("解密↑");
    decryptButton.setOnAction(event -> {
      try {
        this.plainTextArea.setText(
          decrypt(this.cipherTextArea.getText()));
      } catch (Exception e) {
        Alerts.warning(e.getMessage(), e);
      }
    });
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
    byte[] key = validateKey(this.keyTextField.getText());

    switch (encryptionMode) {
      case "ECB":
        aes = new ECB(paddingMode, key);
        break;
      case "CBC":
        byte[] iv = validateIV(this.ivTextField.getText());
        aes = new CBC(paddingMode, key, iv);
        break;
      default:
        throw new IllegalStateException("不支持的模式: " + encryptionMode);
    }
    return aes;
  }

  /**
   * 校验密钥是否合法
   *
   * @return 返回合法密钥的字符数组
   */
  private byte[] validateKey(String key) {
    if (null == key || key.isBlank()) {
      throw new RuntimeException("请输入密钥");
    }

    int length = key.length();
    if (length % 16 != 0) {
      throw new RuntimeException("请输入合法长度的密钥, 例如长度为16的字符串");
    }

    return key.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * @param iv 校验向量是否合法
   * @return 返回合法向量的字符数组
   */
  private byte[] validateIV(String iv) {
    if (null == iv || iv.isBlank()) {
      throw new RuntimeException("请输入向量");
    }

    if (iv.length() != 16) {
      throw new RuntimeException("请输入合法长度的向量, 例如长度为16的字符串");
    }

    return iv.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * @param plainText 要加密的字符串
   * @return 返回加密后的字符串
   * @throws GeneralSecurityException 加密失败
   */
  private String encrypt(String plainText) throws GeneralSecurityException {
    if (null == plainText) {
      throw new RuntimeException("请输入待加密的文本");
    }
    AES aes = buildAES();
    return aes.encrypt(plainText);
  }

  /**
   * @param cipherText 要解密的字符串
   * @return 返回解密后的字符串
   * @throws GeneralSecurityException 解密失败
   */
  private String decrypt(String cipherText) throws GeneralSecurityException {
    if (null == cipherText) {
      throw new RuntimeException("请在结果中输入待解密的文本");
    }
    AES aes = buildAES();
    return aes.decrypt(cipherText);
  }
}
