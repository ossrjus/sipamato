package ch.difty.scipamato.publ.persistence.paper;

import static ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD;
import static ch.difty.scipamato.publ.db.tables.Paper.PAPER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.publ.db.tables.Language;
import ch.difty.scipamato.publ.db.tables.Paper;
import ch.difty.scipamato.publ.db.tables.records.PaperRecord;
import ch.difty.scipamato.publ.entity.Keyword;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;

/**
 * The repository to read {@link PublicPaper}s.
 *
 * @author u.joss
 */
@Repository
public class JooqPublicPaperRepo implements PublicPaperRepository {

    private final DSLContext                                      dsl;
    private final JooqSortMapper<PaperRecord, PublicPaper, Paper> sortMapper;
    private final PublicPaperFilterConditionMapper                filterConditionMapper;
    private final AuthorsAbbreviator                              authorsAbbreviator;
    private final JournalExtractor                                journalExtractor;

    public JooqPublicPaperRepo(@NotNull final DSLContext dsl,
        @NotNull final JooqSortMapper<PaperRecord, PublicPaper, Paper> sortMapper,
        @NotNull final PublicPaperFilterConditionMapper filterConditionMapper,
        @NotNull final AuthorsAbbreviator authorsAbbreviator, @NotNull final JournalExtractor journalExtractor) {
        this.dsl = dsl;
        this.sortMapper = sortMapper;
        this.filterConditionMapper = filterConditionMapper;
        this.authorsAbbreviator = authorsAbbreviator;
        this.journalExtractor = journalExtractor;
    }

    private DSLContext getDsl() {
        return dsl;
    }

    private JooqSortMapper<PaperRecord, PublicPaper, Paper> getSortMapper() {
        return sortMapper;
    }

    private Paper getTable() {
        return PAPER;
    }

    private Class<? extends PaperRecord> getRecordClass() {
        return PaperRecord.class;
    }

    @Nullable
    @Override
    public PublicPaper findByNumber(@NotNull final Long number) {
        final PaperRecord tuple = getDsl()
            .selectFrom(getTable())
            .where(PAPER.NUMBER.equal(number))
            .fetchOneInto(getRecordClass());
        if (tuple != null)
            return map(tuple);
        else
            return null;
    }

    @NotNull
    @Override
    public List<PublicPaper> findPageByFilter(@Nullable final PublicPaperFilter filter,
        @NotNull final PaginationContext pc) {
        final Condition conditions = getConditions(filter);
        final Collection<SortField<PublicPaper>> sortCriteria = getSortMapper().map(pc.getSort(), getTable());
        final List<PaperRecord> tuples = getDsl()
            .selectFrom(getTable())
            .where(conditions)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetchInto(getRecordClass());
        return tuples
            .stream()
            .map(this::map)
            .collect(Collectors.toList());
    }

    /** package-private for test purposes */
    PublicPaper map(final PaperRecord r) {
        final PublicPaper pp = PublicPaper
            .builder()
            .id(r.getId())
            .number(r.getNumber())
            .pmId(r.getPmId())
            .authors(r.getAuthors())
            .authorsAbbreviated(authorsAbbreviator.abbreviate(r.getAuthors()))
            .title(r.getTitle())
            .location(r.getLocation())
            .journal(journalExtractor.extractJournal(r.getLocation()))
            .publicationYear(r.getPublicationYear())
            .goals(r.getGoals())
            .methods(r.getMethods())
            .population(r.getPopulation())
            .result(r.getResult())
            .comment(r.getComment())
            .build();
        pp.setCreated(r.getCreated() != null ?
            r
                .getCreated()
                .toLocalDateTime() :
            null);
        pp.setLastModified(r.getLastModified() != null ?
            r
                .getLastModified()
                .toLocalDateTime() :
            null);
        pp.setVersion(r.getVersion());
        return pp;
    }

    @Override
    public int countByFilter(@Nullable final PublicPaperFilter filter) {
        final Condition conditions = getConditions(filter);
        return getDsl().fetchCount(getDsl()
            .selectOne()
            .from(PAPER)
            .where(conditions));
    }

    @NotNull
    @Override
    public List<Long> findPageOfNumbersByFilter(@Nullable final PublicPaperFilter filter,
        @NotNull final PaginationContext pc) {
        final Condition conditions = getConditions(filter);
        final Collection<SortField<PublicPaper>> sortCriteria = getSortMapper().map(pc.getSort(), getTable());
        return getDsl()
            .select()
            .from(getTable())
            .where(conditions)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetch(PAPER.NUMBER);
    }

    private Condition getConditions(final PublicPaperFilter filter) {
        final Condition conditions = filterConditionMapper.map(filter);
        if (CollectionUtils.isEmpty(filter.getKeywords()))
            return conditions;
        else
            return DSL.and(conditions, evaluateKeywords(filter.getKeywords()));
    }

    private Condition evaluateKeywords(final List<Keyword> keywords) {
        final String mainLanguage = getMainLanguage();

        final List<Integer> keywordIds = keywords
            .stream()
            .map(Keyword::getKeywordId)
            .collect(Collectors.toList());

        final List<Condition> keywordConditions = new ArrayList<>();
        for (final Integer keywordId : keywordIds) {
            final String searchTerm = getDsl()
                .select(DSL.coalesce(KEYWORD.SEARCH_OVERRIDE, KEYWORD.NAME))
                .from(KEYWORD)
                .where(KEYWORD.KEYWORD_ID
                    .eq(keywordId)
                    .and(KEYWORD.LANG_CODE.eq(mainLanguage)))
                .orderBy(KEYWORD.NAME)
                .limit(1)
                .fetchOne()
                .value1();
            keywordConditions.add(PAPER.METHODS.containsIgnoreCase(searchTerm));
        }
        if (keywordConditions.size() == 1)
            return keywordConditions.get(0);
        else
            return DSL.and(keywordConditions);
    }

    /** protected for stubbing **/
    protected String getMainLanguage() {
        return getDsl()
            .select(Language.LANGUAGE.CODE)
            .from(Language.LANGUAGE)
            .where(Language.LANGUAGE.MAIN_LANGUAGE.eq(true))
            .limit(1)
            .fetchOne()
            .value1();
    }
}
