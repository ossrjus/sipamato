package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class CodeTest extends PublicEntityTest<Code> {

    @Override
    protected Code newEntity() {
        return Code
            .builder()
            .codeClassId(1)
            .code("code")
            .langCode("lc")
            .name("name")
            .comment("comment")
            .sort(3)
            .build();
    }

    @Override
    protected void assertSpecificGetters() {
        assertThat(getEntity().getCodeClassId()).isEqualTo(1);
        assertThat(getEntity().getCode()).isEqualTo("code");
        assertThat(getEntity().getLangCode()).isEqualTo("lc");
        assertThat(getEntity().getName()).isEqualTo("name");
        assertThat(getEntity().getComment()).isEqualTo("comment");
        assertThat(getEntity().getSort()).isEqualTo(3);
    }

    @Override
    protected String getToString() {
        return "Code(codeClassId=1, code=code, langCode=lc, name=name, comment=comment, sort=3)";
    }

    @Override
    protected void verifyEquals() {
        EqualsVerifier
            .forClass(Code.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getName(), MODIFIED.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void displayValue() {
        assertThat(getEntity().getDisplayValue()).isEqualTo("name");
    }

    @Test
    void assertEnumFields() {
        assertThat(Code.CodeFields.values())
            .extracting("name")
            .containsExactly("codeClassId", "code", "langCode", "name", "comment", "sort", "displayValue");
    }

}
