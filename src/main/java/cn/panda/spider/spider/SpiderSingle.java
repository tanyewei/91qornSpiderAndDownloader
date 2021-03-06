package cn.panda.spider.spider;

import cn.panda.downloader.Utils.VideoDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * @author ZhuYunpeng
 * woscaizi@gmail.com
 * 2018/1/30
 */
@Component
public class SpiderSingle implements PageProcessor {


    Logger logger = LoggerFactory.getLogger(getClass());


    @Bean
    SpiderSingle getSpiderSingle(){
        return new SpiderSingle();
    }

    private Site site = Site.me().
            setDomain("91porn.com").
            addCookie("language","cn_CN").
            addCookie("91username","").     //请自己登录91根据实际填写
            addCookie("__cfduid","").   //请自己登录91根据实际填写
            addCookie("CLIPSHARE","").  //请自己登录91根据实际填写
            addCookie("DUID","bc33%").
            addCookie("EMAILVERIFIED","no").
            addCookie("level","7").
            addCookie("user_level","7").
            addCookie("USERNAME","").   //请自己登录91根据实际填写
            setRetryTimes(3).
            setSleepTime(1000).
            setTimeOut(10000);

    @Override
    public Site getSite() {
        return site;
    }


    @Override
    public void process(Page page) {

       String vedioUrl =  page.getHtml().xpath("//div/video/source/@src").toString();
       String name = page.getHtml().xpath("//*[@id=\"viewvideo-title\"]/text()").toString();

//        System.out.println("============================");
//        System.out.println(page.getHtml().toString());
//        System.out.println("============================");

       logger.info("vedioUrl====>"+vedioUrl);

        if(null != vedioUrl){

            logger.info("====================");
            logger.info("spider===================="+vedioUrl);
            logger.info("name===================="+name);
            logger.info("====================");

            try {

                VideoDownloader videoDownloader = new VideoDownloader(vedioUrl,name);
                new Thread(videoDownloader).start();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    public void getVedio(String url, HttpClientDownloader httpClientDownloader){

        Spider.create(getSpiderSingle()).addUrl(url).
                setScheduler(new QueueScheduler()).
                setDownloader(httpClientDownloader).
                thread(1).
                run();

    }




}
