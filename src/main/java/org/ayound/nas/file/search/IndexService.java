package org.ayound.nas.file.search;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;

@Component
public class IndexService {
    // 保持原有单例模式（与Spring @Component共存）
    private static IndexService instance;
    private Directory indexDirectory;
    private Analyzer analyzer;

    // 私有构造函数
    private IndexService() throws IOException {
        this.analyzer = new NGramAnalyzer(); // 替换为N-Gram分词器
        Path indexPath = Paths.get(Configuration.getIndexFilePath());
        this.indexDirectory = new NIOFSDirectory(indexPath);
    }

    // 自定义N-Gram分词器（内置类）
    private static class NGramAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            Tokenizer tokenizer = new NGramTokenizer(1, 2); // 1-2 gram
            return new TokenStreamComponents(tokenizer);
        }
    }

    // 单例获取方法（保持原有）
    public static synchronized IndexService getInstance() throws IOException {
        if (instance == null) {
            instance = new IndexService();
        }
        return instance;
    }

    // 更新索引路径（保持原有方法签名）
    public void update() throws IOException {
        Path indexPath = Paths.get(Configuration.getIndexFilePath());
        this.indexDirectory = new NIOFSDirectory(indexPath);
        this.analyzer = new NGramAnalyzer(); // 重新初始化N-Gram
    }

    // 其余方法完全保持不变
    @PreDestroy
    public void cleanup() throws IOException {
        if (analyzer != null) analyzer.close();
        if (indexDirectory != null) indexDirectory.close();
    }

    public Directory getIndexDirectory() throws IOException {
        Path indexPath = Paths.get(Configuration.getIndexFilePath());
        return new NIOFSDirectory(indexPath);
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public IndexWriter newIndexWriter() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(indexDirectory, config);
    }
}
