package ch.difty.scipamato.core.entity.search;

import java.util.stream.Collectors;

import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.PaperCodeBox;

public class SearchConditionCodeBox extends PaperCodeBox {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return getCodes().stream()
            .sorted((c1, c2) -> c1.getCode()
                .compareTo(c2.getCode()))
            .map(Code::getCode)
            .collect(Collectors.joining("&"));
    }
}