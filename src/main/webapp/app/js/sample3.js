/**
 * Created by Robert on 30.12.14.
 */

'use strict';

apartido.controller('LocaleCtrl', function($scope) {

    var map = { en : 0, pl : 1, de : 2, fr : 3, ru : 4 };

    $scope.init = function(uri, lang) {
        $scope.uri = uri;
        $scope.current = lang;
        $scope.ddSelectOptions = [ { text : 'ENG', clazz:'uk-flag'  + (lang === 'en' ? ' disabled' : ''), href : '', value : 'en' }, { text : 'POL', clazz:'pol-flag' + (lang === 'pl' ? ' disabled' : ''), href : '', value : 'pl' },
            { text : 'GER', clazz:'ger-flag' + (lang === 'de' ? ' disabled' : ''), href : '', value : 'de' }, { text : 'FRA', clazz:'fra-flag' + (lang === 'fr' ? ' disabled' : ''), href : '', value : 'fr' },
            { text : 'RUS', clazz:'rus-flag' + (lang === 'ru' ? ' disabled' : ''), href : '', value : 'ru' } ];
        $scope.ddSelectSelected = $scope.ddSelectOptions[map[lang]];
    };

    $scope.select = function(selected) {
        if (selected.value !== $scope.current) {
            var query = window.location.search ? (window.location.search + '&') : '?';
            window.location.replace($scope.uri + query + 'locale=' + selected.value);
        }
    };
}).controller('CurrencyCtrl', function($scope) {
    $scope.init = function(uri, currency) {
        $scope.uri = uri;
        $scope.current = currency;
        $scope.ddSelectSelected = { text : currency };
        $scope.ddSelectOptions = [ { text : 'PLN', href : '', clazz: (currency === 'PLN' ? ' disabled' : '') }, { text : 'USD', href : '', clazz: (currency === 'USD' ? ' disabled' : '') },
            { text : 'EUR', href : '', clazz: (currency === 'EUR' ? ' disabled' : '') }, {text: 'GBP', href: '', clazz: (currency === 'GBP' ? ' disabled' : '')} ];
    };

    $scope.select = function(selected) {
        if (selected.text !== $scope.current) {
            var query = window.location.search ? (window.location.search + '&') : '?';
            window.location.replace($scope.uri + query + 'currencyCode=' + selected.text);
        }
    };
}).controller('CarouselCtrl', function($scope, $timeout, $http, $rootScope, $location) {

    var timeOut = null;

    $scope.regions = {
        position: {
            inView: function(tag) {
                $location.search('section', tag);
            }
        }
    };

    $scope.play = function() {
        timeOut = $timeout(function() {
            $scope.next();
            $scope.play();
        }, 5000);
    };

    $scope.stop = function() {
        $timeout.cancel(timeOut);
    };

    $scope.next = function() {
        var total = $scope.slides.length;
        if (total > 0) {
            $scope.slideIndex = ($scope.slideIndex == total - 1) ? 0 : $scope.slideIndex + 1;
        }
    };

    $scope.prev = function() {
        var total = $scope.slides.length;
        if (total > 0) {
            $scope.slideIndex = ($scope.slideIndex == 0) ? total - 1 : $scope.slideIndex - 1;
        }
    };

    $scope.index = function(index) {
        $scope.stop();
        $scope.slideIndex = index;
        setTimeout($scope.play(), 5000);
    };

    $scope.init = function(url, lang) {
        $scope.url = url;
        $scope.slideIndex = 0;
        $http.get($scope.url + 'spring/api/structure/struct/.json',
            {params: {
                client: 'web',
                joinResources: 'true',
                locale: lang,
                recursive: true,
                status: [8, 9, 10, 11, 12, 13, 14, 15],
                minApartments: 10
            }})
            .then(function(data) {
                $scope.slides = [];
                recursive(data.data);
            });
        $scope.play();
    };

    var recursive = function(data) {
        if (data.entry.length > 0) {
            for(var i = 0; i < data.entry.length; i++) {
                recursive(data.entry[i]);
            }
        } else {
            $scope.slides.push(data);
        }
    };

}).controller('NextWeekendCtrl', function($scope, $http, $location, geolocation) {

    $scope.position =  {
        inView: function(tag) {
            $location.search('section', tag);
        }
    };

    $scope.sort = function(property, type) {
        $scope.typeSort[property] = $scope.typeSort[property] * type;
        var type = $scope.typeSort[property];
        $scope.regions.sort(function(first, second) {
            return first[property] < second[property] ? type : (first[property] > second[property] ? (type * -1) : 0);});
        $scope.pageChange(0 - $scope.page, false);
        $scope.propertySort = property;
    };

    $scope.radius = [ 100, 200, 300 ];
    $scope.maxVisibleRows = 2;

    $scope.init = function(lang, url) {
        $scope.lang = lang;
        $scope.radiusIdx = 0;
        $scope.left = [];
        $scope.right = [];
        $scope.url = url;
        $scope.propertySort = 'distance';
        $scope.typeSort = {
            distance: -1,
            name: 1,
            apartments: -1
        };
        geolocation.getLocation().then(function(data) {
            $scope.coords = { lat : data.coords.latitude, long : data.coords.longitude };
        });

        $scope.search($scope.radius[$scope.radiusIdx]);
    };

    $scope.myClass="normal";

    $scope.pageChange = function(page, concat) {
        $scope.page += page;
        var left = [];
        var right = [];
        for (var i = 0; i < $scope.maxVisibleRows * 2; i++) {
            var idx = i + $scope.page * $scope.maxVisibleRows * 2;
            if (idx < $scope.regions.length) {
                i % 2 === 0 ? left.push($scope.regions[idx]) : right.push($scope.regions[idx]);
            } else {
                break;
            }
        }
        $scope.right = concat ? $scope.right.concat(right) : right;
        $scope.left = concat ? $scope.left.concat(left) : left;

    };

    $scope.search = function(radius) {
        $scope.radiusIdx = $scope.radius.indexOf(radius);
        $scope.page = 0;
        $scope.pages = 0;

        $http.get($scope.url + 'spring/api/struct/region/distance/' + $scope.radius[$scope.radiusIdx] * 1000 + '.json',
            { params : { geometry : $scope.coords ? 'POINT (' + $scope.coords.long + ' ' + $scope.coords.lat + ')' : null, status : [ 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 ], minApartments: 10 }
            }).success(function(data) {
                $scope.regions = data.result;
                $scope.sort($scope.propertySort, 1);
                $scope.pages = Math.ceil($scope.regions.length / ($scope.maxVisibleRows * 2));
                $scope.pageChange(0, false);
            });
    };
}).controller('pickCtrl', function($scope) {
    function DropdownCtrl($scope) {

        $scope.status = {
            isopen: false
        };

        $scope.toggled = function(open) {
        };

        $scope.toggleDropdown = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.status.isopen = !$scope.status.isopen;
        };
    }

}).controller("regionController", function($scope, $http, $upload, $modal) {
    var requestURI = null;
    $scope.init = function(uri, lang, id, type) {
        requestURI = uri;
        $scope.node = null;
        $scope.parentPolygon = null;
        $scope.polygon = null;
        $scope.map = null;
        $scope.levels = [];
        $scope.lang = lang;
        if (id) {
            $scope.structureType = type;
            $http.get(requestURI + '/hierarchy/' + id).success(function(data) {
                $scope.show(data[data.length - 1]);
                $scope.structure(type, data[data.length - 2]);
                $scope.levels = angular.copy(data);
                $scope.levels.pop();
            });
        }
        $http.get(requestURI + '/types').success(function(data) {
            $scope.structureTypes = data;
        });
        $http.get(requestURI + '/providers').success(function(data) {
            $scope.providers = data;
        });
        $http.get(requestURI + '/regions').success(function(data) {
            $scope.regionTypes = data;
        });
        $http.get(requestURI + '/status/browsable').success(function(data) {
            $scope.browsable = data;
        });
    };

    $scope.structure = function(type, parent) {
        var url = requestURI + '/' + type;
        if (parent !== null) {
            url += '?id=' + parent.id;
            $scope.levels.push(parent);
        }
        $http.get(url).success(function(data) {
            $scope.regions = data.sort(function(first,second){return first.region.names[$scope.lang]<second.region.names[$scope.lang]?-1:(first.region.names[$scope.lang]>second.region.names[$scope.lang]?1:0);});
        });
    };

    $scope.levelUp = function() {
        $scope.levels.splice($scope.levels.length - 1, 1);
        var parent = $scope.levels.length > 0 ? $scope.levels.splice($scope.levels.length - 1, 1)[0] : null;
        $scope.structure($scope.structureType, parent);
    };

    $scope.parent = function() {
        if ($scope.levels.length > 0) {
            if ($scope.parentPolygon == null) {
                $http.get(requestURI + '/init/' + $scope.levels[$scope.levels.length - 1].id).success(function(data) {
                    var mapCoords = [];
                    for (var i = 0; i < data.coordinates.length; i++) {
                        mapCoords.push(new google.maps.LatLng(data.coordinates[i].y, data.coordinates[i].x));
                    }
                    mapCoords.push(new google.maps.LatLng(data.coordinates[data.coordinates.length - 1].y, data.coordinates[data.coordinates.length - 1].x));

                    $scope.parentPolygon = new google.maps.Polygon({
                        paths: mapCoords,
                        strokeColor: '#FFA000',
                        strokeOpacity: 0.8,
                        strokeWeight: 2,
                        fillColor: '#FFA000',
                        fillOpacity: 0.35,
                        zIndex: 7
                    });

                    $scope.parentPolygon.setMap($scope.map);
                });
            } else {
                $scope.parentPolygon.setMap(null);
                $scope.parentPolygon = null;
            }
        }
    };

    function prepare(data) {
        if ($scope.parentPolygon !== null) {
            $scope.parentPolygon.setMap(null);
            $scope.parentPolygon = null;
        }
        $scope.node = data;

        $scope.map = new google.maps.Map(document.getElementById('map_canvas'), {zoom: 5, center: new google.maps.LatLng($scope.node.coordinates[0].y, $scope.node.coordinates[0].x),mapTypeId: google.maps.MapTypeId.ROADMAP});

        var mapCoords = [];
        for (var i = 0; i < $scope.node.coordinates.length; i++) {
            mapCoords.push(new google.maps.LatLng($scope.node.coordinates[i].y, $scope.node.coordinates[i].x));
        }
        mapCoords.push(new google.maps.LatLng($scope.node.coordinates[$scope.node.coordinates.length - 1].y, $scope.node.coordinates[$scope.node.coordinates.length - 1].x));
        if ($scope.polygon !== null) {
            $scope.polygon.setMap(null);
        }

        $scope.polygon = new google.maps.Polygon({
            paths: mapCoords,
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35,
            editable: true,
            draggable: true,
            zIndex: 8
        });

        google.maps.event.addListener($scope.polygon, 'click', function(event) {
            path = $scope.polygon.getPath();
            for (i=0; i<path.length; i++){
                if (event.latLng == path.getAt(i)){
                    path.removeAt(i);
                }
            }
        });

        $scope.polygon.setMap($scope.map);
    }

    $scope.show = function(r) {
        $http.get(requestURI + '/init/' + r.id).success(function(data) {
            prepare(data);
            $http.get(requestURI + '/images?regionId=' + r.region.id + '&clean=true').success(function(imgs) {
                $scope.images = [];
                for (var i = 0; i < imgs.length; i++) {
                    $scope.images[i] = imgs[i] + '?rand=' + new Date().getTime();
                }
            });
        });
    };

    $scope.find = function() {
        $http.get(requestURI + '/find/'+ $scope.provider +'/name/'+ $scope.query).success(function(data) {
            $scope.searchResult = data;
        });
    };



    $scope.removeImage = function(handler) {
        $http.delete(requestURI + '/removeImage/?handler=' + handler.split('?')[0]).success(function(data) {
            $scope.images.splice($scope.images.indexOf(handler), 1);
        });
    }

    $scope.create = function(region) {
        if (region === null) {
            var id = $scope.levels.length > 0 ? '?id=' + $scope.levels[$scope.levels.length - 1].id : '';
            $http.get(requestURI + '/create' + id).success(function(data) {
                prepare(data);
            });
        } else {
            prepare(region);
        }
    };

    $scope.save = function() {
        $scope.node.coordinates = [];
        for (var i = 0; i < $scope.polygon.getPath().getLength(); i++) {
            $scope.node.coordinates.push({x: $scope.polygon.getPath().getAt(i).lng(), y: $scope.polygon.getPath().getAt(i).lat()});
        }
        $scope.node.coordinates.push({x: $scope.polygon.getPath().getAt(0).lng(), y: $scope.polygon.getPath().getAt(0).lat()});
        var id = $scope.levels.length > 0 ? '?parent=' + $scope.levels[$scope.levels.length - 1].id : '';
        $http.post(requestURI + '/' + $scope.structureType + '/save' + id, $scope.node).success(function(data) {
        });
    };

    $scope.breadcrumbs = function() {
        var result = '';
        if ($scope.levels.length > 0) {
            for(var i = 0; i < $scope.levels.length; i++) {
                result += $scope.levels[i].region.names[$scope.lang];
                if (i < $scope.levels.length - 1) {
                    result += ' -> ';
                }
            }
        }
        return result;
    };

    $scope.random = function() {
        return new Data().getTime();
    };

    $scope.onFileSelect = function($files, id) {
        for (var i = 0; i < $files.length; i++) {
            var file = $files[i];
            $scope.upload = $upload.upload({
                url: requestURI + '/upload/' + id,
                method: 'POST',
                file: file
            }).success(function(data, status, headers, config) {
                $http.get(requestURI + '/images?regionId=' + id).success(function(imgs) {
                    $scope.images = [];
                    for (var i = 0; i < imgs.length; i++) {
                        $scope.images[i] = imgs[i] + '?rand=' + new Date().getTime();
                    }
                });
            });
        }
    };

    $scope.open = function (item) {
        var modalInstance = $modal.open({
            template: '<div class="modal-body" style="max-height: 510px"><img style="display: block; margin-left: auto; margin-right: auto" ng-src="<c:url value="${servletPath}/resource/500x500/{{item}}"/>" alt=""/></div><div class="modal-footer"><button class="pre-button" ng-click="close()">OK</button></div>',
            controller: ModalInstanceCtrl,
            resolve: {
                item: function () {
                    return item;
                }
            }
        });

        modalInstance.result.then(function (selectedItem) {

        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
        });
    };

    $scope.openConfirm = function(node) {
        var modalConfirmInstance = $modal.open({
            template: '<div class="modal-body" style="max-height: 510px; color: #000;">Jesteś pewny, że chcesz usunąć<strong> {{node.region.names[\'en\']}} </strong> ze struktury <strong> {{structureType}} </strong> razem z jego potomkami?</div><div class="modal-footer"><button class="pre-button" ng-click="submit()">Tak</button> <button class="pre-button" ng-click="cancel()">Nie</button></div>',
            controller: ModalConfirmInstanceCtrl,
            resolve: {
                node: function () {
                    return node;
                },
                structureType: function() {
                    return $scope.structureType;
                },
                regions: function() {
                    return $scope.regions;
                },
                uri: function() {
                    return requestURI;
                },
                lang: function() {
                    return $scope.lang;
                }
            }
        });
    }

}).controller("regionsController", function($scope, $http, $upload, $modal) {
    var requestURI = null;

    $scope.init = function(uri) {
        requestURI = uri;
        $scope.pageNumber = 1;
        $scope.pageCounter = 1;
        $scope.maxResults = 10;
        $scope.status = '0,2,4,6,12,14';
        $scope.hierarchy = [];
        $scope.update(0, 'status');
        $scope.selected = 0;
    };

    $scope.update = function(increment, type) {
        $scope.pageNumber = parseInt($scope.pageNumber) + parseInt(increment);
        if ($scope.pageNumber > $scope.pageCounter) {
            $scope.pageNumber = $scope.pageCounter;
        }
        if (type === 'status' || type === 'maxResults') {
            $scope.pageNumber = 1;
        }
        if ($scope.status) {
            var statuses = $scope.status.split(',');
            var params = '';
            for( var i = 0; i < statuses.length; i++) {
                params += '&status=' + statuses[i];
            }
            $http.get(requestURI + '/regions?firstResult=' + (($scope.pageNumber - 1) * $scope.maxResults) + '&maxResults=' + $scope.maxResults + params).success(function(data) {

                $scope.regions = data.pageList;
                $scope.pageCounter = Math.ceil(data.nrOfElements / $scope.maxResults);

            });
        }
    };

    $scope.edit = function(region) {
        $scope.images = [];
        $scope.selected = region.id;
        $http.get(requestURI + '/images?regionId=' + region.id + '&clean=true').success(function(imgs) {
            for (var j = 0; j < imgs.length; j++) {
                imgs[j] = imgs[j] + '?rand=' + new Date().getTime();
            }
            $scope.images = imgs;
        });
        $scope.hierarchy = [];
        $http.get(requestURI + '/hierarchy/' + region.id).success(function(data) {
            $scope.hierarchy = data;
        });
    };

    $scope.removeImage = function(handler) {
        $http.delete(requestURI + '/removeImage/?handler=' + handler.split('?')[0]).success(function(data) {
            $scope.images.splice($scope.images.indexOf(handler), 1);
        });
    };

    $scope.save = function(id) {
        $http.post(requestURI + '/save/' + id).success(function() {
            $scope.update(0, '');
        });

    };

    $scope.onFileSelect = function($files, id) {
        for (var i = 0; i < $files.length; i++) {
            var file = $files[i];
            $scope.upload = $upload.upload({
                url: requestURI + '/upload/' + id,
                method: 'POST',
                file: file
            }).success(function(data, status, headers, config) {
                $http.get(requestURI + '/images?regionId=' + id).success(function(imgs) {
                    for (var i = 0; i < imgs.length; i++) {
                        imgs[i] = imgs[i] + '?rand=' + new Date().getTime();
                    }
                    $scope.images = imgs;
                });
            });
        }
    };

    $scope.open = function (item) {
        var modalInstance = $modal.open({
            template: '<div class="modal-body" style="max-height: 510px"><img style="display: block; margin-left: auto; margin-right: auto" ng-src="<c:url value="${servletPath}/resource/500x500/{{item}}"/>" alt=""/></div><div class="modal-footer"><button class="pre-button" ng-click="close()">OK</button></div>',
            controller: ModalInstanceCtrl,
            resolve: {
                item: function () {
                    return item;
                }
            }
        });

        modalInstance.result.then(function (selectedItem) {

        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
        });
    };
}).controller('CancellationModalInstanceCtrl', function ($scope, $http, $modalInstance, cancellation) {
    $scope.cancellation = cancellation;
    $scope.close = function() {
        $modalInstance.dismiss('cancel');
    };
}).controller('StatusModalInstanceCtrl', function ($scope, $http, $modalInstance, action, status, currency, command, inputValidateService, actionService) {
    $scope.status = {
        action: action,
        type: status,
        currency: currency,
        command: command,
        post: function(form) {
            inputValidateService.validateForm(form);
            if (form.$valid) {
                actionService.status(angular.extend({}, this.command, {action: {id: this.action.id}})).then(function(response) {
                    if (response.data.response.result === 'SUCCESS') {
                        $modalInstance.close(true);
                    }
                });
            }
        },
        input: {
            isNotValid: function(input) {
                return inputValidateService.isNotValid(input);
            }
        },
        close: function() {
            $modalInstance.dismiss('cancel');
        }
    };
}).controller('NavigationCtrl', function ($scope) {
    $scope.isCollapsed = true;
});

var ModalInstanceCtrl = function ($scope, $modalInstance, item) {

    $scope.item = item;

    $scope.close = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}

var ModalConfirmInstanceCtrl = function ($scope, $modalInstance, $http, node, structureType, regions, uri) {

    $scope.structureType = structureType;
    $scope.node = node;
    var requestURI = uri;

    $scope.submit = function () {
        $http.delete(requestURI + '/'+ structureType +'/delete/' + node.id).success(function(data) {
            regions.splice(regions.indexOf(node), 1);
        });
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.close();
    };
}
