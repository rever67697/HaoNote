## 使用Tink进行数据加密

[TOC]

Tink是谷歌官方提供的加密方案.

### 添加依赖

moudle的build.gradle中添加:

~~~~java
repositories {
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
}

dependencies {
    // Tink HEAD-SNAPSHOT for Android.
    compile 'com.google.crypto.tink:tink-android:HEAD-SNAPSHOT'
}
~~~~



### 使用

在application中初始化

~~~~java
public class TinkApplication extends Application {
  private static final String TAG = TinkApplication.class.toString();
  private static final String PREF_FILE_NAME = "hello_world_pref";
  private static final String TINK_KEYSET_NAME = "hello_world_keyset";
  private static final String MASTER_KEY_URI = "android-keystore://hello_world_master_key";
  public Aead aead;

  @Override
  public final void onCreate() {
    super.onCreate();
    try {
      Config.register(TinkConfig.TINK_1_0_0);
      aead = AeadFactory.getPrimitive(getOrGenerateNewKeysetHandle());
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  private KeysetHandle getOrGenerateNewKeysetHandle() throws IOException, GeneralSecurityException {
    return new AndroidKeysetManager.Builder()
        .withSharedPref(getApplicationContext(), TINK_KEYSET_NAME, PREF_FILE_NAME)
        .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
        .withMasterKeyUri(MASTER_KEY_URI)
        .build()
        .getKeysetHandle();
  }
}
~~~~



加密解密

~~~~java
private static final byte[] EMPTY_ASSOCIATED_DATA = new byte[0];
/**
   * 加密
   */
  private void attemptEncrypt() {
    try {
      //从mPlaintextView中拿到要加密的字符串转成byte数组
      byte[] plaintext = mPlaintextView.getText().toString().getBytes("UTF-8");
      //ciphertext为加密之后的数组
      byte[] ciphertext = mApplication.aead.encrypt(plaintext, EMPTY_ASSOCIATED_DATA);
      mCiphertextView.setText(base64Encode(ciphertext));
    } catch (UnsupportedEncodingException | GeneralSecurityException | IllegalArgumentException e) {
    }
  }

  /**
   * 解密
   */
  private void attemptDecrypt() {
    try {
      //从mCiphertextView中拿到要解密的字符串转成byte数组
      byte[] ciphertext = base64Decode(mCiphertextView.getText().toString());
      //plaintext为解密之后的数组
      byte[] plaintext = mApplication.aead.decrypt(ciphertext, EMPTY_ASSOCIATED_DATA);
      mPlaintextView.setText(new String(plaintext, "UTF-8"));
    } catch (UnsupportedEncodingException | GeneralSecurityException | IllegalArgumentException e) {
    }
  }

private static String base64Encode(final byte[] input) {
    return Base64.encodeToString(input, Base64.DEFAULT);
  }

  private static byte[] base64Decode(String input) {
    return Base64.decode(input, Base64.DEFAULT);
  }
~~~~

