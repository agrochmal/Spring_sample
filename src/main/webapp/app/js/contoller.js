'use strict';
/**
 * All app controllers
 */
angular.module('app.controlles', [])
.controller('MainController', function($scope, DashboardService, SearchService, AdvertService, $location) {

	init();

	function init() {
		$scope.mainInterval = 4000;
		var slides = $scope.slides = [];
		var nr = 1;
		$scope.addSlide = function () {
			slides.push({
				image: 'app/images/' + nr + '.jpeg',
				text: ['deatils 111111111', 'deatils 22222222'][slides.length % 2] + ' ' +
				['.................', '.............', '..............'][slides.length % 2]
			});
			nr++;
		};

		for (var i = 0; i < 2; i++) {
			$scope.addSlide();
		}

		DashboardService.get(function (dashbord) {
			$scope.dashbord = dashbord;

		});
	}
		$scope.searchCommand = {
		keyword: '',
		radius:'',
		autoComplete: {
			details: null,
			options: {country: 'pl', types: '(cities)'},
			getLatitude: function() {
				if(null == this.details)
					return '';
				return this.details.geometry.location.lat();
			},
			getLongitude: function() {
				if(null == this.details)
					return '';
				return this.details.geometry.location.lng();
			}
		},
		getSearchCriteria: function(){
			return {'keyWords' : this.keyword, 'locLatitude': this.autoComplete.getLatitude() ,
				'locLongitude': this.autoComplete.getLongitude(), 'locRadius': this.radius}
		},
		search: function(){
			var searchCriteria = this.getSearchCriteria();
			SearchService.search(searchCriteria, 1, 10)
				.success( function (result) {
					SearchService.setResult(searchCriteria, result);
					$location.path('/search');
				}
			);
		}
	};
})
.controller('LoginController', function($scope, $rootScope, $location, $cookieStore, LoginService, UserService, Alertify) {

	function login(event, username,password) {
		LoginService.authenticate($.param({
			username: username,
			password: password
		}), function (authenticationResult) {
			var authToken = authenticationResult.token;
			$rootScope.authToken = authToken;
			if ($scope.login.rememberMe) {
				$cookieStore.put('authToken', authToken);
			}
			LoginService.getLogged(function (user) {
				Alertify.success('Zalogowano');
				$rootScope.user = user;
				$scope.dismiss(event);
			});
		});
	}

	$scope.login = {
		rememberMe: false,
		login: function($event) {
			if ($scope.loginForm.$valid) {
				login($event, $scope.username, $scope.password);
			}
		},
		register: {
			user: null,
			autologin:false,
			autoComplete: {
				details: null,
				options: {country: 'pl', types: '(cities)'},
				getLatitude: function() {
					if(null == this.details)
						return '';
					return this.details.geometry.location.lat();
				},
				getLongitude: function() {
					if(null == this.details)
						return '';
					return this.details.geometry.location.lng();
				}
			},
			save: function($event) {
				if ($scope.userForm.$valid) {
					$scope.login.register.user.lat = this.autoComplete.getLatitude();
					$scope.login.register.user.lng = this.autoComplete.getLongitude();

					$scope.login.register.user.$save(function($event) {
						Alertify.success('Zarejstrowano');
						$scope.dismiss($event);
						if($scope.login.register.autologin){
							login($event, $scope.login.register.user.username, $scope.login.register.user.password);
						}
					});
				} else {
					scrollTop();
				}
			}
		}
	};

	init();

	function init() {
		$scope.login.register.user = new UserService();
	}
	})
.controller('AdvertCreateController', function($scope, $rootScope, $location, AdvertService, Alertify) {

   $scope.example = true;

	//datepicker
	$scope.open = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.opened = true;
	};

	$scope.saveCommand = {
		advert: null,
		autoComplete: {
			details: null,
			options: {country: 'pl', types: '(cities)'},
			getLatitude: function() {
				if(null == this.details)
					return '';
				return this.details.geometry.location.lat();
			},
			getLongitude: function() {
				if(null == this.details)
					return '';
				return this.details.geometry.location.lng();
			}
		},
		save: function() {
			if ($scope.advertForm.$valid) {
				if ($scope.saveCommand.advert.latitude===0) {
					$scope.saveCommand.advert.latitude = this.autoComplete.getLatitude();
				}
				if ($scope.saveCommand.advert.longitude===0) {
					$scope.saveCommand.advert.longitude = this.autoComplete.getLongitude();
				}
				$scope.saveCommand.advert.$save(function() {
					Alertify.success('Dodano nowe ogloszenie');
					$location.path('/');
				});

			} else {
				scrollTop();
			}
		}
	};

	init();

	function init(){
		$scope.saveCommand.advert = new AdvertService();

		AdvertService.createNew(function(advert){
			$scope.saveCommand.advert = advert;
		});
	}
	})
.controller('AllAdvertView', function($scope, SearchService, AdvertService) {

	init();

	function init(){
		var page = SearchService.getData();
		page.number=1;
		$scope.currentPage=1;
		renderPage(page);
	}
		function renderPage(page) {
		$scope.adverts = page.content;
		$scope.totalElements = page.totalElements;
		$scope.totalPages = page.totalPages;
	}
		$scope.loadPage = function(currentPage, currentPageSize){
		var searchCriteria = SearchService.getSearchCriteria();
		SearchService.search(searchCriteria, currentPage, currentPageSize)
			.success( function (page) {
				renderPage(page);
			});
	};

	$scope.delete=function(id) {
		AdvertService.delete({id: id}, function(res){
		});
	};

})
.controller('UserAccountController', function($scope, $rootScope, user, UserService, AdvertService) {

	$scope.editCommand = {
		user : null,
		isEditable : true,
		enableDisable: function() {
			this.isEditable = !this.isEditable;
		},
		save: function() {
			this.user.roles =[];
			UserService.update({id: this.user.username}, this.user, function(res){
				$scope.editCommand.user = UserService.get({id: $route.current.params.id});
			});
		}
	};

	$scope.advertGrid = {
		model : null,
		edit: function() {
		},
		delete: function(id) {
			AdvertService.delete({id: id}, function(res){
				loadAdverts();
			});
		}
	};

	init();

	function init(){
		//from resolver
		$scope.editCommand.user = user;
		loadAdverts();
	}
		function loadAdverts(){
		UserService.getAdverts({id: $rootScope.user.id}, function(adverts){
			$scope.advertGrid.model = adverts;
		});
	}
	})
.controller('AccountAdvertController', function($scope, AdvertService, $http) {
	$scope.$watch('advert.active', function (newValue, oldValue, scope) {
		if (newValue != oldValue) {
			console.log(newValue);
			$http.post('api/adverts/'+scope.advert.id+'/status',
				$.param({status:newValue}), {
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
					}
				}
			).success(function(data, status, headers, config) {
					console.log('State updated');
			});
		}
	});
})
.controller('AdvertViewController', function($scope, advert){
	$scope.advert = advert;
	advert.$promise.then(
		function(answer) {
			$scope.navigationBarTitle = answer.title;
			$scope.navigationBarPhone= answer.phone;
			$scope.initialized=true;
		},
		function(error) {
			console.log(error);
		}
	);
})
.controller('SendMailController', function($scope, $http){
	$scope.sendCommand = {
		message : {
			sender:'',
			receipt:'',
			title:'',
			content:''
		},
		isSent:false,
		send: function($event) {
			if ($scope.emailForm.$valid) {
				$http.post('api/adverts/1/email', this.message).
					success(function (data, status, headers, config) {
						console.log('E-mail was sent');
						$scope.sendCommand.isSent = true;
						$scope.dismiss($event);
					});
			}
		}
	};

	var advertPrm = $scope.$parent.$parent.advert.$promise;
	advertPrm.then(
		function(answer) {
			$scope.sendCommand.message.receipt = answer.email;
			$scope.sendCommand.message.title = 'Re: '+ answer.title;
		},
		function(error) {
			console.log(error);
		}
	);

})
.controller('CommentController', function($scope, CommentService){

	$scope.commentGrid = {
		model : null,
		edit: function() {},
		delete: function() {}
	};

	init();
	function init() {
		loadComments();
	}

	function loadComments(){
		$scope.advert.$promise.then(
			function(answer) {
				CommentService.getComments({id:answer.id}, function(comments){
					$scope.commentGrid.model = comments;
				});
			},
			function(error) {
				console.log(error);
			}
		);


	}

	$scope.sendCommand = {
		comment : {
			text:'',
			nick:'',
			rate:'1'
		},
		postComment: function() {
			CommentService.postComment({id:$scope.advert.id,}, $scope.sendCommand.comment, function(res){
				console.log('Post comment');
				$scope.sendCommand.clearModel();
				loadComments();
			});
		},
		clearModel:function(){
			$scope.sendCommand.comment.text='';
			$scope.sendCommand.comment.nick='';
			$scope.sendCommand.comment.rate='1';
		}
	};
});


