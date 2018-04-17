require.config({
	baseUrl: '../js',
    paths: {
        "request":"interface"
    }
});
require(["manushome"]);
define(['request'],function(request){
	var newsParam = {
		pageSize:10,
		pageNum:1,
		keyword:"",
		channel:""
	},
	data = [],
	jsonContent = {
		path:''
	};
	var Home = function(){
		this.init();
		initPage();
	}
	Home.prototype.init = function() {
		$(function(){
			$('#switchPage .home').addClass('mui-active');
			mui.init({
				pullRefresh : {
			    container:"#pullrefresh",//
			    down : {
			      callback :pulldownRefresh //
			    },
			    up : {
			      contentrefresh : "正在加载...",//可选，正在加载状态时，上拉加载控件上显示的标题内容
			      callback :pullupRefresh //
			    }
			  }
			});

			mui('#newsList').on('tap', 'li', function() {
				var type = $(this).attr('data-type');
				var href = $(this).find('a').attr('_href');
				if(type == 'type_article'){
					mui.openWindow({
						id:'article',
						url: href
					});
				}
			});
		});	
	};
	function initPage(){
		$('#newsList').html('');
		initNewsList();
	}
	//从服务器中去读数据
	function getNewsList(){
		request.getNewsList(newsParam).then(function(result){
		  newsParam.totalPage = result.totalPages;
	      genNewsList(result.content);
	    },function(err){
	      console.error(err);
	    });
	}
	function pulldownRefresh(){
		console.log('下拉刷新');
		setTimeout(function() {
			newsParam.pageNum = 1;
			mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed
			getNewsList();
		},1500);
	}
	function pullupRefresh(){
		newsParam.pageNum = newsParam.pageNum + 1;

    getDataFromServer();
		mui('#pullrefresh').pullRefresh().endPullupToRefresh((newsParam.pageNum > newsParam.totalPage));
		console.log('加载更多');
	}
	//初始化新闻 获取数据
	function initNewsList(){
		getDataFromServer();
	}
	//从服务器中去读数据
	function getDataFromServer(){
		//.categoryIds = _channelId;
		request.getNewsList(newsParam).then(function(result){
			newsParam.totalPage = result.totalPages;
			genNewsList(result.content);
	    },function(err){
	      console.error(err);
	    });
	}
	function genNewsList(list){
		var arr = initList(list);
		if (newsParam.pageNum == 1) {
			$('#newsList').html("");
		} 
		$('#newsList').append(arr.join(''));
	}
	function initList(data){
		var arr = [];
		data.map(function(value,index){
			arr[index] = genTxt(value,true);
		});
		return arr;
	}
	function genTxt(data){
    	var str = '<li class="mui-table-view-cell mui-media" data-type="type_article">\n\
						<a class="text list1 clearfix" _href="'+data.url+'">\n\
							<p class="main-title mui-ellipsis-2">\n\
								<span>'+data.title+'</span>\n\
							</p>\n\
							<div class="info clearfix">\n\
								<span class="label-style label-source">'+data.channel+'</span>\n\
								<span class="label-style label-time">'+data.publishTime+'</span>\n\
							</div>\n\
						</a>\n\
					</li>';
        return str;
    }
	return new Home();
});