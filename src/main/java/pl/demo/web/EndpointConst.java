package pl.demo.web;

import pl.demo.core.util.Assert;

/**
 * Created by robertsikora on 29.08.15.
 */
final class EndpointConst {

    private EndpointConst(){
        Assert.noObject();
    }

    final static class ADVERT{
        static final String ADVERT_ENDPOINT = "/adverts";
        static final String ADVERT_NEW = "/new";
        static final String ADVERT_SEARCH = "/search";
        static final String ADVERT_UPDATE_STATUS = "/{id}/status";
        static final String ADVERT_SEND_MAIL = "/{id}/email";
    }

    final static class COMMENT{
        static final String COMMENT_ENDPOINT = "/comments";
        static final String COMMENT_NEW = "/advert/{id}";
        static final String COMMENT_GETALL = "/advert/{id}";
    }

    final static class DASHBOARD{
        static final String DASHBOARD_ENDPOINT = "/dashboard";
    }

    final static class IMAGE{
        static final String IMAGE_ENDPOINT = "/resources";
    }

    final static class USER{
        static final String USER_ENDPOINT = "/users";
        static final String USER_GET_LOGGED = "/logged";
        static final String USER_IS_UNIQUE = "/unique";
        static final String USER_FIND_ADVERTS = "/{userId}/adverts";
        static final String ACCOUNT = "/account/{userId}";
    }

    final static class AUTHENTICATE{
        static final String ENDPOINT = "/authenticate";
    }
}
