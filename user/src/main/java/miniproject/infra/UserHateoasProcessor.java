package miniproject.infra;

import miniproject.domain.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class UserHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<User>> {

    @Override
    public EntityModel<User> process(EntityModel<User> model) {
        String self = model.getRequiredLink("self").getHref();
        User user = model.getContent();
        if (user == null) return model;

        // 임시 정책: 로그인 미구현 단계이므로, 무조건 계정 생성만 허용
        boolean isAuthenticated = false; // 로그인 기능 도입 전이므로 항상 false

        if (isAuthenticated) {
            // 로그인된 상태: 모든 기능 링크 노출
            model.add(Link.of(self + "/subscribe").withRel("subscribe"));
            model.add(Link.of(self + "/cancelsubscription").withRel("cancelsubscription"));
            model.add(Link.of(self + "/writerquest").withRel("writerquest"));
            model.add(Link.of(self + "/chargepoint").withRel("chargepoint"));
        } else {
            // 로그인 전: 계정 생성만 가능 (이미 회원이면 register 노출 필요 없을 수도 있음)
            model.add(Link.of(self + "/register").withRel("register"));
        }

        return model;
    }
}


