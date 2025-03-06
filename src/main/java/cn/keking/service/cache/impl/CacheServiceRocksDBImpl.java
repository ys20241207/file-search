//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.keking.service.cache.impl;

import cn.keking.service.cache.CacheService;
import cn.keking.utils.ConfigUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

@ConditionalOnExpression("'${cache.type:default}'.equals('default')")
@Service
public class CacheServiceRocksDBImpl implements CacheService {
  private static final String DB_PATH;
  private static final int QUEUE_SIZE = 500000;
  private static final Logger LOGGER;
  private final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(500000);
  private RocksDB db;

  public CacheServiceRocksDBImpl() {
    try {
      this.db = RocksDB.open(DB_PATH);
      HashMap initPDFIMGCache;
      if (this.db.get("converted-preview-pdf-file".getBytes()) == null) {
        initPDFIMGCache = new HashMap();
        this.db.put("converted-preview-pdf-file".getBytes(), this.toByteArray(initPDFIMGCache));
      }

      if (this.db.get("converted-preview-imgs-file".getBytes()) == null) {
        initPDFIMGCache = new HashMap();
        this.db.put("converted-preview-imgs-file".getBytes(), this.toByteArray(initPDFIMGCache));
      }

      if (this.db.get("converted-preview-pdfimgs-file".getBytes()) == null) {
        initPDFIMGCache = new HashMap();
        this.db.put("converted-preview-pdfimgs-file".getBytes(), this.toByteArray(initPDFIMGCache));
      }
    } catch (IOException | RocksDBException var2) {
      LOGGER.error("Uable to init RocksDB" + var2);
    }

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
    try {
      Map<String, String> pdfCacheItem = this.getPDFCache();
      pdfCacheItem.put(key, value);
      this.db.put("converted-preview-pdf-file".getBytes(), this.toByteArray(pdfCacheItem));
    } catch (IOException | RocksDBException var4) {
      LOGGER.error("Put into RocksDB Exception" + var4);
    }

  }

  public void putImgCache(String key, List<String> value) {
    try {
      Map<String, List<String>> imgCacheItem = this.getImgCache();
      imgCacheItem.put(key, value);
      this.db.put("converted-preview-imgs-file".getBytes(), this.toByteArray(imgCacheItem));
    } catch (IOException | RocksDBException var4) {
      LOGGER.error("Put into RocksDB Exception" + var4);
    }

  }

  public Map<String, String> getPDFCache() {
    Object result = new HashMap();

    try {
      result = (Map)this.toObject(this.db.get("converted-preview-pdf-file".getBytes()));
    } catch (IOException | ClassNotFoundException | RocksDBException var3) {
      LOGGER.error("Get from RocksDB Exception" + var3);
    }

    return (Map)result;
  }

  public String getPDFCache(String key) {
    String result = "";

    try {
      Map<String, String> map = (Map)this.toObject(this.db.get("converted-preview-pdf-file".getBytes()));
      result = (String)map.get(key);
    } catch (IOException | ClassNotFoundException | RocksDBException var4) {
      LOGGER.error("Get from RocksDB Exception" + var4);
    }

    return result;
  }

  public Map<String, List<String>> getImgCache() {
    Object result = new HashMap();

    try {
      result = (Map)this.toObject(this.db.get("converted-preview-imgs-file".getBytes()));
    } catch (IOException | ClassNotFoundException | RocksDBException var3) {
      LOGGER.error("Get from RocksDB Exception" + var3);
    }

    return (Map)result;
  }

  public List<String> getImgCache(String key) {
    Object result = new ArrayList();

    try {
      Map<String, List<String>> map = (Map)this.toObject(this.db.get("converted-preview-imgs-file".getBytes()));
      result = (List)map.get(key);
    } catch (IOException | ClassNotFoundException | RocksDBException var5) {
      LOGGER.error("Get from RocksDB Exception" + var5);
    }

    return (List)result;
  }

  public Integer getPdfImageCache(String key) {
    Integer result = 0;

    try {
      Map<String, Integer> map = (Map)this.toObject(this.db.get("converted-preview-pdfimgs-file".getBytes()));
      result = (Integer)map.get(key);
    } catch (IOException | ClassNotFoundException | RocksDBException var5) {
      LOGGER.error("Get from RocksDB Exception" + var5);
    }

    return result;
  }

  public void putPdfImageCache(String pdfFilePath, int num) {
    try {
      Map<String, Integer> pdfImageCacheItem = this.getPdfImageCaches();
      pdfImageCacheItem.put(pdfFilePath, num);
      this.db.put("converted-preview-pdfimgs-file".getBytes(), this.toByteArray(pdfImageCacheItem));
    } catch (IOException | RocksDBException var4) {
      LOGGER.error("Put into RocksDB Exception" + var4);
    }

  }

  public Map<String, String> getMediaConvertCache() {
    Object result = new HashMap();

    try {
      result = (Map)this.toObject(this.db.get("converted-preview-media-file".getBytes()));
    } catch (IOException | ClassNotFoundException | RocksDBException var3) {
      LOGGER.error("Get from RocksDB Exception" + var3);
    }

    return (Map)result;
  }

  public void putMediaConvertCache(String key, String value) {
    try {
      Map<String, String> mediaConvertCacheItem = this.getMediaConvertCache();
      mediaConvertCacheItem.put(key, value);
      this.db.put("converted-preview-media-file".getBytes(), this.toByteArray(mediaConvertCacheItem));
    } catch (IOException | RocksDBException var4) {
      LOGGER.error("Put into RocksDB Exception" + var4);
    }

  }

  public String getMediaConvertCache(String key) {
    String result = "";

    try {
      Map<String, String> map = (Map)this.toObject(this.db.get("converted-preview-media-file".getBytes()));
      result = (String)map.get(key);
    } catch (IOException | ClassNotFoundException | RocksDBException var4) {
      LOGGER.error("Get from RocksDB Exception" + var4);
    }

    return result;
  }

  public void cleanCache() {
    try {
      this.cleanPdfCache();
      this.cleanImgCache();
      this.cleanPdfImgCache();
      this.cleanMediaConvertCache();
    } catch (RocksDBException | IOException var2) {
      LOGGER.error("Clean Cache Exception" + var2);
    }

  }

  public void addQueueTask(String url) {
    this.blockingQueue.add(url);
  }

  public String takeQueueTask() throws InterruptedException {
    return (String)this.blockingQueue.take();
  }

  private Map<String, Integer> getPdfImageCaches() {
    Object map = new HashMap();

    try {
      map = (Map)this.toObject(this.db.get("converted-preview-pdfimgs-file".getBytes()));
    } catch (IOException | ClassNotFoundException | RocksDBException var3) {
      LOGGER.error("Get from RocksDB Exception" + var3);
    }

    return (Map)map;
  }

  private byte[] toByteArray(Object obj) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(obj);
    oos.flush();
    byte[] bytes = bos.toByteArray();
    oos.close();
    bos.close();
    return bytes;
  }

  private Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    ObjectInputStream ois = new ObjectInputStream(bis);
    Object obj = ois.readObject();
    ois.close();
    bis.close();
    return obj;
  }

  private void cleanPdfCache() throws IOException, RocksDBException {
    Map<String, String> initPDFCache = new HashMap();
    this.db.put("converted-preview-pdf-file".getBytes(), this.toByteArray(initPDFCache));
  }

  private void cleanImgCache() throws IOException, RocksDBException {
    Map<String, List<String>> initIMGCache = new HashMap();
    this.db.put("converted-preview-imgs-file".getBytes(), this.toByteArray(initIMGCache));
  }

  private void cleanPdfImgCache() throws IOException, RocksDBException {
    Map<String, Integer> initPDFIMGCache = new HashMap();
    this.db.put("converted-preview-pdfimgs-file".getBytes(), this.toByteArray(initPDFIMGCache));
  }

  private void cleanMediaConvertCache() throws IOException, RocksDBException {
    Map<String, String> initMediaConvertCache = new HashMap();
    this.db.put("converted-preview-media-file".getBytes(), this.toByteArray(initMediaConvertCache));
  }

  static {
    RocksDB.loadLibrary();
    DB_PATH = ConfigUtils.getHomePath() + File.separator + "cache";
    LOGGER = LoggerFactory.getLogger(CacheServiceRocksDBImpl.class);
  }
}
