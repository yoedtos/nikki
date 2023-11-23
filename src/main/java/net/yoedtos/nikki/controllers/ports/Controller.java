package net.yoedtos.nikki.controllers.ports;

public interface Controller<T> {
    void handle(T t);
}
