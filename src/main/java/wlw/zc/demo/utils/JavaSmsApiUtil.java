package wlw.zc.demo.utils;
/**
 * Created by bingone on 15/12/16.
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信http接口的java代码调用示例
 * 基于Apache HttpClient 4.3
 *
 * @author songchao
 * @since 2015-04-03
 */
@Slf4j
public class JavaSmsApiUtil {

    //查账户信息的http地址
//    private static String URI_GET_USER_INFO = "https://sms.yunpian.com/v2/user/get.json";

    //智能匹配模版发送接口的http地址
    private static String URI_SEND_SMS = "https://sms.yunpian.com/v2/sms/single_send.json";

    //模板发送接口的http地址
//    private static String URI_TPL_SEND_SMS = "https://sms.yunpian.com/v2/sms/tpl_single_send.json";

    //发送语音验证码接口的http地址
//    private static String URI_SEND_VOICE = "https://voice.yunpian.com/v2/voice/send.json";

    private static String APIKEY="2f4f730fa5b4e9606b552f29e2880931";

    //    private  static String TXET = "【OKNi云算力】您的验证码是{code}，如有疑问请联系客服。";
    private  static String TXET = "【xxx】您的验证码是{code}，如有疑问请联系客服。";
    // 国际号短信模版
    private  static String INTERNATION_TEXT = "【xxx】Your verification code is {code}";

    //编码格式。发送编码格式统一用UTF-8
    private static String ENCODING = "UTF-8";

//    public static void main(String[] args) throws IOException, URISyntaxException {
//
//        //修改为您的apikey.apikey可在官网（http://www.yuanpian.com)登录后获取
//
//        //修改为您要发送的手机号
//        String mobile = URLEncoder.encode("13634115746",ENCODING);
//
//
//        /**************** 使用智能匹配模版接口发短信(推荐) *****************/
//        //设置您要发送的内容(内容必须和某个模板匹配。以下例子匹配的是系统提供的1号模板）
//        String text = "【OKNi云算力】您的验证码是8888，如有疑问请联系客服。";
//        //发短信调用示例
//        System.out.println(JavaSmsApiUtil.sendSms(text, mobile));
//
//    }



    /**
     * 智能匹配模版接口发短信
     *
     * @param code   　短信内容
     * @param mobile 　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String sendSms(String code, String mobile) throws IOException {
        if(mobile.indexOf("+") != 0){
            //应使用国际号
            log.warn("发送短信手机号未使用国际号标准 +国际号手机号 ");
            return null;
        }
        String text = null;
        if(mobile.indexOf("+86") == 0)
            text = TXET.replace("{code}", code);
        else
            text = INTERNATION_TEXT.replace("{code}", code);
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey",APIKEY);
        params.put("text", text);
        params.put("mobile", mobile);
        return post(URI_SEND_SMS, params);
    }

    /**
     * 基于HttpClient 4.3的通用POST方法
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */

    public static String post(String url, Map<String, String> paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }

//    /**
//     * 取账户信息
//     *
//     * @return json格式字符串
//     * @throws IOException
//     */
//
//    public static String getUserInfo(String apikey) throws IOException, URISyntaxException {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("apikey", apikey);
//        return post(URI_GET_USER_INFO, params);
//    }
//    /**
//     * 通过模板发送短信(不推荐)
//     *
//     * @param apikey    apikey
//     * @param tpl_id    　模板id
//     * @param tpl_value 　模板变量值
//     * @param mobile    　接受的手机号
//     * @return json格式字符串
//     * @throws IOException
//     */
//
//    public static String tplSendSms(String apikey, long tpl_id, String tpl_value, String mobile) throws IOException {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("apikey", apikey);
//        params.put("tpl_id", String.valueOf(tpl_id));
//        params.put("tpl_value", tpl_value);
//        params.put("mobile", mobile);
//        return post(URI_TPL_SEND_SMS, params);
//    }
//
//    /**
//     * 通过接口发送语音验证码
//     * @param apikey apikey
//     * @param mobile 接收的手机号
//     * @param code   验证码
//     * @return
//     */
//
//    public static String sendVoice(String apikey, String mobile, String code) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("apikey", apikey);
//        params.put("mobile", mobile);
//        params.put("code", code);
//        return post(URI_SEND_VOICE, params);
//    }


}



