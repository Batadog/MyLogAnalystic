import com.qianfeng.etl.util.IpParserUtil;
import com.qianfeng.etl.util.ip.IPSeeker;

import java.util.List;

public class IpTest {
    public static  void main(String[] args){


        List<String> ips =IPSeeker.getInstance().getAllIp();

        for (String ip :ips){
           // System.out.println(ip+"=="+IPSeeker.getInstance().getCountry("18.222.159.4"));
            try {
                System.out.println(ip+"===="+new IpParserUtil().parserIp1("http://ip.taobao.com/service/getIpInfo.php?ip="+ip,"utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


      //  System.out.println(IPSeeker.getInstance().getCountry("18.222.159.4"));
    }


}
