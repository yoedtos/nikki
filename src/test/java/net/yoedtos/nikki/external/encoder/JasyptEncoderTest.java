package net.yoedtos.nikki.external.encoder;

import static net.yoedtos.nikki.usecases.TestConstant.VALID_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class JasyptEncoderTest {

    @Test
    public void shouldEncodeAndDecodeStringCorrectly() {
        var encoder = new JasyptEncoder();
        var encoded = encoder.encode(VALID_PASSWORD);
        assertThat(encoded).isNotEqualTo(VALID_PASSWORD);
        assertThat(encoder.compare(VALID_PASSWORD, encoded)).isTrue();
    }
}
