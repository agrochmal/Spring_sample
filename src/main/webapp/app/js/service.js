'use strict';
/**
 * Services
 */

angular.module('app.services', ['ngResource'])
.factory('LoginService', function($resource) {
	
	return $resource('api/users/:action', {},
			{
				authenticate: {
					method: 'POST',
					params: {'action' : 'authenticate'},
					headers : {'Content-Type': 'application/x-www-form-urlencoded'}
				},

				getLogged:{
					method: 'GET',
					params: {'action' : 'logged'}
				}
			}
	);
})
.factory('UserService', function($resource) {
	
	return $resource('api/users/:id/:action', {
			id: '@id',
			action: "@action"
		},
		{
			getAdverts: {method:"GET", params: {id:"@id", action:"adverts"}, isArray: true},
			createNew: {method:"GET", params: {action:"new"}, isArray: false},
			update: {method:'PUT', params: {id:"@id"} }
		}
	);
})
.factory('UniqueService', function($http){
		return {
			checkUniqueValue: function(username){
				return $http.get('api/users/unique', {
					params: { username: username}
				});
			}
		}
	}
)
.factory('AdvertService', function($resource) {
	
	return $resource('api/adverts/:action:id', 
			{
		    	id: '@id',
		    	action: "@action"
			},
			{
				delete: {method:"DELETE", params: {id:"@id"}, isArray: false},
				createNew: {method:"GET", params: {action:"new"}, isArray: false},
				get: {method:"GET", params: {id:"@id"}, isArray: false}
			}			
	);
})
.factory('SearchService', function($http){

		var data = [];
		var searchCriteria;

		return {
			search: function(_searchCriteria, _page, _size){
				if(_page>0)
					_page = _page -1;

				return $http.get('api/adverts/search', {
					params: { keyWords: _searchCriteria.keyWords,
						      locLatitude: _searchCriteria.locLatitude,
						      locLongitude:_searchCriteria.locLongitude,
						      locRadius:_searchCriteria.locRadius,

						      page:_page,
						      size:_size
					}
				});
			},

			setResult: function(searchCriteria, data){
				this.searchCriteria=searchCriteria;
				this.data = data;
			},

			getData :function(){
				return this.data;
			},

			getSearchCriteria: function(){
				return this.searchCriteria;
			}
		}
	}
)
.factory('DashbordService', function($resource) {
	return $resource('api/dashbord');
})