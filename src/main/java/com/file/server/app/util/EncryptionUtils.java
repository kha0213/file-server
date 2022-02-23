package com.file.server.app.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static javax.crypto.Cipher.ENCRYPT_MODE;

/**
 * 파일 암호화 복호화에 사용됨
 */
@Slf4j
public class EncryptionUtils {
    private final SecretKey secretKey;
    private final Cipher cipher;

    public EncryptionUtils(SecretKey secretKey, String transformation) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.secretKey = secretKey;
        this.cipher = Cipher.getInstance(transformation);
    }

    /**
     * 암호화
     * @param content 저장할 파일 내용 (암호화)
     * @param saveFile 저장할 파일 객체
     * @throws InvalidKeyException Key가 부정확할 때
     * @throws IOException createNewFile에서 Exception
     */
    public void encryptContentSaveFile(byte[] content, File saveFile) throws InvalidKeyException, IOException {
        cipher.init(ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();

        if (!saveFile.createNewFile()) {
            log.info("already exist file filename : [{}]", saveFile.getName());
        }

        try (FileOutputStream fileOut = new FileOutputStream(saveFile);
             CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)) {
            fileOut.write(iv);
            cipherOut.write(content);
        }
    }

    /**
     *
     * @param realFile 암호화 전 파일 객체
     * @return file data
     */
    public byte[] decryptContentByFile(File realFile) {
        byte[] result = null;

        try {
            byte[] fileIv = new byte[16];
            FileInputStream inputStream = new FileInputStream(realFile);
            inputStream.read(fileIv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));

            try ( CipherInputStream cipherIn = new CipherInputStream(inputStream, cipher) ) {
                result = IOUtils.toByteArray(cipherIn);
            }
        } catch (InvalidAlgorithmParameterException e) {
            log.error("InvalidAlgorithmParameterException [{}]", e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException [{}]", e.getMessage());
        } catch (IOException e) {
            log.error("IOException [{}]", e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("InvalidKeyException [{}]", e.getMessage());
        }
        return result;
    }
}
