(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('BidMySuffixcdDeleteController',BidMySuffixcdDeleteController);

    BidMySuffixcdDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bid'];

    function BidMySuffixcdDeleteController($uibModalInstance, entity, Bid) {
        var vm = this;

        vm.bid = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bid.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
