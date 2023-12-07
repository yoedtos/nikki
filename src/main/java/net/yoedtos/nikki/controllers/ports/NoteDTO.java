package net.yoedtos.nikki.controllers.ports;

import lombok.Value;

@Value
public class NoteDTO {
    Long id;
    String title;
    String content;

    @Override
    public String toString() {
        return title;
    }
}
