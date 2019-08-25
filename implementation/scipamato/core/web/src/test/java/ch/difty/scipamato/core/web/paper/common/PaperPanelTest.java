package ch.difty.scipamato.core.web.paper.common;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.junit.jupiter.api.AfterEach;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.core.NewsletterAware;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.CodeBoxAware;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.web.common.PanelTest;

@SuppressWarnings("SpellCheckingInspection")
public abstract class PaperPanelTest<T extends CodeBoxAware & NewsletterAware, P extends PaperPanel<T>>
    extends PanelTest<P> {

    private static final String LOCALE = "en_us";

    private final List<CodeClass> codeClasses   = new ArrayList<>();
    private final List<Code>      codesOfClass1 = new ArrayList<>();
    private final List<Code>      codesOfClass2 = new ArrayList<>();
    private final List<Code>      codesOfClass3 = new ArrayList<>();
    private final List<Code>      codesOfClass4 = new ArrayList<>();
    private final List<Code>      codesOfClass5 = new ArrayList<>();
    private final List<Code>      codesOfClass6 = new ArrayList<>();
    private final List<Code>      codesOfClass7 = new ArrayList<>();
    private final List<Code>      codesOfClass8 = new ArrayList<>();

    // See https://issues.apache.org/jira/browse/WICKET-2790
    protected void applyTestHackWithNestedMultiPartForms() {
        MockHttpServletRequest servletRequest = getTester().getRequest();
        servletRequest.setUseMultiPartContentType(true);
    }

    @Override
    protected final void setUpHook() {
        codeClasses.addAll(
            Arrays.asList(newCC(1), newCC(2), newCC(3), newCC(4), newCC(5), newCC(6), newCC(7), newCC(8)));
        when(codeClassServiceMock.find(LOCALE)).thenReturn(codeClasses);

        int ccId = 0;
        codesOfClass1.addAll(Arrays.asList(newC(++ccId, "F"), newC(ccId, "G"), newC(ccId, "A")));
        codesOfClass2.addAll(Arrays.asList(newC(++ccId, "A"), newC(ccId, "B")));
        codesOfClass3.addAll(Arrays.asList(newC(++ccId, "A"), newC(ccId, "B")));
        codesOfClass4.addAll(Arrays.asList(newC(++ccId, "A"), newC(ccId, "B")));
        codesOfClass5.addAll(Arrays.asList(newC(++ccId, "A"), newC(ccId, "B")));
        codesOfClass6.addAll(Arrays.asList(newC(++ccId, "A"), newC(ccId, "B")));
        codesOfClass7.addAll(Arrays.asList(newC(++ccId, "A"), newC(ccId, "B")));
        codesOfClass8.addAll(Arrays.asList(newC(++ccId, "A"), newC(ccId, "B")));

        when(codeServiceMock.findCodesOfClass(CodeClassId.CC1, LOCALE)).thenReturn(codesOfClass1);
        when(codeServiceMock.findCodesOfClass(CodeClassId.CC2, LOCALE)).thenReturn(codesOfClass2);
        when(codeServiceMock.findCodesOfClass(CodeClassId.CC3, LOCALE)).thenReturn(codesOfClass3);
        when(codeServiceMock.findCodesOfClass(CodeClassId.CC4, LOCALE)).thenReturn(codesOfClass4);
        when(codeServiceMock.findCodesOfClass(CodeClassId.CC5, LOCALE)).thenReturn(codesOfClass5);
        when(codeServiceMock.findCodesOfClass(CodeClassId.CC6, LOCALE)).thenReturn(codesOfClass6);
        when(codeServiceMock.findCodesOfClass(CodeClassId.CC7, LOCALE)).thenReturn(codesOfClass7);
        when(codeServiceMock.findCodesOfClass(CodeClassId.CC8, LOCALE)).thenReturn(codesOfClass8);

        setUpLocalHook();
    }

    protected void setUpLocalHook() {
    }

    @AfterEach
    final void tearDown() {
        verifyNoMoreInteractions(codeClassServiceMock, codeServiceMock);

        tearDownLocalHook();
    }

    protected void tearDownLocalHook() {
    }

    private CodeClass newCC(int id) {
        return new CodeClass(id, "cc" + id, "");
    }

    protected Code newC(int ccId, String c) {
        return new Code(ccId + c, "Code " + ccId + c, "", false, ccId, "cc" + ccId, "", 0);
    }

    @SuppressWarnings("SameParameterValue")
    protected void verifyCodeAndCodeClassCalls(int times) {
        verifyCodeAndCodeClassCalls(times, times);
    }

    protected void verifyCodeAndCodeClassCalls(int times, int timesCC1) {
        verify(codeClassServiceMock).find(LOCALE);
        verify(codeServiceMock, times(timesCC1)).findCodesOfClass(CodeClassId.CC1, LOCALE);
        verify(codeServiceMock, times(times)).findCodesOfClass(CodeClassId.CC2, LOCALE);
        verify(codeServiceMock, times(times)).findCodesOfClass(CodeClassId.CC3, LOCALE);
        verify(codeServiceMock, times(times)).findCodesOfClass(CodeClassId.CC4, LOCALE);
        verify(codeServiceMock, times(times)).findCodesOfClass(CodeClassId.CC5, LOCALE);
        verify(codeServiceMock, times(times)).findCodesOfClass(CodeClassId.CC6, LOCALE);
        verify(codeServiceMock, times(times)).findCodesOfClass(CodeClassId.CC7, LOCALE);
        verify(codeServiceMock, times(times)).findCodesOfClass(CodeClassId.CC8, LOCALE);
    }

    private void assertTextAreaWithLabel(String path, String modelValue, String labelText) {
        assertComponentWithLabel(path, TextArea.class, modelValue, labelText);
    }

    protected void assertTextFieldWithLabel(String path, Object modelValue, String labelText) {
        assertComponentWithLabel(path, TextField.class, modelValue, labelText);
    }

    private void assertMultiselectWithLabel(String path, Code modelValue, String labelText) {
        getTester().assertComponent(path, BootstrapMultiSelect.class);
        getTester().assertModelValue(path, Collections.singletonList(modelValue));
        getTester().assertLabel(path + "Label", labelText);
    }

    private void assertComponentWithLabel(String path, Class<? extends Component> componentClass, Object modelValue,
        String labelText) {
        getTester().assertComponent(path, componentClass);
        getTester().assertModelValue(path, modelValue);
        getTester().assertLabel(path + "Label", labelText);
    }

    protected void assertCommonComponents(String id) {
        String b = id + ":form";
        getTester().assertComponent(b, Form.class);

        assertTextAreaWithLabel(b + ":authors", "a", "Authors");
        assertTextFieldWithLabel(b + ":firstAuthor", "fa", "First Author");
        assertComponentWithLabel(b + ":firstAuthorOverridden", CheckBoxX.class, false, "Override");

        assertTextAreaWithLabel(b + ":title", "t", "Title");
        assertTextFieldWithLabel(b + ":location", "l", "Location");
        assertTextFieldWithLabel(b + ":doi", "doi", "DOI");

        b += ":tabs";
        getTester().assertComponent(b, BootstrapTabbedPanel.class);

        String bb = b + ":panel";
        String bbb = bb + ":tab1Form";
        getTester().assertComponent(bbb, Form.class);
        assertTextAreaWithLabel(bbb + ":goals", "g", "Goals");
        assertTextAreaWithLabel(bbb + ":population", "p", "Population");
        assertTextAreaWithLabel(bbb + ":methods", "m", "Methods");

        assertTextAreaWithLabel(bbb + ":populationPlace", "ppl", "Place/Country (study name)");
        assertTextAreaWithLabel(bbb + ":populationParticipants", "ppa", "Participants");
        assertTextAreaWithLabel(bbb + ":populationDuration", "pd", "Study Duration");
        assertTextAreaWithLabel(bbb + ":exposurePollutant", "ep", "Pollutant");
        assertTextAreaWithLabel(bbb + ":exposureAssessment", "ea", "Exposure Assessment");
        assertTextAreaWithLabel(bbb + ":methodStudyDesign", "msd", "Study Design");
        assertTextAreaWithLabel(bbb + ":methodOutcome", "mo", "Outcome");
        assertTextAreaWithLabel(bbb + ":methodStatistics", "ms", "Statistical Method");
        assertTextAreaWithLabel(bbb + ":methodConfounders", "mc", "Confounders");

        getTester().clickLink("panel:form:tabs:tabs-container:tabs:1:link");

        bbb = bb + ":tab2Form";
        getTester().assertComponent(bbb, Form.class);
        assertTextAreaWithLabel(bbb + ":result", "r", "Results");
        assertTextAreaWithLabel(bbb + ":comment", "c", "Comment");
        assertTextAreaWithLabel(bbb + ":intern", "i", "Internal");
        assertTextAreaWithLabel(bbb + ":resultMeasuredOutcome", "rmo", "Measured Outcome");
        assertTextAreaWithLabel(bbb + ":resultExposureRange", "rer", "Exposure (Range)");
        assertTextAreaWithLabel(bbb + ":resultEffectEstimate", "ree", "Effect Estimate/Results");
        assertTextAreaWithLabel(bbb + ":conclusion", "cc", "Conclusion");

        getTester().clickLink("panel:form:tabs:tabs-container:tabs:2:link");

        bbb = bb + ":tab3Form";
        getTester().assertComponent(bbb, Form.class);
        assertMultiselectWithLabel(bbb + ":codesClass1", newC(1, "F"), "cc1");
        assertTextFieldWithLabel(bbb + ":mainCodeOfCodeclass1", "mcocc1", "Main Exposure Agent");
        assertMultiselectWithLabel(bbb + ":codesClass2", newC(2, "A"), "cc2");
        assertMultiselectWithLabel(bbb + ":codesClass3", newC(3, "A"), "cc3");
        assertMultiselectWithLabel(bbb + ":codesClass4", newC(4, "A"), "cc4");
        assertMultiselectWithLabel(bbb + ":codesClass5", newC(5, "A"), "cc5");
        assertMultiselectWithLabel(bbb + ":codesClass6", newC(6, "A"), "cc6");
        assertMultiselectWithLabel(bbb + ":codesClass7", newC(7, "A"), "cc7");
        assertMultiselectWithLabel(bbb + ":codesClass8", newC(8, "A"), "cc8");

        getTester().clickLink("panel:form:tabs:tabs-container:tabs:3:link");

        bbb = bb + ":tab4Form";
        getTester().assertComponent(bbb, Form.class);
        assertTextAreaWithLabel(bbb + ":populationPlace", "ppl", "Place/Country (study name)");
        assertTextAreaWithLabel(bbb + ":populationParticipants", "ppa", "Participants");
        assertTextAreaWithLabel(bbb + ":populationDuration", "pd", "Study Duration");
        assertTextAreaWithLabel(bbb + ":exposurePollutant", "ep", "Pollutant");
        assertTextAreaWithLabel(bbb + ":exposureAssessment", "ea", "Exposure Assessment");
        assertTextAreaWithLabel(bbb + ":methodStudyDesign", "msd", "Study Design");
        assertTextAreaWithLabel(bbb + ":methodOutcome", "mo", "Outcome");
        assertTextAreaWithLabel(bbb + ":methodStatistics", "ms", "Statistical Method");
        assertTextAreaWithLabel(bbb + ":methodConfounders", "mc", "Confounders");
        assertTextAreaWithLabel(bbb + ":resultMeasuredOutcome", "rmo", "Measured Outcome");
        assertTextAreaWithLabel(bbb + ":resultExposureRange", "rer", "Exposure (Range)");
        assertTextAreaWithLabel(bbb + ":conclusion", "cc", "Conclusion");
        assertTextAreaWithLabel(bbb + ":resultEffectEstimate", "ree", "Effect Estimate/Results");

        getTester().clickLink("panel:form:tabs:tabs-container:tabs:4:link");

        bbb = bb + ":tab5Form";
        assertTextAreaWithLabel(bbb + ":originalAbstract", "oa", "Original Abstract");
        getTester().assertComponent(bbb, Form.class);

        getTester().clickLink("panel:form:tabs:tabs-container:tabs:5:link");
        bbb = bb + ":tab6Form";
        getTester().assertComponent(bbb, Form.class);

        getTester().clickLink("panel:form:tabs:tabs-container:tabs:6:link");

        bbb = bb + ":tab7Form";
        getTester().assertComponent(bbb, Form.class);

        bb = b + ":tabs-container:tabs:";
        getTester().assertLabel(bb + "0:link:title", "Population, Goals, and Methods");
        getTester().assertLabel(bb + "1:link:title", "Results and Comments");
        getTester().assertLabel(bb + "2:link:title", "Codes and new Studies");
        getTester().assertLabel(bb + "3:link:title", "New Field Entry");
        getTester().assertLabel(bb + "4:link:title", "Original Abstract");
        getTester().assertLabel(bb + "5:link:title", "Attachments");
        getTester().assertLabel(bb + "6:link:title", "Newsletter");
    }

}
