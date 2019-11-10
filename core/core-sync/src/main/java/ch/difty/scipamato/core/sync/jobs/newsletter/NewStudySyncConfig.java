package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static ch.difty.scipamato.core.db.tables.PaperNewsletter.PAPER_NEWSLETTER;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.TableField;
import org.jooq.conf.ParamType;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.db.tables.Newsletter;
import ch.difty.scipamato.core.db.tables.records.PaperNewsletterRecord;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.sync.jobs.SyncConfig;
import ch.difty.scipamato.publ.db.tables.NewStudy;
import ch.difty.scipamato.publ.db.tables.NewsletterTopic;
import ch.difty.scipamato.publ.db.tables.records.NewStudyRecord;

/**
 * Defines the newStudy synchronization job, applying two steps:
 * <ol>
 * <li>insertingOrUpdating: inserts new records or updates if already present</li>
 * <li>purging: removes records that have not been touched during the first
 * step (within a defined grace time in minutes)</li>
 * </ol>
 *
 * @author u.joss
 */
@SuppressWarnings("SameParameterValue")
@Configuration
@Profile("!wickettest")
public class NewStudySyncConfig
    extends SyncConfig<PublicNewStudy, ch.difty.scipamato.publ.db.tables.records.NewStudyRecord> {

    private static final String TOPIC      = "newStudy";
    private static final int    CHUNK_SIZE = 100;

    // relevant fields of the core paperNewsletter as well as paper record
    private static final TableField<PaperNewsletterRecord, Long>      PN_PAPER_ID            = PAPER_NEWSLETTER.PAPER_ID;
    private static final TableField<PaperNewsletterRecord, Integer>   PN_NEWSLETTER_ID       = PAPER_NEWSLETTER.NEWSLETTER_ID;
    private static final TableField<PaperNewsletterRecord, Integer>   PN_NEWSLETTER_TOPIC_ID = PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID;
    private static final TableField<PaperNewsletterRecord, String>    PN_HEADLINE            = PAPER_NEWSLETTER.HEADLINE;
    private static final TableField<PaperRecord, Integer>             P_YEAR                 = PAPER.PUBLICATION_YEAR;
    private static final TableField<PaperRecord, Long>                P_NUMBER               = PAPER.NUMBER;
    private static final TableField<PaperRecord, String>              P_FIRST_AUTHOR         = PAPER.FIRST_AUTHOR;
    private static final TableField<PaperRecord, String>              P_AUTHORS              = PAPER.AUTHORS;
    private static final TableField<PaperRecord, String>              P_GOALS                = PAPER.GOALS;
    private static final TableField<PaperNewsletterRecord, Integer>   PN_VERSION             = PAPER_NEWSLETTER.VERSION;
    private static final TableField<PaperNewsletterRecord, Timestamp> PN_CREATED             = PAPER_NEWSLETTER.CREATED;
    private static final TableField<PaperNewsletterRecord, Timestamp> PN_LAST_MODIFIED       = PAPER_NEWSLETTER.LAST_MODIFIED;

    protected NewStudySyncConfig(@Qualifier("dslContext") @NotNull final DSLContext jooqCore,
        @Qualifier("publicDslContext") @NotNull final DSLContext jooqPublic,
        @Qualifier("dataSource") @NotNull final DataSource coreDataSource,
        @NotNull final JobBuilderFactory jobBuilderFactory, @NotNull final StepBuilderFactory stepBuilderFactory,
        @NotNull final DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @NotNull
    @Bean
    public Job syncNewStudyJob() {
        return createJob();
    }

    @NotNull
    @Override
    protected String getJobName() {
        return "syncNewStudyJob";
    }

    @NotNull
    @Override
    protected ItemWriter<PublicNewStudy> publicWriter() {
        return new NewStudyItemWriter(getJooqPublic());
    }

    @NotNull
    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(PN_NEWSLETTER_ID, PN_PAPER_ID, PN_NEWSLETTER_TOPIC_ID, P_YEAR, P_NUMBER, P_FIRST_AUTHOR, P_AUTHORS,
                PN_HEADLINE, P_GOALS, PN_VERSION, PN_CREATED, PN_LAST_MODIFIED)
            .from(PAPER_NEWSLETTER)
            .innerJoin(PAPER)
            .on(PAPER_NEWSLETTER.PAPER_ID.eq(PAPER.ID))
            .innerJoin(Newsletter.NEWSLETTER)
            .on(PAPER_NEWSLETTER.NEWSLETTER_ID.eq(Newsletter.NEWSLETTER.ID))
            .where(Newsletter.NEWSLETTER.PUBLICATION_STATUS.eq(PUBLICATION_STATUS_PUBLISHED))
            .getSQL(ParamType.INLINED);
    }

    @NotNull
    @Override
    protected PublicNewStudy makeEntity(@NotNull final ResultSet rs) throws SQLException {
        return PublicNewStudy
            .builder()
            .newsletterId(getInteger(PN_NEWSLETTER_ID, rs))
            .newsletterTopicId(getInteger(PN_NEWSLETTER_TOPIC_ID, rs))
            .sort(1) // change this if the users will request to be able to sort the studies within the topics
            .paperNumber(getLong(P_NUMBER, rs))
            .year(getInteger(P_YEAR, rs))
            .authors(getAuthors(P_FIRST_AUTHOR, P_AUTHORS, rs))
            .headline(getString(PN_HEADLINE, rs))
            .description(getString(P_GOALS, rs))
            .version(getInteger(PN_VERSION, rs))
            .created(getTimestamp(PN_CREATED, rs))
            .lastModified(getTimestamp(PN_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .build();
    }

    private String getAuthors(final TableField<PaperRecord, String> firstAuthor,
        final TableField<PaperRecord, String> authors, final ResultSet rs) throws SQLException {
        final String firstAuthorString = getString(firstAuthor, rs);
        final String authorString = getString(authors, rs);
        if (authorString.contains(","))
            return firstAuthorString + " et al.";
        else
            return firstAuthorString;
    }

    @NotNull
    @Override
    protected TableField<NewStudyRecord, Timestamp> lastSynchedField() {
        return NewStudy.NEW_STUDY.LAST_SYNCHED;
    }

    @Nullable
    @Override
    public DeleteConditionStep<ch.difty.scipamato.publ.db.tables.records.NewStudyRecord> getPseudoFkDcs() {
        return getJooqPublic()
            .delete(NewStudy.NEW_STUDY)
            .where(NewStudy.NEW_STUDY.NEWSLETTER_TOPIC_ID.notIn(getJooqPublic()
                .selectDistinct(NewsletterTopic.NEWSLETTER_TOPIC.ID)
                .from(NewsletterTopic.NEWSLETTER_TOPIC)));
    }
}
