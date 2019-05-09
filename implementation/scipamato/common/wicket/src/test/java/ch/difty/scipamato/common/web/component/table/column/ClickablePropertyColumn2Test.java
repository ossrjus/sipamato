package ch.difty.scipamato.common.web.component.table.column;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.web.component.SerializableBiConsumer;
import ch.difty.scipamato.common.web.component.SerializableSupplier;

@ExtendWith(MockitoExtension.class)
class ClickablePropertyColumn2Test {

    @Mock
    private SerializableBiConsumer<IModel<String>, Integer> biConsumerMock;
    @Mock
    private SerializableSupplier<Integer>                   supplierMock;

    private final IModel<String> displayModel  = new Model<>("foo");
    private final String         property      = "prop";
    private final int            suppliedValue = 5;
    private final Model<String>  clickModel    = Model.of("bar");

    private ClickablePropertyColumn2<String, String, Integer> c;

    @Test
    void testOnClick_withSortProperty() {
        String sort = "sort";

        when(supplierMock.get()).thenReturn(suppliedValue);

        c = new ClickablePropertyColumn2<>(displayModel, sort, property, biConsumerMock, supplierMock);
        c.onClick(clickModel);

        verify(supplierMock).get();
        verify(biConsumerMock).accept(clickModel, suppliedValue);
    }

    @Test
    void testOnClick_inNewTab() {
        String sort = "sort";

        when(supplierMock.get()).thenReturn(suppliedValue);

        c = new ClickablePropertyColumn2<>(displayModel, sort, property, biConsumerMock, supplierMock, true);
        c.onClick(clickModel);

        verify(supplierMock).get();
        verify(biConsumerMock).accept(clickModel, suppliedValue);
    }

    @Test
    void testOnClick_withoutSortProperty() {
        when(supplierMock.get()).thenReturn(suppliedValue);

        c = new ClickablePropertyColumn2<>(displayModel, property, biConsumerMock, supplierMock);
        c.onClick(clickModel);

        verify(supplierMock).get();
        verify(biConsumerMock).accept(clickModel, suppliedValue);
    }

    @Test
    void gettingProperty() {
        c = new ClickablePropertyColumn2<>(displayModel, property, biConsumerMock, supplierMock);
        assertThat(c.getProperty()).isEqualTo(property);
    }

}
