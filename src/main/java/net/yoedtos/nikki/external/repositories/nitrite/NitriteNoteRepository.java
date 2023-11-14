package net.yoedtos.nikki.external.repositories.nitrite;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

import net.yoedtos.nikki.external.repositories.nitrite.helper.NitriteException;
import net.yoedtos.nikki.external.repositories.nitrite.helper.NitriteHelper;
import net.yoedtos.nikki.usecases.ports.NoteData;
import net.yoedtos.nikki.usecases.ports.NoteRepository;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.util.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class NitriteNoteRepository implements NoteRepository, Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(NitriteNoteRepository.class);

    private NitriteHelper helper;
    private ObjectRepository<Note> noteRepo;

    public NitriteNoteRepository() {
        this.helper = new NitriteHelper();
        noteRepo = helper.getRepository(Note.class);
    }

    @Override
    public NoteData add(NoteData noteData) {
        try {
            var note = toNote(noteData);
            var result = noteRepo.insert(note);
            var id = Iterables.firstOrDefault(result).getIdValue();
            return new NoteData(id,
                    noteData.getOwnerId(),
                    noteData.getOwnerEmail(),
                    noteData.getTitle(),
                    noteData.getContent());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NitriteException(e.getMessage());
        }
    }

    @Override
    public List<NoteData> findAllNotesFrom(Long userId) {
        var notes = new ArrayList<NoteData>();
        try {
            var cursor = noteRepo.find();
            for (Note note : cursor) {
                if (note.getOwnerId().equals(userId)) {
                    notes.add(toNoteData(note));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NitriteException(e.getMessage());
        }
        return notes;
    }

    @Override
    public NoteData findById(Long noteId) {
        try {
            var note = noteRepo.getById(NitriteId.createId(noteId));
            return toNoteData(note);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NitriteException(e.getMessage());
        }
    }

    @Override
    public NoteData remove(Long noteId) {
        try {
            var note = noteRepo.getById(NitriteId.createId(noteId));
            noteRepo.remove(eq("id", noteId));
            return toNoteData(note);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NitriteException(e.getMessage());
        }
    }

    @Override
    public NoteData update(Long noteId, String title, String content) {
        try {
            var note = noteRepo.getById(NitriteId.createId(noteId));
            note.setTitle(title);
            note.setContent(content);
            noteRepo.update(note);
            return toNoteData(note);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NitriteException(e.getMessage());
        }
    }

    @Override
    public void close() {
        helper.close();
    }

    private Note toNote(NoteData noteData) {
        return new Note(noteData.getOwnerId(),
                noteData.getOwnerEmail(),
                noteData.getTitle(),
                noteData.getContent());
    }

    private NoteData toNoteData(Note note) {
        return new NoteData(note.getId().getIdValue(),
                note.getOwnerId(),
                note.getOwnerEmail(),
                note.getTitle(),
                note.getContent());
    }
}

class Note {
    @Id
    private NitriteId id;
    private Long ownerId;
    private String ownerEmail;
    private String title;
    private String content;

    public Note() {}

    public Note(Long ownerId, String ownerEmail, String title, String content) {
        this.ownerId = ownerId;
        this.ownerEmail = ownerEmail;
        this.title = title;
        this.content = content;
    }

    public NitriteId getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
