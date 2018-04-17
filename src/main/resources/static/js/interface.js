define(function(){
  var ajaxRequest = function(){

  }
  //移动端列表页  获取栏目资源
  ajaxRequest.prototype.getNavList = function(obj){
      var dtd = $.Deferred();
      $.ajax({
			url:BASE_URL+'/mapi/manuscript/channelList',
			data:obj,
			type:'post',
			dataType:'json',
			success:function(data){
				dtd.resolve(data);
			},
			error : function() {
				dtd.reject('系统异常！');
			}
		});
      return dtd.promise(); // 返回promise对象
  };
  //移动端列表页 获取新闻列表
  ajaxRequest.prototype.getNewsList = function(obj){
      var dtd = $.Deferred();
      $.ajax({
			url:'/elasticsearch/article/list',
			data:obj,
			type:'post',
			dataType:'json',
			success:function(data){
				dtd.resolve(data);
			},
			error : function() {
				dtd.reject('系统异常！');
			}
		});
      return dtd.promise(); // 返回promise对象
  };
  return new ajaxRequest();
});