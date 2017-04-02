(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('AuctionItemStatusMySuffixcdDetailController', AuctionItemStatusMySuffixcdDetailController);

    AuctionItemStatusMySuffixcdDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AuctionItemStatus'];

    function AuctionItemStatusMySuffixcdDetailController($scope, $rootScope, $stateParams, previousState, entity, AuctionItemStatus) {
        var vm = this;

        vm.auctionItemStatus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('auctionApp:auctionItemStatusUpdate', function(event, result) {
            vm.auctionItemStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
