package br.gov.caixa.faleconosco.portal.util.captcha;

import com.octo.captcha.engine.image.ImageCaptchaEngine;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
 
public class CaptchaServiceSingleton {
 
    private static ImageCaptchaService instance;
 
    public static ImageCaptchaService getInstance(){
    	if(instance==null){
    		ImageCaptchaEngine engine = new CustomImageCaptchaEngine();
    		instance = new DefaultManageableImageCaptchaService(new FastHashMapCaptchaStore(),engine,180,100000,7500) ;
    	}
    	
        return instance;
    }
}