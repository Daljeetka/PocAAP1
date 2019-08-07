var app = angular.module('app', []);

//#######################
//JSA CONTROLLER
//#######################

app.controller('jsaController', function($scope, $http, $location) {
	$scope.listCustomers = [];
	
	
	
	function getAllCustomer(){
		// get URL
		var url = $location.absUrl() + "api/customer/all";
		
		// do getting
		$http.get(url).then(function (response) {
			$scope.getDivAvailable = true;
			$scope.listCustomers = response.data;
			
			// initialize selectedCustomer for Select Boxes Using ng-options
			$scope.selectedCustomer = $scope.listCustomers[0]; 
		}, function error(response) {
			$scope.postResultMessage = "Error Status: " +  response.statusText;
		});
		
	}
	
	$scope.showDetails = function () {
		var id = $scope.selectedCustomer;
		
				var url1 = $location.absUrl() + "api/customer/json?id="+$scope.selectedCustomer;
					// do getting
				  		
					$http.get(url1).then(function (response) {
						$scope.getDivAvailable = true;
						$scope.sqlJson = response.data;
						
								}, function error(response) {
						$scope.postResultMessage = "Error Status: " +  response.statusText;
					});
					var url2 = $location.absUrl() + "api/customer/sql";
					// do getting
				  		
					$http.get(url2).then(function (response) {
						$scope.getDivAvailable = true;
						$scope.sqlQuery = response.data;
					}, function error(response) {
						$scope.postResultMessage = "Error Status: " +  response.statusText;
					}
				
			
		
		);
	};
	
	getAllCustomer();
	
	
});



app.controller('BasicReportController', BasicReportController);

BasicReportController.$inject = [ '$scope', 'ReportDataResource', 'ReportChartService'];

function BasicReportController($scope, ReportDataResource, ReportChartService) {

        ReportDataResource.getData().then(function(response) {
            ReportChartService.populateChart(response.data);
        })


}