package ch.difty.scipamato.core.web

import ch.difty.scipamato.TestApplication
import ch.difty.scipamato.common.ClearAllMocksExtension
import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.navigator.ItemNavigator
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.core.logic.exporting.RisAdapterFactory
import ch.difty.scipamato.core.logic.exporting.RisAdapterFactory.Companion.create
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import ch.difty.scipamato.core.logic.parsing.AuthorParserFactory
import ch.difty.scipamato.core.logic.parsing.AuthorParserFactory.Companion.create
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy
import ch.difty.scipamato.core.persistence.CodeClassService
import ch.difty.scipamato.core.persistence.CodeService
import ch.difty.scipamato.core.persistence.KeywordService
import ch.difty.scipamato.core.persistence.NewsletterService
import ch.difty.scipamato.core.persistence.NewsletterTopicService
import ch.difty.scipamato.core.persistence.PaperService
import ch.difty.scipamato.core.persistence.PaperSlimService
import ch.difty.scipamato.core.persistence.SearchOrderService
import ch.difty.scipamato.core.persistence.UserService
import ch.difty.scipamato.core.pubmed.PubmedArticleService
import ch.difty.scipamato.core.pubmed.PubmedImporter
import com.ninjasquad.springmockk.MockkBean
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("wickettest")
@ExtendWith(MockKExtension::class, ClearAllMocksExtension::class)
abstract class AbstractWicketTest {

    @Bean
    fun authorParserFactory(): AuthorParserFactory = create(AuthorParserStrategy.PUBMED)

    @Bean
    fun risAdapterFactory(): RisAdapterFactory = create(RisExporterStrategy.DEFAULT)

    @Autowired
    protected lateinit var application: TestApplication

    @Autowired
    protected lateinit var applicationContextMock: ApplicationContext

    @Autowired
    protected lateinit var dateTimeService: DateTimeService

    @MockkBean(relaxed = true)
    protected lateinit var sessionFacadeMock: ScipamatoWebSessionFacade

    @MockkBean(relaxed = true)
    protected lateinit var itemNavigatorMock: ItemNavigator<Long>

    @MockkBean
    protected lateinit var pubmedImporterMock: PubmedImporter

    @MockkBean
    protected lateinit var pubmedArticleServiceMock: PubmedArticleService

    @MockkBean(relaxed = true)
    protected lateinit var codeServiceMock: CodeService

    @MockkBean
    protected lateinit var codeClassServiceMock: CodeClassService

    @MockkBean(relaxed = true)
    protected lateinit var keywordServiceMock: KeywordService

    @MockkBean(relaxed = true)
    protected lateinit var newsletterServiceMock: NewsletterService

    @MockkBean(relaxed = true)
    protected lateinit var newsletterTopicServiceMock: NewsletterTopicService

    @MockkBean(relaxed = true)
    protected lateinit var paperServiceMock: PaperService

    @MockkBean(relaxed = true)
    protected lateinit var paperSlimServiceMock: PaperSlimService

    @MockkBean(relaxed = true)
    protected lateinit var searchOrderServiceMock: SearchOrderService

    @MockkBean(relaxed = true)
    protected lateinit var userServiceMock: UserService
}
