package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class AnnotationCriteriaPresenter implements CriteriaPresenter<AnnotationComponentCriteria> {

    @Nonnull
    private final AnnotationCriteriaView view;

    @Nonnull
    private final AnnotationValueListCriteriaPresenter valuePresenter;

    @Inject
    public AnnotationCriteriaPresenter(@Nonnull AnnotationCriteriaView view, @Nonnull AnnotationValueListCriteriaPresenter valuePresenter) {
        this.view = checkNotNull(view);
        this.valuePresenter = checkNotNull(valuePresenter);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        valuePresenter.start(view.getValueCriteriaViewContainer());
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends AnnotationComponentCriteria> getCriteria() {
        AnnotationPropertyCriteria propertyCriteria = view.getSelectedProperty()
                                                          .map(prop -> (AnnotationPropertyCriteria) IriEqualsCriteria.get(prop))
                                                          .orElse(AnyAnnotationPropertyCriteria.get());
        Optional<? extends AnnotationValueCriteria> valueCriteria = valuePresenter.getCriteria();
        if(!valueCriteria.isPresent()) {
            return Optional.empty();
        }
        AnnotationComponentCriteria annotationComponentCriteria = AnnotationComponentCriteria.get(
                propertyCriteria,
                valueCriteria.get(),
                AnyAnnotationSetCriteria.get(),
                view.getAnnotationPresence()
        );
        return Optional.of(annotationComponentCriteria);
    }
}