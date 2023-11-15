package net.yoedtos.nikki.external.encoder;

import net.yoedtos.nikki.usecases.ports.Encoder;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class JasyptEncoder implements Encoder {

    private final StrongPasswordEncryptor encryptor;

    public JasyptEncoder() {
        this.encryptor = new StrongPasswordEncryptor();
    }

    @Override
    public String encode(String plain) {
        return encryptor.encryptPassword(plain);
    }

    @Override
    public boolean compare(String plain, String hashed) {
        return encryptor.checkPassword(plain, hashed);
    }
}
