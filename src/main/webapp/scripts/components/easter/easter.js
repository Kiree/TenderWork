/**
 * Created by jukka on 23.2.2016.
 */

angular.module('tenderworkApp').controller('EasterController', ['$scope', '$rootScope', function($scope, $rootScope) {
    var quotes = ['Think different!', 'Work!', 'Null Pointer!', 'Revert!', 'Project Manage!', 'Super Fusion!',
        'but not with Firefox!', 'do juttuja!', 'work in the right order!', '...coffee.', 'cause issues!',
        '...paitsi jos Sebastian - niijoo', 'j', 'koo', '... no nyt mää unohin'];
    var current_index = 0;
    $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
      $scope.newIndex();
    });
    $scope.newIndex = function() {
      current_index =   Math.floor(Math.random() * quotes.length);
    };
    $scope.randomQuote = function() {
        return quotes[current_index];
    };
}]);
