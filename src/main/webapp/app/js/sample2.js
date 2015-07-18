/**
 * Created by Robert on 30.12.14.
 */

apartido.controller("SearchCtrl", function($scope, $http, $filter, $document, dateParser, regionService, dictionaryService) {

    var DAYS_IN_MONTH = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    var currentDate = new Date();
    var firstCalendarDate = null;
    var map = null;
    var buttonSwitch = null;

    $scope.resultCommand = {
        apartments: [],
        clear: function() {
            this.apartments = [];
        },
        add: function(apartment) {
            result.addMarker(apartment.id, apartment.element.location.lat, apartment.element.location.lng, true, function() {
                $document.scrollToElement(angular.element(document.getElementById('results-container-' + apartment.id)), 0, 800);
            });
            this.apartments.push(apartment);
        },
        addAll: function(apartments) {
            for(var i = 0; i < apartments.length; i++) {
                this.add(apartments[i]);
            }
        }
    };

    $scope.alertCommand = {
        text: function(arg) {
            return arg.replace('%s', $scope.nrOfElements);
        },
        visible: false,
        type: null,
        toggle: function() {
            this.visible = !this.visible;
        },
        activate: function(type) {
            this.type = type;
            if (!this.visible) {
                this.toggle();
            }
        },
        isVisible: function() {
            return this.type && this.visible;
        }
    };

    $scope.searchCommand = {
        person: {
            select: function(id) {
                $scope.person = id;
            }
        },
        range: {
            parse: function(code) {
                return {
                    min: code ? parseInt((Number(code.substring(code.indexOf('[') + 1, code.indexOf(','))) / 100 * this.rate).toFixed()) : null,
                    max: code ? parseInt((Number(code.substring(code.indexOf(',') + 1, code.indexOf(']'))) / 100 * this.rate).toFixed()) : null
                };
            },
            code: function(range) {
                return range.min && range.max ? 'Range['+ (range.min * 100 / this.rate).toFixed() + ',' + (range.max * 100 / this.rate).toFixed() + ']' : null;
            },
            rate: null
        },
        slider: {
            visible: true,
            min: 0,
            max: 1,
            fill: function(range) {
                this.min = range.min ? range.min : 0;
                this.max = range.max ? range.max : 1000;
            }
        },
        query: {
            change: function() {
                $scope.priceRange = 1;
                $scope.minPrice = null;
                $scope.maxPrice = null;
            }
        },
        autoComplete: {
            details: null,
            options: { types : 'geocode',  watchEnter: true },
            callSearch: false,
            callback: function() {
                if (this.callSearch && this.details) {
                    $scope.query = this.details.formatted_address;
                    $scope.radius = this.getRadius();
                    if ($scope.loadData) {
                        distanceWidget.set('distance', $scope.radius);
                    }
                    $scope.geometry = 'POINT ('+ this.details.geometry.location.lng() + ' ' + this.details.geometry.location.lat() + ')';
                    $scope.search();
                    this.callSearch = false;
                }
            },
            getRadius: function() {
                for (var i =0; i < this.details.types.length; i++) {
                    var type = this.details.types[i];
                    return type === 'locality' ? 10000 : type.indexOf('administrative_area_level') >= 0 ? 50000 :
                        type === 'country' ? 100000 : type === 'continent' ? 1000000 : 10000;
                }
            },
            getBounds: function(lat, lng, radius) {
                var circ = new google.maps.Circle();
                circ.setRadius(radius);
                circ.setCenter(new google.maps.LatLng(lat, lng));
                return circ.getBounds();
            }
        },
        calendar: {
            inputs: {
                show: function(from, to) {
                    $scope.calendarInputsVisible = from || to;
                }
            }
        },
        location: {
            fill: function(page, prices, guiPrices, query, quantity, sort, from, to, geometry, radius, regionId) {
                var params = {
                    page: $scope.page,
                    prices: $scope.searchCommand.range.code({min: $scope.minPrice, max: $scope.maxPrice}),
                    guiPrices: $scope.searchCommand.range.code({min: $scope.searchCommand.slider.min, max: $scope.searchCommand.slider.max}),
                    query: !$scope.isPolygon() ? $scope.query : null,
                    person: $scope.person,
                    sort: $scope.sortProperty,
                    from: $scope.from ? moment($scope.from).format('DD-MM-YYYY') : null,
                    to: $scope.to ? moment($scope.to).format('DD-MM-YYYY') : null,
                    geometry: !$scope.isPolygon() ? $scope.geometry : null,
                    radius: !$scope.isPolygon() ? $scope.radius : null,
                    regionId: $scope.isPolygon() ? $scope.regionId : null
                };

                var tmpQuery = '';
                for (var key in params) {
                    if (params[key]) {
                        tmpQuery += (tmpQuery != '' ? '&' : '') + key + "=" + params[key];
                    }
                }

                this.query = tmpQuery ? ('?' + tmpQuery) : '';
                window.history.pushState('state', 'apartido', $scope.url + 'spring/index/search/' + this.query);
            },
            query: ''
        }
    };

    $scope.headerCommand = {
        params: {
            query: null,
            from: null,
            to: null,
            quantity: null,
            nrOfElements: -1,
            minPrice: null,
            maxPrice: null
        },
        fill: function(property, value) {
            this.params[property] = angular.copy(value);
        },
        nights: function() {
            return Math.floor((this.params.to - this.params.from) / 86400000);
        }
    };

    $scope.landingCommand = {
        params: {
            region: null,
        },
        isExpand: true,
        toggle: function() {
            this.isExpand = !this.isExpand;
        },
        fill: function(property, value) {
            this.params[property] = angular.copy(value);
        },
    };

    $scope.breadCrumbCommand = {
        'default': '',
        fill: function(text) {
            this['default'] = angular.copy(text);
        }
    };

    $scope.init = function(url, query, from, to, person, page, prices, guiPrices, sortDefinition, geometry, radius, regionGeometry, regionName, regionId, load, iconsCatalogUrl, currencyCode) {

        $scope.headerCommand.fill('query', query);
        $scope.headerCommand.fill('from', from ? moment(from, 'DD-MM-YYYY').toDate() : null);
        $scope.headerCommand.fill('to', to ? moment(to, 'DD-MM-YYYY').toDate() : null);
        $scope.headerCommand.fill('quantity', person);

        $scope.priceRange = prices ? 0 : 1;

        if (regionId && !geometry) {
            regionService.getRegion(regionId).then(function(response) {
                $scope.landingCommand.fill('region', response.data.result);
            });
        }
        $scope.regionId = regionId;
        $scope.sortProperty = sortDefinition ? sortDefinition : 'distance';
        $scope.sortType = 1;
        $scope.initCalendar();
        $scope.breadCrumbCommand.fill(query);
        $scope.query = query;
        $scope.from = from ? moment(from, 'DD-MM-YYYY').toDate() : null;
        uiDate('from');
        $scope.to = to ? moment(to, 'DD-MM-YYYY').toDate() : null;
        uiDate('to');
        $scope.person = person ? person : 1;

        $scope.searchCommand.calendar.inputs.show($scope.from, $scope.to);

        $scope.minFromDate = new Date();
        $scope.minToDate = new Date($scope.minFromDate .getTime() + 1 * 24 * 60 * 60 * 1000);
        $scope.page = page ? Number(page) : 1;
        $scope.nrOfElements = -1;
        $scope.radius = radius ? Number(radius) : 10000;
        $scope.coords = { latitude : 0.0, longitude : 0.0 };
        $scope.url = url;
        $scope.openCalendar = { from : false, to : false };
        $scope.loadData = load;
        if ($scope.loadData) {
            if (prices) {
                dictionaryService.getExchange('EUR', currencyCode).then(function (response) {

                    $scope.searchCommand.range.rate = response.data.result.rate;
                    $scope.searchCommand.slider.fill($scope.searchCommand.range.parse(guiPrices));
                    var minMax = $scope.searchCommand.range.parse(prices);

                    $scope.minPrice = minMax.min;
                    $scope.maxPrice = minMax.max;

                    $scope.headerCommand.fill('minPrice', $scope.minPrice);
                    $scope.headerCommand.fill('maxPrice', $scope.maxPrice);
                    search(geometry, regionGeometry, iconsCatalogUrl, regionName);
                });
            } else {
                search(geometry, regionGeometry, iconsCatalogUrl, regionName);
            }



        }

        ResultWidget.prototype.selected_changed = function() {
            $('.results-container-' + this.get('nodeId') + ' .results-main-box').toggleClass('results-container-hover', this.get('selected'));
            $('.results-container-' + this.get('nodeId') + ' span.arrow-img').toggleClass('ng-hide', !this.get('selected'));
        };
    };

    function search(geometry, regionGeometry, iconsCatalogUrl, regionName) {
        if (geometry) {
            if (geometry.indexOf('POINT') >= 0) {
                var coords = geometry.substring(geometry.indexOf('(') + 1, geometry.indexOf(')')).trim().split(/[ ,]+/);
                $scope.coords = { latitude : coords[1], longitude : coords[0] };
                initMap(iconsCatalogUrl);
            } else {
                var coords = geometry.substring(geometry.lastIndexOf('(') + 1, geometry.indexOf(')')).trim().split(/[ ,]+/);
                for (var i = 0; i < coords.length;) {
                    lng = coords[i++];
                    lat = coords[i++];
                    $scope.map.polygon.path.push(new google.maps.LatLng(lat, lng));
                    $scope.geometry  = geometry;
                    initMap(iconsCatalogUrl, geometry);
                }
            }
            $scope.nrOfElements = -1;
            $scope.load(0);
        } else if (!regionGeometry && $scope.query) {
            var geocoder = new google.maps.Geocoder();
            geocoder.geocode({ 'address' : $scope.query },
                function(results, status) {
                    $scope.coords = { latitude : results[0].geometry.location.lat(), longitude : results[0].geometry.location.lng() };
                    $scope.nrOfElements = -1;
                    $scope.load(0);
                    initMap(iconsCatalogUrl, regionGeometry);
                });
        } else if (regionGeometry) {
            var coords = regionGeometry.substring(regionGeometry.lastIndexOf('(') + 1, regionGeometry.indexOf(')')).trim().split(/[ ,]+/);
            for (var i = 0; i < coords.length;) {
                lng = coords[i++];
                lat = coords[i++];
                $scope.map.polygon.path.push(new google.maps.LatLng(lat, lng));
            }
            $scope.query = regionName;
            $scope.breadCrumbCommand.fill(regionName);
            $scope.geometry = regionGeometry;
            $scope.nrOfElements = -1;
            $scope.load(0);
            initMap(iconsCatalogUrl, regionGeometry);
        } else {
            initMap(iconsCatalogUrl, null);
        }
    }

    $scope.weekDays = function() {
        var days = moment.weekdaysShort();
        var result = [];
        for(var i = 1; i < days.length; i++) {
            result.push(days[i].toUpperCase());
        }
        result.push(days[0].toUpperCase());
        return result;
    };

    $scope.open = function(calendar) {
        $scope.openCalendar[calendar] = true;
    };

    $scope.groupTitle = function(groups, id) {
        var array = groups.split(',');
        return array[id];
    };

    $scope.$watch('[searchCommand.autoComplete.callSearch, searchCommand.autoComplete.details]', function() {
        $scope.searchCommand.autoComplete.callback();
    }, true);

    $scope.hideCalendar = function() {
        $scope.calendarInputsVisible = $scope.from || $scope.to;
        $scope.calendarVisible = false;
    };

    $scope.isPolygon = function() {
        return $scope.geometry && $scope.geometry.indexOf('POLYGON') === 0;
    };

    $scope.search = function() {
        if ($scope.query) {
            if (!$scope.loadData) {
                var params = {
                    query: $scope.query,
                    person: $scope.person,
                    from: $scope.from ? moment($scope.from).format('DD-MM-YYYY') : null,
                    to: $scope.to ? moment($scope.to).format('DD-MM-YYYY') : null,
                    geometry: $scope.geometry,
                    radius: $scope.radius
                };

                var tmpQuery = '';
                for (var key in params) {
                    if (params[key]) {
                        tmpQuery += (tmpQuery != '' ? '&' : '') + key + "=" + params[key];
                    }
                }

                window.location.replace($scope.url + 'spring/index/search/?' + tmpQuery);
            } else {
                $scope.landingCommand.fill('region', null);
                $scope.page = 1;
                $scope.nrOfElements = -1;
                buttonSwitch.setIndex(0);
                $scope.geometry = null;
                $scope.resultCommand.clear();
                $scope.breadCrumbCommand.fill($scope.query);
                if ($scope.searchCommand.autoComplete.details) {
                    $scope.radius = $scope.searchCommand.autoComplete.getRadius();
                    distanceWidget.set('distance', $scope.radius);
                    $scope.coords = { latitude : $scope.searchCommand.autoComplete.details.geometry.location.lat(), longitude : $scope.searchCommand.autoComplete.details.geometry.location.lng() };
                    distanceWidget.set('position', new google.maps.LatLng($scope.coords.latitude, $scope.coords.longitude));
                    map.panTo(new google.maps.LatLng($scope.coords.latitude, $scope.coords.longitude));
                    map.setCenter(new google.maps.LatLng($scope.coords.latitude, $scope.coords.longitude));
                    map.fitBounds($scope.searchCommand.autoComplete.getBounds($scope.coords.latitude, $scope.coords.longitude, $scope.radius));
                    $scope.map.center = angular.copy($scope.coords);
                    $scope.load(0);
                    $scope.searchCommand.autoComplete.details = null;
                } else if ($scope.query) {
                    var geocoder = new google.maps.Geocoder();
                    geocoder.geocode({ 'address' : $scope.query }, function(results, status) {
                        $scope.coords = { latitude : results[0].geometry.location.lat(), longitude : results[0].geometry.location.lng() };
                        $scope.map.center = angular.copy($scope.coords);
                        distanceWidget.set('position', new google.maps.LatLng($scope.coords.latitude, $scope.coords.longitude));
                        map.panTo(new google.maps.LatLng($scope.coords.latitude, $scope.coords.longitude));
                        map.setCenter(new google.maps.LatLng($scope.coords.latitude, $scope.coords.longitude));
                        map.fitBounds($scope.searchCommand.autoComplete.getBounds($scope.coords.latitude, $scope.coords.longitude, $scope.radius));
                        $scope.load(0);
                    });
                }
            }
        } else {
            $scope.invalidate = true;
        }
    };

    $scope.uiNights = function() {
        return Math.floor(($scope.from && $scope.hoverDate && $scope.selectedInput === 'to' && $scope.hoverDate > $scope.from ? ($scope.hoverDate - $scope.from) : $scope.to && $scope.hoverDate && $scope.selectedInput === 'from' && $scope.hoverDate < $scope.to? ($scope.to - $scope.hoverDate) : $scope.to && $scope.from ? ($scope.to - $scope.from) : 0) / 86400000);
    };

    $scope.sortDropdown = {
        sort: function(property, type) {
            $scope.sortProperty = property;
            $scope.sortType = type;
            $scope.page = 0;
            $scope.sortDropdown.isOpen = false;
            $scope.load(0);
        },
        isOpen: false,
        open: function() {
            $scope.sortDropdown.isOpen = true;
        }
    };

    $scope.load = function(step) {
        $scope.page += step;
        $scope.geometry = $scope.geometry ? $scope.geometry : 'POINT ('+ $scope.coords.longitude + ' ' + $scope.coords.latitude + ')';
        var price = $scope.searchCommand.range.code({min: $scope.minPrice, max: $scope.maxPrice});
        var guiPrice = $scope.searchCommand.range.code({min: $scope.searchCommand.slider.min, max: $scope.searchCommand.slider.max});
        $http.get($scope.url + 'spring/api/search.json', {
            params : {
                address : $scope.query,
                geometry : $scope.geometry,
                radius : $scope.radius,
                sortDefinition : $scope.sortProperty + ',1,' + $scope.sortType,
                pageNo : $scope.page,
                nrOfElements : $scope.nrOfElements,
                maxResults : 10,
                mode : 'SEARCH',
                client: 'web',
                from : $scope.from ? moment($scope.from).format('DD-MM-YYYY') : null,
                to : $scope.to ? moment($scope.to).format('DD-MM-YYYY') : null,
                capacity : $scope.person,
                prices : price && price !== guiPrice ? price : null,
                guiPrices : price && guiPrice && price !== guiPrice ? guiPrice : null,
                priceRange : $scope.priceRange
            }
        })
            .success(function(data) {
                result.clear();
                $scope.resultCommand.clear();
                if ($scope.priceRange !== 0) {

                    $scope.searchCommand.slider.fill({min: data.guiPrices.minPrice, max: data.guiPrices.maxPrice});

                    $scope.minPrice = Number(data.guiPrices.minPrice);
                    $scope.maxPrice = Number(data.guiPrices.maxPrice);

                    $scope.searchCommand.range.rate = data.guiPrices.rate;
                }
                $scope.headerCommand.fill('minPrice', $scope.minPrice);
                $scope.headerCommand.fill('maxPrice', $scope.maxPrice);
                var prices = $scope.searchCommand.range.code({min: $scope.minPrice, max: $scope.maxPrice});
                var guiPrices = $scope.searchCommand.range.code({min: $scope.searchCommand.slider.min, max: $scope.searchCommand.slider.max});

                $scope.searchCommand.location.fill($scope.page, prices, guiPrices, $scope.query, $scope.person, $scope.sortProperty, $scope.from ? moment($scope.from).format('DD-MM-YYYY') : null, $scope.to ? moment($scope.to).format('DD-MM-YYYY') : null, $scope.geometry, $scope.radius, $scope.regionId);

                $scope.headerCommand.fill('query', $scope.query);
                $scope.headerCommand.fill('person', $scope.person);
                $scope.headerCommand.fill('from', $scope.from);
                $scope.headerCommand.fill('to', $scope.to);
                $scope.headerCommand.fill('nrOfElements', data.paging.nrOfElements);

                $scope.priceRange = 0;
                $scope.nrOfElements = data.paging.nrOfElements;

                $scope.alertCommand.activate(data.node.length === 0 ? 'NODATA' : data.mode === 'NEAREST' ? 'NEAREST' : null);
                $scope.searchCommand.slider.visible = data.mode !== 'NEAREST';

                $scope.resultCommand.addAll(data.node);
                $document.scrollTop(0, 800);
            });
    };

    $scope.calendarVisibility = function($event) {
        if ($event) {
            $event.stopPropagation();
        }
        $scope.calendarInputsVisible = $scope.from || $scope.to ? true : !$scope.calendarInputsVisible;
        $scope.calendarVisible = !$scope.calendarVisible;
        $scope.hoverDate = null;
    };

    $scope.selectInput = function(input, $event) {
        if ($event) {
            $event.stopPropagation();
        }
        $scope.selectedInput = input;
        $scope.calendarVisible = true;
    };

    $scope.selectedInput = 'from';

    $scope.initCalendar = function() {
        currentDate = moment(currentDate).set('hour', 0).set('minute', 0).set('second', 0).set('millisecond', 0).toDate();
        firstCalendarDate = moment(currentDate).subtract(1, 'months').set('hour', 0).set('minute', 0).set('second', 0).set('millisecond', 0).toDate();
        $scope.monthIncrement();
    };

    $scope.uiChange = function(type) {
        var date = dateParser.parse($scope['ui' + type], 'dd-MM-yyyy');
        $scope.priceRange = 1;
        $scope.minPrice = null;
        $scope.maxPrice = null;
        $scope[type] = date < moment(currentDate).set('date', 1).toDate() ? moment(firstCalendarDate).set('date', 1).toDate() : date;
        if (type === 'from' && $scope.from >= $scope.to) {
            $scope.to = null;
            uiDate('to');
        } else if (type === 'to' && $scope.to <= $scope.from) {
            $scope.from = null;
            uiDate('from');
        }
    };

    $scope.monthDecrement = function($event) {
        $event.stopPropagation();
        firstCalendarDate = moment(firstCalendarDate).subtract(1, 'months').set('date', 1).toDate();
        $scope.months = [{rows: []}, {rows: []}];
        var date = firstCalendarDate;
        var days = initialize(date);
        $scope.months[0].name = moment(firstCalendarDate).format('MMMM');
        var i,j,chunk = 7;
        for (i=0,j=days.length; i<j; i+=chunk) {
            $scope.months[0].rows.push({days: days.slice(i,i+chunk)});
        }

        date = moment(date).add(1, 'months').toDate();
        days = initialize(date);
        $scope.months[1].name = moment(date).format('MMMM');
        for (i=0,j=days.length; i<j; i+=chunk) {
            $scope.months[1].rows.push({days: days.slice(i,i+chunk)});
        }
    };

    $scope.monthIncrement = function($event) {
        if ($event) {
            $event.stopPropagation();
        }
        firstCalendarDate = moment(firstCalendarDate).add(1, 'months').set('date', 1).toDate();
        $scope.months = [{rows: []}, {rows: []}];
        var date = firstCalendarDate;
        var days = initialize(date);
        $scope.months[0].name = moment(firstCalendarDate).format('MMMM');
        var i,j,chunk = 7;
        for (i=0,j=days.length; i<j; i+=chunk) {
            $scope.months[0].rows.push({days: days.slice(i,i+chunk)});
        }

        date = moment(date).add(1, 'months').toDate();
        days = initialize(date);
        $scope.months[1].name = moment(date).format('MMMM');
        for (i=0,j=days.length; i<j; i+=chunk) {
            $scope.months[1].rows.push({days: days.slice(i,i+chunk)});
        }
    };

    $scope.futureMonth = function(month) {
        return moment(currentDate).format('MMMM') !== month.name;
    };

    function getDaysInMonth(year, month) {
        return ((month === 1) && (year % 4 === 0) && ((year % 100 !== 0) || (year % 400 === 0))) ? 29 : DAYS_IN_MONTH[month];
    }

    function initialize(startDate) {
        var days = [];
        var threshold = startDate.getDay() - 1;
        for (var i = 0; i < 35; i++) {
            var date = new Date(i < threshold ? moment(startDate).subtract(threshold - i, 'days').toDate().getTime() : moment(startDate).add(i - threshold, 'days').toDate().getTime());
            days.push({
                date: date,
                number: date.getDate(),
                active: i >= threshold && getDaysInMonth(startDate.getFullYear(), startDate.getMonth()) > i && date.getTime() >= currentDate.getTime() ? true : false
            });
        }
        return days;
    }

    $scope.compareDates = function(date1, date2) {
        return date1 && date2 && date1.getTime() === date2.getTime();
    };

    $scope.dayClass = function(day) {
        return $scope.compareDates(day.date, $scope.from) && day.active ? 'datepickerSelected datepickerSelectedFirst' :  $scope.compareDates(day.date, $scope.to) && day.active?
            'datepickerSelected datepickerSelectedLast' : day.active && ($scope.from && $scope.to && day.date > $scope.from && day.date < $scope.to || $scope.from && $scope.hoverDate && day.date <= $scope.hoverDate && day.date >= $scope.from || $scope.to && $scope.hoverDate && day.date >= $scope.hoverDate && day.date <= $scope.to) ? 'datepickerSelected' : '';
    };

    $scope.mouseOver = function(day) {
        if (day.active) {
            $scope.hoverDate = day.date;
        }
    };

    $scope.resetHover = function($event) {
        $scope.hoverDate =  null;
    };

    var uiDate = function(type) {
        $scope['ui' + type] = $filter('date')($scope[type], 'dd-MM-yyyy');
    };

    $scope.selectDay = function(day, $event) {
        if ($event) {
            $event.stopPropagation();
        }
        if (day.active) {
            $scope.priceRange = 1;
            $scope.minPrice = null;
            $scope.maxPrice = null;
            if ($scope.selectedInput === 'from') {
                if (day.date >= $scope.to) {
                    $scope.to = null;
                    uiDate('to');
                }
            } else {
                if (day.date <= $scope.from) {
                    $scope.from = null;
                    uiDate('from');
                }
            }
            $scope[$scope.selectedInput ? $scope.selectedInput : $scope.from && day.date > $scope.from ? 'to' : 'from'] = day.date;
            uiDate($scope.selectedInput ? $scope.selectedInput : $scope.from && day.date > $scope.from ? 'to' : 'from');
            if (!$scope.to) {
                $scope.selectInput('to');
            } else {
                $scope.calendarVisibility();
                $scope.selectedInput = null;
            }
        }
    };

    $scope.hoverIn = function(id) {
        $scope.hoverApartmentId = id;
        result.select(id, true);
    };

    $scope.hoverOut = function(id){
        $scope.hoverApartmentId = null;
        result.select(id, false);
    };

    function initMap(iconsCatalogUrl, geometry) {

        var mapOptions = {
            zoom: 10,
            center: new google.maps.LatLng($scope.coords.latitude, $scope.coords.longitude)
        };

        map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

        result = new Result(map, {
            activeIcon: iconsCatalogUrl + 'blue-marker.png',
            icon: iconsCatalogUrl + 'red-marker.png'
        });

        distanceWidget = new DistanceWidget(map, {
            title : '',
            sizer_title : '',
            centerIcon: iconsCatalogUrl + 'map-center.png',
            resizeIcon: iconsCatalogUrl + 'map-resize.png',
            distance: $scope.radius
        });

        google.maps.event.addListener(distanceWidget, 'radius_changed', function(data) {
            if (data) {
                $scope.priceRange = 1;
                $scope.radius = data.radius;
                $scope.nrOfElements = -1;
                $scope.page = 1;
                $scope.load(0);
            }
        });

        google.maps.event.addListener(distanceWidget, 'location_changed', function(data) {
            $scope.coords = data;
            $scope.priceRange = 1;
            $scope.nrOfElements = -1;
            $scope.page = 1;
            $scope.geometry = 'POINT ('+ $scope.coords.longitude + ' ' + $scope.coords.latitude + ')';
            var geocoder = new google.maps.Geocoder();
            var latlng = new google.maps.LatLng($scope.coords.latitude, $scope.coords.longitude);
            geocoder.geocode({ 'latLng' : latlng }, function(results, status) {
                $scope.query = results[0].formatted_address;
                $scope.breadCrumbCommand.fill(results[0].formatted_address);
            });
            $scope.load(0);
        });

        polygonWidget = new PolygonWidget(map, {
                fillColor : '#003399',
                fillOpacity : 0.10,
                center_title: '',
                centerIcon: iconsCatalogUrl + 'map-center.png',
                strokeColor: '#003399',
                strokeOpacity: 1,
                strokeWeight: 2,
                map: map
            },
            {
                draggable: true,
                title: '',
                resizeIcon: iconsCatalogUrl + 'map-resize.png'
            });

        google.maps.event.addListener(polygonWidget, 'geography_changed', function(data) {
            $scope.map.polygon.path = data;
            $scope.priceRange = 1;
            $scope.nrOfElements = -1;
            $scope.page = 1;
            $scope.geometry = 'POLYGON ((';
            for (var i = 0; i < $scope.map.polygon.path.length; i++) {
                $scope.geometry += $scope.map.polygon.path[i].lng() + ' ' + $scope.map.polygon.path[i].lat() + (i < $scope.map.polygon.path.length - 1 ? ', ' : '');
            }
            $scope.geometry += '))';
            $scope.load(0);
        });

        buttonSwitch = new SwitchButton([distanceWidget, polygonWidget], ['', ''], '', map);

        if (!geometry) {
            distanceWidget.set('position', new google.maps.LatLng($scope.coords.latitude, $scope.coords.longitude));
            buttonSwitch.setIndex(0);
        } else {
            var bounds = new google.maps.LatLngBounds();
            for (var i = 0; i < $scope.map.polygon.path.length; i++) {
                bounds.extend($scope.map.polygon.path[i]);
            }
            map.setCenter(bounds.getCenter());
            map.panTo(bounds.getCenter());
            polygonWidget.setPath(geometry);
            map.fitBounds(polygonWidget.getBounds());
            buttonSwitch.setIndex(1);
        }
    }

    $scope.map = {
        polygon : { path : [] },
        markers : []
    };
});
