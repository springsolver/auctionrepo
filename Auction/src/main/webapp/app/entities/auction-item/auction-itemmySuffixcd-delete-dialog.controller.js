(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('AuctionItemMySuffixcdDeleteController',AuctionItemMySuffixcdDeleteController);

    AuctionItemMySuffixcdDeleteController.$inject = ['$uibModalInstance', 'entity', 'AuctionItem'];

    function AuctionItemMySuffixcdDeleteController($uibModalInstance, entity, AuctionItem) {
        var vm = this;

        vm.auctionItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AuctionItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
