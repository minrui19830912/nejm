(function ($) {
   $("#btn_user_login").click(function(){

    $("body,html").animate({scrollTop:0},800);
       $(".cart-list").css("display","block");
         $(".cart-list").css("opacity","1");
         $(".cart-list").addClass("animated");
         $(".cart-list").addClass("fadeInDown");
         $(".cart-list").removeClass("fadeOutUp");
    
  });

   


   //鼠标移动到下拉菜单按钮，打开菜单
   $(".megamenu-fw .dropdown-toggle").mouseover(function(){

       $(this).next("ul").css("display","block");
       $(this).next("ul").css("opacity","1");

       $(this).next("ul").addClass("animated");
       $(this).next("ul").removeClass("fadeOutUp");
       $(this).next("ul").addClass("fadeInDown");
       $(".cart-list").addClass("fadeOutUp");
       
   });
   
   //鼠标移除菜单，关闭
   $(".megamenu-content").mouseleave(function(){ 
       
       $(this).addClass("fadeOutUp");
       $(this).removeClass("fadeInDown");
       $(this).css("display","none");
   });

   $(".btn_menu_close").click(function(){ 
       
       $(".megamenu-content").addClass("fadeOutUp");
       $(".megamenu-content").removeClass("fadeInDown");
       $(".megamenu-content").css("display","none");
   });




  
//判断设备
  if(navigator.userAgent.match(/(iPhone|iPod|iPad|Android|ios)/i)){
    //alert("移动");

     $(".btn_menu_close").show();

     $(".attr-nav .dropdown-toggle").click(function(){
        if($(".cart-list").hasClass("fadeInDown")){
          //  alert("off");
           $(".cart-list").addClass("fadeOutUp"); 
           $(".cart-list").removeClass("fadeInDown");    
           $(".cart-list").css("display","none");   
         }else{
         // alert("on");
          $(".cart-list").css("display","block");
          $(".cart-list").css("opacity","1");
          $(".megamenu-content").addClass("fadeOutUp");
          $(".cart-list").addClass("animated");
          $(".cart-list").addClass("fadeInDown");
          $(".cart-list").removeClass("fadeOutUp");  
         }  
     });




  }else{

    //alert("labtop");
       $(".attr-nav .dropdown-toggle").mouseover(function(){
        
         $(".cart-list").css("display","block");
         $(".cart-list").css("opacity","1");
         $(".megamenu-content").addClass("fadeOutUp");
         $(".cart-list").addClass("animated");
         $(".cart-list").addClass("fadeInDown");
         $(".cart-list").removeClass("fadeOutUp");
       });

       $(".attr-nav .dropdown-toggle").click(function(){
        if($(".cart-list").hasClass("fadeInDown")){
          //  alert("off");
           $(".cart-list").addClass("fadeOutUp"); 
           $(".cart-list").removeClass("fadeInDown");       
         }else{
         // alert("on");
          $(".cart-list").css("display","block");
          $(".cart-list").css("opacity","1");
          $(".megamenu-content").addClass("fadeOutUp");
          $(".cart-list").addClass("animated");
          $(".cart-list").addClass("fadeInDown");
          $(".cart-list").removeClass("fadeOutUp");  
         }  
     });


       //$(".cart-list").mouseleave(function(){
       //   $(".cart-list").addClass("fadeOutUp");
     
       // });

  }


  



   $(".nav > li > a").mouseover(function(){
     if ($(this).hasClass("dropdown-toggle")==false) {
        $(".megamenu-content").addClass("fadeOutUp");
        //$(".megamenu-content").removeClass("fadeInDown");
        
     }

    });

   $(".attr-nav .search").click(function(){
     $(".top-search").slideToggle();
    });

   $(".close-search").click(function(){
     $(".top-search").slideToggle();
    });
   


}(jQuery));