package net.yoedtos.usecases.ports;

public interface Encoder {
    String encode(String plain);
    boolean compare(String plain, String hashed);
}
