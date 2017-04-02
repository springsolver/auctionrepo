(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('AuctionItemStatusMySuffixcdDialogController', AuctionItemStatusMySuffixcdDialogController);

    AuctionItemStatusMySuffixcdDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuctionItemStatus'];

    function AuctionItemStatusMySuffixcdDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuctionItemStatus) {
        var vm = this;

        vm.auctionItemStatus = entity;
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
            if (vm.auctionItemStatus.id !== null) {
                AuctionItemStatus.update(vm.auctionItemStatus, onSaveSuccess, onSaveError);
            } else {
                AuctionItemStatus.save(vm.auctionItemStatus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('auctionApp:auctionItemStatusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
