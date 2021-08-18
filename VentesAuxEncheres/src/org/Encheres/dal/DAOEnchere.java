package org.Encheres.dal;

import java.util.List;

import org.Encheres.BusinessException;
import org.Encheres.bo.Article;
import org.Encheres.bo.Enchere;

public interface DAOEnchere extends DAO<Enchere> {
	
	
	@Override
	public void insert(Enchere data) throws BusinessException;
	
	public void delete(int noArticle) throws BusinessException;
	
	public void update(int noArticle, int noUtilisateur, int montantEnchere) throws BusinessException;
}