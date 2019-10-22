package com.cn.xmf.service.music.service;


import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Music;
import com.cn.xmf.service.common.SysCommonService;
import com.cn.xmf.service.music.dao.MusicDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * MusicService(微信音乐)
 * @author rufei.cn
 * service 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 * @version 2019-10-21
 */
@RestController
@RequestMapping(value = "/server/music/")
@SuppressWarnings("all")
public class MusicService  {

    private static Logger logger = LoggerFactory.getLogger(MusicService.class);

	@Autowired
	private MusicDao musicDao;
	@Autowired
	private MusicHelperService  musicHelperService;
	@Autowired
    private SysCommonService sysCommonService; //如果不需要发钉钉消息可以注释了

      /**
	   * getList(获取微信音乐带分页数据-服务)
       * @author rufei.cn
       * @param json
	   * @return
	   */
	   @RequestMapping("getList")
	   public Partion getList(@RequestBody JSONObject json)
	   {
          logger.info("getList(获取微信音乐带分页数据-服务) 开始 json={}", json);
          if(json==null||json.size()<1)
          {
             return null;
          }
          long totalcount =musicHelperService.getTotalCount(json);
          List<Music> list= null;
          if(totalcount>0)
          {
             list= musicDao.getList(json);
          }
          Partion pt = new Partion(json, list, totalcount);
		  logger.info("getList(获取微信音乐带分页数据-服务) 结束 ");
		  return pt;
	   }

	   /**
	   * getMusicList(获取微信音乐 不带分页数据-服务)
       * @author rufei.cn
       * @param music
	   * @return
	   */
	   @RequestMapping("getMusicList")
	   public List<Music>  getMusicList(@RequestBody Music music)
	   {
	       List<Music> list=null;
          logger.info("getMusicList(获取微信音乐 不带分页数据-服务) 开始 parms={}", music);
          if(music==null)
          {
             return list;
          }
	      list=musicDao.getMusicList(music);
	      logger.info("getMusicList(获取微信音乐 不带分页数据-服务) 结束");
	      return list;
	   }


    /**
	 * save (保存微信音乐 数据-服务)
	 * @param music
     * @author rufei.cn
	 * @return
	 */
    @RequestMapping("save")
	public Music save(@RequestBody Music music) throws  Exception
	{
        logger.info("save (保存微信音乐 数据-服务) 开始 parms={}", music);
        Music  ret=musicHelperService.save(music);
	    logger.info("save (保存微信音乐 数据-服务) 结束");
	    return  ret;
	}


	   /**
	   * getMusic(获取微信音乐单条数据-服务)
	   * @author rufei.cn
	   * @param music
	   * @return
	   */
	   @RequestMapping("getMusic")
	   public Music  getMusic(@RequestBody Music music)
	   {
	       Music ret=null;
           logger.info("getMusic(获取微信音乐单条数据-服务) 开始 parms={}", music);
           if(music==null)
           {
             return ret;
           }
	       ret=musicHelperService.getSignleMusic(music);
	       logger.info("getMusic(获取微信音乐单条数据-服务) 结束 ");
	       return ret;
	   }

    /**
	 * delete(逻辑删除微信音乐数据-服务)
	 * @param id
	 * @author rufei.cn
	 * @return
	 */
     @RequestMapping("delete")
	 public boolean delete(Long id)
	 {
	    logger.info("delete(逻辑删除微信音乐数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        Music dt = musicHelperService.getMusicById(id);
        if (dt == null) {
            return isSuccess;
        }
	    musicDao.delete(id);
	    isSuccess = true;
        logger.info("delete(逻辑删除微信音乐数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
	 }
}