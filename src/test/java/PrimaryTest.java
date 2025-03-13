import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class PrimaryTest {

    @Test
    void primary() {
        assertThat(1, is(1));
    }
}
