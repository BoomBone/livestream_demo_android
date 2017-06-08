package cn.ucai.live.data.restapi;

        import cn.ucai.live.I;
        import okhttp3.MultipartBody;
        import okhttp3.RequestBody;
        import retrofit2.Call;
        import retrofit2.http.GET;
        import retrofit2.http.Multipart;
        import retrofit2.http.POST;
        import retrofit2.http.Part;
        import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/6/7.
 */

public interface LiveService {
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/getAllGifts
    //@请求方式("请求语句")
    //获取全部礼物
    @GET("live/getAllGifts")
    Call<String> getAllGift();

    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/givingGifts?uname=1&anchor=1&giftId=1&giftNum=1
    //赠送礼物
    @GET("live/givingGifts")
    Call<String> givingGifts(@Query("uname") String username,
                             @Query("anchor") String anchor,
                             @Query("giftId") int giftId,
                             @Query("giftNum") int giftNum);

    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/getRechargeStatements?uname=1&pageId=1&pageSize=1
    //分页获取直播流水
    @GET("live/getRechargeStatements")
    Call<String> getRechargeStatements(@Query("uname") String username,
                                       @Query("pageId") int pageId,
                                       @Query("pageSize") int pageSize);

    //根据用户名获取账户余额
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/getBalance?uname=1
    @GET("live/getBalance")
    Call<String> getBalance(@Query("uname") String username);

    //统计主播礼物信息
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/getGiftStatementsByAnchor?anchor=1
    @GET("live/getGiftStatementsByAnchor")
    Call<String> getGiftStatementsByAnchor(@Query("anchor") String anchor);

    //分页获取送礼物流水
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/getGivingGiftStatements?uname=1&pageId=1&pageSize=1
    @GET("live/getGivingGiftStatements")
    Call<String> getGivingGiftStatements(@Query("uname") String username,
                                         @Query("pageId") int pageId,
                                         @Query("pageSize") int pageSize);

    //分页获取收礼物流水
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/getReceivingGiftStatementsServlet?anchor=1&pageId=1&pageSize=1
    @GET("live/getReceivingGiftStatementsServlet")
    Call<String> getReceivingGiftStatementsServlet(@Query("anchor") String anchor,
                                                   @Query("pageId") int pageId,
                                                   @Query("pageSize") int pageSize);

    //账户充值
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/recharge?uname=1&rmb=1
    @GET("live/recharge")
    Call<String> recharge(@Query("uname") String uname,
                          @Query("rmb") int rmb);

    //创建直播室
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/
    // live/createChatRoom?auth=1&name=1&description=1&owner=1&maxusers=300&members=1
    @GET("live/createChatRoom")
    Call<String> createChatRoom(@Query("auth") String auth,
                                @Query("name") String name,
                                @Query("description") String description,
                                @Query("owner") String owner,
                                @Query("maxusers") int maxusers,
                                @Query("members") String members);

    //上传直播室头像图片
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/uploadChatRoomAvatar?chatRoomId=23
    @Multipart
    @POST("live/uploadChatRoomAvatar")
    Call<String> uploadChatRoomAvatar(@Query("chatRoomId") String chatRoomId,
                                      @Part MultipartBody.Part file);

    //查询全部直播室
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/getAllChatRoom
    @GET("live/getAllChatRoom")
    Call<String> getAllChatRoom();

    //获取直播室详情
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/getChatRoomDetail?chatRoomId=1
    @GET("live/recharge")
    Call<String> getChatRoomDetail(@Query("chatRoomId") String chatRoomId);

    //删除直播室
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/deleteChatRoom?auth=1&chatRoomId=1
    @GET("live/deleteChatRoom")
    Call<String> deleteChatRoom(@Query("auth") String auth,
                                @Query("chatRoomId") String chatRoomId);

    //删除直播室指定成员
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/live/deleteChatRoomMember?auth=1&chatRoomId=1&username=1
    @GET("live/deleteChatRoomMember")
    Call<String> deleteChatRoomMember(@Query("auth") String auth,
                                      @Query("chatRoomId") String chatRoomId,
                                      @Query("username") String username);

    //注册
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/
    // register?m_user_name=1&m_user_nick=1&m_user_password=1
    @Multipart
    @POST("register")
    Call<String> register(@Query(I.User.USER_NAME) String username,
                          @Query(I.User.NICK) String usernick,
                          @Query(I.User.PASSWORD) String password,
                          @Part MultipartBody.Part file);


    //取消注册
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/unregister?m_user_name=1
    @GET("unregister")
    Call<String> unregister(@Query("m_user_name") String username);

    //根据用户名查找用户信息
    //http://101.251.196.90:8080/SuperWeChatServerV2.0/findUserByUserName?m_user_name=1
    @GET("findUserByUserName")
    Call<String> findUserByUserName(@Query("m_user_name") String username);
}
