package cn.itcast.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import com.hankcs.lucene.HanLPAnalyzer;

public class CreateIndex {

	//创建索引库的方法
	@Test
	public void createIndex() {
		//创建写入索引库的对象

		//左边加入注释右边修改

		//左边加入注释测试冲突

		//Directory d 目录指的是索引库存储目录
		//IndexWriterConfig conf 写入对象的配置信息
		try {
			Directory directory = FSDirectory.open(Paths.get("D:\\luceneIndex"));
			//使用的分词器
			Analyzer analyzer = new HanLPAnalyzer();
			IndexWriterConfig conf = new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(directory,conf);
			//1.读取原始的数据  
			//当前文件所在的文件夹
			File fileDir = new File("D:\\searchsource");
			//获取文件夹下的所有文件
			File [] files = fileDir.listFiles();
			int i=0;
			for(File file:files) {
				System.out.println("当前的文件名为：===="+file.getName());
				System.out.println("当前的文件路径为：===="+file.getPath());
				System.out.println("当前的文件内容为：===="+FileUtils.readFileToString(file));
				System.out.println("当前的文件的大小为：===="+FileUtils.sizeOf(file));
				//2.将数据封装为document
				Document document = new Document();
				/*文档添加域字段
				 * StringField 特点域字段存储数据 不分词 支持存储和查询   11009933223
				 * TextFile  分词存储数据支持查询
				 * LongPoint 不存储数据支持数据的范围查询
				 * StoredField 专用于存储数据 不支持查询
				 * Store YES/NO 是否存储当前的域数据 
				 * */
				document.add(new StringField("fileNum", "000000"+i, Store.YES));
				document.add(new TextField("fileName", file.getName(),Store.YES) );
				document.add(new TextField("fileContent", FileUtils.readFileToString(file),Store.YES) );
				document.add(new StoredField("filePath",file.getPath()) );
				document.add(new LongPoint("fileSize", FileUtils.sizeOf(file)));
				writer.addDocument(document);
				i++;
			}
			//3.写入文档到索引库
			writer.commit();
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	   //修改索引库的方法
		@Test
		public void updateIndex() {
			//创建写入索引库的对象
			//Directory d 目录指的是索引库存储目录
			//IndexWriterConfig conf 写入对象的配置信息
			try {
				Directory directory = FSDirectory.open(Paths.get("D:\\luceneIndex"));
				//使用的分词器
				Analyzer analyzer = new HanLPAnalyzer();
				IndexWriterConfig conf = new IndexWriterConfig(analyzer);
				IndexWriter writer = new IndexWriter(directory,conf);
				//参数一根据词条条件更新文档
				//参数二是更新后的文档数据
				//update 动作逻辑是 先执行删除匹配的文档 再添加文档
				//update 针对唯一标识修改 
				Document doc = new Document();
				doc.add(new TextField("fileName", "测试修改唯一",Store.YES));
				writer.updateDocument(new Term("fileNum","0000001"), doc);
				writer.commit();
				writer.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		//修改索引库的方法
		@Test
		public void deleteIndex() {
			//创建写入索引库的对象
			//Directory d 目录指的是索引库存储目录
			//IndexWriterConfig conf 写入对象的配置信息
			try {
				Directory directory = FSDirectory.open(Paths.get("D:\\luceneIndex"));
				//使用的分词器
				Analyzer analyzer = new HanLPAnalyzer();
				IndexWriterConfig conf = new IndexWriterConfig(analyzer);
				IndexWriter writer = new IndexWriter(directory,conf);
				//删除所有
				writer.deleteAll();
				//删除文档根据query结果删除
				//writer.deleteDocuments(new Term("fileNum","00000014"));
				writer.commit();
				writer.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
}
