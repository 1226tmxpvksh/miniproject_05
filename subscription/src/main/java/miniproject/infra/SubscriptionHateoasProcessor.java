package miniproject.infra;

import miniproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionHateoasProcessor implements RepresentationModelProcessor<EntityModel<Subscription>> {

    @Override
    public EntityModel<Subscription> process(EntityModel<Subscription> model) {
        //self 링크: /subscriptions/{userId}
        String selfHref = model.getRequiredLink("self").getHref();

        //구독 상태(권한) 확인 링크
        model.add(
            Link.of(selfHref + "/check").withRel("check-subscription")
        );

        //구독 등록 링크
        model.add(
            Link.of(selfHref + "/register").withRel("register-subscription")
        );

        //구독 취소 링크
        model.add(
            Link.of(selfHref + "/cancel").withRel("cancel-subscription")
        );

        return model;
    }
}

