function customersController($scope,$http) {
    $scope.confirmStatus="";
    $scope.confirmMsg="";
    $scope.register = function() {

        $http({
            method: 'POST',
            url: 'http://127.0.0.1:9000/register',
            data: 'email='+$scope.email,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(response) {
            $scope.confirmStatus="Success !";
            $scope.confirmMsg="Your E-mail "+response.email+" is registered";
        }).error(function (response) {
                $scope.confirmStatus="Error !";
               $scope.confirmMsg=response.email;
        });
    }
}