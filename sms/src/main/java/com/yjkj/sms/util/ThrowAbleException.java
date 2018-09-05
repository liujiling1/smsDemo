package com.yjkj.sms.util;


/**
 * 接口异常类
 * 
 */
public class ThrowAbleException extends RuntimeException
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1486475752889928305L;

	/**
     * 构造器
     * 
     * @param vadlidCodeError
     */
    public ThrowAbleException(ResponseStatus vadlidCodeError)
    {
        super(vadlidCodeError.getReasonPhrase());
        this.statusEnumcode = vadlidCodeError;
    }
    
    /**
     * 构造器
     * 
     * @param vadlidCodeError
     */
    public ThrowAbleException(ResponseStatus vadlidCodeError,Exception ex)
    {
        super(vadlidCodeError.getReasonPhrase());
        this.exception = ex;
        this.statusEnumcode = vadlidCodeError;
    }
    
    /**
     * 构造器
     * 
     * @param vadlidCodeError
     */
    public ThrowAbleException(ResponseStatus vadlidCodeError,Object data)
    {
        super(vadlidCodeError.getReasonPhrase());
        this.data = data;
        this.statusEnumcode = vadlidCodeError;
    }
    
    /**
     * 错误码
     */
    protected ResponseStatus statusEnumcode;

    protected Exception exception;
    
    protected Object data;
    
    
    public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	/***
     * 获取异常状态
     * @return
     */
    public ResponseStatus getStatusEnumcode() {
        return this.statusEnumcode;
    }
    
    @Override
    public String toString()
    {
        return "error code is:" + this.statusEnumcode.getStatusCode() + " and error message is:"
                + this.statusEnumcode.getReasonPhrase();
    }

    
}
