package ch.difty.scipamato.common.entity;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Code Classes are collections of code that can be maintained separately for a
 * paper. In order to profit from i18n, there is a separate set of tables with
 * one entry per member here.
 *
 * @author u.joss
 */
public enum CodeClassId {

    CC1(1),
    CC2(2),
    CC3(3),
    CC4(4),
    CC5(5),
    CC6(6),
    CC7(7),
    CC8(8);

    private static final Map<Integer, CodeClassId> ID2ENUM = Stream
        .of(values())
        .collect(Collectors.toMap(CodeClassId::getId, e -> e));

    private final int id;

    CodeClassId(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Optional<CodeClassId> fromId(final int id) {
        return Optional.ofNullable(ID2ENUM.get(id));
    }

}
