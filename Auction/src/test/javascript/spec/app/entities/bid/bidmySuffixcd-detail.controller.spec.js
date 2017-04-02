'use strict';

describe('Controller Tests', function() {

    describe('Bid Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBid, MockBidder, MockAuctionItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBid = jasmine.createSpy('MockBid');
            MockBidder = jasmine.createSpy('MockBidder');
            MockAuctionItem = jasmine.createSpy('MockAuctionItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Bid': MockBid,
                'Bidder': MockBidder,
                'AuctionItem': MockAuctionItem
            };
            createController = function() {
                $injector.get('$controller')("BidMySuffixcdDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'auctionApp:bidUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
