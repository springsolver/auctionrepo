(function() {
    'use strict';
    angular
        .module('auctionApp')
        .factory('Bidder', Bidder);

    Bidder.$inject = ['$resource'];

    function Bidder ($resource) {
        var resourceUrl =  'api/bidders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
