(function() {
    'use strict';
    angular
        .module('auctionApp')
        .factory('AuctionItem', AuctionItem);

    AuctionItem.$inject = ['$resource', 'DateUtils'];

    function AuctionItem ($resource, DateUtils) {
        var resourceUrl =  'api/auction-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.closeDate = DateUtils.convertDateTimeFromServer(data.closeDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
