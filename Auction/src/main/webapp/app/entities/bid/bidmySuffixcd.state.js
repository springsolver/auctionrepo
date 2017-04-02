(function() {
    'use strict';

    angular
        .module('auctionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bidmySuffixcd', {
            parent: 'entity',
            url: '/bidmySuffixcd',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'auctionApp.bid.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bid/bidsmySuffixcd.html',
                    controller: 'BidMySuffixcdController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bid');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bidmySuffixcd-detail', {
            parent: 'entity',
            url: '/bidmySuffixcd/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'auctionApp.bid.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bid/bidmySuffixcd-detail.html',
                    controller: 'BidMySuffixcdDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bid');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bid', function($stateParams, Bid) {
                    return Bid.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bidmySuffixcd',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bidmySuffixcd-detail.edit', {
            parent: 'bidmySuffixcd-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bid/bidmySuffixcd-dialog.html',
                    controller: 'BidMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bid', function(Bid) {
                            return Bid.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bidmySuffixcd.new', {
            parent: 'bidmySuffixcd',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bid/bidmySuffixcd-dialog.html',
                    controller: 'BidMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                price: null,
                                successful: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bidmySuffixcd', null, { reload: 'bidmySuffixcd' });
                }, function() {
                    $state.go('bidmySuffixcd');
                });
            }]
        })
        .state('bidmySuffixcd.edit', {
            parent: 'bidmySuffixcd',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bid/bidmySuffixcd-dialog.html',
                    controller: 'BidMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bid', function(Bid) {
                            return Bid.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bidmySuffixcd', null, { reload: 'bidmySuffixcd' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bidmySuffixcd.delete', {
            parent: 'bidmySuffixcd',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bid/bidmySuffixcd-delete-dialog.html',
                    controller: 'BidMySuffixcdDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bid', function(Bid) {
                            return Bid.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bidmySuffixcd', null, { reload: 'bidmySuffixcd' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
