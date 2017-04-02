(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('BidMySuffixcdDialogController', BidMySuffixcdDialogController);

    BidMySuffixcdDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bid', 'Bidder', 'AuctionItem'];

    function BidMySuffixcdDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bid, Bidder, AuctionItem) {
        var vm = this;

        vm.bid = entity;
        vm.clear = clear;
        vm.save = save;
        vm.bidders = Bidder.query();
        vm.auctionitems = AuctionItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bid.id !== null) {
                Bid.update(vm.bid, onSaveSuccess, onSaveError);
            } else {
                Bid.save(vm.bid, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('auctionApp:bidUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
