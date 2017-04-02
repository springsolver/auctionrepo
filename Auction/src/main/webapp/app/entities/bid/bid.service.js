(function() {
    'use strict';
    angular
        .module('auctionApp')
        .factory('Bid', Bid);

    Bid.$inject = ['$resource'];

    function Bid ($resource) {
        var resourceUrl =  'api/bids/:id';

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
