// JavaScript Document
(function ($) {

	// ------------------------------------------------------------------------------ //
	// Toggle Side Menu
	// ------------------------------------------------------------------------------ //

	$(".side .close-side").on("click", function(e){
			e.preventDefault();
			$(".side").removeClass("on");
			$("body").removeClass("on-side");
	});






	 $(".commentary-title").on("click", function(e){
	     $(".commentary-title").toggleClass("hidden");
   });

	 $(".article-title-b").on("click", function(e){
			$(".article-title-b").toggleClass("hidden");
	 });

    //


		 $("#videoqt").on("click", function(e){
			 var Video1=document.getElementById("videoqt");
			 Video1.play();
     });


	$("#nejm-article-content").find("a[href*='#']").on("click", function(e){
		  e.preventDefault();
			
	 	  $(".side").toggleClass("on");
	 	  //$("body").toggleClass("on-side");
	 });

// Affix plugin
		//-----------------------------------------------
		if ($("#affix").length>0) {
			$(window).load(function() {

				var affixBottom = $(".footer").outerHeight(true) + $(".subfooter").outerHeight(true) + $(".blogpost footer").outerHeight(true),
				affixTop = $("#affix").offset().top;
				
				if ($(".comments").length>0) {
					affixBottom = affixBottom + $(".comments").outerHeight(true);
				}

				if ($(".comments-form").length>0) {
					affixBottom = affixBottom + $(".comments-form").outerHeight(true);
				}

				if ($(".footer-top").length>0) {
					affixBottom = affixBottom + $(".footer-top").outerHeight(true);
				}

				if ($(".header.fixed").length>0) {
					$("#affix").affix({
				        offset: {
				          top: affixTop-150,
				          bottom: affixBottom+100
				        }
				    });
				} else {
					$("#affix").affix({
				        offset: {
				          top: affixTop-35,
				          bottom: affixBottom+100
				        }
				    });
				}

			});
		}
		if ($(".affix-menu").length>0) {
			setTimeout(function () {
				var $sideBar = $('.sidebar')

				$sideBar.affix({
					offset: {
						top: function () {
							var offsetTop      = $sideBar.offset().top
							return (this.top = offsetTop - 65)
						},
						bottom: function () {
							var affixBottom = $(".footer").outerHeight(true) + $(".subfooter").outerHeight(true)
							if ($(".footer-top").length>0) {
								affixBottom = affixBottom + $(".footer-top").outerHeight(true)
							}						
							return (this.bottom = affixBottom+10)
						}
					}
				})
			}, 100)
		}

       //Smooth Scroll
		//-----------------------------------------------
		if ($(".smooth-scroll").length>0) {
			if($(".header.fixed").length>0) {
				$('.smooth-scroll a[href*=#]:not([href=#]), a[href*=#]:not([href=#]).smooth-scroll').click(function() {
					if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
						var target = $(this.hash);
						target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
						if (target.length) {
							$('html,body').animate({
								scrollTop: target.offset().top-10
							}, 1000);
							return false;
						}
					}
				});
			} else {
				$('.smooth-scroll a[href*=#]:not([href=#]), a[href*=#]:not([href=#]).smooth-scroll').click(function() {
					if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
						var target = $(this.hash);
						target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
						if (target.length) {
							$('html,body').animate({
								scrollTop: target.offset().top
							}, 1000);
							return false;
						}
					}
				});
			}
		}

		//Scroll Spy
		//-----------------------------------------------
		if($(".scrollspy").length>0) {
			$("body").addClass("scroll-spy");
			if($(".fixed.header").length>0) {
				$('body').scrollspy({ 
					target: '.scrollspy',
					offset: 85
				});
			} else {
				$('body').scrollspy({ 
					target: '.scrollspy',
					offset: 20
				});
			}
		}

        
 $(document).scroll(function(){  
            var tops=$(document).scrollTop();  
            if(tops<200){  
                $("#scroll_backtop").hide(); 
            }  
            else{  
                $("#scroll_backtop").show();
            }  
}) 
			
		     
 $("#scroll_backtop").click(function() {
          $("body,html").animate({scrollTop:0},800);
    });



      

          
           $(".close_box").click(function(){

           	    hiddenToolsbox();
           });

           

           //按期浏览， mobile 月份toggle
          
          $(".btn_switch").click(function(){
          	    
                $("#monthly_meun_height").toggleClass("monthly_meun_height_off");
                $("#monthly_meun_height").toggleClass("monthly_meun_height_on");
                $("#type_meun_height").toggleClass("type_meun_height_off");
                $("#type_meun_height").toggleClass("type_meun_height_on");
                $(".weekly_mobile_tab_date").slideToggle(300);
                $(".type_mobile_tab_list").slideToggle(300);
                $(".atype_mobile_tab_list").slideToggle(300);
                $(".nejmjw_mobile_tab_date").slideToggle(300);
                $(".btn_switch").toggleClass("btn_switch_on");

          });


           $(".btn_box").click(function(){
                
                $("#article_relation").slideToggle(300);

                $(".btn_box").toggleClass("btn_box_on");

          });

         
         // $("#nav_tools_nav").click(function(){
          //      $(".nav_tools_nav_list_box").toggleClass("nodisplay");
           //     $(".nav_tools_nav_list_box").toggleClass("display");
          //      $("#nav_tools_nav").toggleClass("a_nav_tools_hover");
         // });

          
        // 
        function hiddenToolsbox(){
               $(".a_nav_tools_box ul>li>a").each(function(){
                    $(this).removeClass("a_nav_tools_hover");
                });

                $(".a_nav_tools_box ul>li>div").each(function(){
                    $(this).removeClass("display");
                    $(this).addClass("nodisplay");
                });

        } 

      
        
        
        $(window).resize(function() {
           var winWR=$(window).width();
           var winH =$(window).height();

           if (winW!=winWR){
           	location.reload();
           }

           if (winWR<737) {
                 if (winH<winW){
                    $("#HWnote").show().delay(3000).fadeOut();
                }
           }
         });

}(jQuery));



function pageTitle_specialty(specialty) {
	           //alert(specialty);
                switch (specialty)
                {
                case "1":
                  x="心脑血管疾病";
                  y="Cardiovascular & Cerebrovascular Disease"
                  break;
                case "2":
                  x="肿瘤";
                  y="Oncology"
                  break;
                case "3":
                  x="糖尿病";
                  y="Diabetes"
                  break;
                case "4":
                  x="呼吸系统疾病";
                  y="Pulmonary Disease"
                  break;
                case "5":
                  x="其他";
                  y="Other Medical Specialties"
                  break;
                }
                $(".type_mobile_tab_title h3").html(x);
                var Specialty_title=x+"<span class='font-TNR ft-red ft_title_e'>"+ y +"</span></h3>";
                $(".left-title").html(Specialty_title);          
}

function pageTitle_NEJM(specialty) {
	            //alert(specialty);
                switch (specialty)
                {
                case "1":
                  x="综述类文章";
                  y="Review Article, Clinical Practice, Clinical Therapeutics";
                  break;
                case "2":
                  x="临床病例类文章";
                  y="Clinical Problem Solving, Case Records from the Massachusetts General Hospital";
                  break;
                case "3":
                  x="论著类文章";
                  y="Original Article, Special Article, Special Report, Brief Report";
                  break;
                case "4":
                  x="评论类文章";
                  y="Editorial, Perspective, Medicine and Society, Clinical Implications of Basic Research, Clinical Decisions";
                  break;
                case "5":
                  x="其它";
                  y="Others";
                  break;
                }
                $(".weekly_mobile_tab_title h3").html(x);
                var Specialty_title=x+"<span class='font-TNR ft-red ft_title_e'>"+ y +"</span></h3>";
                $(".left-title").html(Specialty_title);          
}

function pageTitle_JW(specialty) {
	           // alert(specialty);
                switch (specialty)
                {
                case "1":
                  x="对临床实践的启示";
                  y="Informing Practice";
                  break;
                case "2":
                  x="临床指南";
                  y="Clinical Guidelines";
                  break;
                case "3":
                  x="临床实践的改变";
                  y="Practice Changing";
                  break;
                case "4":
                  x="新兴领域";
                  y="On the Horizon";
                  break;
                case "5":
                  x="其它";
                  y="Others";
                  break;
                }
                $(".weekly_mobile_tab_title h3").html(x);
                var Specialty_title=x+"<span class='font-TNR ft-red ft_title_e'>"+ y +"</span></h3>";
                $(".left-title").html(Specialty_title);          
}
function pageTitle_videotype(video_type) {
             // alert(specialty);
                switch (video_type)
                {
                case "1":
                  x="NEJM动画解读";
                  y="Quick Takes";
                  break;
                case "2":
                  x="其它";
                  y="Others";
                  break;
                }
                $(".weekly_mobile_tab_title h3").html(x);
                var video_type=x+"<span class='font-TNR ft-red ft_title_e'>"+ y +"</span></h3>";
                $(".left-title").html(video_type);          
}
function pageTitle_month(year,month) {
	           //alert(month);
                switch (month)
                {
                case "01":
                  x="1月";
                  y="January"
                  break;
                case "02":
                  x="2月";
                  y="February"
                  break;
                case "03":
                  x="3月";
                  y="March"
                  break;
                case "04":
                  x="4月";
                  y="April"
                  break;
                case "05":
                  x="5月";
                  y="May"
                  break;
                case "06":
                  x="6月";
                  y="June"
                  break;
                case "07":
                  x="7月";
                  y="July"
                  break;
                case "08":
                  x="8月";
                  y="August"
                  break;
                case "09":
                  x="9月";
                  y="September"
                  break;
                case "10":
                  x="10月";
                  y="October"
                  break;
                case "11":
                  x="11月";
                  y="November"
                  break;
                case "12":
                  x="12月";
                  y="December"
                  break;
                }
                $(".weekly_mobile_tab_title h3").html(x);
                var Specialty_title=year + x+"<span class='font-TNR ft-red ft_title_e'>"+ y +"</span></h3>";
                $(".left-title").html(Specialty_title);          
}
function pageTitle_userinfo(userinfo) {
              //alert(userinfo);
                switch (userinfo)
                {
                case "1":
                  x="我的收藏";
                  y="读者中心";
                  break;
                case "2":
                  x="阅读记录";
                  y="读者中心";
                  break;
                case "3":
                  x="个人资料设置";
                  y="读者中心";
                  break;
                }
                $(".type_mobile_tab_title h3").html(x);
                var userinfo_type=x+"<span class='font-TNR ft-red ft_title_e'>"+ y +"</span></h3>";
                $(".left-title").html(userinfo_type);          
}




