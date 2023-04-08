package net.yoedtos.usecases.createnote.ports;

import lombok.Value;

@Value
public class NoteData {
    private Long id;
    private Long ownerId;
    private String ownerEmail;
    private String title;
    private String content;
}
