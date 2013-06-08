package application.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import application.helper.ServerResourceHelper;
import application.model.Publication;

import commons.dao.BaseHibernateDao;
import commons.util.FileManagerUtil;
import commons.util.HibernateUtil;

public class PublicationDao extends BaseHibernateDao<Publication, Long> {
	public static enum SRC_KIND {
		IMAGE,
		AUDIO,
		VIDEO,
		ARCHIVE,
		PLAIN,
		OTHER,
	}
	@SuppressWarnings("deprecation")
	public PublicationDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public PublicationDao() {
		super();
	}
	
	public List<Publication> lastNews() {
		List<Publication> newslist = new ArrayList<Publication>();
		Criteria criteria = HibernateUtil.currentSession().createCriteria(Publication.class);
		criteria.add(Restrictions.eq("kind", "News"));
		criteria.createCriteria("histories").addOrder(Order.desc("timestamp"));
		newslist = list(criteria);
		return newslist;
	}
	
	public List<String> getResourcePathWithKind(Long pubId, SRC_KIND kind, String hostPath) {
		ArrayList<String> pathset = new ArrayList<String>();
		Publication pub =  get(pubId);
		String relativePath = pub.getResourcesURL();
		String srcFolder = hostPath + relativePath + File.separator;
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
		
		System.out.println("src folder for "+pubId+" = " + srcFolder);
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
	
	public String getThumbnailURLForPub(Long id, String hostPath) {
	    String url = null;
	    List<String> images = getResourcePathWithKind(id, SRC_KIND.IMAGE, hostPath);
	    if (images != null && images.size()>0) {
	        url = images.get(0);
	    }
	    return url;
	}

}
