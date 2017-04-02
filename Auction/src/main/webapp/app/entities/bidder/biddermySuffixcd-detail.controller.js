(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('BidderMySuffixcdDetailController', BidderMySuffixcdDetailController);

    BidderMySuffixcdDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bidder'];

    function BidderMySuffixcdDetailController($scope, $rootScope, $stateParams, previousState, entity, Bidder) {
        var vm = this;

        vm.bidder = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('auctionApp:bidderUpdate', function(event, result) {
            vm.bidder = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
