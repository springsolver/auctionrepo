'use strict';

describe('Controller Tests', function() {

    describe('AuctionItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAuctionItem, MockBidder, MockAuctionItemStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAuctionItem = jasmine.createSpy('MockAuctionItem');
            MockBidder = jasmine.createSpy('MockBidder');
            MockAuctionItemStatus = jasmine.createSpy('MockAuctionItemStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AuctionItem': MockAuctionItem,
                'Bidder': MockBidder,
                'AuctionItemStatus': MockAuctionItemStatus
            };
            createController = function() {
                $injector.get('$controller')("AuctionItemMySuffixcdDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'auctionApp:auctionItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
