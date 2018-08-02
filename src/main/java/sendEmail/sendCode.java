package sendEmail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * Created by hello on 2018/4/25.
 */
public class sendCode {

    public static boolean sendEmail(String email, StringBuffer code) {

        HtmlEmail htmlEmail = new HtmlEmail();
        htmlEmail.setHostName("smtp.163.com");
        htmlEmail.setCharset("utf-8");
        try {
            htmlEmail.addTo(email);
            htmlEmail.setFrom("18792962699@163.com", "验证系统");
            htmlEmail.setAuthentication("18792962699@163.com", "sxuyan920127");
            htmlEmail.setSubject("验证系统");
            htmlEmail.setMsg("验证码是:" + code);
            htmlEmail.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }
    }
}


