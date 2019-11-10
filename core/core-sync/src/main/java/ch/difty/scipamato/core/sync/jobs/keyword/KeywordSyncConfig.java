package ch.difty.scipamato.core.sync.jobs.keyword;

import static ch.difty.scipamato.core.db.tables.Keyword.KEYWORD;
import static ch.difty.scipamato.core.db.tables.KeywordTr.KEYWORD_TR;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.db.tables.Keyword;
import ch.difty.scipamato.core.db.tables.KeywordTr;
import ch.difty.scipamato.core.db.tables.records.KeywordRecord;
import ch.difty.scipamato.core.db.tables.records.KeywordTrRecord;
import ch.difty.scipamato.core.sync.jobs.SyncConfig;

/**
 * Defines the code synchronization job, applying two steps:
 * <ol>
 * <li>keywordInsertingOrUpdating: inserts new records or updates if already
 * present</li>
 * <li>keywordPurging: removes records that have not been touched by the first step
 * (within a defined grace time in minutes)</li>
 * </ol>
 *
 * @author u.joss
 */
@Configuration
@Profile("!wickettest")
public class KeywordSyncConfig
    extends SyncConfig<PublicKeyword, ch.difty.scipamato.publ.db.tables.records.KeywordRecord> {

    private static final String TOPIC      = "keyword";
    private static final int    CHUNK_SIZE = 100;

    // relevant fields of the core Keyword class record
    private static final TableField<KeywordTrRecord, Integer>   KT_ID              = KEYWORD_TR.ID;
    private static final TableField<KeywordRecord, Integer>     KW_ID              = KEYWORD.ID;
    private static final String                                 ALIAS_KEYWORD_ID   = "KeywordId";
    private static final TableField<KeywordRecord, String>      KW_SEARCH_OVERRIDE = KEYWORD.SEARCH_OVERRIDE;
    private static final TableField<KeywordTrRecord, String>    KT_LANG_CODE       = KEYWORD_TR.LANG_CODE;
    private static final TableField<KeywordTrRecord, String>    KT_NAME            = KEYWORD_TR.NAME;
    private static final TableField<KeywordTrRecord, Integer>   KT_VERSION         = KEYWORD_TR.VERSION;
    private static final TableField<KeywordTrRecord, Timestamp> KT_CREATED         = KEYWORD_TR.CREATED;
    private static final TableField<KeywordTrRecord, Timestamp> KT_LAST_MODIFIED   = KEYWORD_TR.LAST_MODIFIED;

    protected KeywordSyncConfig(@Qualifier("dslContext") @NotNull final DSLContext jooqCore,
        @Qualifier("publicDslContext") @NotNull final DSLContext jooqPublic,
        @Qualifier("dataSource") @NotNull final DataSource coreDataSource,
        @NotNull final JobBuilderFactory jobBuilderFactory, @NotNull final StepBuilderFactory stepBuilderFactory,
        @NotNull final DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @NotNull
    @Bean
    public Job syncKeywordJob() {
        return createJob();
    }

    @NotNull
    @Override
    protected String getJobName() {
        return "syncKeywordJob";
    }

    @NotNull
    @Override
    protected ItemWriter<PublicKeyword> publicWriter() {
        return new KeywordItemWriter(getJooqPublic());
    }

    @NotNull
    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(KT_ID, KW_ID.as(ALIAS_KEYWORD_ID), KT_LANG_CODE, KT_NAME, KT_VERSION, KT_CREATED, KT_LAST_MODIFIED,
                KW_SEARCH_OVERRIDE)
            .from(Keyword.KEYWORD)
            .innerJoin(KeywordTr.KEYWORD_TR)
            .on(KW_ID.eq(KeywordTr.KEYWORD_TR.KEYWORD_ID))
            .getSQL();
    }

    @NotNull
    @Override
    protected PublicKeyword makeEntity(@NotNull final ResultSet rs) throws SQLException {
        final Integer value = getInteger(KT_ID, rs);
        return PublicKeyword.builder()
            // TODO think the next line through
            .id(value != null ? value : -1)
            .keywordId(rs.getInt(ALIAS_KEYWORD_ID))
            .langCode(getString(KT_LANG_CODE, rs))
            .name(getString(KT_NAME, rs))
            .version(getInteger(KT_VERSION, rs))
            .created(getTimestamp(KT_CREATED, rs))
            .lastModified(getTimestamp(KT_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .searchOverride(getString(KW_SEARCH_OVERRIDE, rs))
            .build();
    }

    @NotNull
    @Override
    protected TableField<ch.difty.scipamato.publ.db.tables.records.KeywordRecord, Timestamp> lastSynchedField() {
        return ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.LAST_SYNCHED;
    }
}
