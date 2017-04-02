(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('BidderMySuffixcdDeleteController',BidderMySuffixcdDeleteController);

    BidderMySuffixcdDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bidder'];

    function BidderMySuffixcdDeleteController($uibModalInstance, entity, Bidder) {
        var vm = this;

        vm.bidder = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bidder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
