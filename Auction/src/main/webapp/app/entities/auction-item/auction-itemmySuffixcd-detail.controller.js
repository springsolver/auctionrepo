(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('AuctionItemMySuffixcdDetailController', AuctionItemMySuffixcdDetailController);

    AuctionItemMySuffixcdDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AuctionItem', 'Bidder', 'AuctionItemStatus'];

    function AuctionItemMySuffixcdDetailController($scope, $rootScope, $stateParams, previousState, entity, AuctionItem, Bidder, AuctionItemStatus) {
        var vm = this;

        vm.auctionItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('auctionApp:auctionItemUpdate', function(event, result) {
            vm.auctionItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
