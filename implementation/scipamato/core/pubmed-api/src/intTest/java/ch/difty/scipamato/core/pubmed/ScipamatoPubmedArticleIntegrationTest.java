package ch.difty.scipamato.core.pubmed;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.oxm.XmlMappingException;

import ch.difty.scipamato.core.pubmed.api.*;

class ScipamatoPubmedArticleIntegrationTest extends PubmedIntegrationTest {

    private static final String XML_2539026  = "xml/pubmed_result_25395026.xml";
    private static final String XML_23454700 = "xml/pubmed_result_23454700.xml";
    private static final String XML_26607712 = "xml/pubmed_result_26607712.xml";
    private static final String XML_27214671 = "xml/pubmed_result_27214671.xml";
    private static final String XML_27224452 = "xml/pubmed_result_27224452.xml";
    private static final String XML_27258721 = "xml/pubmed_result_27258721.xml";
    private static final String XML_30124840 = "xml/pubmed_result_30124840.xml";
    private static final String XML_29144419 = "xml/pubmed_result_29144419.xml";
    private static final String XML_30077140 = "xml/pubmed_result_30077140.xml";

    @Test
    void feedIntoScipamatoArticle_25395026() throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_2539026);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertArticle239026(sa);
    }

    private void assertArticle239026(final PubmedArticleFacade sa) {
        assertThat(sa.getPmId()).isEqualTo("25395026");
        assertThat(sa.getAuthors()).isEqualTo(
            "Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Turner");
        assertThat(sa.getPublicationYear()).isEqualTo("2014");
        assertThat(sa.getLocation()).isEqualTo("Am J Epidemiol. 2014; 180 (12): 1145-1149.");
        assertThat(sa.getTitle()).isEqualTo(
            "Interactions between cigarette smoking and fine particulate matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II.");
        assertThat(sa.getDoi()).isEqualTo("10.1093/aje/kwu275");
        assertThat(sa.getOriginalAbstract()).startsWith(
            "The International Agency for Research on Cancer recently classified outdoor air pollution");
        assertThat(sa
            .getOriginalAbstract()
            .trim()).endsWith("based on reducing exposure to either risk factor alone.");
    }

    @Test
    void feedIntoScipamatoArticle_23454700_withoutIssue_stillHasColon() throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_23454700);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertThat(sa.getPmId()).isEqualTo("23454700");
        assertThat(sa.getAuthors()).isEqualTo(
            "Pascal M, Corso M, Chanel O, Declercq C, Badaloni C, Cesaroni G, Henschel S, Meister K, Haluza D, Martin-Olmedo P, Medina S; Aphekom group.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Pascal");
        assertThat(sa.getPublicationYear()).isEqualTo("2013");
        assertThat(sa.getLocation()).isEqualTo("Sci Total Environ. 2013; 449: 390-400.");
        assertThat(sa.getTitle()).isEqualTo(
            "Assessing the public health impacts of urban air pollution in 25 European cities: results of the Aphekom project.");
        assertThat(sa.getDoi()).isEqualTo("10.1016/j.scitotenv.2013.01.077");
        assertThat(sa.getOriginalAbstract()).startsWith(
            "INTRODUCTION: The Aphekom project aimed to provide new, clear, and meaningful information on the health effects of air pollution in Europe.");
        assertThat(sa.getOriginalAbstract()).endsWith("EU legislation is being revised for an update in 2013.");
    }

    @Test
    void feedIntoScipamatoArticle_26607712_notHavingPaginationButElocationIdOfTypePii_usesThat()
        throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_26607712);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertThat(sa.getPmId()).isEqualTo("26607712");
        assertThat(sa.getAuthors()).isEqualTo("Hart JE, Puett RC, Rexrode KM, Albert CM, Laden F.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Hart");
        assertThat(sa.getPublicationYear()).isEqualTo("2015");
        assertThat(sa.getLocation()).isEqualTo("J Am Heart Assoc. 2015; 4 (12). pii: e002301.");
        assertThat(sa.getTitle()).isEqualTo(
            "Effect Modification of Long-Term Air Pollution Exposures and the Risk of Incident Cardiovascular Disease in US Women.");
        assertThat(sa.getDoi()).isEqualTo("10.1161/JAHA.115.002301");
        assertThat(sa.getOriginalAbstract()).startsWith(
            "BACKGROUND: Ambient air pollution exposures have been frequently linked to cardiovascular disease (CVD) morbidity and mortality. However, less is known about the populations most susceptible to these adverse effects.");
        assertThat(sa.getOriginalAbstract()).endsWith(
            "women with diabetes were identified as the subpopulation most sensitive to the adverse cardiovascular health effects of PM.");
    }

    @Test
    void feedIntoScipamatoArticle_27214671_yearFromMedDate() throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_27214671);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertThat(sa.getPmId()).isEqualTo("27214671");
        assertThat(sa.getAuthors()).isEqualTo("Clark-Reyna SE, Grineski SE, Collins TW.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Clark-Reyna");
        assertThat(sa.getPublicationYear()).isEqualTo("2016");
        assertThat(sa.getLocation()).isEqualTo("Fam Community Health. 2016; 39 (3): 160-168.");
        assertThat(sa.getTitle()).isEqualTo(
            "Health Status and Residential Exposure to Air Toxics: What Are the Effects on Children's Academic Achievement?");
        assertThat(sa.getDoi()).isEqualTo("10.1097/FCH.0000000000000112");
        assertThat(sa.getOriginalAbstract()).startsWith(
            "This article examines the effects of children's subjective health status and exposure to residential environmental toxins");
        assertThat(sa.getOriginalAbstract()).endsWith(
            "there is an independent effect of air pollution on children's academic achievement that cannot be explained by poor health alone.");
    }

    @Test
    void feedIntoScipamatoArticle_27224452_authorWithCollectiveName() throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_27224452);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertThat(sa.getPmId()).isEqualTo("27224452");
        assertThat(sa.getAuthors()).isEqualTo(
            "Lanzinger S, Schneider A, Breitner S, Stafoggia M, Erzen I, Dostal M, Pastorkova A, Bastian S, Cyrys J, Zscheppang A, Kolodnitska T, Peters A; UFIREG study group.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Lanzinger");
        assertThat(sa.getPublicationYear()).isEqualTo("2016");
        assertThat(sa.getLocation()).isEqualTo("Am J Respir Crit Care Med. 2016; 194 (10): 1233-1241.");
        assertThat(sa.getTitle()).isEqualTo(
            "Ultrafine and Fine Particles and Hospital Admissions in Central Europe. Results from the UFIREG Study.");
        assertThat(sa.getDoi()).isEqualTo("10.1164/rccm.201510-2042OC");
        assertThat(sa.getOriginalAbstract()).startsWith(
            "RATIONALE: Evidence of short-term effects of ultrafine particles (UFP) on health is still inconsistent and few multicenter studies have been conducted so far especially in Europe.");
        assertThat(sa.getOriginalAbstract()).endsWith(
            "harmonized UFP measurements to draw definite conclusions on health effects of UFP.");
    }

    @Test
    void feedIntoScipamatoArticle_27258721() throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_27258721);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertThat(sa.getPmId()).isEqualTo("27258721");
        assertThat(sa.getAuthors()).isEqualTo(
            "Aguilera I, Dratva J, Caviezel S, Burdet L, de Groot E, Ducret-Stich RE, Eeftens M, Keidel D, Meier R, Perez L, Rothe T, Schaffner E, Schmit-Trucksäss A, Tsai MY, Schindler C, Künzli N, Probst-Hensch N.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Aguilera");
        assertThat(sa.getPublicationYear()).isEqualTo("2016");
        assertThat(sa.getLocation()).isEqualTo("Environ Health Perspect. 2016; 124 (11): 1700-1706.");
        assertThat(sa.getTitle()).isEqualTo(
            "Particulate Matter and Subclinical Atherosclerosis: Associations between Different Particle Sizes and Sources with Carotid Intima-Media Thickness in the SAPALDIA Study.");
        assertThat(sa.getDoi()).isEqualTo("10.1289/EHP161");
        assertThat(sa.getOriginalAbstract()).startsWith(
            "BACKGROUND: Subclinical atherosclerosis has been associated with long-term exposure to particulate matter (PM)");
        assertThat(sa.getOriginalAbstract()).endsWith(
            "SAPALDIA study. Environ Health Perspect 124:1700-1706; http://dx.doi.org/10.1289/EHP161.");
    }

    @Test
    void manualExplorationOfFile_25395026() throws XmlMappingException, IOException {
        PubmedArticleSet articleSet = getPubmedArticleSet(XML_2539026);
        assertThat(articleSet.getPubmedArticleOrPubmedBookArticle()).hasSize(1);

        java.lang.Object pubmedArticleObject = articleSet
            .getPubmedArticleOrPubmedBookArticle()
            .get(0);
        assertThat(pubmedArticleObject).isInstanceOf(PubmedArticle.class);

        PubmedArticle pubmedArticle = (PubmedArticle) articleSet
            .getPubmedArticleOrPubmedBookArticle()
            .get(0);

        MedlineCitation medlineCitation = pubmedArticle.getMedlineCitation();
        assertThat(medlineCitation
            .getPMID()
            .getvalue()).isEqualTo("25395026");

        Article article = medlineCitation.getArticle();
        Journal journal = article.getJournal();
        JournalIssue journalIssue = journal.getJournalIssue();
        assertThat(journalIssue.getVolume()).isEqualTo("180");
        assertThat(journalIssue.getIssue()).isEqualTo("12");

        assertThat(journalIssue
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()).hasSize(3);
        assertThat(journalIssue
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()
            .get(0)).isInstanceOf(Year.class);
        assertThat(journalIssue
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()
            .get(1)).isInstanceOf(Month.class);
        assertThat(journalIssue
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()
            .get(2)).isInstanceOf(Day.class);
        Year year = (Year) journalIssue
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()
            .get(0);
        assertThat(year.getvalue()).isEqualTo("2014");

        assertThat(journal.getTitle()).isEqualTo("American journal of epidemiology");
        assertThat(journal.getISOAbbreviation()).isEqualTo("Am. J. Epidemiol.");
        assertThat(article
            .getArticleTitle()
            .getvalue()).isEqualTo(
            "Interactions between cigarette smoking and fine particulate matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II.");

        assertThat(article.getPaginationOrELocationID()).hasSize(2);
        assertThat(article
            .getPaginationOrELocationID()
            .get(0)).isInstanceOf(Pagination.class);
        assertThat(article
            .getPaginationOrELocationID()
            .get(1)).isInstanceOf(ELocationID.class);

        Pagination pagination = (Pagination) article
            .getPaginationOrELocationID()
            .get(0);
        assertThat(pagination.getStartPageOrEndPageOrMedlinePgn()).hasSize(1);
        assertThat(pagination
            .getStartPageOrEndPageOrMedlinePgn()
            .get(0)).isInstanceOf(MedlinePgn.class);
        MedlinePgn pgn = (MedlinePgn) pagination
            .getStartPageOrEndPageOrMedlinePgn()
            .get(0);
        assertThat(pgn.getvalue()).isEqualTo("1145-9");

        ELocationID elocationId = (ELocationID) article
            .getPaginationOrELocationID()
            .get(1);
        assertThat(elocationId.getValidYN()).isEqualTo("Y");
        assertThat(elocationId.getvalue()).isEqualTo("10.1093/aje/kwu275");
        assertThat(elocationId.getEIdType()).isEqualTo("doi");

        assertThat(article
            .getAbstract()
            .getAbstractText()).hasSize(1);
        assertThat(article
            .getAbstract()
            .getAbstractText()
            .get(0)
            .getMixedContent()
            .get(0)
            .toString()).startsWith(
            "The International Agency for Research on Cancer recently classified outdoor air pollution");

        AuthorList authorList = article.getAuthorList();
        assertThat(authorList.getCompleteYN()).isEqualTo("Y");
        assertThat(authorList.getType()).isNull();
        assertThat(authorList.getAuthor()).hasSize(9);
        assertThat(authorList.getAuthor())
            .extracting("validYN")
            .containsOnly("Y");

        List<String> authorNames = authorList
            .getAuthor()
            .stream()
            .map(Author::getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName)
            .flatMap(Collection::stream)
            .filter((o) -> o instanceof LastName)
            .map(lm -> ((LastName) lm).getvalue())
            .collect(Collectors.toList());
        assertThat(authorNames).contains("Turner", "Cohen", "Jerrett", "Gapstur", "Diver", "Pope", "Krewski",
            "Beckerman", "Samet");

        assertThat(article.getArticleDate()).hasSize(1);
        ArticleDate articleDate = article
            .getArticleDate()
            .get(0);
        assertThat(articleDate.getDateType()).isEqualTo("Electronic");
        assertThat(articleDate
            .getYear()
            .getvalue()).isEqualTo("2014");

        MedlineJournalInfo medlineJournalInfo = medlineCitation.getMedlineJournalInfo();
        assertThat(medlineJournalInfo.getCountry()).isEqualTo("United States");
        assertThat(medlineJournalInfo.getMedlineTA()).isEqualTo("Am J Epidemiol");
        assertThat(medlineJournalInfo.getNlmUniqueID()).isEqualTo("7910653");
        assertThat(medlineJournalInfo.getISSNLinking()).isEqualTo("0002-9262");

        ChemicalList chemicalList = medlineCitation.getChemicalList();
        assertThat(chemicalList.getChemical()).hasSize(3);

        MeshHeadingList meshHeadingList = medlineCitation.getMeshHeadingList();
        assertThat(meshHeadingList.getMeshHeading()).hasSize(20);

        List<KeywordList> keywordList = medlineCitation.getKeywordList();
        assertThat(keywordList).hasSize(1);
    }

    @Test
    void feedIntoScipamatoArticle_30124840() throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_30124840);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertThat(sa.getPmId()).isEqualTo("30124840");
        assertThat(sa.getAuthors()).isEqualTo(
            "Münzel T, Gori T, Al-Kindi S, Deanfield J, Lelieveld J, Daiber A, Rajagopalan S.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Münzel");
        assertThat(sa.getPublicationYear()).isEqualTo("2018");
        assertThat(sa.getLocation()).isEqualTo(
            "Eur Heart J. 2018 Aug 14. doi: 10.1093/eurheartj/ehy481. [Epub ahead of print]");
        assertThat(sa.getTitle()).isEqualTo(
            "Effects of gaseous and solid constituents of air pollution on endothelial function.");
        assertThat(sa.getDoi()).isEqualTo("10.1093/eurheartj/ehy481");
    }

    @Test
    void feedIntoScipamatoArticle_29144419_withTagsInAbstract_canExtractAbstract()
        throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_29144419);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertThat(sa.getPmId()).isEqualTo("29144419");
        assertThat(sa.getAuthors()).isEqualTo("Oudin A, Bråbäck L, Oudin Åström D, Forsberg B.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Oudin");
        assertThat(sa.getPublicationYear()).isEqualTo("2017");
        assertThat(sa.getLocation()).isEqualTo("Int J Environ Res Public Health. 2017; 14 (11). pii: E1392.");
        assertThat(sa.getTitle()).isEqualTo(
            "Air Pollution and Dispensed Medications for Asthma, and Possible Effect Modifiers Related to Mental Health and Socio-Economy: A Longitudinal Cohort Study of Swedish Children and Adolescents.");
        assertThat(sa.getDoi()).isEqualTo("10.3390/ijerph14111392");
        assertThat(sa.getOriginalAbstract()).startsWith(
            "It has been suggested that children that are exposed to a stressful environment at home have an increased susceptibility for air pollution-related asthma.");
        assertThat(sa.getOriginalAbstract()).endsWith(
            "We did not observe support for our hypothesis that stressors linked to socio-economy or mental health problems would increase susceptibility to the effects of air pollution on the development of asthma.");
        assertThat(sa.getOriginalAbstract()).hasSize(1844);
    }

    @Test
    void feedIntoScipamatoArticle_30077140_withTagsInTitleAndAbstract_canExtractFullTitleAndAbstract()
        throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_30077140);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertThat(sa.getPmId()).isEqualTo("30077140");
        assertThat(sa.getAuthors()).isEqualTo("Vodonos A, Awad YA, Schwartz J.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Vodonos");
        assertThat(sa.getPublicationYear()).isEqualTo("2018");
        assertThat(sa.getLocation()).isEqualTo("Environ Res. 2018; 166: 677-689.");
        assertThat(sa.getTitle()).isEqualTo(
            "The concentration-response between long-term PM2.5 exposure and mortality; A meta-regression approach.");
        assertThat(sa.getDoi()).isEqualTo("10.1016/j.envres.2018.06.021");
        assertThat(sa.getOriginalAbstract()).startsWith(
            "BACKGROUND: Long-term exposure to ambient fine particulate matter (≤ 2.5 μg/m3 in");
        assertThat(sa.getOriginalAbstract()).endsWith(
            "The concentration -response function produced here can be further applied in the global health risk assessment of air particulate matter.");
        assertThat(sa.getOriginalAbstract()).hasSize(2295);
    }
}
