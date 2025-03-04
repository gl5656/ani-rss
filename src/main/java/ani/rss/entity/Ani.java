package ani.rss.entity;

import cn.hutool.core.lang.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class Ani implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * RSS URL
     */
    private String url;

    /**
     * 备用rss
     */
    private List<String> backRss;

    /**
     * 标题
     */
    private String title;

    /**
     * 剧集偏移
     */
    private Integer offset;

    /**
     * 年度
     */
    private Integer year;

    /**
     * 月
     */
    private Integer month;

    /**
     * 季度
     */
    private Integer season;

    /**
     * 封面
     */
    private String cover;

    /**
     * 字幕组
     */
    private String subgroup;

    /**
     * 匹配
     */
    private List<String> match;

    /**
     * 排除
     */
    private List<String> exclude;

    /**
     * 是否启用全局排除
     */
    private Boolean globalExclude;

    /**
     * 剧场版
     */
    private Boolean ova;

    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 启用
     */
    private Boolean enable;

    /**
     * 当前集数
     */
    private Integer currentEpisodeNumber;

    /**
     * 总集数
     */
    private Integer totalEpisodeNumber;

    private String themoviedbName;

    private String type;

    private String bangumiId;

    private String bgmUrl;

    /**
     * 自定义下载位置
     */
    private Boolean customDownloadPath;

    /**
     * 自定义下载位置
     */
    private String downloadPath;

    public static Ani bulidAni() {
        Ani newAni = new Ani();
        newAni
                .setId(UUID.fastUUID().toString())
                .setBackRss(new ArrayList<>())
                .setOffset(0)
                .setYear(1970)
                .setMonth(1)
                .setEnable(true)
                .setOva(false)
                .setThemoviedbName("")
                .setCustomDownloadPath(false)
                .setDownloadPath("")
                .setGlobalExclude(false)
                .setCurrentEpisodeNumber(0)
                .setTotalEpisodeNumber(0)
                .setMatch(List.of())
                .setExclude(List.of("720"))
                .setBgmUrl("");
        return newAni;
    }
}
