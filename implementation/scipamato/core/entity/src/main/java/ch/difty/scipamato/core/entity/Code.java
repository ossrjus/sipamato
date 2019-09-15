package ch.difty.scipamato.core.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.CodeLike;
import ch.difty.scipamato.common.entity.FieldEnumType;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Code extends CoreEntity implements CodeLike {

    private static final long serialVersionUID = 1L;

    public static final String CODE_REGEX = "[1-9][A-Z]";

    @NotNull
    @Pattern(regexp = CODE_REGEX, message = "{code.invalidCode}")
    private final String    code;
    @NotNull
    private final String    name;
    private final String    comment;
    private final boolean   internal;
    @NotNull
    private final CodeClass codeClass;
    private final int       sort;

    public enum CodeFields implements FieldEnumType {
        CODE("code"),
        CODE_CLASS("codeClass"),
        NAME("name"),
        COMMENT("comment"),
        INTERNAL("internal"),
        SORT("sort");

        private final String name;

        CodeFields(final String name) {
            this.name = name;
        }

        @Override
        public String getFieldName() {
            return name;
        }
    }

    public Code(final String code, final String name, final String comment, final boolean internal,
        final Integer codeClassId, final String codeClassName, final String codeClassDescription, final int sort) {
        this(code, name, comment, internal, codeClassId, codeClassName, codeClassDescription, sort, null, null, null,
            null, null);
    }

    public Code(final String code, final String name, final String comment, final boolean internal,
        final Integer codeClassId, final String codeClassName, final String codeClassDescription, final int sort,
        final LocalDateTime created, final Integer createdBy, final LocalDateTime lastModified,
        final Integer lastModifiedBy, final Integer version) {
        this.code = AssertAs.INSTANCE.notNull(code, "code");
        this.name = name;
        this.comment = comment;
        this.internal = internal;
        this.codeClass = new CodeClass(AssertAs.INSTANCE.notNull(codeClassId, "codeClassId"), codeClassName,
            codeClassDescription);
        this.sort = sort;
        setCreated(created);
        setCreatedBy(createdBy);
        setLastModified(lastModified);
        setLastModifiedBy(lastModifiedBy);
        setVersion(version != null ? version : 0);
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Code(final Code from) {
        this(from.code, from.name, from.comment, from.internal, new CodeClass(from.codeClass), from.sort,
            from.getCreated(), from.getCreatedBy(), from.getLastModified(), from.getLastModifiedBy(),
            from.getVersion());
    }

    private Code(final String code, final String name, final String comment, final boolean internal,
        final CodeClass codeClass, final int sort, final LocalDateTime created, final Integer createdBy,
        final LocalDateTime lastModified, final Integer lastModifiedBy, final Integer version) {
        this.code = AssertAs.INSTANCE.notNull(code, CodeFields.CODE.getFieldName());
        this.name = name;
        this.comment = comment;
        this.internal = internal;
        this.codeClass = AssertAs.INSTANCE.notNull(codeClass, CodeFields.CODE_CLASS.getFieldName());
        this.sort = sort;
        setCreated(created);
        setCreatedBy(createdBy);
        setLastModified(lastModified);
        setLastModifiedBy(lastModifiedBy);
        setVersion(version);
    }

    @Override
    public String getDisplayValue() {
        return name + " (" + code + ")";
    }

}
