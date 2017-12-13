package ch.difty.scipamato.core.entity;

import static ch.difty.scipamato.core.entity.PaperAttachment.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PaperAttachmentTest {

    private final PaperAttachment pa = new PaperAttachment(1, 2l, "pdf1", "content".getBytes(), "ct", 2048l);

    @Test
    public void test() {
        assertThat(pa.getId()).isEqualTo(1);
        assertThat(pa.getPaperId()).isEqualTo(2l);
        assertThat(pa.getName()).isEqualTo("pdf1");
        assertThat(pa.getContent()).isEqualTo("content".getBytes());
        assertThat(pa.getContentType()).isEqualTo("ct");
        assertThat(pa.getSize()).isEqualTo(2048l);
        assertThat(pa.getSizeKiloBytes()).isEqualTo(2l);
    }

    @Test
    public void sizeInKiloBytes_withNullSize_isNull() {
        assertThat(new PaperAttachment().getSizeKiloBytes()).isNull();
    }

    @Test
    public void sizeInKiloBytes_isRoundedUp() {
        pa.setSize(2047l);
        assertThat(pa.getSizeKiloBytes()).isEqualTo(2l);
        pa.setSize(2050l);
        assertThat(pa.getSizeKiloBytes()).isEqualTo(3l);
    }

    @Test
    public void displayValue_isName() {
        assertThat(pa.getDisplayValue()).isEqualTo(pa.getName());
    }

    @Test
    public void toString_isMinimal() {
        assertThat(pa.toString()).isEqualTo("PaperAttachment[paperId=2,name=pdf1,id=1]");
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(PaperAttachment.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CONTENT, CREATED, CREATOR_ID, MODIFIED, MODIFIER_ID)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}