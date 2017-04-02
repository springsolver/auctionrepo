(function() {
    'use strict';

    angular
        .module('auctionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('auction-itemmySuffixcd', {
            parent: 'entity',
            url: '/auction-itemmySuffixcd',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'auctionApp.auctionItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auction-item/auction-itemsmySuffixcd.html',
                    controller: 'AuctionItemMySuffixcdController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('auctionItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('auction-itemmySuffixcd-detail', {
            parent: 'entity',
            url: '/auction-itemmySuffixcd/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'auctionApp.auctionItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auction-item/auction-itemmySuffixcd-detail.html',
                    controller: 'AuctionItemMySuffixcdDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('auctionItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AuctionItem', function($stateParams, AuctionItem) {
                    return AuctionItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'auction-itemmySuffixcd',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('auction-itemmySuffixcd-detail.edit', {
            parent: 'auction-itemmySuffixcd-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auction-item/auction-itemmySuffixcd-dialog.html',
                    controller: 'AuctionItemMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuctionItem', function(AuctionItem) {
                            return AuctionItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('auction-itemmySuffixcd.new', {
            parent: 'auction-itemmySuffixcd',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auction-item/auction-itemmySuffixcd-dialog.html',
                    controller: 'AuctionItemMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                startDate: null,
                                closeDate: null,
                                startPrice: null,
                                actualPrice: null,
                                soldPrice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('auction-itemmySuffixcd', null, { reload: 'auction-itemmySuffixcd' });
                }, function() {
                    $state.go('auction-itemmySuffixcd');
                });
            }]
        })
        .state('auction-itemmySuffixcd.edit', {
            parent: 'auction-itemmySuffixcd',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auction-item/auction-itemmySuffixcd-dialog.html',
                    controller: 'AuctionItemMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuctionItem', function(AuctionItem) {
                            return AuctionItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auction-itemmySuffixcd', null, { reload: 'auction-itemmySuffixcd' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('auction-itemmySuffixcd.delete', {
            parent: 'auction-itemmySuffixcd',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auction-item/auction-itemmySuffixcd-delete-dialog.html',
                    controller: 'AuctionItemMySuffixcdDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AuctionItem', function(AuctionItem) {
                            return AuctionItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auction-itemmySuffixcd', null, { reload: 'auction-itemmySuffixcd' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
