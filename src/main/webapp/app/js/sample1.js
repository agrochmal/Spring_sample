/**
 * Created by Robert on 30.12.14.
 */

apartido.controller('LoginCtrl', function($scope, $modal, $http, inputValidateService) {

    $scope.init = function(url, profile, open, isSocial) {
        $scope.login = {
            isSocial: isSocial,
            init: function() {
                this.password.isReset = false;
                this.password.isError = false;
                this.password.form.username = null;
                this.password.isForgotten = false;
                this.params.providerUserId = null;
                this.params.accessToken = null;
                this.params._spring_security_remember_me = null;
                if (!this.isSocial) {
                    this.profile.email = null;
                    this.profile.password = null;
                    this.profile.repeat = null;
                    this.profile.cellPhone = null;
                    this.profile.firstName = null;
                    this.profile.lastName = null;
                    this.profile.terms = null;
                    this.response = null;
                }
            },
            input: {
                isNotValid: function(input) {
                    return inputValidateService.isNotValid(input);
                },
                isValid: function(input) {
                    return inputValidateService.isValid(input);
                }
            },
            tab: null,
            url: url,
            providers: null,
            profile: profile,
            password: {
                post: function(form) {
                    inputValidateService.validateForm(form);
                    if (form.$valid) {
                        var me = this;
                        $http.post($scope.login.url + '/api/login/password.json', $scope.login.password.form).success(function(data) {
                            me.isReset = data.response.result === 'SUCCESS';
                            me.isError = data.response.result !== 'SUCCESS';
                        });
                    }
                },
                form: {
                    username: null
                },
                isReset: false,
                isError: false
            },
            register: {
                post: function(form) {
                    inputValidateService.validateForm(form);
                    if (form.$valid) {
                        var post = {
                            username: $scope.login.profile.username,
                            email: $scope.login.profile.email,
                            mode: 3,
                            password: $scope.login.profile.password
                        };
                        if ($scope.login.profile.isLocal) {
                            post.username = $scope.login.profile.email;
                            post.repeat = $scope.login.profile.repeat;
                        } else {
                            post.repeat = $scope.login.profile.password;
                        }
                        post.dynamic = {
                            firstName: $scope.login.profile.firstName,
                            lastName: $scope.login.profile.lastName,
                            cellPhone: $scope.login.profile.cellPhone
                        };
                        $http.post($scope.login.url + '/api/login/signOn.json', post).success(function(data) {
                            data.response.result === 'SUCCESS' ? ($scope.login.profile.isLocal ? location.reload() : location.href = $scope.login.profile.url) : null;
                            $scope.login.register.response = data.response;
                            $scope.login.register.validate(form);
                        });
                    }
                },
                response: null,
                hasErrors: function() {
                    return $scope.login.register.response.result === 'FAIL';
                },
                inputValid: function(input) {
                    return input ? input.$invalid && !input.$pristine && !input.$focused : true;
                },
                change: function(input, form) {
                    form[input].$setValidity('duplicated', true);
                    form[input].$setValidity('invalid', true);
                },
                validate: function(form) {
                    if (this.hasErrors()) {
                        for (var i = 0; i < $scope.login.register.response.errors.length; i++) {
                            var error = $scope.login.register.response.errors[i];
                            var array = error.split('.');
                            var field = array[array.length - 1] === 'username' ? 'email' : array[array.length - 1];
                            form[field].$setValidity(array[array.length - 2], false);
                        }
                    }
                }
            },
            response: null,
            params: {
                providerUserId: null,
                accessToken: null,
                providerId: 'local',
                _spring_security_remember_me: null
            },
            isLogin: function() {
                return this.tab === 'login';
            },
            isRegister: function() {
                return this.tab === 'register';
            },
            changeTab: function() {
                this.tab = this.tab === 'login' ? 'register' : 'login';
            },
            logIn: function(form) {
                inputValidateService.validateForm(form);
                if (form.$valid) {
                    $http.get(this.url + '/api/login/signIn.json', {
                        params: this.params
                    }).success(function(data) {
                        if (data.response.result === 'SUCCESS') {
                            location.reload();
                        } else {
                            $scope.login.response = data.response;
                        }
                    });
                }
            },
            hasErrors: function() {
                return $scope.login.response != null && $scope.login.response.result !== 'SUCCESS';
            }
        };
        if ($scope.login.profile.username) {
            $scope.login.profile.isLocal = false;
            $scope.open('register');
        } else {
            $scope.login.profile.isLocal = true;
            if (open) {
                $scope.open('login');
            }
        }

        $http.get($scope.login.url + '/api/login/provider.json').success(function(data) {
            $scope.login.providers = data.providers;
        });
    };

    $scope.open = function(tab) {
        modal(tab);
    };

    function modal(tab) {
        $scope.login.tab = tab;
        $scope.login.init();
        $modal.open({
            templateUrl: 'login/modal',
            controller: 'LoginModalInstanceCtrl',
            windowClass: 'login-wrapper',
            resolve: {
                login: function() {
                    return $scope.login;
                }
            }
        });
    }

}).controller('LoginModalInstanceCtrl', function($scope, $http, $modalInstance, login) {
    $scope.login = login;

    $scope.close = function() {
        $modalInstance.dismiss('this is result for dismiss');
    };
});
