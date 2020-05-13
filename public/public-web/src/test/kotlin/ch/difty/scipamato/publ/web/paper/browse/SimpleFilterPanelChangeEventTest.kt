package ch.difty.scipamato.publ.web.paper.browse

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.TextArea
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private const val ID = "id"
private const val MARKUP_ID = "mId"

@ExtendWith(MockKExtension::class)
internal class SimpleFilterPanelChangeEventTest {

    private lateinit var e: SimpleFilterPanelChangeEvent

    @MockK(relaxUnitFun = true)
    private lateinit var targetMock: AjaxRequestTarget

    @MockK(relaxUnitFun = true)
    private lateinit var targetMock2: AjaxRequestTarget

    @MockK
    private lateinit var mockComponent: TextArea<String>

    @BeforeEach
    fun setUp() {
        every { mockComponent.id } returns ID
        every { mockComponent.markupId } returns MARKUP_ID
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(targetMock, targetMock2)
    }

    @Test
    fun canRetrieveTarget() {
        e = SimpleFilterPanelChangeEvent(targetMock)
        e.target shouldBeEqualTo targetMock
        verify { targetMock == targetMock }
    }

    @Test
    fun usingMinimalConstructor_doesNotSetAnySpecialStuff() {
        e = SimpleFilterPanelChangeEvent(targetMock)
        e.id.shouldBeNull()
        e.markupId.shouldBeNull()
    }

    @Test
    fun usingWithId_doesAddId() {
        e = SimpleFilterPanelChangeEvent(targetMock).withId("foo")
        e.id shouldBeEqualTo "foo"
        e.markupId.shouldBeNull()
    }

    @Test
    fun usingWithMarkupId_doesAddMarkupId() {
        e = SimpleFilterPanelChangeEvent(targetMock).withMarkupId("bar")
        e.id.shouldBeNull()
        e.markupId shouldBeEqualTo "bar"
    }

    @Test
    fun usingWithIdAndMarkupId_doesAddBoth() {
        e = SimpleFilterPanelChangeEvent(targetMock)
            .withId("hups")
            .withMarkupId("goo")
        e.id shouldBeEqualTo "hups"
        e.markupId shouldBeEqualTo "goo"
    }

    @Test
    fun canOverrideTarget() {
        e = SimpleFilterPanelChangeEvent(targetMock)
        e.target shouldBeEqualTo targetMock
        e.target = targetMock2
        e.target shouldBeEqualTo targetMock2
        verify { targetMock == targetMock }
        verify { targetMock2 == targetMock2 }
    }

    @Test
    fun consideringAddingToTarget_withIdLessEvent_addsTarget() {
        e = SimpleFilterPanelChangeEvent(targetMock)
        e.id.shouldBeNull()
        e.considerAddingToTarget(mockComponent)
        verify { targetMock.add(mockComponent) }
    }

    @Test
    fun consideringAddingToTarget_withDifferingId_doesNotAddTarget() {
        e = SimpleFilterPanelChangeEvent(targetMock)
            .withId("otherId")
            .withMarkupId(MARKUP_ID)
        e.considerAddingToTarget(mockComponent)
        verify(exactly = 0) { targetMock.add(mockComponent) }
    }

    @Test
    fun consideringAddingToTarget_withSameIdButNullMarkupId_addsTarget() {
        every { mockComponent.id } returns ID
        every { mockComponent.markupId } returns MARKUP_ID
        e = SimpleFilterPanelChangeEvent(targetMock).withId(ID)
        e.markupId.shouldBeNull()
        e.considerAddingToTarget(mockComponent)
        verify { targetMock.add(mockComponent) }
    }

    @Test
    fun consideringAddingToTarget_withSameIdAndDifferingMarkupId_addsTarget() {
        every { mockComponent.id } returns ID
        every { mockComponent.markupId } returns MARKUP_ID
        e = SimpleFilterPanelChangeEvent(targetMock)
            .withId(ID)
            .withMarkupId("otherMarkupId")
        e.considerAddingToTarget(mockComponent)
        verify { targetMock.add(mockComponent) }
    }

    @Test
    fun consideringAddingToTarget_withSameIdButSameMarkupId_doesNotAddTarget() {
        e = SimpleFilterPanelChangeEvent(targetMock)
            .withId(ID)
            .withMarkupId(MARKUP_ID)
        e.considerAddingToTarget(mockComponent)
        verify(exactly = 0) { targetMock.add(mockComponent) }
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(SimpleFilterPanelChangeEvent::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }
}