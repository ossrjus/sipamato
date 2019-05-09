package ch.difty.scipamato.publ.web.paper.browse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextArea;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleFilterPanelChangeEventTest {

    private SimpleFilterPanelChangeEvent e;

    @Mock
    private AjaxRequestTarget targetMock, targetMock2;

    @Mock
    private TextArea<?> mockComponent;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(targetMock, targetMock2);
    }

    @Test
    void canRetrieveTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }

    @Test
    void usingMinimalConstructor_doesNotSetAnySpecialStuff() {
        e = new SimpleFilterPanelChangeEvent(targetMock);
        assertThat(e.getId()).isNull();
        assertThat(e.getMarkupId()).isNull();
    }

    @Test
    void usingWithId_doesAddId() {
        e = new SimpleFilterPanelChangeEvent(targetMock).withId("foo");
        assertThat(e.getId()).isEqualTo("foo");
        assertThat(e.getMarkupId()).isNull();
    }

    @Test
    void usingWithMarkupId_doesAddMarkupId() {
        e = new SimpleFilterPanelChangeEvent(targetMock).withMarkupId("bar");
        assertThat(e.getId()).isNull();
        assertThat(e.getMarkupId()).isEqualTo("bar");
    }

    @Test
    void usingWithIdAndMarkupId_doesAddBoth() {
        e = new SimpleFilterPanelChangeEvent(targetMock)
            .withId("hups")
            .withMarkupId("goo");
        assertThat(e.getId()).isEqualTo("hups");
        assertThat(e.getMarkupId()).isEqualTo("goo");
    }

    @Test
    void canOverrideTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
        e.setTarget(targetMock2);
        assertThat(e.getTarget()).isEqualTo(targetMock2);
    }

    @Test
    void consideringAddingToTarget_withIdLessEvent_addsTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock);
        assertThat(e.getId()).isNull();

        e.considerAddingToTarget(mockComponent);

        verify(targetMock).add(mockComponent);
    }

    @Test
    void consideringAddingToTarget_withDifferingId_doesNotAddTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock)
            .withId("otherId")
            .withMarkupId("mId");
        e.considerAddingToTarget(mockComponent);
        verify(targetMock, never()).add(mockComponent);
    }

    @Test
    void consideringAddingToTarget_withSameIdButNullMarkupId_addsTarget() {
        when(mockComponent.getId()).thenReturn("id");
        when(mockComponent.getMarkupId()).thenReturn("mId");

        e = new SimpleFilterPanelChangeEvent(targetMock).withId("id");
        assertThat(e.getMarkupId()).isNull();

        e.considerAddingToTarget(mockComponent);

        verify(targetMock).add(mockComponent);
    }

    @Test
    void consideringAddingToTarget_withSameIdAndDifferingMarkupId_addsTarget() {
        when(mockComponent.getId()).thenReturn("id");
        when(mockComponent.getMarkupId()).thenReturn("mId");

        e = new SimpleFilterPanelChangeEvent(targetMock)
            .withId("id")
            .withMarkupId("otherMarkupId");
        e.considerAddingToTarget(mockComponent);
        verify(targetMock).add(mockComponent);
    }

    @Test
    void consideringAddingToTarget_withSameIdButSameMarkupId_doesNotAddTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock)
            .withId("id")
            .withMarkupId("mId");
        e.considerAddingToTarget(mockComponent);
        verify(targetMock, never()).add(mockComponent);
    }

    @Test
    void equals() {
        EqualsVerifier
            .forClass(SimpleFilterPanelChangeEvent.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

}
