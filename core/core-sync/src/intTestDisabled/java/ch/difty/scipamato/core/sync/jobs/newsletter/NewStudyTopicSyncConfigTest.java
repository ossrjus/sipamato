package ch.difty.scipamato.core.sync.jobs.newsletter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jooq.DeleteConditionStep;
import org.jooq.TableField;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic;
import ch.difty.scipamato.core.db.tables.NewsletterTopicTr;
import ch.difty.scipamato.core.db.tables.PaperNewsletter;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.tables.NewStudyTopic;
import ch.difty.scipamato.publ.db.tables.records.NewStudyTopicRecord;

@SpringBootTest
class NewStudyTopicSyncConfigTest extends SyncConfigTest<NewStudyTopicRecord> {

    @Autowired
    private NewStudyTopicSyncConfig config;

    @Override
    protected Job getJob() {
        return config.syncNewStudyTopicJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected TableField<NewStudyTopicRecord, Timestamp> lastSynchedField() {
        return config.lastSynchedField();
    }

    @Override
    protected DeleteConditionStep<NewStudyTopicRecord> getPseudoFkDcs() {
        return config.getPseudoFkDcs();
    }

    @Override
    protected String expectedJobName() {
        return "syncNewStudyTopicJob";
    }

    @Override
    protected String expectedSelectSql() {
        // @formatter:off
        return
            "select \"public\".\"paper_newsletter\".\"newsletter_id\", \"public\".\"newsletter_topic_tr\".\"newsletter_topic_id\", "
                + "\"public\".\"newsletter_topic_tr\".\"version\", \"public\".\"newsletter_topic_tr\".\"created\", "
                + "\"public\".\"newsletter_topic_tr\".\"last_modified\" as \"NTTLM\", \"public\".\"newsletter_newsletter_topic\".\"sort\", "
                + "\"public\".\"newsletter_newsletter_topic\".\"last_modified\" as \"NNTLM\" from \"public\".\"paper_newsletter\" "
            + "join \"public\".\"newsletter_topic\" "
                + "on \"public\".\"paper_newsletter\".\"newsletter_topic_id\" = \"public\".\"newsletter_topic\".\"id\" "
            + "join \"public\".\"newsletter_topic_tr\" "
                + "on \"public\".\"newsletter_topic\".\"id\" = \"public\".\"newsletter_topic_tr\".\"newsletter_topic_id\" "
            + "left outer join \"public\".\"newsletter_newsletter_topic\" "
                + "on (\"public\".\"paper_newsletter\".\"newsletter_id\" = \"public\".\"newsletter_newsletter_topic\".\"newsletter_id\" "
                    + "and \"public\".\"newsletter_topic\".\"id\" = \"public\".\"newsletter_newsletter_topic\".\"newsletter_topic_id\") "
            + "join \"public\".\"newsletter\" on \"public\".\"paper_newsletter\".\"newsletter_id\" = \"public\".\"newsletter\".\"id\" "
            + "where \"public\".\"newsletter\".\"publication_status\" = 1";
        // @formatter:on
    }

    @Override
    protected TableField<NewStudyTopicRecord, Timestamp> expectedLastSyncField() {
        return NewStudyTopic.NEW_STUDY_TOPIC.LAST_SYNCHED;
    }

    @Override
    public String expectedPseudoFkSql() {
        return
            "delete from \"public\".\"new_study_topic\" where \"public\".\"new_study_topic\".\"newsletter_topic_id\" not in "
            + "(select distinct \"public\".\"newsletter_topic\".\"id\" from \"public\".\"newsletter_topic\")";
    }

    @Test
    void makingEntityWithNonNullSortAmdNttTimestamp() throws SQLException {
        makingEntityWithSort(3, 3, MODIFIED, MODIFIED, MODIFIED);
    }

    @Test
    void makingEntityWithNullSort_usesSortMaxInt() throws SQLException {
        makingEntityWithSort(null, Integer.MAX_VALUE, MODIFIED, MODIFIED, MODIFIED);
    }

    @Test
    void makingEntityWithNullNttAndNntTimestamp() throws SQLException {
        makingEntityWithSort(3, 3, null, null, null);
    }

    @Test
    void makingEntityWithNullNttTimestamp() throws SQLException {
        makingEntityWithSort(3, 3, null, MODIFIED, MODIFIED);
    }

    @Test
    void makingEntityWithNullNntTimestamp() throws SQLException {
        makingEntityWithSort(3, 3, MODIFIED, null, MODIFIED);
    }

    @Test
    void makingEntityWitNntTimestampAfterNttTimestamp() throws SQLException {
        Timestamp nttLm = Timestamp.from(MODIFIED
            .toInstant()
            .plusSeconds(2));
        makingEntityWithSort(3, 3, MODIFIED, nttLm, nttLm);
    }

    @Test
    void makingEntityWitNntTimestampBEforeNttTimestamp() throws SQLException {
        Timestamp nttLm = Timestamp.from(MODIFIED
            .toInstant()
            .minusSeconds(2));
        makingEntityWithSort(3, 3, MODIFIED, nttLm, MODIFIED);
    }

    private void makingEntityWithSort(final Integer sort, final Integer expectedSort, final Timestamp nttLastMod,
        final Timestamp nntLastMod, final Timestamp expectedLastMod) throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getInt(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID.getName())).thenReturn(1);
        when(rs.getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID.getName())).thenReturn(2);
        if (sort == null) {
            when(rs.getInt(NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.SORT.getName())).thenReturn(0);
            when(rs.wasNull()).thenReturn(false, false, true, false);
        } else {
            when(rs.getInt(NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.SORT.getName())).thenReturn(sort);
        }
        when(rs.getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION.getName())).thenReturn(4);
        when(rs.getTimestamp(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.CREATED.getName())).thenReturn(CREATED);
        when(rs.getTimestamp("NTTLM")).thenReturn(nttLastMod);
        when(rs.getTimestamp("NNTLM")).thenReturn(nntLastMod);

        PublicNewStudyTopic pns = config.makeEntity(rs);

        pns.getNewsletterId() shouldBeEqualTo 1;
        pns.getNewsletterTopicId() shouldBeEqualTo 2;
        pns.getSort() shouldBeEqualTo expectedSort;
        pns.getVersion() shouldBeEqualTo 4;
        pns.getCreated() shouldBeEqualTo CREATED;
        pns.getLastModified() shouldBeEqualTo expectedLastMod;
        assertThat(pns.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify{ rs.getInt(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID.getName()); }
        verify{ rs.getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID.getName()); }
        //verify{ rs.getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.SORT.getName()); }
        verify{ rs.getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION.getName()); }
        verify{ rs.getInt(NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.SORT.getName()); }
        verify{ rs.getTimestamp(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.CREATED.getName()); }
        verify{ rs.getTimestamp("NTTLM"); }
        verify{ rs.getTimestamp("NNTLM"); }
        verify(exactly=4) { rs.wasNull(); }

        confirmVerified(rs);
    }

}
