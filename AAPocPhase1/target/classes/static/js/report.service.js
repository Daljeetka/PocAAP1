(function() {
    'use strict'

    var app = angular.module("app");

    app.factory("ReportDataResource", ReportDataResource);

    ReportDataResource.$inject = ['$http'];

    function ReportDataResource($http) {

        var service = {};

        service.getData = function() {

            return $http({
                method : 'GET',
                url : '/api/customer/data'
            });
        }

        return service;
    }

})();