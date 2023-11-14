package net.yoedtos.nikki.usecases;

import net.yoedtos.nikki.usecases.ports.NoteData;
import net.yoedtos.nikki.usecases.ports.UserData;

public class TestConstant {
    public static final String VALID_EMAIL = "any@email.com";
    public static final String VALID_PASSWORD = "123Password";
    public static final String INVALID_EMAIL = "invalid_email";
    public static final String INVALID_PASSWORD = "password";
    public static final String ENCODED_PASSWORD = "ENCRYPTED";
    public static final String VALID_TITLE = "My Note";
    public static final String INVALID_TITLE = "";
    public static final String VALID_CONTENT = "Content of my note";
    public static final String TITLE_ONE = "Note 1";
    public static final String TITLE_TWO = "Note 2";
    public static final String TITLE_MOD = "Note 1 Modified";
    public static final String CONTENT_MOD = "Content One Modified";
    public static final Long VALID_USER_ID = 0L;
    public static final Long NOTE_ID = 0L;
    public static final NoteData NOTE_ONE =  new NoteData(NOTE_ID, VALID_USER_ID, VALID_EMAIL, TITLE_ONE, "Content One");
    public static final NoteData NOTE_TWO =  new NoteData(1L, VALID_USER_ID, VALID_EMAIL, TITLE_TWO, "Content Two");
    public static final UserData DB_USER = new UserData(VALID_USER_ID, VALID_EMAIL, ENCODED_PASSWORD);
}
