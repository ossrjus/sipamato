package ch.difty.scipamato.core.sync.jobs.newstudypagelink

import ch.difty.scipamato.core.sync.PublicNewStudyPageLink
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest
import org.jooq.DSLContext

internal class NewStudyPageLinkItemWriterTest :
    AbstractItemWriterTest<PublicNewStudyPageLink, NewStudyPageLinkItemWriter>() {
    override fun newWriter(dslContextMock: DSLContext) = NewStudyPageLinkItemWriter(dslContextMock)
}
