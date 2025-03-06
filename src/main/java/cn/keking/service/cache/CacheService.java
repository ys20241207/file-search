//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.keking.service.cache;

import java.util.List;
import java.util.Map;

public interface CacheService {

  String FILE_PREVIEW_PDF_KEY = "converted-preview-pdf-file";
  String FILE_PREVIEW_IMGS_KEY = "converted-preview-imgs-file";
  String FILE_PREVIEW_PDF_IMGS_KEY = "converted-preview-pdfimgs-file";
  String FILE_PREVIEW_MEDIA_CONVERT_KEY = "converted-preview-media-file";
  String TASK_QUEUE_NAME = "convert-task";
  Integer DEFAULT_PDF_CAPACITY = 500000;
  Integer DEFAULT_IMG_CAPACITY = 500000;
  Integer DEFAULT_PDFIMG_CAPACITY = 500000;
  Integer DEFAULT_MEDIACONVERT_CAPACITY = 500000;

  void initPDFCachePool(Integer var1);

  void initIMGCachePool(Integer var1);

  void initPdfImagesCachePool(Integer var1);

  void initMediaConvertCachePool(Integer var1);

  void putPDFCache(String var1, String var2);

  void putImgCache(String var1, List<String> var2);

  Map<String, String> getPDFCache();

  String getPDFCache(String var1);

  Map<String, List<String>> getImgCache();

  List<String> getImgCache(String var1);

  Integer getPdfImageCache(String var1);

  void putPdfImageCache(String var1, int var2);

  Map<String, String> getMediaConvertCache();

  void putMediaConvertCache(String var1, String var2);

  String getMediaConvertCache(String var1);

  void cleanCache();

  void addQueueTask(String var1);

  String takeQueueTask() throws InterruptedException;
}
