package net.yoedtos.nikki.usecases.ports;

public interface Encoder {
    String encode(String plain);
    boolean compare(String plain, String hashed);
}
