package net.yoedtos.nikki.presenters.ports;

import java.util.List;

public interface Presenter<T> {
    List<T> handle(Object object);
}
