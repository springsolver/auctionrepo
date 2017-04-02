(function() {
    'use strict';

    angular
        .module('auctionApp')
        .controller('AuctionItemMySuffixcdDialogController', AuctionItemMySuffixcdDialogController);

    AuctionItemMySuffixcdDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuctionItem', 'Bidder', 'AuctionItemStatus'];

    function AuctionItemMySuffixcdDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuctionItem, Bidder, AuctionItemStatus) {
        var vm = this;

        vm.auctionItem = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.bidders = Bidder.query();
        vm.auctionitemstatuses = AuctionItemStatus.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.auctionItem.id !== null) {
                AuctionItem.update(vm.auctionItem, onSaveSuccess, onSaveError);
            } else {
                AuctionItem.save(vm.auctionItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('auctionApp:auctionItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.closeDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
