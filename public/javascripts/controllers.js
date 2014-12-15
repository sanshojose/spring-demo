var userRegModule = angular.module("regUser", []);
userRegModule.controller("customerRegController", function($scope,$http) {
    $scope.confirmStatus="";
    $scope.confirmMsg="";

    $scope.register = function(user) {
        $http({
            method: 'POST',
            url: 'http://127.0.0.1:9000/register',
            headers: {'Content-Type': 'application/json'},
            data: user
        }).success(function(response) {
            $scope.confirmStatus="Success !";
            $scope.confirmMsg="Hello "+response.name+", Your E-mail "+response.email+" is registered";
        }).error(function (response) {
            $scope.confirmStatus="Error !";
            $scope.confirmMsg=response.email;
        });
    }
});