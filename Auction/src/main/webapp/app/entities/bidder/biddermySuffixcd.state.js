(function() {
    'use strict';

    angular
        .module('auctionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('biddermySuffixcd', {
            parent: 'entity',
            url: '/biddermySuffixcd',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'auctionApp.bidder.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bidder/biddersmySuffixcd.html',
                    controller: 'BidderMySuffixcdController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bidder');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('biddermySuffixcd-detail', {
            parent: 'entity',
            url: '/biddermySuffixcd/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'auctionApp.bidder.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bidder/biddermySuffixcd-detail.html',
                    controller: 'BidderMySuffixcdDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bidder');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bidder', function($stateParams, Bidder) {
                    return Bidder.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'biddermySuffixcd',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('biddermySuffixcd-detail.edit', {
            parent: 'biddermySuffixcd-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bidder/biddermySuffixcd-dialog.html',
                    controller: 'BidderMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bidder', function(Bidder) {
                            return Bidder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('biddermySuffixcd.new', {
            parent: 'biddermySuffixcd',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bidder/biddermySuffixcd-dialog.html',
                    controller: 'BidderMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                jmbg: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('biddermySuffixcd', null, { reload: 'biddermySuffixcd' });
                }, function() {
                    $state.go('biddermySuffixcd');
                });
            }]
        })
        .state('biddermySuffixcd.edit', {
            parent: 'biddermySuffixcd',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bidder/biddermySuffixcd-dialog.html',
                    controller: 'BidderMySuffixcdDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bidder', function(Bidder) {
                            return Bidder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('biddermySuffixcd', null, { reload: 'biddermySuffixcd' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('biddermySuffixcd.delete', {
            parent: 'biddermySuffixcd',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bidder/biddermySuffixcd-delete-dialog.html',
                    controller: 'BidderMySuffixcdDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bidder', function(Bidder) {
                            return Bidder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('biddermySuffixcd', null, { reload: 'biddermySuffixcd' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
