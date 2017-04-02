(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('AuctionItemStatusMySuffixcdDeleteController',AuctionItemStatusMySuffixcdDeleteController);

    AuctionItemStatusMySuffixcdDeleteController.$inject = ['$uibModalInstance', 'entity', 'AuctionItemStatus'];

    function AuctionItemStatusMySuffixcdDeleteController($uibModalInstance, entity, AuctionItemStatus) {
        var vm = this;

        vm.auctionItemStatus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AuctionItemStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
