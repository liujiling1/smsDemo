package com.yjkj.sms.common;

public interface SmsConstant {
	public static final int SMS_MAX = 5000;
	public static final String REGEX_PHONE = "^((13[0-9])|17[0-9]|(166)|(19[8-9])|(14[5,7,9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
	
	/**云之树短信发送返回值*/
	public enum YzsCode{
		S0(0,"提交成功"),
		S1001(1001,	"短信提交失败，请重新提交或联系管理员"),
		S1002(1002,	"出现未知情况，请联系管理员"),
		S1003(1003,	"超出许可连接数"),
		S1004(1004,	"系统错误"),
		S1005(1005,	"账户未开通闪信功能"),
		S1006(1006,	"该账户未开通API接口功能"),
		S1007(1007,	"用户参数配置有误或用户已被停用"),
		S1008(1008,	"该账户已停用"),
		S1009(1009,	"用户名或者密码错误"),
		S1010(1010,	"闪信不支持超长短信发送，请确认字数保证在70字内"),
		S1011(1011,	"不存在充值用户"),
		S1012(1012,	"充值条数不足"),
		S1013(1013,	"要充值的用户不是子账号"),
		S1014(1014,	"充值失败，请重试"),
		S1015(1015,	"参数错误"),
		S1016(1016,	"Action参数错误"),
		S1017(1017,	"UserName参数错误"),
		S1018(1018,	"Password参数错误"),
		S1019(1019,	"Password参数错误"),
		S1020(1020,	"Message或P2pList参数错误"),
		S1021(1021,	"SmsCou参数错误"),
		S1022(1022,	"DestUser参数错误"),
		S1023(1023,	"Remark参数错误"),
		S1024(1024,	"MobanID参数错误"),
		S1025(1025,	"群发超过最大提交量"),
		S1026(1026,	"点对点超过最大提交量"),
		S1027(1027,	"今天的发送量已大于每天最大限额"),
		S1028(1028,	"每周的发送量已大于每周最大限额*"),
		S1029(1029,	"每月的发送量已大于每月最大限额*"),
		S1030(1030,	"当前发送量大于剩余发送量"),
		S1031(1031,	"非鉴权请求IP，提供账号绑定后再使用"),
		S1032(1032,	"请求IP与请求账号不匹配，请核对请求账号"),
		S1033(1033,	"请求账号与绑定IP不匹配，请核对请求IP"),
		S1034(1034,	"目标号码不正确"),
		S1035(1035,	"短信内容过长")
		;
		private int code;
		private String content;
		YzsCode(int code , String content){
			this.code = code;
			this.content = content;
		}
		public int code() {
			return code;
		}
		public String content() {
			return content;
		}
	}
	
	
	/**
	 * 短信类型
	 */
	public enum SmsType{
		DJZC("1","登记注册"),
		XGMM("2","修改密码"),
		YUNY("3","运营"),
		DIND("4","订单"),
		OTHR("5","其他")
		;
		private String code;
		private String content;
		private SmsType(String code, String content) {
			this.code = code;
			this.content = content;
		}
		public String getCode() {
			return code;
		}
		public String getContent() {
			return content;
		}
	}
	
	public enum Todo{
		ADD("0","增"),
		DEL("1","删"),
		UPD("2","改"),
		QRY("3","查");
		private String code;
		private String dec;
		Todo(String code, String dec){
			this.code = code;
			this.dec = dec;
		}
		public String code() {
			return code;
		}
		public String dec() {
			return dec;
		}
		public boolean eq(String t) {
			return this.name().equalsIgnoreCase(t);
		}
	}
	
	public enum Success{
		SUCCESS(1,"成功"),
		FAIL(0,"失败");
		private int code;
		private String dec;
		Success(int code, String dec){
			this.code = code;
			this.dec = dec;
		}
		public int code() {
			return code;
		}
		public String dec() {
			return dec;
		}
	}

}
