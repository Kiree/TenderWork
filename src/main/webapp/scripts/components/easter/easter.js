/**
 * Created by jukka on 23.2.2016.
 */

angular.module('tenderworkApp').controller('EasterController', ['$scope', '$rootScope', '$interval', function($scope, $rootScope, $interval) {
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
    $scope.randomHours = function() {
        return Math.floor(Math.random() * 50);
    };
    var LSDwhite = 10;
    var LSDpurple = 0;
    var LSDpink = 5;
    $scope.LSD = function() {
        if(LSDpurple > 10) {
            $('.well').css('background', 'repeating-radial-gradient(#ff33ff '+LSDpink+'%, #ffffff '+LSDwhite+'%, #aa55ff '+LSDpurple+'%,#ff33ff '+(LSDpink+15)+'%');
        } else if(LSDpurple > 5) {
            $('.well').css('background', 'repeating-radial-gradient(#ffffff '+LSDwhite+'%, #aa55ff '+LSDpurple+'%, #ff33ff '+LSDpink+'%,#ffffff '+(LSDwhite+15)+'%');
        } else {
            $('.well').css('background', 'repeating-radial-gradient(#aa55ff '+LSDpurple+'%, #ff33ff '+LSDpink+'%, #ffffff '+LSDwhite+'%,#aa55ff '+(LSDpurple+15)+'%');
        }
        LSDwhite += 1;
        LSDpurple += 1;
        LSDpink += 1;
        if(LSDwhite > 15) { LSDwhite = 0; }
        if(LSDpink > 15) { LSDpink = 0; }
        if(LSDpurple > 15) { LSDpurple = 0; }
    };
    $scope.enableLSD = function() {
        $interval($scope.LSD, 25);
    }

}]);
