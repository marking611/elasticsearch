require.config({
	baseUrl: BASE_URL+'/mstatic/js',
    paths: {
        "request":"interface"
    }
});
require(["article"]);
define(['request'],function(request){
});