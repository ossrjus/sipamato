package ch.difty.scipamato.core.entity.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.entity.search.AuditSearchTerm.MatchType;
import ch.difty.scipamato.core.entity.search.AuditSearchTerm.TokenType;

@SuppressWarnings("SameParameterValue")
class AuditSearchTermTest {

    private static final String CREATED    = "CREATED";
    private static final String CREATED_BY = "CREATED_BY";

    private AuditSearchTerm st;

    private void assertSingleToken(String fieldName, TokenType tt, String userRawData, String userData,
        String dateRawData, String dateData) {
        assertThat(st.getFieldName()).isEqualTo(fieldName);
        assertThat(st.getTokens()).hasSize(1);
        assertToken(0, tt, userRawData, userData, dateRawData, dateData);
    }

    private void assertToken(int idx, TokenType tt, String userRawData, String userData, String dateRawData,
        String dateData) {
        if (userRawData != null) {
            assertThat(st
                .getTokens()
                .get(idx)
                .getUserRawData()).isEqualTo(userRawData);
            assertThat(st
                .getTokens()
                .get(idx)
                .getUserSqlData()).isEqualTo(userData);
        } else {
            assertThat(st
                .getTokens()
                .get(idx)
                .getUserRawData()).isNull();
            assertThat(st
                .getTokens()
                .get(idx)
                .getUserSqlData()).isNull();
        }
        if (dateRawData != null) {
            assertThat(st
                .getTokens()
                .get(idx)
                .getDateRawData()).isEqualTo(dateRawData);
            assertThat(st
                .getTokens()
                .get(idx)
                .getDateSqlData()).isEqualTo(dateData);
        } else {
            assertThat(st
                .getTokens()
                .get(idx)
                .getDateRawData()).isNull();
            assertThat(st
                .getTokens()
                .get(idx)
                .getDateSqlData()).isNull();
        }
        assertThat(st
            .getTokens()
            .get(idx)
            .getType()).isEqualTo(tt);
    }

    @Test
    void lexingUserSpecs_findsUserOnly() {
        String fieldName = CREATED_BY;
        st = new AuditSearchTerm(fieldName, "mkj");
        assertSingleToken(fieldName, TokenType.WORD, "mkj", "mkj", null, null);
    }

    @Test
    void lexingUserSpecsForNonUserField_findsNothing() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "mkj");
        assertThat(st.getFieldName()).isEqualTo(fieldName);
        assertThat(st.getTokens()).isEmpty();
    }

    @Test
    void lexingMinimumDate_findsDate() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, ">=2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.GREATEROREQUAL, null, null, "2017-12-01 23:15:13",
            "2017-12-01 23:15:13");
    }

    @Test
    void lexingMinimumDateWithoutTime_usesTimestampAtStartOfDay() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, ">=2017-12-01");
        assertSingleToken(fieldName, TokenType.GREATEROREQUAL, null, null, "2017-12-01", "2017-12-01 00:00:00");
    }

    @Test
    void lexingMinimumDateQuoted_findsDate() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, ">=\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.GREATEROREQUALQUOTED, null, null, "2017-12-01 23:15:13",
            "2017-12-01 23:15:13");
    }

    @Test
    void lexingMinimumDateExcluded_findsDate() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, ">2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.GREATERTHAN, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    void lexingMinimumDateExcludedWithoutDate_usesTimestampAtEndOfDay() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, ">2017-12-01");
        assertSingleToken(fieldName, TokenType.GREATERTHAN, null, null, "2017-12-01", "2017-12-01 23:59:59");
    }

    @Test
    void lexingMinimumDateExcludedQuoted_findsDate() {
        String fieldName = "LAST_MODIFIED";
        st = new AuditSearchTerm(fieldName, ">\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.GREATERTHANQUOTED, null, null, "2017-12-01 23:15:13",
            "2017-12-01 23:15:13");
    }

    @Test
    void lexingMaximumDate_findsDate() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "<=2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.LESSOREQUAL, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    void lexingMaximumDateWithoutTime_usesTimestampAtEndOfDay() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "<=2017-12-01");
        assertSingleToken(fieldName, TokenType.LESSOREQUAL, null, null, "2017-12-01", "2017-12-01 23:59:59");
    }

    @Test
    void lexingMaximumDateQuoted_findsDate() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "<=\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.LESSOREQUALQUOTED, null, null, "2017-12-01 23:15:13",
            "2017-12-01 23:15:13");
    }

    @Test
    void lexingMaximumDateExcludedQuoted_findsDate() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "<\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.LESSTHANQUOTED, null, null, "2017-12-01 23:15:13",
            "2017-12-01 23:15:13");
    }

    @Test
    void lexingMaximumDateExcludedQuotedWithoutTime_usesTimestampAtStartOfDay() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "<\"2017-12-01\"");
        assertSingleToken(fieldName, TokenType.LESSTHANQUOTED, null, null, "2017-12-01", "2017-12-01 00:00:00");
    }

    @Test
    void lexingMaximumDateExcluded_findsDate() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "<2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.LESSTHAN, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    void lexingExactDate_findsDate() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.EXACT, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    void lexingDateRangeQuoted_withEquals_findsBothDates() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "=\"2017-12-01 10:15:13\"-\"2017-12-02 23:12:11\"");
        assertSingleToken(fieldName, TokenType.RANGEQUOTED, null, null, "2017-12-01 10:15:13-2017-12-02 23:12:11",
            "2017-12-01 10:15:13-2017-12-02 23:12:11");
    }

    @Test
    void lexingDateRangeUnquoted_withEquals_findsBothDates() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "=2017-12-01 10:15:13-2017-12-02 23:12:11");
        assertSingleToken(fieldName, TokenType.RANGE, null, null, "2017-12-01 10:15:13-2017-12-02 23:12:11",
            "2017-12-01 10:15:13-2017-12-02 23:12:11");
    }

    @Test
    void lexingDateRangeUnquoted_withoutEquals_findsBothDates() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "2017-12-01 10:15:13-2017-12-02 23:12:11");
        assertSingleToken(fieldName, TokenType.RANGE, null, null, "2017-12-01 10:15:13-2017-12-02 23:12:11",
            "2017-12-01 10:15:13-2017-12-02 23:12:11");
    }

    @Test
    void lexingDateRange_findsBothDates() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "\"2017-12-01 10:15:13\"-\"2017-12-02 23:12:11\"");
        assertSingleToken(fieldName, TokenType.RANGEQUOTED, null, null, "2017-12-01 10:15:13-2017-12-02 23:12:11",
            "2017-12-01 10:15:13-2017-12-02 23:12:11");
    }

    @Test
    void lexingDateRangeQuoted_withDatePartOnly_findsBothDatesExtended() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "=\"2017-12-01\"-\"2017-12-02\"");
        assertSingleToken(fieldName, TokenType.RANGEQUOTED, null, null, "2017-12-01 00:00:00-2017-12-02 23:59:59",
            "2017-12-01 00:00:00-2017-12-02 23:59:59");
    }

    @Test
    void lexingDateRangeUnquoted_withDatePartOnly_findsBothDatesExtended() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "=2017-12-01-2017-12-02");
        assertSingleToken(fieldName, TokenType.RANGE, null, null, "2017-12-01 00:00:00-2017-12-02 23:59:59",
            "2017-12-01 00:00:00-2017-12-02 23:59:59");
    }

    @Test
    void lexingDateRangeUnquoted_withDatePartOnly2_findsBothDatesExtended() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "2017-12-01-2017-12-02");
        assertSingleToken(fieldName, TokenType.RANGE, null, null, "2017-12-01 00:00:00-2017-12-02 23:59:59",
            "2017-12-01 00:00:00-2017-12-02 23:59:59");
    }

    @Test
    void lexingDateRange_withMixedDateParts_findsBothDatesExtended() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "\"2017-12-01 12:13:14\"-\"2017-12-02\"");
        assertSingleToken(fieldName, TokenType.RANGEQUOTED, null, null, "2017-12-01 12:13:14-2017-12-02 23:59:59",
            "2017-12-01 12:13:14-2017-12-02 23:59:59");
    }

    @Test
    void lexingDateRange_withMixedDateParts2_findsBothDatesExtended() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "\"2017-12-01\"-\"2017-12-02 14:15:16\"");
        assertSingleToken(fieldName, TokenType.RANGEQUOTED, null, null, "2017-12-01 00:00:00-2017-12-02 14:15:16",
            "2017-12-01 00:00:00-2017-12-02 14:15:16");
    }

    /**
     * This might turn out questionable and might have to be rewritten to include
     * the entire day. Let's see
     */
    @Test
    void lexingExactDateWithoutTime_usesTimestampAtStartOfDay() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "2017-12-01");
        assertSingleToken(fieldName, TokenType.EXACT, null, null, "2017-12-01", "2017-12-01 00:00:00");
    }

    @Test
    void lexingExactDateQuoted_findsDate() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.EXACTQUOTED, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    void lexingExactDateWithEquals_findsDate() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "=2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.EXACT, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    void lexingExactDateQuotedWithEquals_findsDate() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "=\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.EXACTQUOTED, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    void lexingImproperDate_findsNothing() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "\"2017-12- 01 23:15:13\"");
        assertThat(st.getFieldName()).isEqualTo(fieldName);
        assertThat(st.getTokens()).isEmpty();
    }

    @Test
    void lexingUserAndDate_forUserField_findsUserTokenOnly() {
        String fieldName = CREATED_BY;
        st = new AuditSearchTerm(fieldName, "user =\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.WORD, "user", "user", null, null);
    }

    @Test
    void lexingUserAndDate_forDateField_findsDateTokenOnly() {
        String fieldName = CREATED;
        st = new AuditSearchTerm(fieldName, "user =\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.EXACTQUOTED, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    void tokenToString_forDateField() {
        st = new AuditSearchTerm(CREATED, "user =\"2017-12-01 23:15:13\"");
        assertThat(st.getTokens()).hasSize(1);
        assertThat(st
            .getTokens()
            .get(0)
            .toString()).isEqualTo("(DATE EXACTQUOTED 2017-12-01 23:15:13)");
    }

    @Test
    void tokenToString_forUserField() {
        st = new AuditSearchTerm(CREATED_BY, "foo =\"2017-12-01 23:15:13\"");
        assertThat(st.getTokens()).hasSize(1);
        assertThat(st
            .getTokens()
            .get(0)
            .toString()).isEqualTo("(USER WORD foo)");
    }

    @Test
    void byMatchType_withNullMatchType_isEmpty() {
        assertThat(AuditSearchTerm.TokenType.byMatchType(null)).isEmpty();
    }

    @Test
    void byMatchType_withValidMatchTypeNONE() {
        assertThat(AuditSearchTerm.TokenType.byMatchType(MatchType.NONE)).containsExactly(TokenType.WHITESPACE,
            TokenType.RAW);
    }

    @Test
    void byMatchType_withValidMatchTypeRANGE() {
        assertThat(AuditSearchTerm.TokenType.byMatchType(MatchType.RANGE)).containsExactly(TokenType.RANGEQUOTED,
            TokenType.RANGE);
    }

    @Test
    void byMatchType_withValidMatchTypeGREATER_OR_EQUAL() {
        assertThat(AuditSearchTerm.TokenType.byMatchType(MatchType.GREATER_OR_EQUAL)).containsExactly(
            TokenType.GREATEROREQUALQUOTED, TokenType.GREATEROREQUAL);
    }

    @Test
    void byMatchType_withValidMatchTypeGREATER_THAN() {
        assertThat(AuditSearchTerm.TokenType.byMatchType(MatchType.GREATER_THAN)).containsExactly(
            TokenType.GREATERTHANQUOTED, TokenType.GREATERTHAN);
    }

    @Test
    void byMatchType_withValidMatchTypeLESS_OR_EQUAL() {
        assertThat(AuditSearchTerm.TokenType.byMatchType(MatchType.LESS_OR_EQUAL)).containsExactly(
            TokenType.LESSOREQUALQUOTED, TokenType.LESSOREQUAL);
    }

    @Test
    void byMatchType_withValidMatchTypeLESS_THAN() {
        assertThat(AuditSearchTerm.TokenType.byMatchType(MatchType.LESS_THAN)).containsExactly(TokenType.LESSTHANQUOTED,
            TokenType.LESSTHAN);
    }

    @Test
    void byMatchType_withValidMatchTypeEQUALS() {
        assertThat(AuditSearchTerm.TokenType.byMatchType(MatchType.EQUALS)).containsExactly(TokenType.EXACTQUOTED,
            TokenType.EXACT);
    }

    @Test
    void byMatchType_withValidMatchTypeCONTAINS() {
        assertThat(AuditSearchTerm.TokenType.byMatchType(MatchType.CONTAINS)).containsExactly(TokenType.WORD);
    }
}
