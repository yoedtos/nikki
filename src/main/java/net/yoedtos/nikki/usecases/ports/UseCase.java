package net.yoedtos.nikki.usecases.ports;

import io.vavr.concurrent.Future;

public interface UseCase<T> {
    Future perform(T any);
}
