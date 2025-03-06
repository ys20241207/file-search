//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.keking.service.cache.impl;

import cn.keking.service.cache.CacheService;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap.Builder;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnExpression("'${cache.type:default}'.equals('jdk')")
public class CacheServiceJDKImpl implements CacheService {

  private Map<String, String> pdfCache;
  private Map<String, List<String>> imgCache;
  private Map<String, Integer> pdfImagesCache;
  private Map<String, String> mediaConvertCache;
  private static final int QUEUE_SIZE = 500000;
  private final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(500000);

  public CacheServiceJDKImpl() {
  }

  @PostConstruct
  public void initCache() {
    this.initPDFCachePool(CacheService.DEFAULT_PDF_CAPACITY);
    this.initIMGCachePool(CacheService.DEFAULT_IMG_CAPACITY);
    this.initPdfImagesCachePool(CacheService.DEFAULT_PDFIMG_CAPACITY);
    this.initMediaConvertCachePool(CacheService.DEFAULT_MEDIACONVERT_CAPACITY);
  }

  public void putPDFCache(String key, String value) {
    this.pdfCache.put(key, value);
  }

  public void putImgCache(String key, List<String> value) {
    this.imgCache.put(key, value);
  }

  public Map<String, String> getPDFCache() {
    return this.pdfCache;
  }

  public String getPDFCache(String key) {
    return (String) this.pdfCache.get(key);
  }

  public Map<String, List<String>> getImgCache() {
    return this.imgCache;
  }

  public List<String> getImgCache(String key) {
    return (List) (StringUtils.isEmpty(key) ? new ArrayList() : (List) this.imgCache.get(key));
  }

  public Integer getPdfImageCache(String key) {
    return (Integer) this.pdfImagesCache.get(key);
  }

  public void putPdfImageCache(String pdfFilePath, int num) {
    this.pdfImagesCache.put(pdfFilePath, num);
  }

  public Map<String, String> getMediaConvertCache() {
    return this.mediaConvertCache;
  }

  public void putMediaConvertCache(String key, String value) {
    this.mediaConvertCache.put(key, value);
  }

  public String getMediaConvertCache(String key) {
    return (String) this.mediaConvertCache.get(key);
  }

  public void cleanCache() {
    this.initPDFCachePool(CacheService.DEFAULT_PDF_CAPACITY);
    this.initIMGCachePool(CacheService.DEFAULT_IMG_CAPACITY);
    this.initPdfImagesCachePool(CacheService.DEFAULT_PDFIMG_CAPACITY);
    this.initMediaConvertCachePool(CacheService.DEFAULT_MEDIACONVERT_CAPACITY);
  }

  public void addQueueTask(String url) {
    this.blockingQueue.add(url);
  }

  public String takeQueueTask() throws InterruptedException {
    return (String) this.blockingQueue.take();
  }

  public void initPDFCachePool(Integer capacity) {
    this.pdfCache = (new Builder()).maximumWeightedCapacity((long) capacity)
        .weigher(Weighers.singleton()).build();
  }

  public void initIMGCachePool(Integer capacity) {
    this.imgCache = (new Builder()).maximumWeightedCapacity((long) capacity)
        .weigher(Weighers.singleton()).build();
  }

  public void initPdfImagesCachePool(Integer capacity) {
    this.pdfImagesCache = (new Builder()).maximumWeightedCapacity((long) capacity)
        .weigher(Weighers.singleton()).build();
  }

  public void initMediaConvertCachePool(Integer capacity) {
    this.mediaConvertCache = (new Builder()).maximumWeightedCapacity((long) capacity)
        .weigher(Weighers.singleton()).build();
  }
}
