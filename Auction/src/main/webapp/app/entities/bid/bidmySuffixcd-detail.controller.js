(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('BidMySuffixcdDetailController', BidMySuffixcdDetailController);

    BidMySuffixcdDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bid', 'Bidder', 'AuctionItem'];

    function BidMySuffixcdDetailController($scope, $rootScope, $stateParams, previousState, entity, Bid, Bidder, AuctionItem) {
        var vm = this;

        vm.bid = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('auctionApp:bidUpdate', function(event, result) {
            vm.bid = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
