package pl.demo.web;

/**
 * Created by robertsikora on 29.08.15.
 */
class EndpointConst {

    private EndpointConst(){
        throw new AssertionError("Cannot crate instance of object!");
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
        static final String DASHBOARD_STATISTIC = "/statistic";
    }

    final static class IMAGE{
        static final String IMAGE_ENDPOINT = "/resources";
        static final String IMAGE_UPLOAD = "/upload";
    }

    final static class USER{
        static final String USER_ENDPOINT = "/users";
        static final String USER_GET_LOGGED = "/logged";
        static final String USER_IS_UNIQUE = "/unique";
        static final String USER_AUTHENTICATE = "/authenticate";
        static final String USER_FIND_ADVERTS = "/{userId}/adverts";
    }
}
