package miniproject.infra;

import miniproject.domain.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class UserHateoasProcessor implements RepresentationModelProcessor<EntityModel<User>> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public EntityModel<User> process(EntityModel<User> model) {
        String self = model.getRequiredLink("self").getHref();
        User user = model.getContent();
        if (user == null) return model;

        Long sessionUserId = null;
        if (request.getSession(false) != null) {
            sessionUserId = (Long) request.getSession(false).getAttribute("userId");
        }

        boolean isAuthenticated = (sessionUserId != null);
        boolean isMyPage = isAuthenticated && sessionUserId.equals(user.getUserId());

        if (isAuthenticated && isMyPage) {
            // 로그인된 본인 계정만 모든 기능 허용
            model.add(Link.of(self + "/subscribe").withRel("subscribe"));
            model.add(Link.of(self + "/cancelsubscription").withRel("cancelsubscription"));
            model.add(Link.of(self + "/writerquest").withRel("writerquest"));
            model.add(Link.of(self + "/chargepoint").withRel("chargepoint"));
        } else if (!isAuthenticated) {
            // 비로그인 상태면 회원가입/로그인만 노출
            model.add(Link.of("/users/register").withRel("register"));
            model.add(Link.of("/users/login").withRel("login"));
        }
        
        return model;
    }
}



