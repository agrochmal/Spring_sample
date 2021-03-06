'use strict';

/**
 *  Angular main file
 */

angular.module('app', ['ngRoute','ngCookies','ngSanitize','app.services','app.controlles',
	'app.directives.validation',
	'app.directives.autocomplete',
	'app.directives.autofocus',
	'app.directives.modal',
	'app.directives.editor',
	'app.directives.autoclick',
	'app.directives.uniqueEmail',
	'app.directives.pagination',
	'app.directives.navigationBar',
	'app.directives.switchCheckbox',
	'app.directives.loginPanel',
	'ngMap',
	'ui.bootstrap',
	'flow',
	'Alertify'])

.config(['$routeProvider', '$locationProvider', '$httpProvider', function($routeProvider, $locationProvider, $httpProvider) {
			
		 $routeProvider.
		   when('/main', {
			  templateUrl: 'app/partials/main.html',
			  controller: 'MainController'
		   }).
		   when('/new', {
			  templateUrl: 'app/partials/advert-create.html',
			  controller: 'AdvertCreateController'
		   }).
		   when('/search', {
			  templateUrl: 'app/partials/all-advert-view.html',
			  controller: 'AllAdvertView'
		   }).
		   when('/users/:id', {
			  templateUrl: 'app/partials/user-account.html',
			  controller: 'UserController',
			  resolve: {
				 user: function (UserService, $route) {
					 return UserService.get({id: $route.current.params.id});
				 }
			  }
		   }).
		   when('/adverts/:id', {
		      templateUrl: 'app/partials/advert-view.html',
		      controller: 'AdvertViewController',
			  resolve: {
				 advert: function (AdvertService, $route) {
					 return AdvertService.get({id: $route.current.params.id});
				 }
			  }
		   });

			/*	$locationProvider.html5Mode(true);
			$locationProvider.hashPrefix('!');*/
			
			/* Register error provider that shows message on failed requests or redirects to login page on
			 * unauthenticated requests */
		    $httpProvider.interceptors.push(['$q', '$rootScope', '$location', '$injector', function ($q, $rootScope, $location, $injector) {
				return {
					'responseError': function(rejection) {
						var status = rejection.status;
						var config = rejection.config;
						var method = config.method;
						var url = config.url;
						var alertify = $injector.get('Alertify');
						//Unauthorized
						if (status == 401) {
							$location.path("/main");
							alertify.error('Brak uwierzytelnienia. Proszę się zalogować.');
						} else if (status == 403) {
							$location.path("/main");
							alertify.error('Dostęp zabroniony.');
						} else {
							alertify.error('Poważny błąd serwera');
							$rootScope.error = method + " on " + url + " failed with status " + status;
						}
					}
				};
			}
		    ]);
		    
		    /* Registers auth token interceptor, auth token is either passed by header or by query parameter
		     * as soon as there is an authenticated user */
		    $httpProvider.interceptors.push(['$q', '$rootScope', function ($q, $rootScope) {
		        return {
		        	'request': function(config) {
		        		var isRestCall = config.url.indexOf('api') == 0;
		        		if (isRestCall && angular.isDefined($rootScope.authToken)) {
		        			var authToken = $rootScope.authToken;
		        			if (exampleAppConfig.useAuthTokenHeader) {
		        				config.headers['X-Auth-Token'] = authToken;
		        			} else {
		        				config.url = config.url + "?token=" + authToken;
		        			}
		        		}
		        		return config || $q.when(config);
		        	}
		        };
		    }]);
	} ]
)
.run(['$rootScope', '$location', '$cookieStore', 'LoginService', function($rootScope, $location, $cookieStore, LoginService) {
	   $rootScope.dateOptions = {
			formatYear: 'yy',
			startingDay: 1
	   };
	   $rootScope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	   $rootScope.format = $rootScope.formats[0];

		/* Reset error when a new view is loaded */
		$rootScope.$on('$viewContentLoaded', function() {
			delete $rootScope.error;
			scrollTop();
		});
		
		$rootScope.hasRole = function(role) {
			if ($rootScope.user === undefined) {
				return false;
			}
			if ($rootScope.user.roles === undefined) {
				return false;
			}

			for (var i = 0; i < $rootScope.user.roles.length; i++) {
				if($rootScope.user.roles[i].authority === role){
					return true;
				}
			}

			return false;
		};
		$rootScope.logout = function() {
			delete $rootScope.user;
			delete $rootScope.authToken;
			$cookieStore.remove('authToken');
			$location.path("/main");
		};
		
		 /* Try getting valid user from cookie or go to login page */
		var originalPath = $location.path();
		$location.path("/main");
		var authToken = $cookieStore.get('authToken');
		if (authToken !== undefined) {
			$rootScope.authToken = authToken;
			LoginService.getLogged(function(user) {
				$rootScope.user = user;
				$location.path(originalPath);
			});
		}
		
		$rootScope.initialized = true;
}]);


