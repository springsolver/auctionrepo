(function() {
    'use strict';
    angular
        .module('auctionApp')
        .factory('AuctionItemStatus', AuctionItemStatus);

    AuctionItemStatus.$inject = ['$resource'];

    function AuctionItemStatus ($resource) {
        var resourceUrl =  'api/auction-item-statuses/:id';

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
