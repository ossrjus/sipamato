package ch.difty.scipamato.core.web.behavior

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import ch.difty.scipamato.core.web.WicketTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class AjaxTextDownloadTest : WicketTest() {

    private val ad = AjaxTextDownload(false)

    private val targetDummy = AjaxRequestTargetSpy()

    @AfterEach
    fun tearDown() {
        targetDummy.reset()
    }

    @Test
    fun `can instantiate AjaxTextDownload`() {
        ad.content.shouldBeNull()
        ad.fileName.shouldBeNull()
    }

    @Test
    fun `can set title and content`() {
        ad.apply {
            content = "foo"
            fileName = "bar.txt"
        }
        ad.content shouldBeEqualTo "foo"
        ad.fileName shouldBeEqualTo "bar.txt"
    }

    @Test
    fun `clicking the link adds javascript to target2`() {
        val l = object : AjaxLink<Void>("l") {
            override fun onClick(target: AjaxRequestTarget?) = ad.initiate(targetDummy)
        }
        l.add(ad)
        tester.startComponentInPage(l)
        tester.clickLink(l)
        targetDummy.javaScripts.size shouldBeEqualTo 1
        targetDummy.javaScripts.contains("""setTimeout("window.location.href='./page?2-1.0-l'", 100);""")
    }

    @Test
    fun `clicking the link adds javascript to target`() {
        val ad2 = AjaxTextDownload(true)
        val l = object : AjaxLink<Void>("l") {
            override fun onClick(target: AjaxRequestTarget?) {
                ad2.initiate(targetDummy)
            }
        }
        l.add(ad2)
        tester.startComponentInPage(l)
        tester.clickLink(l)
        // containing timestamp -> difficult to test
        targetDummy.javaScripts.size shouldBeEqualTo 1
    }
}