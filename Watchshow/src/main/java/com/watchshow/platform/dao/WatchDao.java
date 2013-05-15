package com.watchshow.platform.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

import com.watchshow.common.dao.BaseHibernateDao;
import com.watchshow.common.util.FileManagerUtil;
import com.watchshow.platform.domain.Watch;
import com.watchshow.platform.helper.ServerResourceHelper;

public class WatchDao  extends BaseHibernateDao<Watch, Long>{
	
	public static enum SRC_KIND {
		IMAGE,
		AUDIO,
		VIDEO,
		ARCHIVE,
		PLAIN,
		OTHER,
	}

	public WatchDao() {
		super();
	}

	@SuppressWarnings("deprecation")
	public WatchDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public List<String> getResourcePathWithKind(Long watchId, SRC_KIND kind, String internalPath) {
		ArrayList<String> pathset = new ArrayList<String>();
		Watch watch =  get(watchId);
		String relativePath = watch.getDescResourceURL();
		String srcFolder = internalPath + relativePath + File.separator;
		if (kind == SRC_KIND.IMAGE) {
			srcFolder += ServerResourceHelper.imageRoot;
		} else if (kind == SRC_KIND.AUDIO) {
			srcFolder += ServerResourceHelper.audioRoot;
		} else if (kind == SRC_KIND.VIDEO) {
			srcFolder += ServerResourceHelper.videoRoot;
		} else if (kind == SRC_KIND.PLAIN) {
			srcFolder += ServerResourceHelper.plainRoot;
		} else if (kind == SRC_KIND.ARCHIVE) {
			srcFolder += ServerResourceHelper.archiveRoot;
		} else {
			srcFolder += ServerResourceHelper.othersRoot;
		}
		
		System.out.println("image folder for "+watchId+" = " + srcFolder);
		File folder = new File(srcFolder);
		if (folder.exists()) {
			List<File> files = FileManagerUtil.getAllSubFiles(folder);
			for (File f : files) {
				String path = f.getPath();
				int start = path.indexOf(relativePath);
				path = path.substring(start, path.length());
				System.out.println("file path = " + path);
				pathset.add(path);
			}
		}
		return pathset;
	}
	
}
