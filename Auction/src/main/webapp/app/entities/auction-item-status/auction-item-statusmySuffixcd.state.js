(function() {
    'use strict';

    angular
        .module('auctionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('auction-item-statusmySuffixcd', {
            parent: 'entity',
            url: '/auction-item-statusmySuffixcd',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'auctionApp.auctionItemStatus.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auction-item-status/auction-item-statusesmySuffixcd.html',
                    controller: 'AuctionItemStatusMySuffixcdController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('auctionItemStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('auction-item-statusmySuffixcd-detail', {
            parent: 'entity',
            url: '/auction-item-statusmySuffixcd/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'auctionApp.auctionItemStatus.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auction-item-status/auction-item-statusmySuffixcd-detail.html',
                    controller: 'AuctionItemStatusMySuffixcdDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('auctionItemStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AuctionItemStatus', function($stateParams, AuctionItemStatus) {
                    return AuctionItemStatus.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'auction-item-statusmySuffixcd',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('auction-item-statusmySuffixcd-detail.edit', {
            parent: 'auction-item-statusmySuffixcd-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auction-item-status/auction-item-statusmySuffixcd-dialog.html',
                    controller: 'AuctionItemStatusMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuctionItemStatus', function(AuctionItemStatus) {
                            return AuctionItemStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('auction-item-statusmySuffixcd.new', {
            parent: 'auction-item-statusmySuffixcd',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auction-item-status/auction-item-statusmySuffixcd-dialog.html',
                    controller: 'AuctionItemStatusMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('auction-item-statusmySuffixcd', null, { reload: 'auction-item-statusmySuffixcd' });
                }, function() {
                    $state.go('auction-item-statusmySuffixcd');
                });
            }]
        })
        .state('auction-item-statusmySuffixcd.edit', {
            parent: 'auction-item-statusmySuffixcd',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auction-item-status/auction-item-statusmySuffixcd-dialog.html',
                    controller: 'AuctionItemStatusMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuctionItemStatus', function(AuctionItemStatus) {
                            return AuctionItemStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auction-item-statusmySuffixcd', null, { reload: 'auction-item-statusmySuffixcd' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('auction-item-statusmySuffixcd.delete', {
            parent: 'auction-item-statusmySuffixcd',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auction-item-status/auction-item-statusmySuffixcd-delete-dialog.html',
                    controller: 'AuctionItemStatusMySuffixcdDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AuctionItemStatus', function(AuctionItemStatus) {
                            return AuctionItemStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auction-item-statusmySuffixcd', null, { reload: 'auction-item-statusmySuffixcd' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
