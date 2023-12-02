package net.yoedtos.nikki.presenters;

import net.yoedtos.nikki.controllers.ports.NoteDTO;
import net.yoedtos.nikki.presenters.ports.Presenter;
import net.yoedtos.nikki.usecases.loadnote.LoadNotes;

import java.util.List;
import java.util.stream.Collectors;

public class LoadNotesPresenter implements Presenter<NoteDTO> {

    private final LoadNotes loadNotesUserCase;

    public LoadNotesPresenter(LoadNotes useCase) {
        this.loadNotesUserCase = useCase;
    }

    @Override
    public List<NoteDTO> handle(Object ownerId) {
        var result = loadNotesUserCase.perform((Long)ownerId).get();
        if (result == null) {
            return List.of();
        }
        return result.stream()
                .map(n -> new NoteDTO(n.getId(), n.getTitle(), n.getContent()))
                .collect(Collectors.toList());
    }
}
