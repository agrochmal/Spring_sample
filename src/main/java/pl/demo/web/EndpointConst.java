package pl.demo.web;

/**
 * Created by robertsikora on 29.08.15.
 */

    final class ADVERT {
        static final String ENDPOINT = "/adverts";
        static final String ADVERT_NEW = "/new";
        static final String ADVERT_SEARCH = "/search";
        static final String ADVERT_UPDATE_STATUS = "/{id}/status";
        static final String ADVERT_SEND_MAIL = "/{id}/email";
    }

    final class COMMENT {
        static final String ENDPOINT = "/comments";
        static final String COMMENT_NEW = "/advert/{id}";
        static final String COMMENT_GETALL = "/advert/{id}";
    }

    final class DASHBOARD {
        static final String ENDPOINT = "/dashboard";
    }

    final class IMAGE {
        static final String ENDPOINT = "/resources";
    }

    final class USER {
        static final String ENDPOINT = "/users";
        static final String USER_FIND_ADVERTS = "/{userId}/adverts";
    }

    final class AUTHENTICATE {
        static final String ENDPOINT = "/authentication";
    }

    final class ACCOUNT {
        static final String ENDPOINT = "/account";
    }

