//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.keking.service.cache.impl;

import cn.keking.service.cache.CacheService;
import java.util.List;
import java.util.Map;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

@ConditionalOnExpression("'${cache.type:default}'.equals('redis')")
@Service
public class CacheServiceRedisImpl implements CacheService {
  private final RedissonClient redissonClient;

  public CacheServiceRedisImpl(Config config) {
    this.redissonClient = Redisson.create(config);
  }

  public void initPDFCachePool(Integer capacity) {
  }

  public void initIMGCachePool(Integer capacity) {
  }

  public void initPdfImagesCachePool(Integer capacity) {
  }

  public void initMediaConvertCachePool(Integer capacity) {
  }

  public void putPDFCache(String key, String value) {
    RMapCache<String, String> convertedList = this.redissonClient.getMapCache("converted-preview-pdf-file");
    convertedList.fastPut(key, value);
  }

  public void putImgCache(String key, List<String> value) {
    RMapCache<String, List<String>> convertedList = this.redissonClient.getMapCache("converted-preview-imgs-file");
    convertedList.fastPut(key, value);
  }

  public Map<String, String> getPDFCache() {
    return this.redissonClient.getMapCache("converted-preview-pdf-file");
  }

  public String getPDFCache(String key) {
    RMapCache<String, String> convertedList = this.redissonClient.getMapCache("converted-preview-pdf-file");
    return (String)convertedList.get(key);
  }

  public Map<String, List<String>> getImgCache() {
    return this.redissonClient.getMapCache("converted-preview-imgs-file");
  }

  public List<String> getImgCache(String key) {
    RMapCache<String, List<String>> convertedList = this.redissonClient.getMapCache("converted-preview-imgs-file");
    return (List)convertedList.get(key);
  }

  public Integer getPdfImageCache(String key) {
    RMapCache<String, Integer> convertedList = this.redissonClient.getMapCache("converted-preview-pdfimgs-file");
    return (Integer)convertedList.get(key);
  }

  public void putPdfImageCache(String pdfFilePath, int num) {
    RMapCache<String, Integer> convertedList = this.redissonClient.getMapCache("converted-preview-pdfimgs-file");
    convertedList.fastPut(pdfFilePath, num);
  }

  public Map<String, String> getMediaConvertCache() {
    return this.redissonClient.getMapCache("converted-preview-media-file");
  }

  public void putMediaConvertCache(String key, String value) {
    RMapCache<String, String> convertedList = this.redissonClient.getMapCache("converted-preview-media-file");
    convertedList.fastPut(key, value);
  }

  public String getMediaConvertCache(String key) {
    RMapCache<String, String> convertedList = this.redissonClient.getMapCache("converted-preview-media-file");
    return (String)convertedList.get(key);
  }

  public void cleanCache() {
    this.cleanPdfCache();
    this.cleanImgCache();
    this.cleanPdfImgCache();
    this.cleanMediaConvertCache();
  }

  public void addQueueTask(String url) {
    RBlockingQueue<String> queue = this.redissonClient.getBlockingQueue("convert-task");
    queue.addAsync(url);
  }

  public String takeQueueTask() throws InterruptedException {
    RBlockingQueue<String> queue = this.redissonClient.getBlockingQueue("convert-task");
    return (String)queue.take();
  }

  private void cleanPdfCache() {
    RMapCache<String, String> pdfCache = this.redissonClient.getMapCache("converted-preview-pdf-file");
    pdfCache.clear();
  }

  private void cleanImgCache() {
    RMapCache<String, List<String>> imgCache = this.redissonClient.getMapCache("converted-preview-imgs-file");
    imgCache.clear();
  }

  private void cleanPdfImgCache() {
    RMapCache<String, Integer> pdfImg = this.redissonClient.getMapCache("converted-preview-pdfimgs-file");
    pdfImg.clear();
  }

  private void cleanMediaConvertCache() {
    RMapCache<String, Integer> mediaConvertCache = this.redissonClient.getMapCache("converted-preview-media-file");
    mediaConvertCache.clear();
  }
}
