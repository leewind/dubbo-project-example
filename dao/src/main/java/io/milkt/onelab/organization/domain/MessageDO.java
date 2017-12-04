package io.milkt.onelab.organization.domain;

import com.sfebiz.common.dao.domain.BaseDO;
import java.sql.Timestamp;

/**
 * Created by leewind on 2017/12/4.
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.12.4
 */
public class MessageDO extends BaseDO {

  private static final long serialVersionUID = -1515425652681377842L;

  /**
   * 标题
   */
  private String title;

  /**
   * 阅读时间
   */
  private Timestamp readTime;

  /**
   * 副标题
   */
  private String subtitle;

  /**
   * 内容
   */
  private String content;

  /**
   * 状态
   */
  private String status;

  /**
   * 系统来源
   */
  private String source;

  /**
   * 目标用户
   */
  private Long target;

  /**
   * 发布时间
   */
  private Timestamp publishTime;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Timestamp getReadTime() {
    return readTime;
  }

  public void setReadTime(Timestamp readTime) {
    this.readTime = readTime;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Long getTarget() {
    return target;
  }

  public void setTarget(Long target) {
    this.target = target;
  }

  public Timestamp getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Timestamp publishTime) {
    this.publishTime = publishTime;
  }
}
