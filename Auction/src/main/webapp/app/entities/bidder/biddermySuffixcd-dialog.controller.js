(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('BidderMySuffixcdDialogController', BidderMySuffixcdDialogController);

    BidderMySuffixcdDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bidder'];

    function BidderMySuffixcdDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bidder) {
        var vm = this;

        vm.bidder = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bidder.id !== null) {
                Bidder.update(vm.bidder, onSaveSuccess, onSaveError);
            } else {
                Bidder.save(vm.bidder, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('auctionApp:bidderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
